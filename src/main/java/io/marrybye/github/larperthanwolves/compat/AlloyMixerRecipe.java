package io.marrybye.github.larperthanwolves.compat;

import net.minecraft.world.item.ItemStack;
import java.util.List;

public class AlloyMixerRecipe {
    private final List<ItemStack> inputs;
    private final ItemStack output;
    private final int cookTime;

    public AlloyMixerRecipe(List<ItemStack> inputs, ItemStack output, int cookTime) {
        this.inputs = inputs;
        this.output = output;
        this.cookTime = cookTime;
    }

    public List<ItemStack> getInputs() {
        return this.inputs;
    }

    public ItemStack getOutput() {
        return this.output;
    }

    public int getCookTime() {
        return this.cookTime;
    }
}
