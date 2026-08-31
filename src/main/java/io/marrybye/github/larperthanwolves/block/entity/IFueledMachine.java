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
