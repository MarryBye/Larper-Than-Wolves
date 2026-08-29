package io.marrybye.github.larperthanwolves.compat;

import net.minecraft.world.item.ItemStack;
import java.util.List;

public class MachineFuelRecipe {
    private final List<ItemStack> fuels;
    private final int burnDuration;
    private final int cookSpeed;
    private final List<ItemStack> ignitionTools;
    private final List<ItemStack> machines;

    public MachineFuelRecipe(List<ItemStack> fuels, int burnDuration, int cookSpeed, List<ItemStack> ignitionTools, List<ItemStack> machines) {
        this.fuels = fuels;
        this.burnDuration = burnDuration;
        this.cookSpeed = cookSpeed;
        this.ignitionTools = ignitionTools;
        this.machines = machines;
    }

    public List<ItemStack> getFuels() { return fuels; }
    public int getBurnDuration() { return burnDuration; }
    public int getCookSpeed() { return cookSpeed; }
    public List<ItemStack> getIgnitionTools() { return ignitionTools; }
    public List<ItemStack> getMachines() { return machines; }
}
