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
    // 0: Diamond, 1: Iron, 2: Copper, 3: Result Diamond Ingot, 4: Stored Fuel
    private final NonNullList<ItemStack> items = NonNullList.withSize(5, ItemStack.EMPTY);

    private int burnTime = 0;
    private int maxBurnTime = 0;
    private int cookTime = 0;
    private int cookTimeTotal = 600;
    private boolean wasLitOnce = false;

    public AlloyMixerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ALLOY_MIXER.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.larperthanwolves.alloy_mixer");
    }

    public boolean isLit() {
        return this.burnTime > 0;
    }

    public int getBurnTime() {
        return this.burnTime;
    }

    public int getMaxBurnTime() {
        return this.maxBurnTime;
    }

    public boolean addFuel(ItemStack fuelStack) {
        if (!BrickFurnaceBlockEntity.isValidFuel(fuelStack)) return false;

        BrickFurnaceBlockEntity.FuelInfo info = BrickFurnaceBlockEntity.getFuelInfo(fuelStack);
        if (info == null) return false;

        if (this.burnTime > 0) {
            if (this.burnTime + info.burnDuration <= 72000) {
                this.burnTime += info.burnDuration;
                this.maxBurnTime = Math.max(this.maxBurnTime, this.burnTime);
                this.wasLitOnce = true;
                setChanged();
                return true;
            }
            return false;
        }

        if (this.items.get(4).isEmpty()) {
            this.items.set(4, fuelStack.copyWithCount(1));
            setChanged();
            return true;
        }

        return false;
    }

    public boolean lightMixer() {
        if (this.burnTime > 0) return false;

        ItemStack stored = this.items.get(4);
        if (!stored.isEmpty() && BrickFurnaceBlockEntity.isValidFuel(stored)) {
            BrickFurnaceBlockEntity.FuelInfo info = BrickFurnaceBlockEntity.getFuelInfo(stored);
            if (info != null) {
                this.burnTime = info.burnDuration;
                this.maxBurnTime = info.burnDuration;
                this.wasLitOnce = true;
                this.items.set(4, ItemStack.EMPTY);
                setChanged();
                return true;
            }
        }
        return false;
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
        tag.putBoolean("WasLitOnce", this.wasLitOnce);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        ContainerHelper.loadAllItems(tag, this.items, registries);
        this.burnTime = tag.getInt("BurnTime");
        this.maxBurnTime = tag.getInt("MaxBurnTime");
        this.cookTime = tag.getInt("CookTime");
        this.cookTimeTotal = tag.getInt("CookTimeTotal");
        this.wasLitOnce = tag.getBoolean("WasLitOnce");
        if (this.burnTime > 0) this.wasLitOnce = true;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AlloyMixerBlockEntity entity) {
        if (level.isClientSide) return;

        boolean changed = false;

        if (entity.burnTime > 0) {
            entity.burnTime--;
            changed = true;
        }

        int configuredCook = ModConfig.SERVER != null ? ModConfig.SERVER.alloyMixerCookTimeTicks.get() : 600;
        entity.cookTimeTotal = configuredCook > 0 ? configuredCook : 600;

        boolean hasIngredients = entity.hasValidIngredients();
        boolean canOutput = entity.canOutputResult();

        // When burnTime reaches 0, mixer stops and requires manual ignition with lighter/flint&steel

        if (hasIngredients && canOutput && entity.burnTime > 0) {
            entity.cookTime++;
            if (entity.cookTime >= entity.cookTimeTotal) {
                entity.mixAlloy();
                entity.cookTime = 0;
                changed = true;
            }
        } else {
            if (entity.cookTime > 0) {
                entity.cookTime = Math.max(0, entity.cookTime - 2);
                changed = true;
            }
        }

        int targetStage;
        if (entity.burnTime > 0) {
            targetStage = (entity.maxBurnTime > 0 && entity.burnTime <= entity.maxBurnTime / 4) ? 3 : 2;
        } else if (!entity.items.get(4).isEmpty()) {
            targetStage = 1;
        } else {
            targetStage = 0;
        }

        if (state.hasProperty(AlloyMixerBlock.STAGE)) {
            int currentStage = state.getValue(AlloyMixerBlock.STAGE);
            if (currentStage != targetStage) {
                level.setBlock(pos, state.setValue(AlloyMixerBlock.STAGE, targetStage), 3);
                changed = true;
            }
        }

        if (changed) {
            setChanged(level, pos, state);
        }
    }

    private int findDiamondSlot() {
        for (int i = 0; i < 3; i++) {
            if (items.get(i).is(Items.DIAMOND)) return i;
        }
        return -1;
    }

    private int findIronSlot(int excludeSlot) {
        for (int i = 0; i < 3; i++) {
            if (i != excludeSlot && items.get(i).is(Items.IRON_INGOT)) return i;
        }
        return -1;
    }

    private int findCopperSlot(int exclude1, int exclude2) {
        for (int i = 0; i < 3; i++) {
            if (i != exclude1 && i != exclude2 && items.get(i).is(Items.COPPER_INGOT)) return i;
        }
        return -1;
    }

    public enum ActiveRecipe {
        NONE,
        DIAMOND_INGOT,
        BRONZE_INGOT
    }

    public ActiveRecipe getActiveRecipe() {
        if (canMakeDiamondIngot()) return ActiveRecipe.DIAMOND_INGOT;
        if (canMakeBronzeIngot()) return ActiveRecipe.BRONZE_INGOT;
        return ActiveRecipe.NONE;
    }

    private boolean canMakeDiamondIngot() {
        int d = findDiamondSlot();
        if (d == -1) return false;
        int fe = findIronSlot(d);
        if (fe == -1) return false;
        int cu = findCopperSlot(d, fe);
        return cu != -1;
    }

    private boolean canMakeBronzeIngot() {
        int copperCount = 0;
        int tinCount = 0;
        for (int i = 0; i < 3; i++) {
            ItemStack stack = items.get(i);
            if (stack.isEmpty()) continue;
            if (stack.is(Items.COPPER_INGOT)) {
                copperCount += stack.getCount();
            } else if (stack.is(ModItems.TIN_INGOT.get())) {
                tinCount += stack.getCount();
            } else {
                return false;
            }
        }
        return copperCount >= 2 && tinCount >= 1;
    }

    public boolean hasValidIngredients() {
        return getActiveRecipe() != ActiveRecipe.NONE;
    }

    public boolean canOutputResult() {
        ActiveRecipe recipe = getActiveRecipe();
        if (recipe == ActiveRecipe.NONE) return false;

        ItemStack targetResult = (recipe == ActiveRecipe.DIAMOND_INGOT) ?
                new ItemStack(ModItems.DIAMOND_INGOT.get(), 1) :
                new ItemStack(ModItems.BRONZE_INGOT.get(), 1);

        ItemStack out = items.get(3);
        if (out.isEmpty()) return true;
        if (!ItemStack.isSameItemSameComponents(out, targetResult)) return false;
        return out.getCount() + targetResult.getCount() <= out.getMaxStackSize();
    }

    private void mixAlloy() {
        ActiveRecipe recipe = getActiveRecipe();
        if (recipe == ActiveRecipe.NONE) return;

        if (recipe == ActiveRecipe.DIAMOND_INGOT) {
            int d = findDiamondSlot();
            if (d == -1) return;
            int fe = findIronSlot(d);
            if (fe == -1) return;
            int cu = findCopperSlot(d, fe);
            if (cu == -1) return;

            items.get(d).shrink(1);
            items.get(fe).shrink(1);
            items.get(cu).shrink(1);

            if (items.get(d).isEmpty()) items.set(d, ItemStack.EMPTY);
            if (items.get(fe).isEmpty()) items.set(fe, ItemStack.EMPTY);
            if (items.get(cu).isEmpty()) items.set(cu, ItemStack.EMPTY);

            ItemStack out = items.get(3);
            if (out.isEmpty()) {
                items.set(3, new ItemStack(ModItems.DIAMOND_INGOT.get(), 1));
            } else {
                out.grow(1);
            }
        } else if (recipe == ActiveRecipe.BRONZE_INGOT) {
            int neededCopper = 2;
            for (int i = 0; i < 3 && neededCopper > 0; i++) {
                ItemStack stack = items.get(i);
                if (stack.is(Items.COPPER_INGOT)) {
                    int take = Math.min(neededCopper, stack.getCount());
                    stack.shrink(take);
                    neededCopper -= take;
                }
            }

            int neededTin = 1;
            for (int i = 0; i < 3 && neededTin > 0; i++) {
                ItemStack stack = items.get(i);
                if (stack.is(ModItems.TIN_INGOT.get())) {
                    int take = Math.min(neededTin, stack.getCount());
                    stack.shrink(take);
                    neededTin -= take;
                }
            }

            for (int i = 0; i < 3; i++) {
                if (items.get(i).isEmpty()) {
                    items.set(i, ItemStack.EMPTY);
                }
            }

            ItemStack out = items.get(3);
            if (out.isEmpty()) {
                items.set(3, new ItemStack(ModItems.BRONZE_INGOT.get(), 1));
            } else {
                out.grow(1);
            }
        }
    }

    private static final int[] SLOTS_TOP = new int[]{0, 1, 2};
    private static final int[] SLOTS_OUTPUT = new int[]{3};
    private static final int[] SLOTS_FUEL = new int[]{4};
    private static final int[] SLOTS_NONE = new int[]{};

    @Override
    public int[] getSlotsForFace(Direction side) {
        if (side == null) return SLOTS_NONE;

        Direction facing = this.getBlockState().hasProperty(AlloyMixerBlock.FACING) ?
                this.getBlockState().getValue(AlloyMixerBlock.FACING) : Direction.NORTH;
        Direction back = facing.getOpposite();

        if (side == back) {
            return SLOTS_FUEL;
        }
        if (side == Direction.DOWN) {
            return SLOTS_OUTPUT;
        }
        if (side == Direction.UP) {
            return SLOTS_TOP;
        }
        // Output from sides (left, right, front)
        return SLOTS_OUTPUT;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStack, @Nullable Direction side) {
        if (side == null) return false;

        Direction facing = this.getBlockState().hasProperty(AlloyMixerBlock.FACING) ?
                this.getBlockState().getValue(AlloyMixerBlock.FACING) : Direction.NORTH;
        Direction back = facing.getOpposite();

        // Fuel from back: only when old fuel finished burning (burnTime <= 0) and slot 4 is empty
        if (side == back && index == 4) {
            return BrickFurnaceBlockEntity.isValidFuel(itemStack) && this.burnTime <= 0 && this.items.get(4).isEmpty();
        }

        // Inputs from top: allow placing valid mixer ingredients (Diamond, Iron Ingot, Copper Ingot, Tin Ingot)
        if (side == Direction.UP && index < 3) {
            return itemStack.is(Items.DIAMOND) ||
                    itemStack.is(Items.IRON_INGOT) ||
                    itemStack.is(Items.COPPER_INGOT) ||
                    itemStack.is(ModItems.TIN_INGOT.get());
        }

        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction side) {
        if (side == null) return false;

        Direction facing = this.getBlockState().hasProperty(AlloyMixerBlock.FACING) ?
                this.getBlockState().getValue(AlloyMixerBlock.FACING) : Direction.NORTH;
        Direction back = facing.getOpposite();

        if (side == back || side == Direction.UP) {
            return false;
        }

        // Outputs from down and sides
        return index == 3;
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
