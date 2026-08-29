package io.marrybye.github.larperthanwolves.block.entity;

import io.marrybye.github.larperthanwolves.block.DryingRackBlock;
import io.marrybye.github.larperthanwolves.config.ModConfig;
import io.marrybye.github.larperthanwolves.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class DryingRackBlockEntity extends BlockEntity implements WorldlyContainer {
    private static final int[] SLOTS = new int[]{0};

    private final NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);
    private int dryingProgress = 0;

    public DryingRackBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DRYING_RACK.get(), pos, state);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("DryingProgress", dryingProgress);
        ContainerHelper.saveAllItems(tag, items, registries);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("DryingProgress")) {
            dryingProgress = tag.getInt("DryingProgress");
        }
        items.clear();
        ContainerHelper.loadAllItems(tag, items, registries);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, DryingRackBlockEntity entity) {
        if (level.isClientSide) return;

        ItemStack stack = entity.items.get(0);
        if (stack.isEmpty()) {
            if (state.getValue(DryingRackBlock.CONTENT) != DryingRackBlock.Content.NONE) {
                level.setBlock(pos, state.setValue(DryingRackBlock.CONTENT, DryingRackBlock.Content.NONE), 3);
            }
            entity.dryingProgress = 0;
            return;
        }

        // If already dried item
        if (stack.is(ModItems.DRY_GRASS.get())) {
            if (state.getValue(DryingRackBlock.CONTENT) != DryingRackBlock.Content.DRY_GRASS) {
                level.setBlock(pos, state.setValue(DryingRackBlock.CONTENT, DryingRackBlock.Content.DRY_GRASS), 3);
            }
            return;
        }
        if (stack.is(ModItems.TANNED_LEATHER.get())) {
            if (state.getValue(DryingRackBlock.CONTENT) != DryingRackBlock.Content.TANNED_LEATHER) {
                level.setBlock(pos, state.setValue(DryingRackBlock.CONTENT, DryingRackBlock.Content.TANNED_LEATHER), 3);
            }
            return;
        }

        // Requirements from task:
        // 1. Day time only (level.isDay())
        // 2. Direct sky access (level.canSeeSky(pos.above()))
        // 3. Not raining at this location (!level.isRainingAt(pos.above()))
        if (level.isDay() && level.canSeeSky(pos.above()) && !level.isRainingAt(pos.above())) {
            entity.dryingProgress++;
            int totalTicks = ModConfig.SERVER != null ? ModConfig.SERVER.dryingRackTimeTicks.get() : 1200;
            if (totalTicks <= 0) totalTicks = 1200;

            if (entity.dryingProgress >= totalTicks) {
                if (DryingRackBlock.isDryableGrass(stack)) {
                    entity.items.set(0, new ItemStack(ModItems.DRY_GRASS.get(), 1));
                    level.setBlock(pos, state.setValue(DryingRackBlock.CONTENT, DryingRackBlock.Content.DRY_GRASS), 3);
                    level.playSound(null, pos, SoundEvents.GRASS_HIT, SoundSource.BLOCKS, 0.8f, 1.2f);
                } else if (DryingRackBlock.isDryableLeather(stack)) {
                    entity.items.set(0, new ItemStack(ModItems.TANNED_LEATHER.get(), 1));
                    level.setBlock(pos, state.setValue(DryingRackBlock.CONTENT, DryingRackBlock.Content.TANNED_LEATHER), 3);
                    level.playSound(null, pos, SoundEvents.ARMOR_EQUIP_LEATHER.value(), SoundSource.BLOCKS, 0.8f, 1.2f);
                }
                entity.dryingProgress = 0;
                entity.setChanged();
            }
        }
    }

    public int getDryingProgress() {
        return dryingProgress;
    }

    public void setDryingProgress(int progress) {
        this.dryingProgress = progress;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return SLOTS;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction) {
        return index == 0 && isEmpty() && (DryingRackBlock.isDryableGrass(stack) || DryingRackBlock.isDryableLeather(stack));
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index == 0 && (stack.is(ModItems.DRY_GRASS.get()) || stack.is(ModItems.TANNED_LEATHER.get()));
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return items.get(0).isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return slot == 0 ? items.get(0) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        if (slot == 0 && !items.get(0).isEmpty()) {
            dryingProgress = 0;
            ItemStack res = ContainerHelper.removeItem(items, slot, amount);
            setChanged();
            return res;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        if (slot == 0) {
            dryingProgress = 0;
            return ContainerHelper.takeItem(items, slot);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (slot == 0) {
            items.set(0, stack);
            dryingProgress = 0;
            setChanged();
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return net.minecraft.world.Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        items.clear();
        dryingProgress = 0;
        setChanged();
    }
}
