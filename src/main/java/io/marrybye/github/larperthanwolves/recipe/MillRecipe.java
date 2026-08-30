package io.marrybye.github.larperthanwolves.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class MillRecipe {
    private final String id;
    private final Ingredient ingredient;
    private final int inputCount;
    private final Supplier<List<ItemStack>> resultsSupplier;

    public MillRecipe(String id, Ingredient ingredient, int inputCount, Supplier<List<ItemStack>> resultsSupplier) {
        this.id = id;
        this.ingredient = ingredient;
        this.inputCount = inputCount;
        this.resultsSupplier = resultsSupplier;
    }

    public String getId() {
        return id;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public int getInputCount() {
        return inputCount;
    }

    public boolean matches(ItemStack input) {
        if (input.isEmpty() || input.getCount() < inputCount) return false;
        return ingredient.test(input);
    }

    public boolean containsIngredient(ItemStack stack) {
        if (stack.isEmpty()) return false;
        return ingredient.test(stack);
    }

    public List<ItemStack> getResults() {
        List<ItemStack> list = resultsSupplier.get();
        if (list == null) return Collections.emptyList();
        List<ItemStack> copies = new ArrayList<>(list.size());
        for (ItemStack s : list) {
            copies.add(s.copy());
        }
        return copies;
    }
}
