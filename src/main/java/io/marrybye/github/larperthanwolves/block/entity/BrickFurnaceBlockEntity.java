package io.marrybye.github.larperthanwolves.block.entity;

import io.marrybye.github.larperthanwolves.block.BrickFurnaceBlock;
import io.marrybye.github.larperthanwolves.block.ModBlocks;
import io.marrybye.github.larperthanwolves.config.ModConfig;
import io.marrybye.github.larperthanwolves.item.ModItems;
import io.marrybye.github.larperthanwolves.menu.BrickFurnaceMenu;
import io.marrybye.github.larperthanwolves.recipe.SmeltingRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BrickFurnaceBlockEntity extends BlockEntity implements WorldlyContainer, MenuProvider, IFueledMachine {
    // 0, 1, 2: Inputs; 3, 4, 5: Outputs; 6: Stored Fuel
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
        return ModConfig.SERVER != null ? ModConfig.SERVER.brickFurnaceEfficiencyModifier.get() : 0.85;
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
                case 0 -> BrickFurnaceBlockEntity.this.burnTime;
                case 1 -> BrickFurnaceBlockEntity.this.maxBurnTime;
                case 2 -> BrickFurnaceBlockEntity.this.cookTime;
                case 3 -> BrickFurnaceBlockEntity.this.cookTimeTotal;
                case 4 -> (int) Math.round(BrickFurnaceBlockEntity.this.getFuelEfficiencyModifier() * 100.0);
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> BrickFurnaceBlockEntity.this.burnTime = value;
                case 1 -> BrickFurnaceBlockEntity.this.maxBurnTime = value;
                case 2 -> BrickFurnaceBlockEntity.this.cookTime = value;
                case 3 -> BrickFurnaceBlockEntity.this.cookTimeTotal = value;
            }
        }

        @Override
        public int getCount() {
            return 5;
        }
    };

    public BrickFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BRICK_FURNACE.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.larperthanwolves.brick_furnace");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new BrickFurnaceMenu(containerId, playerInventory, this, this.dataAccess, ContainerLevelAccess.create(this.level, this.worldPosition));
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

    public static void tick(Level level, BlockPos pos, BlockState state, BrickFurnaceBlockEntity entity) {
        if (level.isClientSide) return;

        boolean changed = false;

        if (entity.burnTime > 0) {
            entity.burnTime--;
            changed = true;
        }

        // Auto-refuel from fuel slot 5 ticks before fire goes out to prevent flame dying
        if (entity.tickFuelAutoFeed()) {
            changed = true;
        }

        // Cooking logic
        if (entity.burnTime > 0) {
            // Find first slot in order (0, 1, 2) that can be smelted
            int activeSlot = -1;
            ItemStack resultStack = ItemStack.EMPTY;

            for (int i = 0; i < 3; i++) {
                ItemStack input = entity.items.get(i);
                if (!input.isEmpty()) {
                    ItemStack possibleResult = getSmeltingResult(level, input);
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
                    entity.smeltItem(activeSlot, resultStack);
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

        if (state.hasProperty(BrickFurnaceBlock.STAGE)) {
            int currentStage = state.getValue(BrickFurnaceBlock.STAGE);
            if (currentStage != targetStage) {
                level.setBlock(pos, state.setValue(BrickFurnaceBlock.STAGE, targetStage), 3);
                changed = true;
            }
        }

        if (changed) {
            setChanged(level, pos, state);
        }
    }

    public boolean hasSmeltableItem(Level level) {
        for (int i = 0; i < 3; i++) {
            ItemStack input = items.get(i);
            if (!input.isEmpty()) {
                ItemStack res = getSmeltingResult(level, input);
                if (!res.isEmpty() && canOutput(res)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static ItemStack getSmeltingResult(Level level, ItemStack input) {
        return SmeltingRegistry.getSmeltingResult(level, input);
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

    private void smeltItem(int inputSlotIndex, ItemStack result) {
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

    // Load 1 fuel item into furnace
    public boolean addFuel(ItemStack fuelStack) {
        if (!FuelRegistry.isValidFuel(fuelStack)) return false;

        FuelRegistry.FuelInfo info = FuelRegistry.getFuelInfo(fuelStack);

        if (burnTime > 0) {
            // Already lit: extend burn time
            burnTime = Math.min(burnTime + info.burnDuration, 72000);
            maxBurnTime = Math.max(maxBurnTime, burnTime);
            fuelCookSpeed = info.cookSpeed;
            cookTimeTotal = getEffectiveCookTime(fuelCookSpeed);
            wasLitOnce = true;
            setChanged();
            return true;
        } else {
            // Store fuel item in slot 6
            if (items.get(6).isEmpty()) {
                ItemStack fuelCopy = fuelStack.copy();
                fuelCopy.setCount(1);
                items.set(6, fuelCopy);
                fuelCookSpeed = info.cookSpeed;
                cookTimeTotal = fuelCookSpeed;
                setChanged();
                return true;
            }
        }
        return false;
    }

    // Light furnace with lighter
    public boolean lightFurnace() {
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

    // Container implementation (7 slots: 0..2 input, 3..5 output, 6 fuel)
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
        // Food CANNOT be placed in Brick Furnace input slots (0..2)!
        if (slot < 3) return !io.marrybye.github.larperthanwolves.recipe.FoodCookingRegistry.isFood(stack);
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

        Direction facing = this.getBlockState().hasProperty(BrickFurnaceBlock.FACING) ?
                this.getBlockState().getValue(BrickFurnaceBlock.FACING) : Direction.NORTH;
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
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction side) {
        if (side == null) return false;

        Direction facing = this.getBlockState().hasProperty(BrickFurnaceBlock.FACING) ?
                this.getBlockState().getValue(BrickFurnaceBlock.FACING) : Direction.NORTH;

        // Fuel from back: smart hopper loading (only when unlit or <= 20 ticks before fire extinguishes, exactly 1 piece)
        if (canAcceptHopperFuel(side, facing, slot, stack)) {
            return true;
        }

        // Inputs from top (No food allowed)
        if (side == Direction.UP && slot < 3) {
            return !io.marrybye.github.larperthanwolves.recipe.FoodCookingRegistry.isFood(stack);
        }

        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) {
        if (side == null) return false;

        Direction facing = this.getBlockState().hasProperty(BrickFurnaceBlock.FACING) ?
                this.getBlockState().getValue(BrickFurnaceBlock.FACING) : Direction.NORTH;
        Direction back = facing.getOpposite();

        if (side == back || side == Direction.UP) {
            return false;
        }

        // Outputs from down and sides
        return slot >= 3 && slot <= 5;
    }
}
