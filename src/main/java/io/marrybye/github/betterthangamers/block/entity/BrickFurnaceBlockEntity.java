package io.marrybye.github.betterthangamers.block.entity;

import io.marrybye.github.betterthangamers.block.ModBlocks;
import io.marrybye.github.betterthangamers.item.ModItems;
import io.marrybye.github.betterthangamers.menu.BrickFurnaceMenu;
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

public class BrickFurnaceBlockEntity extends BlockEntity implements WorldlyContainer, MenuProvider {
    // 0, 1, 2: Inputs; 3, 4, 5: Outputs
    private final NonNullList<ItemStack> items = NonNullList.withSize(6, ItemStack.EMPTY);
    private ItemStack storedFuel = ItemStack.EMPTY;

    private int burnTime = 0;
    private int maxBurnTime = 0;
    private int cookTime = 0;
    private int cookTimeTotal = 200;
    private int fuelCookSpeed = 200;

    public static class FuelInfo {
        public final int burnDuration;
        public final int cookSpeed;

        public FuelInfo(int burnDuration, int cookSpeed) {
            this.burnDuration = burnDuration;
            this.cookSpeed = cookSpeed;
        }
    }

    private static final Map<Item, FuelInfo> FUEL_REGISTRY = new HashMap<>();
    private static final FuelInfo DRY_GRASS_INFO = new FuelInfo(600, 200);

    static {
        // Wood / Logs / Planks: 60s burn (1200 ticks), 8s cook speed (160 ticks)
        FuelInfo woodInfo = new FuelInfo(1200, 160);
        FUEL_REGISTRY.put(Items.OAK_LOG, woodInfo);
        FUEL_REGISTRY.put(Items.BIRCH_LOG, woodInfo);
        FUEL_REGISTRY.put(Items.SPRUCE_LOG, woodInfo);
        FUEL_REGISTRY.put(Items.JUNGLE_LOG, woodInfo);
        FUEL_REGISTRY.put(Items.ACACIA_LOG, woodInfo);
        FUEL_REGISTRY.put(Items.DARK_OAK_LOG, woodInfo);
        FUEL_REGISTRY.put(Items.MANGROVE_LOG, woodInfo);
        FUEL_REGISTRY.put(Items.CHERRY_LOG, woodInfo);
        FUEL_REGISTRY.put(Items.OAK_WOOD, woodInfo);
        FUEL_REGISTRY.put(Items.BIRCH_WOOD, woodInfo);
        FUEL_REGISTRY.put(Items.SPRUCE_WOOD, woodInfo);
        FUEL_REGISTRY.put(Items.JUNGLE_WOOD, woodInfo);
        FUEL_REGISTRY.put(Items.ACACIA_WOOD, woodInfo);
        FUEL_REGISTRY.put(Items.DARK_OAK_WOOD, woodInfo);
        FUEL_REGISTRY.put(Items.OAK_PLANKS, woodInfo);
        FUEL_REGISTRY.put(Items.BIRCH_PLANKS, woodInfo);
        FUEL_REGISTRY.put(Items.SPRUCE_PLANKS, woodInfo);
        FUEL_REGISTRY.put(Items.JUNGLE_PLANKS, woodInfo);
        FUEL_REGISTRY.put(Items.ACACIA_PLANKS, woodInfo);
        FUEL_REGISTRY.put(Items.DARK_OAK_PLANKS, woodInfo);

        // Charcoal: 80s burn (1600 ticks), 6s cook speed (120 ticks)
        FUEL_REGISTRY.put(Items.CHARCOAL, new FuelInfo(1600, 120));

        // Coal: 100s burn (2000 ticks), 5s cook speed (100 ticks)
        FUEL_REGISTRY.put(Items.COAL, new FuelInfo(2000, 100));

        // Coal Block: 900s burn (18000 ticks), 4s cook speed (80 ticks)
        FUEL_REGISTRY.put(Items.COAL_BLOCK, new FuelInfo(18000, 80));
    }

    public static boolean isValidFuel(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (stack.is(ModItems.DRY_GRASS.get())) return true;
        return FUEL_REGISTRY.containsKey(stack.getItem());
    }

    public static FuelInfo getFuelInfo(ItemStack stack) {
        if (stack.isEmpty()) return null;
        if (stack.is(ModItems.DRY_GRASS.get())) return DRY_GRASS_INFO;
        return FUEL_REGISTRY.get(stack.getItem());
    }

    protected final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> BrickFurnaceBlockEntity.this.burnTime;
                case 1 -> BrickFurnaceBlockEntity.this.maxBurnTime;
                case 2 -> BrickFurnaceBlockEntity.this.cookTime;
                case 3 -> BrickFurnaceBlockEntity.this.cookTimeTotal;
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
            return 4;
        }
    };

    public BrickFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BRICK_FURNACE.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.betterthangamers.brick_furnace");
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
        if (!storedFuel.isEmpty()) {
            CompoundTag fuelTag = new CompoundTag();
            storedFuel.save(registries, fuelTag);
            tag.put("StoredFuel", fuelTag);
        }
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
        if (tag.contains("StoredFuel")) {
            storedFuel = ItemStack.parseOptional(registries, tag.getCompound("StoredFuel"));
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, BrickFurnaceBlockEntity entity) {
        if (level.isClientSide) return;

        boolean wasLit = entity.burnTime > 0;
        boolean changed = false;

        if (entity.burnTime > 0) {
            entity.burnTime--;
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

        boolean isLit = entity.burnTime > 0;
        if (wasLit != isLit) {
            level.setBlock(pos, state.setValue(io.marrybye.github.betterthangamers.block.BrickFurnaceBlock.LIT, isLit), 3);
            changed = true;
        }

        if (changed) {
            setChanged(level, pos, state);
        }
    }

    private static ItemStack getSmeltingResult(Level level, ItemStack input) {
        Item inputItem = input.getItem();

        // 1. BetterThanGamers Ore processing: Raw ores -> 1 nugget (dust does not smelt)
        if (inputItem == Items.RAW_IRON) return new ItemStack(Items.IRON_NUGGET, 1);
        if (inputItem == Items.RAW_COPPER) return new ItemStack(ModItems.COPPER_NUGGET.get(), 1);
        if (inputItem == Items.RAW_GOLD) return new ItemStack(Items.GOLD_NUGGET, 1);

        // 2. Food & basic blocks
        if (inputItem == Items.BEEF) return new ItemStack(Items.COOKED_BEEF);
        if (inputItem == Items.PORKCHOP) return new ItemStack(Items.COOKED_PORKCHOP);
        if (inputItem == Items.MUTTON) return new ItemStack(Items.COOKED_MUTTON);
        if (inputItem == Items.CHICKEN) return new ItemStack(Items.COOKED_CHICKEN);
        if (inputItem == Items.RABBIT) return new ItemStack(Items.COOKED_RABBIT);
        if (inputItem == Items.COD) return new ItemStack(Items.COOKED_COD);
        if (inputItem == Items.SALMON) return new ItemStack(Items.COOKED_SALMON);
        if (inputItem == Items.POTATO) return new ItemStack(Items.BAKED_POTATO);
        if (inputItem == Items.KELP) return new ItemStack(Items.DRIED_KELP);
        if (inputItem == Items.COBBLESTONE) return new ItemStack(Items.STONE);
        if (inputItem == Items.SAND) return new ItemStack(Items.GLASS);
        if (inputItem == ModBlocks.UNFIRED_BRICK.asItem()) return new ItemStack(Items.BRICK, 1);
        if (inputItem == Items.CLAY) return new ItemStack(Items.TERRACOTTA);
        if (inputItem == Items.WET_SPONGE) return new ItemStack(Items.SPONGE);

        // 3. Fallback to vanilla Smelting recipe registry
        SingleRecipeInput recipeInput = new SingleRecipeInput(input);
        Optional<RecipeHolder<SmeltingRecipe>> match = level.getRecipeManager()
                .getRecipeFor(RecipeType.SMELTING, recipeInput, level);

        if (match.isPresent()) {
            return match.get().value().assemble(recipeInput, level.registryAccess());
        }

        return ItemStack.EMPTY;
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
        // Find output slot
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

        // Shrink input slot
        ItemStack input = items.get(inputSlotIndex);
        input.shrink(1);
        if (input.isEmpty()) {
            items.set(inputSlotIndex, ItemStack.EMPTY);
        }
    }

    // Load 1 fuel item into furnace
    public boolean addFuel(ItemStack fuelStack) {
        if (!isValidFuel(fuelStack)) return false;

        FuelInfo info = getFuelInfo(fuelStack);

        if (burnTime > 0) {
            // Already lit: extend burn time up to max allowed for that fuel
            burnTime = Math.min(burnTime + info.burnDuration, info.burnDuration);
            maxBurnTime = Math.max(maxBurnTime, info.burnDuration);
            fuelCookSpeed = info.cookSpeed;
            cookTimeTotal = fuelCookSpeed;
            setChanged();
            return true;
        } else {
            // Store fuel item
            if (storedFuel.isEmpty()) {
                storedFuel = fuelStack.copy();
                storedFuel.setCount(1);
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
        if (burnTime > 0) return false;

        if (!storedFuel.isEmpty() && isValidFuel(storedFuel)) {
            FuelInfo info = getFuelInfo(storedFuel);
            burnTime = info.burnDuration;
            maxBurnTime = info.burnDuration;
            fuelCookSpeed = info.cookSpeed;
            cookTimeTotal = fuelCookSpeed;
            storedFuel = ItemStack.EMPTY;
            setChanged();
            return true;
        }
        return false;
    }

    public boolean hasStoredFuel() {
        return !storedFuel.isEmpty();
    }

    public boolean isLit() {
        return burnTime > 0;
    }

    // Container implementation (6 slots: 0..2 input, 3..5 output)
    @Override
    public int getContainerSize() {
        return 6;
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
        return slot < 3; // Only slots 0, 1, 2 can receive inputs
    }

    // WorldlyContainer for hoppers
    private static final int[] SLOTS_TOP = new int[]{0, 1, 2};
    private static final int[] SLOTS_BOTTOM = new int[]{3, 4, 5};
    private static final int[] SLOTS_SIDES = new int[]{0, 1, 2};

    @Override
    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.DOWN) return SLOTS_BOTTOM;
        return SLOTS_TOP;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction side) {
        return slot < 3;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) {
        return slot >= 3;
    }
}


