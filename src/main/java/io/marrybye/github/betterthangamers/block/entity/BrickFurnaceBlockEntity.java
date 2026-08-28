package io.marrybye.github.betterthangamers.block.entity;

import io.marrybye.github.betterthangamers.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class BrickFurnaceBlockEntity extends BlockEntity {
    private final NonNullList<ItemStack> inputSlots = NonNullList.withSize(3, ItemStack.EMPTY);
    private final NonNullList<ItemStack> outputSlots = NonNullList.withSize(3, ItemStack.EMPTY);
    private ItemStack fuel = ItemStack.EMPTY;

    private int burnTime = 0;
    private int maxBurnTime = 0;
    private int cookTime = 0;
    private int cookTimeTotal = 200; // 10 seconds for 1 item with dry grass

    // Fuel burn times (in ticks)
    private static final Map<Item, Integer> FUEL_TIMES = new HashMap<>();

    static {
        FUEL_TIMES.put(ModItems.DRY_GRASS.get(), 600); // 30 seconds
        FUEL_TIMES.put(Items.OAK_LOG, 1200); // 60 seconds
        FUEL_TIMES.put(Items.BIRCH_LOG, 1200);
        FUEL_TIMES.put(Items.SPRUCE_LOG, 1200);
        FUEL_TIMES.put(Items.JUNGLE_LOG, 1200);
        FUEL_TIMES.put(Items.ACACIA_LOG, 1200);
        FUEL_TIMES.put(Items.DARK_OAK_LOG, 1200);
        FUEL_TIMES.put(Items.CHARCOAL, 1600); // 80 seconds
        FUEL_TIMES.put(Items.COAL, 1600); // 80 seconds
        FUEL_TIMES.put(Items.COAL_BLOCK, 16000); // 800 seconds
    }

    public BrickFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BRICK_FURNACE.get(), pos, state);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, inputSlots, registries);
        tag.putInt("BurnTime", burnTime);
        tag.putInt("MaxBurnTime", maxBurnTime);
        tag.putInt("CookTime", cookTime);
        tag.putInt("CookTimeTotal", cookTimeTotal);
        if (!fuel.isEmpty()) {
            CompoundTag fuelTag = new CompoundTag();
            fuel.save(registries, fuelTag);
            tag.put("Fuel", fuelTag);
        }
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        ContainerHelper.loadAllItems(tag, inputSlots, registries);
        burnTime = tag.getInt("BurnTime");
        maxBurnTime = tag.getInt("MaxBurnTime");
        cookTime = tag.getInt("CookTime");
        cookTimeTotal = tag.getInt("CookTimeTotal");
        if (tag.contains("Fuel")) {
            fuel = ItemStack.parseOptional(registries, tag.getCompound("Fuel"));
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, BrickFurnaceBlockEntity entity) {
        if (level.isClientSide) return;

        boolean changed = false;

        // Decrease burn time
        if (entity.burnTime > 0) {
            entity.burnTime--;
            if (entity.burnTime <= 0) {
                entity.maxBurnTime = 0;
            }
            changed = true;
        }

        // Update block lit state
        boolean isLit = entity.burnTime > 0;
        if (state.getValue(io.marrybye.github.betterthangamers.block.BrickFurnaceBlock.LIT) != isLit) {
            level.setBlock(pos, state.setValue(io.marrybye.github.betterthangamers.block.BrickFurnaceBlock.LIT, isLit), 3);
            changed = true;
        }

        // Handle cooking
        if (entity.burnTime > 0) {
            // Find first input slot with item
            for (int i = 0; i < entity.inputSlots.size(); i++) {
                ItemStack input = entity.inputSlots.get(i);
                if (!input.isEmpty()) {
                    entity.cookTime++;

                    int cookTimeForItem = 200; // Default 10 seconds

                    if (entity.cookTime >= cookTimeForItem) {
                        // Cook the item
                        if (cookItem(input, entity.outputSlots)) {
                            input.shrink(1);
                            if (input.isEmpty()) {
                                entity.inputSlots.set(i, ItemStack.EMPTY);
                            }
                            entity.cookTime = 0;
                            changed = true;
                        }
                    }
                    break;
                }
            }
        } else {
            entity.cookTime = 0;
        }

        if (changed) {
            setChanged(level, pos, state);
        }
    }

    private static boolean cookItem(ItemStack input, NonNullList<ItemStack> outputSlots) {
        Item inputItem = input.getItem();

        // Map of inputs to outputs (simplified)
        Map<Item, ItemStack> recipes = new HashMap<>();
        recipes.put(Items.RAW_IRON, new ItemStack(Items.IRON_NUGGET, 1));
        recipes.put(Items.RAW_COPPER, new ItemStack(ModItems.COPPER_DUST.get(), 1));
        recipes.put(Items.RAW_GOLD, new ItemStack(Items.GOLD_NUGGET, 1));

        if (recipes.containsKey(inputItem)) {
            ItemStack output = recipes.get(inputItem);
            // Try to add to output slots
            for (ItemStack slot : outputSlots) {
                if (slot.isEmpty() || (slot.is(output.getItem()) && slot.getCount() < slot.getMaxStackSize())) {
                    slot.grow(1);
                    return true;
                }
            }
        }

        return false;
    }

    public void lightFurnace() {
        if (!fuel.isEmpty() && FUEL_TIMES.containsKey(fuel.getItem())) {
            burnTime = FUEL_TIMES.get(fuel.getItem());
            maxBurnTime = burnTime;
            fuel.shrink(1);
            if (fuel.isEmpty()) {
                fuel = ItemStack.EMPTY;
            }
            setChanged();
        }
    }

    public void addFuel(ItemStack fuelStack) {
        if (FUEL_TIMES.containsKey(fuelStack.getItem())) {
            // Don't add fuel if already at max burn time
            if (maxBurnTime > 0 && burnTime >= maxBurnTime) {
                return;
            }
            // Add to fuel slot (only 1 item at a time)
            if (fuel.isEmpty()) {
                fuel = fuelStack.copy();
                fuel.setCount(1);
            }
        }
    }


    public int getBurnTime() {
        return burnTime;
    }

    public int getMaxBurnTime() {
        return maxBurnTime;
    }

    public int getCookTime() {
        return cookTime;
    }

    public ItemStack getFuel() {
        return fuel;
    }

    public NonNullList<ItemStack> getInputSlots() {
        return inputSlots;
    }

    public NonNullList<ItemStack> getOutputSlots() {
        return outputSlots;
    }
}

