package io.marrybye.github.larperthanwolves.compat;

import net.minecraft.world.item.ItemStack;

public class AdvancedSmelterJeiRecipe {
    private final ItemStack input;
    private final ItemStack output;
    private final int cookTime;

    public AdvancedSmelterJeiRecipe(ItemStack input, ItemStack output, int cookTime) {
        this.input = input;
        this.output = output;
        this.cookTime = cookTime;
    }

    public ItemStack getInput() {
        return input;
    }

    public ItemStack getOutput() {
        return output;
    }

    public int getCookTime() {
        return cookTime;
    }
}
