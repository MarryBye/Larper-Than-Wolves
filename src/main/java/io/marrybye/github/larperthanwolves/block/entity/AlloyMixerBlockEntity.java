package io.marrybye.github.larperthanwolves.block.entity;

import io.marrybye.github.larperthanwolves.block.AlloyMixerBlock;
import io.marrybye.github.larperthanwolves.config.ModConfig;
import io.marrybye.github.larperthanwolves.item.ModItems;
import io.marrybye.github.larperthanwolves.menu.AlloyMixerMenu;
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
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class AlloyMixerBlockEntity extends BlockEntity implements WorldlyContainer, MenuProvider {
    // 0: Diamond, 1: Iron Ingot, 2: Copper Ingot, 3: Fuel, 4: Result Diamond Ingot
    private final NonNullList<ItemStack> items = NonNullList.withSize(5, ItemStack.EMPTY);

    private int burnTime = 0;
    private int maxBurnTime = 0;
    private int cookTime = 0;
    private int cookTimeTotal = 600;

    public AlloyMixerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ALLOY_MIXER.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.larperthanwolves.alloy_mixer");
    }

    protected final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> AlloyMixerBlockEntity.this.burnTime;
                case 1 -> AlloyMixerBlockEntity.this.maxBurnTime;
                case 2 -> AlloyMixerBlockEntity.this.cookTime;
                case 3 -> AlloyMixerBlockEntity.this.cookTimeTotal;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> AlloyMixerBlockEntity.this.burnTime = value;
                case 1 -> AlloyMixerBlockEntity.this.maxBurnTime = value;
                case 2 -> AlloyMixerBlockEntity.this.cookTime = value;
                case 3 -> AlloyMixerBlockEntity.this.cookTimeTotal = value;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new AlloyMixerMenu(containerId, playerInventory, this, this.dataAccess, ContainerLevelAccess.create(this.level, this.worldPosition));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, this.items, registries);
        tag.putInt("BurnTime", this.burnTime);
        tag.putInt("MaxBurnTime", this.maxBurnTime);
        tag.putInt("CookTime", this.cookTime);
        tag.putInt("CookTimeTotal", this.cookTimeTotal);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        ContainerHelper.loadAllItems(tag, this.items, registries);
        this.burnTime = tag.getInt("BurnTime");
        this.maxBurnTime = tag.getInt("MaxBurnTime");
        this.cookTime = tag.getInt("CookTime");
        this.cookTimeTotal = tag.getInt("CookTimeTotal");
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AlloyMixerBlockEntity entity) {
        if (level.isClientSide) return;

        boolean wasLit = entity.burnTime > 0;
        boolean changed = false;

        if (entity.burnTime > 0) {
            entity.burnTime--;
        }

        entity.cookTimeTotal = ModConfig.SERVER != null ? ModConfig.SERVER.alloyMixerCookTimeTicks.get() : 600;
        if (entity.cookTimeTotal <= 0) entity.cookTimeTotal = 600;

        boolean hasIngredients = entity.hasValidIngredients();
        boolean canOutput = entity.canOutputResult();

        if (hasIngredients && canOutput) {
            // Need fuel if not burning
            if (entity.burnTime <= 0) {
                ItemStack fuelStack = entity.items.get(3);
                BrickFurnaceBlockEntity.FuelInfo fuelInfo = BrickFurnaceBlockEntity.getFuelInfo(fuelStack);
                if (fuelInfo != null) {
                    entity.burnTime = fuelInfo.burnDuration;
                    entity.maxBurnTime = fuelInfo.burnDuration;
                    fuelStack.shrink(1);
                    changed = true;
                }
            }

            if (entity.burnTime > 0) {
                entity.cookTime++;
                if (entity.cookTime >= entity.cookTimeTotal) {
                    entity.mixAlloy();
                    entity.cookTime = 0;
                    changed = true;
                }
            } else {
                if (entity.cookTime > 0) {
                    entity.cookTime = Math.max(0, entity.cookTime - 2);
                }
            }
        } else {
            entity.cookTime = 0;
        }

        boolean isLit = entity.burnTime > 0;
        if (wasLit != isLit) {
            level.setBlock(pos, state.setValue(AlloyMixerBlock.LIT, isLit), 3);
            changed = true;
        }

        if (changed) {
            setChanged(level, pos, state);
        }
    }

    private boolean hasValidIngredients() {
        ItemStack s0 = items.get(0);
        ItemStack s1 = items.get(1);
        ItemStack s2 = items.get(2);
        return s0.is(Items.DIAMOND) && s1.is(Items.IRON_INGOT) && s2.is(Items.COPPER_INGOT);
    }

    private boolean canOutputResult() {
        ItemStack out = items.get(4);
        if (out.isEmpty()) return true;
        if (!out.is(ModItems.DIAMOND_INGOT.get())) return false;
        return out.getCount() < out.getMaxStackSize();
    }

    private void mixAlloy() {
        items.get(0).shrink(1);
        items.get(1).shrink(1);
        items.get(2).shrink(1);

        ItemStack out = items.get(4);
        if (out.isEmpty()) {
            items.set(4, new ItemStack(ModItems.DIAMOND_INGOT.get(), 1));
        } else {
            out.grow(1);
        }
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.DOWN) return new int[]{4};
        if (side == Direction.UP) return new int[]{0, 1, 2};
        return new int[]{3}; // Fuel from sides
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStack, @Nullable Direction direction) {
        if (index == 4) return false;
        if (index == 3) return BrickFurnaceBlockEntity.isValidFuel(itemStack);
        return true;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index == 4;
    }

    @Override
    public int getContainerSize() {
        return 5;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack s : items) if (!s.isEmpty()) return false;
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return ContainerHelper.removeItem(this.items, slot, amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(this.items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        items.set(slot, stack);
        if (stack.getCount() > getMaxStackSize()) {
            stack.setCount(getMaxStackSize());
        }
        setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        items.clear();
    }
}
