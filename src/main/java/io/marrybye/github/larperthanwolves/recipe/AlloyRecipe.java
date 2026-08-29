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
    private final ItemStack result;
    private final int cookTime; // 0 to use server config default

    public AlloyRecipe(String id, List<IngredientEntry> ingredients, ItemStack result) {
        this(id, ingredients, result, 0);
    }

    public AlloyRecipe(String id, List<IngredientEntry> ingredients, ItemStack result, int cookTime) {
        this.id = id;
        this.ingredients = ingredients;
        this.result = result;
        this.cookTime = cookTime;
    }

    public String getId() {
        return id;
    }

    public List<IngredientEntry> getIngredients() {
        return ingredients;
    }

    public ItemStack getResult() {
        return result.copy();
    }

    public int getCookTime() {
        return cookTime;
    }

    public boolean matches(NonNullList<ItemStack> inventory, int startSlot, int endSlot) {
        List<ItemStack> available = new ArrayList<>();
        for (int i = startSlot; i < endSlot; i++) {
            ItemStack stack = inventory.get(i);
            if (!stack.isEmpty()) {
                available.add(stack.copy());
            }
        }

        if (available.isEmpty()) return false;

        for (IngredientEntry required : ingredients) {
            int needed = required.count();
            for (ItemStack avail : available) {
                if (required.test(avail)) {
                    int taken = Math.min(needed, avail.getCount());
                    avail.shrink(taken);
                    needed -= taken;
                    if (needed <= 0) break;
                }
            }
            if (needed > 0) {
                return false;
            }
        }

        // Ensure there are no extraneous items in the input slots
        for (ItemStack leftover : available) {
            if (!leftover.isEmpty()) {
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
