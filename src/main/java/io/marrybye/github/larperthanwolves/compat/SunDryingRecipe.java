package io.marrybye.github.larperthanwolves.compat;

import net.minecraft.world.item.ItemStack;

public class SunDryingRecipe {
    private final ItemStack input;
    private final ItemStack output;
    private final int durationTicks;

    public SunDryingRecipe(ItemStack input, ItemStack output, int durationTicks) {
        this.input = input;
        this.output = output;
        this.durationTicks = durationTicks;
    }

    public ItemStack getInput() { return input; }
    public ItemStack getOutput() { return output; }
    public int getDurationTicks() { return durationTicks; }
}
