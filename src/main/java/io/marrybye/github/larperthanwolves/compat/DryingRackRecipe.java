package io.marrybye.github.larperthanwolves.compat;

import net.minecraft.world.item.ItemStack;

import java.util.List;

public class DryingRackRecipe {
    private final List<ItemStack> inputs;
    private final ItemStack output;
    private final int durationTicks;

    public DryingRackRecipe(List<ItemStack> inputs, ItemStack output, int durationTicks) {
        this.inputs = inputs;
        this.output = output;
        this.durationTicks = durationTicks;
    }

    public List<ItemStack> getInputs() {
        return inputs;
    }

    public ItemStack getOutput() {
        return output;
    }

    public int getDurationTicks() {
        return durationTicks;
    }
}
