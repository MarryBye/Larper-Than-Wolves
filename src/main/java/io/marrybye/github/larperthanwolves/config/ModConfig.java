package io.marrybye.github.larperthanwolves.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ModConfig {
    public static final ModConfigSpec SPEC;
    public static final Server SERVER;

    static {
        Pair<Server, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Server::new);
        SPEC = specPair.getRight();
        SERVER = specPair.getLeft();
    }

    public static class Server {
        public final ModConfigSpec.IntValue unfiredBrickDryingTimeTicks;
        public final ModConfigSpec.IntValue dryingRackTimeTicks;
        public final ModConfigSpec.IntValue alloyMixerCookTimeTicks;
        public final ModConfigSpec.IntValue brickFurnaceDefaultCookTimeTicks;

        public final ModConfigSpec.IntValue foliageBurnTicks;
        public final ModConfigSpec.IntValue stickBurnTicks;
        public final ModConfigSpec.IntValue woodenSlabBurnTicks;
        public final ModConfigSpec.IntValue plankBurnTicks;
        public final ModConfigSpec.IntValue logBurnTicks;
        public final ModConfigSpec.IntValue charcoalBurnTicks;
        public final ModConfigSpec.IntValue coalBurnTicks;
        public final ModConfigSpec.IntValue blazeRodBurnTicks;
        public final ModConfigSpec.IntValue coalBlockBurnTicks;

        public final ModConfigSpec.IntValue foliageCookSpeed;
        public final ModConfigSpec.IntValue stickCookSpeed;
        public final ModConfigSpec.IntValue woodenSlabCookSpeed;
        public final ModConfigSpec.IntValue plankCookSpeed;
        public final ModConfigSpec.IntValue logCookSpeed;
        public final ModConfigSpec.IntValue charcoalCookSpeed;
        public final ModConfigSpec.IntValue coalCookSpeed;
        public final ModConfigSpec.IntValue blazeRodCookSpeed;
        public final ModConfigSpec.IntValue coalBlockCookSpeed;

        public final ModConfigSpec.DoubleValue copperDustGravelDropChance;
        public final ModConfigSpec.DoubleValue flintGravelDropChance;
        public final ModConfigSpec.DoubleValue siliconShardGravelDropChance;

        public final ModConfigSpec.IntValue sieveProcessTimeTicks;
        public final ModConfigSpec.DoubleValue sieveSiliconShardChance;
        public final ModConfigSpec.DoubleValue sieveFlintChance;
        public final ModConfigSpec.DoubleValue sieveCopperDustChance;

        public final ModConfigSpec.DoubleValue sieveRichCopperDustChance;
        public final ModConfigSpec.DoubleValue sieveRichTinDustChance;
        public final ModConfigSpec.DoubleValue sieveRichIronDustChance;
        public final ModConfigSpec.DoubleValue sieveRichGoldDustChance;
        public final ModConfigSpec.DoubleValue sieveRichDiamondDustChance;

        public final ModConfigSpec.DoubleValue villageMinDistanceFromSpawn;
        public final ModConfigSpec.DoubleValue hoeGrassSeedDropChance;

        public Server(ModConfigSpec.Builder builder) {
            builder.push("drying");
            unfiredBrickDryingTimeTicks = builder
                    .comment("Number of ticks required for an unfired brick to dry into a baked brick in open daylight (default: 2000 ticks = 100 seconds)")
                    .defineInRange("unfiredBrickDryingTimeTicks", 2000, 100, 72000);
            dryingRackTimeTicks = builder
                    .comment("Number of ticks required for grass or leather to dry on the Drying Rack in open daylight (default: 1200 ticks = 60 seconds)")
                    .defineInRange("dryingRackTimeTicks", 1200, 20, 72000);
            builder.pop();

            builder.push("alloy_mixer");
            alloyMixerCookTimeTicks = builder
                    .comment("Number of ticks required for the Alloy Mixer to produce an ingot (default: 600 ticks = 30 seconds)")
                    .defineInRange("alloyMixerCookTimeTicks", 600, 100, 24000);
            builder.pop();

            builder.push("brick_furnace");
            brickFurnaceDefaultCookTimeTicks = builder
                    .comment("Default cook time for brick furnace recipes in ticks (default: 200 ticks = 10 seconds)")
                    .defineInRange("brickFurnaceDefaultCookTimeTicks", 200, 20, 2400);

            builder.push("fuel_durations");
            foliageBurnTicks = builder.comment("Burn duration of foliage/twigs/dry grass in ticks (default: 900 ticks = 45s)").defineInRange("foliageBurnTicks", 900, 20, 72000);
            stickBurnTicks = builder.comment("Burn duration of sticks/bowls in ticks (default: 1300 ticks = 65s)").defineInRange("stickBurnTicks", 1300, 20, 72000);
            woodenSlabBurnTicks = builder.comment("Burn duration of wooden slabs/stairs/fences in ticks (default: 1800 ticks = 90s)").defineInRange("woodenSlabBurnTicks", 1800, 20, 72000);
            plankBurnTicks = builder.comment("Burn duration of planks/doors/boats in ticks (default: 2400 ticks = 120s)").defineInRange("plankBurnTicks", 2400, 20, 72000);
            logBurnTicks = builder.comment("Burn duration of logs/wood/stumps in ticks (default: 3300 ticks = 165s)").defineInRange("logBurnTicks", 3300, 20, 72000);
            charcoalBurnTicks = builder.comment("Burn duration of charcoal in ticks (default: 3600 ticks = 180s)").defineInRange("charcoalBurnTicks", 3600, 20, 72000);
            coalBurnTicks = builder.comment("Burn duration of mineral coal in ticks (default: 4500 ticks = 225s)").defineInRange("coalBurnTicks", 4500, 20, 72000);
            blazeRodBurnTicks = builder.comment("Burn duration of blaze rod in ticks (default: 6000 ticks = 300s)").defineInRange("blazeRodBurnTicks", 6000, 20, 72000);
            coalBlockBurnTicks = builder.comment("Burn duration of coal block in ticks (default: 36000 ticks = 1800s)").defineInRange("coalBlockBurnTicks", 36000, 20, 72000);
            builder.pop();

            builder.push("fuel_cook_speeds");
            foliageCookSpeed = builder.comment("Smelting cook time when fueled with foliage/twigs (default: 260 ticks)").defineInRange("foliageCookSpeed", 260, 20, 2400);
            stickCookSpeed = builder.comment("Smelting cook time when fueled with sticks (default: 240 ticks)").defineInRange("stickCookSpeed", 240, 20, 2400);
            woodenSlabCookSpeed = builder.comment("Smelting cook time when fueled with wooden slabs/stairs (default: 200 ticks)").defineInRange("woodenSlabCookSpeed", 200, 20, 2400);
            plankCookSpeed = builder.comment("Smelting cook time when fueled with planks (default: 180 ticks)").defineInRange("plankCookSpeed", 180, 20, 2400);
            logCookSpeed = builder.comment("Smelting cook time when fueled with logs/wood/stumps (default: 150 ticks)").defineInRange("logCookSpeed", 150, 20, 2400);
            charcoalCookSpeed = builder.comment("Smelting cook time when fueled with charcoal (default: 120 ticks)").defineInRange("charcoalCookSpeed", 120, 20, 2400);
            coalCookSpeed = builder.comment("Smelting cook time when fueled with coal (default: 100 ticks)").defineInRange("coalCookSpeed", 100, 20, 2400);
            blazeRodCookSpeed = builder.comment("Smelting cook time when fueled with blaze rod (default: 70 ticks)").defineInRange("blazeRodCookSpeed", 70, 20, 2400);
            coalBlockCookSpeed = builder.comment("Smelting cook time when fueled with coal block (default: 80 ticks)").defineInRange("coalBlockCookSpeed", 80, 20, 2400);
            builder.pop();

            builder.pop();

            builder.push("gravel_drops");
            copperDustGravelDropChance = builder
                    .comment("Chance of copper dust dropping when gravel/sand/dirt is broken with shovel (default: 0.02 = 2%)")
                    .defineInRange("copperDustGravelDropChance", 0.02, 0.0, 1.0);
            flintGravelDropChance = builder
                    .comment("Chance of flint dropping when gravel/sand/dirt is broken with shovel (default: 0.08 = 8%)")
                    .defineInRange("flintGravelDropChance", 0.08, 0.0, 1.0);
            siliconShardGravelDropChance = builder
                    .comment("Chance of silicon shard dropping when gravel/sand/dirt is broken (default: 0.20 = 20%)")
                    .defineInRange("siliconShardGravelDropChance", 0.20, 0.0, 1.0);
            builder.pop();

            builder.push("sieve");
            sieveProcessTimeTicks = builder
                    .comment("Number of ticks for the sieve to process (default: 10 ticks per shake cycle)")
                    .defineInRange("sieveProcessTimeTicks", 10, 1, 24000);

            // Sieve drop chances for regular soils (gravel, sand, red sand, dirt):
            sieveSiliconShardChance = builder
                    .comment("Chance of sifting Silicon Shard from regular soil (gravel/sand/dirt) (default: 0.40 = 40%)")
                    .defineInRange("sieveSiliconShardChance", 0.40, 0.0, 1.0);
            sieveFlintChance = builder
                    .comment("Chance of sifting Flint from regular soil (gravel/sand/dirt) (default: 0.22 = 22%)")
                    .defineInRange("sieveFlintChance", 0.22, 0.0, 1.0);
            sieveCopperDustChance = builder
                    .comment("Chance of sifting Copper Dust from regular soil (gravel/sand/dirt) (default: 0.08 = 8%)")
                    .defineInRange("sieveCopperDustChance", 0.08, 0.0, 1.0);

            // Sieve drop chances for rich soils (pure natural metal dusts ONLY):
            sieveRichCopperDustChance = builder
                    .comment("Chance of sifting Copper Dust from Rich Soil (default: 0.85 = 85%)")
                    .defineInRange("sieveRichCopperDustChance", 0.85, 0.0, 1.0);
            sieveRichTinDustChance = builder
                    .comment("Chance of sifting Tin Dust from Rich Soil (default: 0.55 = 55%)")
                    .defineInRange("sieveRichTinDustChance", 0.55, 0.0, 1.0);
            sieveRichIronDustChance = builder
                    .comment("Chance of sifting Iron Dust from Rich Soil (default: 0.25 = 25%)")
                    .defineInRange("sieveRichIronDustChance", 0.25, 0.0, 1.0);
            sieveRichGoldDustChance = builder
                    .comment("Chance of sifting Gold Dust from Rich Soil (default: 0.08 = 8%)")
                    .defineInRange("sieveRichGoldDustChance", 0.08, 0.0, 1.0);
            sieveRichDiamondDustChance = builder
                    .comment("Chance of sifting Diamond Dust from Rich Soil (default: 0.03 = 3%)")
                    .defineInRange("sieveRichDiamondDustChance", 0.03, 0.0, 1.0);
            builder.pop();

            builder.push("village_generation");
            villageMinDistanceFromSpawn = builder
                    .comment("Minimum distance in blocks from world spawn (0,0) for village structures to generate (default: 3000.0 blocks)")
                    .defineInRange("villageMinDistanceFromSpawn", 3000.0, 0.0, 100000.0);
            builder.pop();

            builder.push("farming");
            hoeGrassSeedDropChance = builder
                    .comment("Chance of dropping a crop seed when a grass block is tilled into dirt with a hoe (default: 0.35 = 35%)")
                    .defineInRange("hoeGrassSeedDropChance", 0.35, 0.0, 1.0);
            builder.pop();
        }
    }
}
