package io.marrybye.github.larperthanwolves.compat;

import net.minecraft.world.item.ItemStack;
import java.util.List;

public class GravelDiggingRecipe {
    public record DropEntry(ItemStack stack, String chanceText) {}

    private final ItemStack block;
    private final List<DropEntry> drops;

    public GravelDiggingRecipe(ItemStack block, List<DropEntry> drops) {
        this.block = block;
        this.drops = drops;
    }

    public ItemStack getBlock() { return block; }
    public List<DropEntry> getDrops() { return drops; }
}
