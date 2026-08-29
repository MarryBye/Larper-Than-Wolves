package io.marrybye.github.larperthanwolves.recipe;

import io.marrybye.github.larperthanwolves.block.ModBlocks;
import io.marrybye.github.larperthanwolves.item.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class SmeltingRegistry {
    private static final Map<Item, Supplier<ItemStack>> CUSTOM_SMELTING = new HashMap<>();

    static {
        registerDefaults();
    }

    public static void registerDefaults() {
        CUSTOM_SMELTING.clear();

        // 1. Raw ores -> 1 nugget (iron, copper, gold, tin)
        register(Items.RAW_IRON, () -> new ItemStack(Items.IRON_NUGGET, 1));
        register(Items.RAW_COPPER, () -> new ItemStack(ModItems.COPPER_NUGGET.get(), 1));
        register(Items.RAW_GOLD, () -> new ItemStack(Items.GOLD_NUGGET, 1));
        register(ModItems.RAW_TIN.get(), () -> new ItemStack(ModItems.TIN_NUGGET.get(), 1));

        // 2. Food & basic blocks
        register(Items.BEEF, () -> new ItemStack(Items.COOKED_BEEF));
        register(Items.PORKCHOP, () -> new ItemStack(Items.COOKED_PORKCHOP));
        register(Items.MUTTON, () -> new ItemStack(Items.COOKED_MUTTON));
        register(Items.CHICKEN, () -> new ItemStack(Items.COOKED_CHICKEN));
        register(Items.RABBIT, () -> new ItemStack(Items.COOKED_RABBIT));
        register(Items.COD, () -> new ItemStack(Items.COOKED_COD));
        register(Items.SALMON, () -> new ItemStack(Items.COOKED_SALMON));
        register(Items.POTATO, () -> new ItemStack(Items.BAKED_POTATO));
        register(Items.KELP, () -> new ItemStack(Items.DRIED_KELP));
        register(Items.COBBLESTONE, () -> new ItemStack(Items.STONE));
        register(Items.SAND, () -> new ItemStack(Items.GLASS));
        register(ModBlocks.UNFIRED_BRICK.asItem(), () -> new ItemStack(Items.BRICK, 1));
        register(Items.CLAY, () -> new ItemStack(Items.TERRACOTTA));
        register(Items.WET_SPONGE, () -> new ItemStack(Items.SPONGE));
    }

    public static void register(Item input, Supplier<ItemStack> outputSupplier) {
        CUSTOM_SMELTING.put(input, outputSupplier);
    }

    public static ItemStack getSmeltingResult(Level level, ItemStack input) {
        if (input.isEmpty()) return ItemStack.EMPTY;

        Supplier<ItemStack> custom = CUSTOM_SMELTING.get(input.getItem());
        if (custom != null) {
            return custom.get();
        }

        if (level != null) {
            SingleRecipeInput recipeInput = new SingleRecipeInput(input);
            Optional<RecipeHolder<SmeltingRecipe>> match = level.getRecipeManager()
                    .getRecipeFor(RecipeType.SMELTING, recipeInput, level);
            if (match.isPresent()) {
                return match.get().value().assemble(recipeInput, level.registryAccess());
            }
        }

        return ItemStack.EMPTY;
    }
}
