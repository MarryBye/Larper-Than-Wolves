package io.marrybye.github.larperthanwolves.block.entity;

import io.marrybye.github.larperthanwolves.block.OvenBlock;
import io.marrybye.github.larperthanwolves.config.ModConfig;
import io.marrybye.github.larperthanwolves.menu.OvenMenu;
import io.marrybye.github.larperthanwolves.recipe.FoodCookingRegistry;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class OvenBlockEntity extends BlockEntity implements WorldlyContainer, MenuProvider, IFueledMachine {
    // 0, 1, 2: Food Inputs; 3, 4, 5: Food Outputs; 6: Stored Fuel
    private final NonNullList<ItemStack> items = NonNullList.withSize(7, ItemStack.EMPTY);

    private int burnTime = 0;
    private int maxBurnTime = 0;
    private int cookTime = 0;
    private int cookTimeTotal = 200;
    private int fuelCookSpeed = 200;
    private boolean wasLitOnce = false;

    @Override
    public int getBurnTime() { return this.burnTime; }

    @Override
    public void setBurnTime(int burnTime) { this.burnTime = burnTime; }

    @Override
    public int getMaxBurnTime() { return this.maxBurnTime; }

    @Override
    public void setMaxBurnTime(int maxBurnTime) { this.maxBurnTime = maxBurnTime; }

    @Override
    public int getFuelCookSpeed() { return this.fuelCookSpeed; }

    @Override
    public double getFuelEfficiencyModifier() {
        return ModConfig.SERVER != null ? ModConfig.SERVER.ovenEfficiencyModifier.get() : 0.90;
    }

    @Override
    public void setFuelCookSpeed(int cookSpeed) {
        this.fuelCookSpeed = cookSpeed;
        this.cookTimeTotal = getEffectiveCookTime(cookSpeed);
    }

    @Override
    public int getFuelSlot() { return 6; }

    @Override
    public ItemStack getFuelItem() { return this.items.get(6); }

    @Override
    public void setFuelItem(ItemStack stack) { this.items.set(6, stack); }

    protected final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> OvenBlockEntity.this.burnTime;
                case 1 -> OvenBlockEntity.this.maxBurnTime;
                case 2 -> OvenBlockEntity.this.cookTime;
                case 3 -> OvenBlockEntity.this.cookTimeTotal;
                case 4 -> (int) Math.round(OvenBlockEntity.this.getFuelEfficiencyModifier() * 100.0);
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> OvenBlockEntity.this.burnTime = value;
                case 1 -> OvenBlockEntity.this.maxBurnTime = value;
                case 2 -> OvenBlockEntity.this.cookTime = value;
                case 3 -> OvenBlockEntity.this.cookTimeTotal = value;
            }
        }

        @Override
        public int getCount() {
            return 5;
        }
    };

    public OvenBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.OVEN.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.larperthanwolves.oven");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new OvenMenu(containerId, playerInventory, this, this.dataAccess, ContainerLevelAccess.create(this.level, this.worldPosition));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, items, registries);
        tag.putInt("BurnTime", burnTime);
        tag.putInt("MaxBurnTime", maxBurnTime);
        tag.putInt("CookTime", cookTime);
        tag.putInt("CookTimeTotal", cookTimeTotal);
        tag.putInt("FuelCookSpeed", fuelCookSpeed);
        tag.putBoolean("WasLitOnce", wasLitOnce);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        ContainerHelper.loadAllItems(tag, items, registries);
        burnTime = tag.getInt("BurnTime");
        maxBurnTime = tag.getInt("MaxBurnTime");
        cookTime = tag.getInt("CookTime");
        cookTimeTotal = tag.getInt("CookTimeTotal");
        fuelCookSpeed = tag.getInt("FuelCookSpeed");
        if (fuelCookSpeed == 0) fuelCookSpeed = 200;
        wasLitOnce = tag.getBoolean("WasLitOnce");
        if (burnTime > 0) wasLitOnce = true;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, OvenBlockEntity entity) {
        if (level.isClientSide) return;

        boolean changed = false;

        if (entity.burnTime > 0) {
            entity.burnTime--;
            changed = true;
        }

        // Auto-refuel from fuel slot 5 ticks before fire goes out
        if (entity.tickFuelAutoFeed()) {
            changed = true;
        }

        // Cooking logic
        if (entity.burnTime > 0) {
            // Find first slot in order (0, 1, 2) that has valid cookable food
            int activeSlot = -1;
            ItemStack resultStack = ItemStack.EMPTY;

            for (int i = 0; i < 3; i++) {
                ItemStack input = entity.items.get(i);
                if (!input.isEmpty() && FoodCookingRegistry.isFood(input)) {
                    ItemStack possibleResult = FoodCookingRegistry.getCookingResult(level, input);
                    if (!possibleResult.isEmpty() && entity.canOutput(possibleResult)) {
                        activeSlot = i;
                        resultStack = possibleResult;
                        break;
                    }
                }
            }

            if (activeSlot != -1) {
                entity.cookTime++;
                entity.cookTimeTotal = entity.getEffectiveCookTime(entity.fuelCookSpeed);

                if (entity.cookTime >= entity.cookTimeTotal) {
                    entity.cookItem(activeSlot, resultStack);
                    entity.cookTime = 0;
                    changed = true;
                }
            } else {
                entity.cookTime = 0;
            }
        } else {
            entity.cookTime = 0;
        }

        int targetStage;
        if (entity.burnTime > 0) {
            targetStage = (entity.maxBurnTime > 0 && entity.burnTime <= entity.maxBurnTime / 4) ? 3 : 2;
        } else if (!entity.items.get(6).isEmpty()) {
            targetStage = 1;
        } else {
            targetStage = 0;
        }

        if (state.hasProperty(OvenBlock.STAGE)) {
            int currentStage = state.getValue(OvenBlock.STAGE);
            if (currentStage != targetStage) {
                level.setBlock(pos, state.setValue(OvenBlock.STAGE, targetStage), 3);
                changed = true;
            }
        }

        if (changed) {
            setChanged(level, pos, state);
        }
    }

    private boolean canOutput(ItemStack result) {
        for (int i = 3; i <= 5; i++) {
            ItemStack slotStack = items.get(i);
            if (slotStack.isEmpty()) return true;
            if (ItemStack.isSameItemSameComponents(slotStack, result) && slotStack.getCount() + result.getCount() <= slotStack.getMaxStackSize()) {
                return true;
            }
        }
        return false;
    }

    private void cookItem(int inputSlotIndex, ItemStack result) {
        for (int i = 3; i <= 5; i++) {
            ItemStack slotStack = items.get(i);
            if (slotStack.isEmpty()) {
                items.set(i, result.copy());
                break;
            } else if (ItemStack.isSameItemSameComponents(slotStack, result) && slotStack.getCount() + result.getCount() <= slotStack.getMaxStackSize()) {
                slotStack.grow(result.getCount());
                break;
            }
        }

        ItemStack input = items.get(inputSlotIndex);
        input.shrink(1);
        if (input.isEmpty()) {
            items.set(inputSlotIndex, ItemStack.EMPTY);
        }
    }

    public boolean addFuel(ItemStack fuelStack) {
        if (!FuelRegistry.isValidFuel(fuelStack)) return false;

        FuelRegistry.FuelInfo info = FuelRegistry.getFuelInfo(fuelStack);

        if (burnTime > 0) {
            burnTime = Math.min(burnTime + info.burnDuration, 72000);
            maxBurnTime = Math.max(maxBurnTime, burnTime);
            fuelCookSpeed = info.cookSpeed;
            cookTimeTotal = getEffectiveCookTime(fuelCookSpeed);
            wasLitOnce = true;
            setChanged();
            return true;
        } else {
            if (items.get(6).isEmpty()) {
                ItemStack fuelCopy = fuelStack.copy();
                fuelCopy.setCount(1);
                items.set(6, fuelCopy);
                fuelCookSpeed = info.cookSpeed;
                cookTimeTotal = getEffectiveCookTime(fuelCookSpeed);
                setChanged();
                return true;
            }
        }
        return false;
    }

    public boolean lightOven() {
        if (lightFromStoredFuel()) {
            this.wasLitOnce = true;
            setChanged();
            return true;
        }
        return false;
    }

    public boolean hasStoredFuel() {
        return !items.get(6).isEmpty();
    }

    public boolean isLit() {
        return burnTime > 0;
    }

    // Container implementation (7 slots: 0..2 input food, 3..5 output food, 6 fuel)
    @Override
    public int getContainerSize() {
        return 7;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack item : items) {
            if (!item.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack result = ContainerHelper.removeItem(items, slot, amount);
        if (!result.isEmpty()) setChanged();
        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(items, slot);
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
        setChanged();
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        // Strictly FOOD ONLY in slots 0..2! No ores or metals allowed.
        if (slot < 3) return FoodCookingRegistry.isFood(stack);
        if (slot == 6) return FuelRegistry.isValidFuel(stack);
        return false;
    }

    // WorldlyContainer for hoppers
    private static final int[] SLOTS_TOP = new int[]{0, 1, 2};
    private static final int[] SLOTS_OUTPUT = new int[]{3, 4, 5};
    private static final int[] SLOTS_FUEL = new int[]{6};
    private static final int[] SLOTS_NONE = new int[]{};

    @Override
    public int[] getSlotsForFace(Direction side) {
        if (side == null) return SLOTS_NONE;

        Direction facing = this.getBlockState().hasProperty(OvenBlock.FACING) ?
                this.getBlockState().getValue(OvenBlock.FACING) : Direction.NORTH;
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
        return SLOTS_OUTPUT;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction side) {
        if (side == null) return false;

        Direction facing = this.getBlockState().hasProperty(OvenBlock.FACING) ?
                this.getBlockState().getValue(OvenBlock.FACING) : Direction.NORTH;

        if (canAcceptHopperFuel(side, facing, slot, stack)) {
            return true;
        }

        if (side == Direction.UP && slot < 3) {
            return FoodCookingRegistry.isFood(stack);
        }

        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) {
        if (side == null) return false;

        Direction facing = this.getBlockState().hasProperty(OvenBlock.FACING) ?
                this.getBlockState().getValue(OvenBlock.FACING) : Direction.NORTH;
        Direction back = facing.getOpposite();

        if (side == back || side == Direction.UP) {
            return false;
        }

        return slot >= 3 && slot <= 5;
    }
}
