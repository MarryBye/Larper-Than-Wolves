package io.marrybye.github.larperthanwolves.compat;

import net.minecraft.world.item.ItemStack;
import java.util.List;

public class SieveJeiRecipe {
    private final ItemStack input;
    private final List<ItemStack> possibleOutputs;
    private final int processTime;

    public SieveJeiRecipe(ItemStack input, List<ItemStack> possibleOutputs, int processTime) {
        this.input = input;
        this.possibleOutputs = possibleOutputs;
        this.processTime = processTime;
    }

    public ItemStack getInput() {
        return this.input;
    }

    public List<ItemStack> getPossibleOutputs() {
        return this.possibleOutputs;
    }

    public int getProcessTime() {
        return this.processTime;
    }
}
