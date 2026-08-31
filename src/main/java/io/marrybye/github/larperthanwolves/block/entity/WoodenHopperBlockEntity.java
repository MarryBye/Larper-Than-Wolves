package io.marrybye.github.larperthanwolves.block.entity;

import io.marrybye.github.larperthanwolves.block.WoodenHopperBlock;
import io.marrybye.github.larperthanwolves.menu.WoodenHopperMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WoodenHopperBlockEntity extends BlockEntity implements Container, MenuProvider {
    public static final int MOVE_ITEM_SPEED = 14; // Slower than vanilla (8 ticks)
    private final NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);
    private int cooldownTime = -1;
    private long tickedGameTime;

    public WoodenHopperBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.WOODEN_HOPPER.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.larperthanwolves.wooden_hopper");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new WoodenHopperMenu(containerId, playerInventory, this, ContainerLevelAccess.create(this.level, this.worldPosition));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, items, registries);
        tag.putInt("TransferCooldown", this.cooldownTime);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        ContainerHelper.loadAllItems(tag, items, registries);
        this.cooldownTime = tag.getInt("TransferCooldown");
    }

    public static void pushItemsTick(Level level, BlockPos pos, BlockState state, WoodenHopperBlockEntity entity) {
        --entity.cooldownTime;
        entity.tickedGameTime = level.getGameTime();
        if (!entity.isOnCooldown()) {
            entity.setCooldown(0);
            tryMoveItems(level, pos, state, entity);
        }
    }

    private static boolean tryMoveItems(Level level, BlockPos pos, BlockState state, WoodenHopperBlockEntity entity) {
        if (level != null && !level.isClientSide) {
            if (!entity.isOnCooldown() && state.getValue(WoodenHopperBlock.ENABLED)) {
                boolean actionTaken = false;

                if (!entity.isEmpty()) {
                    actionTaken = ejectItems(level, pos, state, entity);
                }

                if (!entity.isFull()) {
                    actionTaken |= suckInItems(level, entity);
                }

                if (actionTaken) {
                    entity.setCooldown(MOVE_ITEM_SPEED);
                    setChanged(level, pos, state);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isFull() {
        ItemStack stack = this.items.get(0);
        return !stack.isEmpty() && stack.getCount() >= stack.getMaxStackSize();
    }

    private static boolean ejectItems(Level level, BlockPos pos, BlockState state, WoodenHopperBlockEntity entity) {
        Direction direction = state.getValue(WoodenHopperBlock.FACING);
        BlockPos targetPos = pos.relative(direction);
        Container targetContainer = getContainerAt(level, targetPos);

        if (targetContainer == null) {
            return false;
        }

        Direction targetSide = direction.getOpposite();
        ItemStack currentStack = entity.getItem(0);
        if (currentStack.isEmpty()) return false;

        ItemStack toInsert = currentStack.copy();
        toInsert.setCount(1);

        ItemStack remaining = addItem(entity, targetContainer, toInsert, targetSide);
        if (remaining.isEmpty()) {
            currentStack.shrink(1);
            entity.setItem(0, currentStack.isEmpty() ? ItemStack.EMPTY : currentStack);
            targetContainer.setChanged();
            return true;
        }

        return false;
    }

    private static boolean suckInItems(Level level, WoodenHopperBlockEntity entity) {
        BlockPos abovePos = entity.getBlockPos().above();
        Container aboveContainer = getContainerAt(level, abovePos);

        if (aboveContainer != null) {
            Direction fromSide = Direction.DOWN;
            int[] slots = getSlots(aboveContainer, fromSide);
            for (int slot : slots) {
                ItemStack stackInSlot = aboveContainer.getItem(slot);
                if (!stackInSlot.isEmpty() && canTakeItem(aboveContainer, slot, stackInSlot, fromSide)) {
                    ItemStack toTake = stackInSlot.copy();
                    toTake.setCount(1);
                    ItemStack current = entity.getItem(0);

                    if (current.isEmpty()) {
                        entity.setItem(0, toTake);
                        stackInSlot.shrink(1);
                        aboveContainer.setItem(slot, stackInSlot.isEmpty() ? ItemStack.EMPTY : stackInSlot);
                        aboveContainer.setChanged();
                        return true;
                    } else if (ItemStack.isSameItemSameComponents(current, toTake) && current.getCount() < current.getMaxStackSize()) {
                        current.grow(1);
                        stackInSlot.shrink(1);
                        aboveContainer.setItem(slot, stackInSlot.isEmpty() ? ItemStack.EMPTY : stackInSlot);
                        aboveContainer.setChanged();
                        return true;
                    }
                }
            }
        } else {
            // Pick up dropped ItemEntities from above
            List<ItemEntity> itemEntities = level.getEntitiesOfClass(ItemEntity.class,
                    new AABB(entity.getBlockPos().getX(), entity.getBlockPos().getY() + 0.5D, entity.getBlockPos().getZ(),
                            entity.getBlockPos().getX() + 1.0D, entity.getBlockPos().getY() + 1.5D, entity.getBlockPos().getZ() + 1.0D),
                    EntitySelector.ENTITY_STILL_ALIVE);

            for (ItemEntity itemEntity : itemEntities) {
                if (addItemEntity(entity, itemEntity)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean addItemEntity(WoodenHopperBlockEntity entity, ItemEntity itemEntity) {
        ItemStack itemStack = itemEntity.getItem();
        ItemStack current = entity.getItem(0);

        if (current.isEmpty()) {
            ItemStack toPut = itemStack.copy();
            toPut.setCount(1);
            entity.setItem(0, toPut);
            itemStack.shrink(1);
            if (itemStack.isEmpty()) {
                itemEntity.discard();
            } else {
                itemEntity.setItem(itemStack);
            }
            return true;
        } else if (ItemStack.isSameItemSameComponents(current, itemStack) && current.getCount() < current.getMaxStackSize()) {
            current.grow(1);
            itemStack.shrink(1);
            if (itemStack.isEmpty()) {
                itemEntity.discard();
            } else {
                itemEntity.setItem(itemStack);
            }
            return true;
        }
        return false;
    }

    private static ItemStack addItem(@Nullable Container from, Container to, ItemStack stack, @Nullable Direction side) {
        if (to instanceof WorldlyContainer worldlyContainer && side != null) {
            int[] slots = worldlyContainer.getSlotsForFace(side);
            for (int slot : slots) {
                if (stack.isEmpty()) return ItemStack.EMPTY;
                if (worldlyContainer.canPlaceItemThroughFace(slot, stack, side)) {
                    stack = tryInsert(to, stack, slot);
                }
            }
            return stack;
        }

        int size = to.getContainerSize();
        for (int i = 0; i < size; i++) {
            if (stack.isEmpty()) return ItemStack.EMPTY;
            if (to.canPlaceItem(i, stack)) {
                stack = tryInsert(to, stack, i);
            }
        }
        return stack;
    }

    private static ItemStack tryInsert(Container container, ItemStack stack, int slot) {
        ItemStack current = container.getItem(slot);
        if (current.isEmpty()) {
            container.setItem(slot, stack.copy());
            return ItemStack.EMPTY;
        } else if (ItemStack.isSameItemSameComponents(current, stack) && current.getCount() < current.getMaxStackSize()) {
            int canGrow = Math.min(stack.getCount(), current.getMaxStackSize() - current.getCount());
            current.grow(canGrow);
            stack.shrink(canGrow);
            return stack.isEmpty() ? ItemStack.EMPTY : stack;
        }
        return stack;
    }

    private static int[] getSlots(Container container, Direction side) {
        if (container instanceof WorldlyContainer worldly) {
            return worldly.getSlotsForFace(side);
        }
        int[] result = new int[container.getContainerSize()];
        for (int i = 0; i < result.length; i++) {
            result[i] = i;
        }
        return result;
    }

    private static boolean canTakeItem(Container container, int slot, ItemStack stack, Direction side) {
        if (container instanceof WorldlyContainer worldly) {
            return worldly.canTakeItemThroughFace(slot, stack, side);
        }
        return true;
    }

    @Nullable
    public static Container getContainerAt(Level level, BlockPos pos) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof Container container) {
            return container;
        }
        return null;
    }

    private boolean isOnCooldown() {
        return this.cooldownTime > 0;
    }

    public void setCooldown(int cooldown) {
        this.cooldownTime = cooldown;
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return this.items.get(0).isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return slot == 0 ? this.items.get(0) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack res = ContainerHelper.removeItem(this.items, slot, amount);
        if (!res.isEmpty()) setChanged();
        return res;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(this.items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (slot == 0) {
            this.items.set(0, stack);
            if (stack.getCount() > this.getMaxStackSize()) {
                stack.setCount(this.getMaxStackSize());
            }
            setChanged();
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        this.items.clear();
        setChanged();
    }
}
