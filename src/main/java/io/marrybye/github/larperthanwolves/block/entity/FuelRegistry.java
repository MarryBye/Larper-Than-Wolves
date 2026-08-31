package io.marrybye.github.larperthanwolves.block.entity;

import io.marrybye.github.larperthanwolves.block.StumpBlock;
import io.marrybye.github.larperthanwolves.config.ModConfig;
import io.marrybye.github.larperthanwolves.item.ModItems;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

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

        // 9. Coal Block (Tier 9: Compressed mineral fuel - 1800s / 30 min, 80t cook)
        if (stack.is(Items.COAL_BLOCK)) {
            int burn = ModConfig.SERVER != null ? ModConfig.SERVER.coalBlockBurnTicks.get() : 36000;
            int speed = ModConfig.SERVER != null ? ModConfig.SERVER.coalBlockCookSpeed.get() : 80;
            return new FuelInfo(burn, speed);
        }

        // 8. Blaze Rod (Tier 8: Nether heat rod - 300s / 5 min, 70t cook)
        if (stack.is(Items.BLAZE_ROD)) {
            int burn = ModConfig.SERVER != null ? ModConfig.SERVER.blazeRodBurnTicks.get() : 6000;
            int speed = ModConfig.SERVER != null ? ModConfig.SERVER.blazeRodCookSpeed.get() : 70;
            return new FuelInfo(burn, speed);
        }

        // 7. Coal (Tier 7: Mineral coal - 225s / 3m 45s, 100t cook)
        if (stack.is(Items.COAL)) {
            int burn = ModConfig.SERVER != null ? ModConfig.SERVER.coalBurnTicks.get() : 4500;
            int speed = ModConfig.SERVER != null ? ModConfig.SERVER.coalCookSpeed.get() : 100;
            return new FuelInfo(burn, speed);
        }

        // 6. Charcoal (Tier 6: Wood charcoal - 180s / 3 min, 120t cook)
        if (stack.is(Items.CHARCOAL)) {
            int burn = ModConfig.SERVER != null ? ModConfig.SERVER.charcoalBurnTicks.get() : 3600;
            int speed = ModConfig.SERVER != null ? ModConfig.SERVER.charcoalCookSpeed.get() : 120;
            return new FuelInfo(burn, speed);
        }

        // 5. Logs, Wood, Stripped Logs & Tree Stumps (Tier 5: Dense timber - 165s / 2m 45s, 150t cook)
        if (stack.is(ItemTags.LOGS) || Block.byItem(stack.getItem()) instanceof StumpBlock) {
            int burn = ModConfig.SERVER != null ? ModConfig.SERVER.logBurnTicks.get() : 3300;
            int speed = ModConfig.SERVER != null ? ModConfig.SERVER.logCookSpeed.get() : 150;
            return new FuelInfo(burn, speed);
        }

        // 4. Planks & Wooden Construction Blocks (Tier 4: Processed lumber - 120s / 2 min, 180t cook)
        if (stack.is(ItemTags.PLANKS) || stack.is(ItemTags.WOODEN_DOORS) || stack.is(ItemTags.WOODEN_PRESSURE_PLATES)
                || stack.is(ItemTags.WOODEN_BUTTONS) || stack.is(ItemTags.BOATS) || stack.is(ItemTags.CHEST_BOATS)
                || stack.is(ItemTags.SIGNS) || stack.is(ItemTags.HANGING_SIGNS)) {
            int burn = ModConfig.SERVER != null ? ModConfig.SERVER.plankBurnTicks.get() : 2400;
            int speed = ModConfig.SERVER != null ? ModConfig.SERVER.plankCookSpeed.get() : 180;
            return new FuelInfo(burn, speed);
        }

        // 3. Wooden Slabs, Stairs, Fences, Gates & Trapdoors (Tier 3: Small wooden parts - 90s / 1m 30s, 200t cook)
        if (stack.is(ItemTags.WOODEN_SLABS) || stack.is(ItemTags.WOODEN_STAIRS) || stack.is(ItemTags.WOODEN_TRAPDOORS)
                || stack.is(ItemTags.WOODEN_FENCES) || stack.is(ItemTags.FENCE_GATES)) {
            int burn = ModConfig.SERVER != null ? ModConfig.SERVER.woodenSlabBurnTicks.get() : 1800;
            int speed = ModConfig.SERVER != null ? ModConfig.SERVER.woodenSlabCookSpeed.get() : 200;
            return new FuelInfo(burn, speed);
        }

        // 2. Sticks, Pointed Sticks & Small Wood (Tier 2: Sticks - 65s / 1m 5s, 240t cook)
        if (stack.is(Items.STICK) || stack.is(ModItems.POINTED_STICK.get()) || stack.is(Items.BOWL)) {
            int burn = ModConfig.SERVER != null ? ModConfig.SERVER.stickBurnTicks.get() : 1300;
            int speed = ModConfig.SERVER != null ? ModConfig.SERVER.stickCookSpeed.get() : 240;
            return new FuelInfo(burn, speed);
        }

        // 1. Foliage, Twigs & Dry Grass (Tier 1: Kindling - 45s, 260t cook)
        if (stack.is(ModItems.TWIG.get()) || stack.is(ModItems.DRY_GRASS.get()) || stack.is(Items.DEAD_BUSH)
                || stack.is(ItemTags.SAPLINGS) || stack.is(ItemTags.LEAVES)) {
            int burn = ModConfig.SERVER != null ? ModConfig.SERVER.foliageBurnTicks.get() : 900;
            int speed = ModConfig.SERVER != null ? ModConfig.SERVER.foliageCookSpeed.get() : 260;
            return new FuelInfo(burn, speed);
        }

        return null;
    }
}
