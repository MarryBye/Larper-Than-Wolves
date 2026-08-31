package io.marrybye.github.larperthanwolves.block.entity;

import net.minecraft.world.item.ItemStack;

/**
 * Unified interface for all heated machinery (Brick Furnace, Advanced Smelter, Food Oven, Alloy Mixer).
 * Handles burn timers, fuel slot access, and seamless auto-refueling before fire extinguishes.
 */
public interface IFueledMachine {
    int getBurnTime();
    void setBurnTime(int burnTime);

    int getMaxBurnTime();
    void setMaxBurnTime(int maxBurnTime);

    int getFuelCookSpeed();
    void setFuelCookSpeed(int cookSpeed);

    int getFuelSlot();
    ItemStack getFuelItem();
    void setFuelItem(ItemStack stack);

    default boolean isLit() {
        return getBurnTime() > 0;
    }

    default boolean hasStoredFuel() {
        return !getFuelItem().isEmpty() && FuelRegistry.isValidFuel(getFuelItem());
    }

    /**
     * Checks if a hopper connected to this machine is permitted to insert fuel.
     * Hoppers are only permitted to insert fuel when:
     * 1) Connected to the machine's BACK face into the fuel slot.
     * 2) The fuel is valid according to FuelRegistry.
     * 3) The fuel slot is currently empty (strictly 1 item at a time, preventing excess buffering).
     * 4) The machine has no burning fuel (burnTime <= 0) OR is within the final window (burnTime <= 20 ticks)
     *    before the fire extinguishes.
     */
    default boolean canAcceptHopperFuel(net.minecraft.core.Direction side, net.minecraft.core.Direction machineFacing, int slot, ItemStack stack) {
        if (side == null || side != machineFacing.getOpposite() || slot != getFuelSlot()) {
            return false;
        }
        if (!FuelRegistry.isValidFuel(stack)) {
            return false;
        }
        if (!getFuelItem().isEmpty()) {
            return false;
        }
        return getBurnTime() <= 20;
    }

    /**
     * Ticks auto-refueling.
     * When machine is actively burning (burnTime > 0) and burnTime <= 5 ticks,
     * if valid fuel is present in the fuel slot, it automatically consumes 1 fuel item
     * to extend the fire continuously without requiring manual re-ignition.
     * Returns true if fuel was consumed.
     */
    default boolean tickFuelAutoFeed() {
        if (getBurnTime() > 0 && getBurnTime() <= 5) {
            ItemStack fuel = getFuelItem();
            if (!fuel.isEmpty() && FuelRegistry.isValidFuel(fuel)) {
                FuelRegistry.FuelInfo info = FuelRegistry.getFuelInfo(fuel);
                if (info != null) {
                    setBurnTime(getBurnTime() + info.burnDuration);
                    setMaxBurnTime(Math.max(getMaxBurnTime(), getBurnTime()));
                    setFuelCookSpeed(info.cookSpeed);

                    ItemStack remainder = fuel.getCraftingRemainingItem();
                    fuel.shrink(1);
                    if (fuel.isEmpty()) {
                        setFuelItem(remainder.isEmpty() ? ItemStack.EMPTY : remainder);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Lights the machine with a lighter / flint & steel from fuel currently in the fuel slot.
     */
    default boolean lightFromStoredFuel() {
        if (getBurnTime() > 0) return false;
        ItemStack fuel = getFuelItem();
        if (!fuel.isEmpty() && FuelRegistry.isValidFuel(fuel)) {
            FuelRegistry.FuelInfo info = FuelRegistry.getFuelInfo(fuel);
            if (info != null) {
                setBurnTime(info.burnDuration);
                setMaxBurnTime(info.burnDuration);
                setFuelCookSpeed(info.cookSpeed);

                ItemStack remainder = fuel.getCraftingRemainingItem();
                fuel.shrink(1);
                if (fuel.isEmpty()) {
                    setFuelItem(remainder.isEmpty() ? ItemStack.EMPTY : remainder);
                }
                return true;
            }
        }
        return false;
    }
}
