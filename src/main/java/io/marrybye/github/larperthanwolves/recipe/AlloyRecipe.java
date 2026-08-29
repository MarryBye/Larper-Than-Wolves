package io.marrybye.github.larperthanwolves.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class AlloyRecipe {
    public record IngredientEntry(Ingredient ingredient, int count) {
        public boolean test(ItemStack stack) {
            return ingredient.test(stack);
        }
    }

    private final String id;
    private final List<IngredientEntry> ingredients;
    private final java.util.function.Supplier<ItemStack> resultSupplier;
    private final int cookTime; // 0 to use server config default

    public AlloyRecipe(String id, List<IngredientEntry> ingredients, ItemStack result) {
        this(id, ingredients, () -> result, 0);
    }

    public AlloyRecipe(String id, List<IngredientEntry> ingredients, ItemStack result, int cookTime) {
        this(id, ingredients, () -> result, cookTime);
    }

    public AlloyRecipe(String id, List<IngredientEntry> ingredients, java.util.function.Supplier<ItemStack> resultSupplier) {
        this(id, ingredients, resultSupplier, 0);
    }

    public AlloyRecipe(String id, List<IngredientEntry> ingredients, java.util.function.Supplier<ItemStack> resultSupplier, int cookTime) {
        this.id = id;
        this.ingredients = ingredients;
        this.resultSupplier = resultSupplier;
        this.cookTime = cookTime;
    }

    public String getId() {
        return id;
    }

    public List<IngredientEntry> getIngredients() {
        return ingredients;
    }

    public ItemStack getResult() {
        return resultSupplier.get().copy();
    }

    public int getCookTime() {
        return cookTime;
    }

    public boolean matches(NonNullList<ItemStack> inventory, int startSlot, int endSlot) {
        boolean hasAny = false;
        // 1. Check for any extraneous items that do not belong to this recipe
        for (int i = startSlot; i < endSlot; i++) {
            ItemStack stack = inventory.get(i);
            if (!stack.isEmpty()) {
                hasAny = true;
                if (!containsIngredient(stack)) {
                    return false;
                }
            }
        }

        if (!hasAny) return false;

        // 2. Ensure each required ingredient is satisfied with sufficient count
        for (IngredientEntry required : ingredients) {
            int availableCount = 0;
            for (int i = startSlot; i < endSlot; i++) {
                ItemStack stack = inventory.get(i);
                if (!stack.isEmpty() && required.test(stack)) {
                    availableCount += stack.getCount();
                }
            }
            if (availableCount < required.count()) {
                return false;
            }
        }

        return true;
    }

    public void consumeInputs(NonNullList<ItemStack> inventory, int startSlot, int endSlot) {
        for (IngredientEntry required : ingredients) {
            int needed = required.count();
            for (int i = startSlot; i < endSlot && needed > 0; i++) {
                ItemStack stack = inventory.get(i);
                if (!stack.isEmpty() && required.test(stack)) {
                    int take = Math.min(needed, stack.getCount());
                    stack.shrink(take);
                    needed -= take;
                    if (stack.isEmpty()) {
                        inventory.set(i, ItemStack.EMPTY);
                    }
                }
            }
        }
    }

    public boolean containsIngredient(ItemStack stack) {
        for (IngredientEntry entry : ingredients) {
            if (entry.test(stack)) return true;
        }
        return false;
    }
}
