package io.marrybye.github.larperthanwolves.block.entity;

import io.marrybye.github.larperthanwolves.block.AdvancedSmelterBlock;
import io.marrybye.github.larperthanwolves.block.ModBlocks;
import io.marrybye.github.larperthanwolves.item.ModItems;
import io.marrybye.github.larperthanwolves.menu.AdvancedSmelterMenu;
import io.marrybye.github.larperthanwolves.recipe.FoodCookingRegistry;
import io.marrybye.github.larperthanwolves.recipe.SmeltingRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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

import java.util.Optional;

public class AdvancedSmelterBlockEntity extends BlockEntity implements WorldlyContainer, MenuProvider, IFueledMachine {
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
    public void setFuelCookSpeed(int cookSpeed) {
        this.fuelCookSpeed = cookSpeed;
        this.cookTimeTotal = cookSpeed;
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
                case 0 -> AdvancedSmelterBlockEntity.this.burnTime;
                case 1 -> AdvancedSmelterBlockEntity.this.maxBurnTime;
                case 2 -> AdvancedSmelterBlockEntity.this.cookTime;
                case 3 -> AdvancedSmelterBlockEntity.this.cookTimeTotal;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> AdvancedSmelterBlockEntity.this.burnTime = value;
                case 1 -> AdvancedSmelterBlockEntity.this.maxBurnTime = value;
                case 2 -> AdvancedSmelterBlockEntity.this.cookTime = value;
                case 3 -> AdvancedSmelterBlockEntity.this.cookTimeTotal = value;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    public AdvancedSmelterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ADVANCED_SMELTER.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.larperthanwolves.advanced_smelter");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new AdvancedSmelterMenu(containerId, playerInventory, this, this.dataAccess, ContainerLevelAccess.create(this.level, this.worldPosition));
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
        wasLitOnce = tag.getBoolean("WasLitOnce");
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AdvancedSmelterBlockEntity entity) {
        if (level.isClientSide) return;

        boolean changed = false;

        // Burn time countdown
        if (entity.burnTime > 0) {
            entity.burnTime--;
            changed = true;
        }

        // Auto-refuel from fuel slot 5 ticks before fire goes out to keep fire roaring
        if (entity.tickFuelAutoFeed()) {
            changed = true;
        }

        // Cooking logic
        if (entity.burnTime > 0) {
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
                entity.cookTimeTotal = entity.fuelCookSpeed;

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

        if (state.hasProperty(AdvancedSmelterBlock.STAGE)) {
            int currentStage = state.getValue(AdvancedSmelterBlock.STAGE);
            if (currentStage != targetStage) {
                level.setBlock(pos, state.setValue(AdvancedSmelterBlock.STAGE, targetStage), 3);
                changed = true;
            }
        }

        if (changed) {
            setChanged(level, pos, state);
        }
    }

    public static ItemStack getSmeltingResult(Level level, ItemStack input) {
        if (input.isEmpty()) return ItemStack.EMPTY;
        if (FoodCookingRegistry.isFood(input)) return ItemStack.EMPTY;

        // Distinct Advanced Smelter Feature: Raw Ores & Chunks smelt directly into FULL INGOTS!
        if (input.is(Items.RAW_IRON)) {
            return new ItemStack(Items.IRON_INGOT, 1);
        }
        if (input.is(Items.RAW_COPPER)) {
            return new ItemStack(Items.COPPER_INGOT, 1);
        }
        if (input.is(Items.RAW_GOLD)) {
            return new ItemStack(Items.GOLD_INGOT, 1);
        }
        if (input.is(ModItems.RAW_TIN.get())) {
            return new ItemStack(ModItems.TIN_INGOT.get(), 1);
        }
        if (input.is(ModItems.RAW_MITHRIL.get())) {
            return new ItemStack(ModItems.MITHRIL_NUGGET.get(), 1);
        }
        if (SmeltingRegistry.isRawZinc(input)) {
            Item zincIngot = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("create", "zinc_ingot"));
            if (zincIngot != Items.AIR) {
                return new ItemStack(zincIngot, 1);
            }
        }

        // Standard materials
        if (input.is(Items.COBBLESTONE)) return new ItemStack(Items.STONE);
        if (input.is(Items.SAND)) return new ItemStack(Items.GLASS);
        if (input.is(ModBlocks.UNFIRED_BRICK.asItem())) return new ItemStack(Items.BRICK);
        if (input.is(Items.CLAY)) return new ItemStack(Items.TERRACOTTA);
        if (input.is(Items.WET_SPONGE)) return new ItemStack(Items.SPONGE);

        if (level != null) {
            SingleRecipeInput recipeInput = new SingleRecipeInput(input);
            Optional<RecipeHolder<SmeltingRecipe>> match = level.getRecipeManager()
                    .getRecipeFor(RecipeType.SMELTING, recipeInput, level);
            if (match.isPresent()) {
                ItemStack result = match.get().value().assemble(recipeInput, level.registryAccess());
                if (result.has(DataComponents.FOOD)) {
                    return ItemStack.EMPTY;
                }
                return result;
            }
        }

        return ItemStack.EMPTY;
    }

    private void smeltItem(int inputSlot, ItemStack result) {
        ItemStack input = items.get(inputSlot);
        input.shrink(1);

        for (int i = 3; i <= 5; i++) {
            ItemStack outputSlot = items.get(i);
            if (outputSlot.isEmpty()) {
                items.set(i, result.copy());
                break;
            } else if (ItemStack.isSameItemSameComponents(outputSlot, result) && outputSlot.getCount() + result.getCount() <= outputSlot.getMaxStackSize()) {
                outputSlot.grow(result.getCount());
                break;
            }
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

    public boolean addFuel(ItemStack fuelStack) {
        if (!FuelRegistry.isValidFuel(fuelStack)) return false;
        FuelRegistry.FuelInfo info = FuelRegistry.getFuelInfo(fuelStack);
        if (info == null) return false;

        if (burnTime > 0) {
            burnTime = Math.min(burnTime + info.burnDuration, 72000);
            maxBurnTime = Math.max(maxBurnTime, burnTime);
            fuelCookSpeed = info.cookSpeed;
            cookTimeTotal = fuelCookSpeed;
            wasLitOnce = true;
            setChanged();
            return true;
        } else {
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

    public boolean lightFurnace() {
        if (lightFromStoredFuel()) {
            this.wasLitOnce = true;
            setChanged();
            return true;
        }
        return false;
    }

    public boolean isLit() {
        return burnTime > 0;
    }

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
        if (slot < 3) return !FoodCookingRegistry.isFood(stack);
        if (slot == 6) return FuelRegistry.isValidFuel(stack);
        return false;
    }

    private static final int[] SLOTS_TOP = new int[]{0, 1, 2};
    private static final int[] SLOTS_OUTPUT = new int[]{3, 4, 5};
    private static final int[] SLOTS_FUEL = new int[]{6};
    private static final int[] SLOTS_NONE = new int[]{};

    @Override
    public int[] getSlotsForFace(Direction side) {
        if (side == null) return SLOTS_NONE;
        Direction facing = this.getBlockState().hasProperty(AdvancedSmelterBlock.FACING) ?
                this.getBlockState().getValue(AdvancedSmelterBlock.FACING) : Direction.NORTH;
        Direction back = facing.getOpposite();

        if (side == back) return SLOTS_FUEL;
        if (side == Direction.DOWN) return SLOTS_OUTPUT;
        if (side == Direction.UP) return SLOTS_TOP;
        return SLOTS_TOP;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction side) {
        if (side == null) return false;

        Direction facing = this.getBlockState().hasProperty(AdvancedSmelterBlock.FACING) ?
                this.getBlockState().getValue(AdvancedSmelterBlock.FACING) : Direction.NORTH;

        if (canAcceptHopperFuel(side, facing, slot, stack)) {
            return true;
        }

        if (side == Direction.UP && slot < 3) {
            return !FoodCookingRegistry.isFood(stack);
        }
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) {
        return side == Direction.DOWN && slot >= 3 && slot <= 5;
    }
}
