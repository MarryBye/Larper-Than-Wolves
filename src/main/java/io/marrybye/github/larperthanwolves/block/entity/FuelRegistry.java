package io.marrybye.github.larperthanwolves.block.entity;

import io.marrybye.github.larperthanwolves.item.ModItems;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class FuelRegistry {

    public static class FuelInfo {
        public final int burnDuration;
        public final int cookSpeed;

        public FuelInfo(int burnDuration, int cookSpeed) {
            this.burnDuration = burnDuration;
            this.cookSpeed = cookSpeed;
        }
    }

    public static boolean isValidFuel(ItemStack stack) {
        return getFuelInfo(stack) != null;
    }

    public static FuelInfo getFuelInfo(ItemStack stack) {
        if (stack.isEmpty()) return null;
        if (stack.is(ModItems.DRY_GRASS.get())) {
            int burn = io.marrybye.github.larperthanwolves.config.ModConfig.SERVER != null ? io.marrybye.github.larperthanwolves.config.ModConfig.SERVER.dryGrassBurnTicks.get() : 400;
            int speed = io.marrybye.github.larperthanwolves.config.ModConfig.SERVER != null ? io.marrybye.github.larperthanwolves.config.ModConfig.SERVER.dryGrassCookSpeed.get() : 200;
            return new FuelInfo(burn, speed);
        }
        if (stack.is(Items.STICK)) {
            int burn = io.marrybye.github.larperthanwolves.config.ModConfig.SERVER != null ? io.marrybye.github.larperthanwolves.config.ModConfig.SERVER.stickBurnTicks.get() : 300;
            int speed = io.marrybye.github.larperthanwolves.config.ModConfig.SERVER != null ? io.marrybye.github.larperthanwolves.config.ModConfig.SERVER.stickCookSpeed.get() : 250;
            return new FuelInfo(burn, speed);
        }
        if (stack.is(ItemTags.LOGS) || stack.is(ItemTags.PLANKS) || stack.is(ItemTags.WOODEN_SLABS) || stack.is(ItemTags.WOODEN_STAIRS)) {
            int burn = io.marrybye.github.larperthanwolves.config.ModConfig.SERVER != null ? io.marrybye.github.larperthanwolves.config.ModConfig.SERVER.logBurnTicks.get() : 800;
            int speed = io.marrybye.github.larperthanwolves.config.ModConfig.SERVER != null ? io.marrybye.github.larperthanwolves.config.ModConfig.SERVER.logCookSpeed.get() : 160;
            return new FuelInfo(burn, speed);
        }
        if (stack.is(Items.COAL) || stack.is(Items.CHARCOAL)) {
            int burn = io.marrybye.github.larperthanwolves.config.ModConfig.SERVER != null ? io.marrybye.github.larperthanwolves.config.ModConfig.SERVER.coalBurnTicks.get() : 1600;
            int speed = io.marrybye.github.larperthanwolves.config.ModConfig.SERVER != null ? io.marrybye.github.larperthanwolves.config.ModConfig.SERVER.coalCookSpeed.get() : 100;
            return new FuelInfo(burn, speed);
        }
        if (stack.is(Items.COAL_BLOCK)) {
            int burn = (io.marrybye.github.larperthanwolves.config.ModConfig.SERVER != null ? io.marrybye.github.larperthanwolves.config.ModConfig.SERVER.coalBurnTicks.get() : 1600) * 9;
            int speed = io.marrybye.github.larperthanwolves.config.ModConfig.SERVER != null ? io.marrybye.github.larperthanwolves.config.ModConfig.SERVER.coalCookSpeed.get() : 80;
            return new FuelInfo(burn, speed);
        }
        return null;
    }
}
