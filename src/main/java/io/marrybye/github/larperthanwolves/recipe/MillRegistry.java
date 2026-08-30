package io.marrybye.github.larperthanwolves.recipe;

import io.marrybye.github.larperthanwolves.item.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MillRegistry {
    private static final List<MillRecipe> RECIPES = new ArrayList<>();
    private static boolean initialized = false;

    public static synchronized void ensureInitialized() {
        if (!initialized) {
            registerDefaults();
            initialized = true;
        }
    }

    public static void registerDefaults() {
        RECIPES.clear();

        // 1. Ingots -> 8 Dusts (1 Ingot = 4 Nuggets = 8 Dusts)
        register(new MillRecipe("iron_ingot", Ingredient.of(Items.IRON_INGOT), 1,
                () -> List.of(new ItemStack(ModItems.IRON_DUST.get(), 8))));

        register(new MillRecipe("copper_ingot", Ingredient.of(Items.COPPER_INGOT), 1,
                () -> List.of(new ItemStack(ModItems.COPPER_DUST.get(), 8))));

        register(new MillRecipe("gold_ingot", Ingredient.of(Items.GOLD_INGOT), 1,
                () -> List.of(new ItemStack(ModItems.GOLD_DUST.get(), 8))));

        register(new MillRecipe("tin_ingot", Ingredient.of(ModItems.TIN_INGOT.get()), 1,
                () -> List.of(new ItemStack(ModItems.TIN_DUST.get(), 8))));

        register(new MillRecipe("bronze_ingot", Ingredient.of(ModItems.BRONZE_INGOT.get()), 1,
                () -> List.of(new ItemStack(ModItems.BRONZE_DUST.get(), 8))));

        // 2. Diamond (1 Diamond = 4 Nuggets = 8 Dusts)
        register(new MillRecipe("diamond", Ingredient.of(Items.DIAMOND), 1,
                () -> List.of(new ItemStack(ModItems.DIAMOND_DUST.get(), 8))));

        // 3. Diamond Ingot (1 Diamond Ingot = 1 Diamond + 1 Iron Ingot + 1 Copper Ingot -> 8 Diamond Dust + 8 Iron Dust + 8 Copper Dust)
        register(new MillRecipe("diamond_ingot", Ingredient.of(ModItems.DIAMOND_INGOT.get()), 1,
                () -> List.of(
                        new ItemStack(ModItems.DIAMOND_DUST.get(), 8),
                        new ItemStack(ModItems.IRON_DUST.get(), 8),
                        new ItemStack(ModItems.COPPER_DUST.get(), 8)
                )));

        // 4. Bones -> Bone Meal (2 Bones -> 1 Bone Meal)
        register(new MillRecipe("bone", Ingredient.of(Items.BONE), 2,
                () -> List.of(new ItemStack(Items.BONE_MEAL, 1))));

        // 5. Raw Ores -> 2 Dusts (2 Ore Dust = 1 Raw Chunk)
        register(new MillRecipe("raw_iron", Ingredient.of(Items.RAW_IRON), 1,
                () -> List.of(new ItemStack(ModItems.IRON_DUST.get(), 2))));

        register(new MillRecipe("raw_copper", Ingredient.of(Items.RAW_COPPER), 1,
                () -> List.of(new ItemStack(ModItems.COPPER_DUST.get(), 2))));

        register(new MillRecipe("raw_gold", Ingredient.of(Items.RAW_GOLD), 1,
                () -> List.of(new ItemStack(ModItems.GOLD_DUST.get(), 2))));

        register(new MillRecipe("raw_tin", Ingredient.of(ModItems.RAW_TIN.get()), 1,
                () -> List.of(new ItemStack(ModItems.TIN_DUST.get(), 2))));

        // 6. Metal Nuggets -> 2 Dusts (1 Nugget = 2 Dusts)
        register(new MillRecipe("iron_nugget", Ingredient.of(Items.IRON_NUGGET), 1,
                () -> List.of(new ItemStack(ModItems.IRON_DUST.get(), 2))));

        register(new MillRecipe("copper_nugget", Ingredient.of(ModItems.COPPER_NUGGET.get()), 1,
                () -> List.of(new ItemStack(ModItems.COPPER_DUST.get(), 2))));

        register(new MillRecipe("gold_nugget", Ingredient.of(Items.GOLD_NUGGET), 1,
                () -> List.of(new ItemStack(ModItems.GOLD_DUST.get(), 2))));

        register(new MillRecipe("tin_nugget", Ingredient.of(ModItems.TIN_NUGGET.get()), 1,
                () -> List.of(new ItemStack(ModItems.TIN_DUST.get(), 2))));

        register(new MillRecipe("bronze_nugget", Ingredient.of(ModItems.BRONZE_NUGGET.get()), 1,
                () -> List.of(new ItemStack(ModItems.BRONZE_DUST.get(), 2))));

        register(new MillRecipe("diamond_nugget", Ingredient.of(ModItems.DIAMOND_NUGGET.get()), 1,
                () -> List.of(new ItemStack(ModItems.DIAMOND_DUST.get(), 2))));

        // 7. Rocks and Minerals Grinding
        register(new MillRecipe("cobblestone", Ingredient.of(Items.COBBLESTONE), 1,
                () -> List.of(new ItemStack(Items.GRAVEL, 1))));

        register(new MillRecipe("gravel", Ingredient.of(Items.GRAVEL), 1,
                () -> List.of(new ItemStack(Items.SAND, 1), new ItemStack(ModItems.SILICON_SHARD.get(), 1))));

        register(new MillRecipe("sandstone", Ingredient.of(Items.SANDSTONE), 1,
                () -> List.of(new ItemStack(Items.SAND, 2))));

        register(new MillRecipe("red_sandstone", Ingredient.of(Items.RED_SANDSTONE), 1,
                () -> List.of(new ItemStack(Items.RED_SAND, 2))));

        register(new MillRecipe("wheat", Ingredient.of(Items.WHEAT), 1,
                () -> List.of(new ItemStack(Items.WHEAT_SEEDS, 1))));
    }

    public static synchronized void register(MillRecipe recipe) {
        RECIPES.add(recipe);
    }

    public static List<MillRecipe> getRecipes() {
        ensureInitialized();
        return Collections.unmodifiableList(RECIPES);
    }

    public static Optional<MillRecipe> findMatchingRecipe(ItemStack input) {
        if (input.isEmpty()) return Optional.empty();
        ensureInitialized();
        for (MillRecipe recipe : RECIPES) {
            if (recipe.matches(input)) {
                return Optional.of(recipe);
            }
        }
        return Optional.empty();
    }

    public static boolean isValidInput(ItemStack stack) {
        if (stack.isEmpty()) return false;
        ensureInitialized();
        for (MillRecipe recipe : RECIPES) {
            if (recipe.containsIngredient(stack)) return true;
        }
        return false;
    }
}
