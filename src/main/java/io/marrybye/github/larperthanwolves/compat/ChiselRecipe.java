package io.marrybye.github.larperthanwolves.compat;

import net.minecraft.world.item.ItemStack;
import java.util.List;

public class ChiselRecipe {
    private final List<ItemStack> inputs;
    private final ItemStack tool;
    private final ItemStack intermediary;
    private final ItemStack output;
    private final int clicks;

    public ChiselRecipe(List<ItemStack> inputs, ItemStack tool, ItemStack intermediary, ItemStack output, int clicks) {
        this.inputs = inputs;
        this.tool = tool;
        this.intermediary = intermediary;
        this.output = output;
        this.clicks = clicks;
    }

    public List<ItemStack> getInputs() { return inputs; }
    public ItemStack getTool() { return tool; }
    public ItemStack getIntermediary() { return intermediary; }
    public ItemStack getOutput() { return output; }
    public int getClicks() { return clicks; }
}
