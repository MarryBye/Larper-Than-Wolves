package io.marrybye.github.larperthanwolves.compat;

import net.minecraft.world.item.ItemStack;

import java.util.List;

public class MillJeiRecipe {
    private final ItemStack input;
    private final List<ItemStack> outputs;
    private final int turns;

    public MillJeiRecipe(ItemStack input, List<ItemStack> outputs, int turns) {
        this.input = input;
        this.outputs = outputs;
        this.turns = turns;
    }

    public ItemStack getInput() {
        return input;
    }

    public List<ItemStack> getOutputs() {
        return outputs;
    }

    public int getTurns() {
        return turns;
    }
}
