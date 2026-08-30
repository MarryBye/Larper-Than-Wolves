package io.marrybye.github.larperthanwolves.block.entity;

import io.marrybye.github.larperthanwolves.menu.FilterGrateMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FilterGrateBlockEntity extends BlockEntity implements MenuProvider, Container {
    public static final int FILTER_SLOTS = 9;
    private final NonNullList<ItemStack> filterItems = NonNullList.withSize(FILTER_SLOTS, ItemStack.EMPTY);

    public FilterGrateBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FILTER_GRATE.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, FilterGrateBlockEntity grate) {
        // Query ItemEntities currently resting on top of the filter grate block
        AABB queryBox = new AABB(
                pos.getX(), pos.getY() + 0.8D, pos.getZ(),
                pos.getX() + 1.0D, pos.getY() + 1.3D, pos.getZ() + 1.0D
        );

        List<ItemEntity> itemsOnTop = level.getEntitiesOfClass(ItemEntity.class, queryBox);
        if (itemsOnTop.isEmpty()) return;

        boolean isPowered = level.hasNeighborSignal(pos);

        for (ItemEntity itemEntity : itemsOnTop) {
            if (!itemEntity.isAlive()) continue;

            ItemStack stack = itemEntity.getItem();
            if (stack.isEmpty()) continue;

            boolean passes = grate.shouldPassItem(stack, isPowered);
            if (passes) {
                // Drop the item entity through the grate
                itemEntity.teleportTo(pos.getX() + 0.5D, pos.getY() - 0.2D, pos.getZ() + 0.5D);
                itemEntity.setDeltaMovement(new Vec3(0, -0.12D, 0));
                itemEntity.hurtMarked = true;

                level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 0.4F, 1.4F);

                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.CRIT,
                            pos.getX() + 0.5D, pos.getY() + 0.9D, pos.getZ() + 0.5D,
                            3, 0.2D, 0.1D, 0.2D, 0.02D);
                }
            }
        }
    }

    public boolean shouldPassItem(ItemStack stack, boolean isPowered) {
        boolean hasAnyFilter = false;
        boolean matchesFilter = false;

        for (ItemStack filter : filterItems) {
            if (!filter.isEmpty()) {
                hasAnyFilter = true;
                if (ItemStack.isSameItemSameComponents(filter, stack)) {
                    matchesFilter = true;
                    break;
                }
            }
        }

        if (!hasAnyFilter) {
            // If no filters are defined:
            // Unpowered: block all items (passes = false)
            // Powered: pass all items (passes = true)
            return isPowered;
        }

        // If filters are defined:
        // Unpowered (Normal mode): pass only items matching filter
        // Powered (Inverted mode): pass all items EXCEPT those matching filter
        return isPowered ? !matchesFilter : matchesFilter;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.larperthanwolves.filter_grate");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new FilterGrateMenu(containerId, inventory, this);
    }

    @Override
    public int getContainerSize() {
        return FILTER_SLOTS;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : filterItems) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return filterItems.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack stack = filterItems.get(slot);
        if (!stack.isEmpty()) {
            filterItems.set(slot, ItemStack.EMPTY);
            setChanged();
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = filterItems.get(slot);
        filterItems.set(slot, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (!stack.isEmpty()) {
            filterItems.set(slot, stack.copyWithCount(1));
        } else {
            filterItems.set(slot, ItemStack.EMPTY);
        }
        setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        filterItems.clear();
        setChanged();
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        filterItems.clear();
        for (int i = 0; i < FILTER_SLOTS; i++) {
            filterItems.set(i, ItemStack.EMPTY);
        }
        if (tag.contains("FilterItems", Tag.TAG_LIST)) {
            ListTag list = tag.getList("FilterItems", Tag.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag itemTag = list.getCompound(i);
                int slot = itemTag.getByte("Slot") & 255;
                if (slot < FILTER_SLOTS) {
                    ItemStack.parse(registries, itemTag).ifPresent(stack -> filterItems.set(slot, stack));
                }
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ListTag list = new ListTag();
        for (int i = 0; i < FILTER_SLOTS; i++) {
            ItemStack stack = filterItems.get(i);
            if (!stack.isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putByte("Slot", (byte) i);
                list.add(stack.save(registries, itemTag));
            }
        }
        tag.put("FilterItems", list);
    }
}
