package io.marrybye.github.larperthanwolves.recipe;

import io.marrybye.github.larperthanwolves.compat.AlloyMixerRecipe;
import io.marrybye.github.larperthanwolves.item.ModItems;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class AlloyRegistry {
    private static final List<AlloyRecipe> RECIPES = new ArrayList<>();
    private static boolean initialized = false;

    public static synchronized void ensureInitialized() {
        if (!initialized) {
            registerDefaults();
            initialized = true;
        }
    }

    public static void registerDefaults() {
        RECIPES.clear();

        // 1. Bronze: 2 Copper Ingot + 1 Tin Ingot -> 1 Bronze Ingot
        register(new AlloyRecipe(
                "bronze_ingot",
                List.of(
                        new AlloyRecipe.IngredientEntry(Ingredient.of(Items.COPPER_INGOT), 2),
                        new AlloyRecipe.IngredientEntry(Ingredient.of(ModItems.TIN_INGOT.get()), 1)
                ),
                () -> new ItemStack(ModItems.BRONZE_INGOT.get(), 1)
        ));

        // 2. Diamond Ingot: 1 Diamond + 1 Iron Ingot + 1 Copper Ingot -> 1 Diamond Ingot
        register(new AlloyRecipe(
                "diamond_ingot",
                List.of(
                        new AlloyRecipe.IngredientEntry(Ingredient.of(Items.DIAMOND), 1),
                        new AlloyRecipe.IngredientEntry(Ingredient.of(Items.IRON_INGOT), 1),
                        new AlloyRecipe.IngredientEntry(Ingredient.of(Items.COPPER_INGOT), 1)
                ),
                () -> new ItemStack(ModItems.DIAMOND_INGOT.get(), 1)
        ));
    }

    public static synchronized void register(AlloyRecipe recipe) {
        RECIPES.add(recipe);
    }

    public static List<AlloyRecipe> getRecipes() {
        ensureInitialized();
        return Collections.unmodifiableList(RECIPES);
    }

    public static Optional<AlloyRecipe> findMatchingRecipe(NonNullList<ItemStack> inventory, int startSlot, int endSlot) {
        ensureInitialized();
        for (AlloyRecipe recipe : RECIPES) {
            if (recipe.matches(inventory, startSlot, endSlot)) {
                return Optional.of(recipe);
            }
        }
        return Optional.empty();
    }

    public static boolean isValidInput(ItemStack stack) {
        if (stack.isEmpty()) return false;
        ensureInitialized();
        for (AlloyRecipe recipe : RECIPES) {
            if (recipe.containsIngredient(stack)) return true;
        }
        return false;
    }

    public static List<AlloyMixerRecipe> getJeiRecipes(int defaultCookTime) {
        ensureInitialized();
        List<AlloyMixerRecipe> jeiList = new ArrayList<>();
        for (AlloyRecipe recipe : RECIPES) {
            List<ItemStack> inputStacks = new ArrayList<>();
            for (AlloyRecipe.IngredientEntry entry : recipe.getIngredients()) {
                ItemStack[] matching = entry.ingredient().getItems();
                if (matching.length > 0) {
                    ItemStack display = matching[0].copy();
                    display.setCount(entry.count());
                    inputStacks.add(display);
                }
            }
            int time = recipe.getCookTime() > 0 ? recipe.getCookTime() : defaultCookTime;
            jeiList.add(new AlloyMixerRecipe(inputStacks, recipe.getResult(), time));
        }
        return jeiList;
    }
}
