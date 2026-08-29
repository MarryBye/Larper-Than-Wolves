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
        public final ModConfigSpec.IntValue alloyMixerCookTimeTicks;
        public final ModConfigSpec.IntValue brickFurnaceDefaultCookTimeTicks;

        public final ModConfigSpec.IntValue dryGrassBurnTicks;
        public final ModConfigSpec.IntValue stickBurnTicks;
        public final ModConfigSpec.IntValue logBurnTicks;
        public final ModConfigSpec.IntValue coalBurnTicks;

        public final ModConfigSpec.IntValue dryGrassCookSpeed;
        public final ModConfigSpec.IntValue stickCookSpeed;
        public final ModConfigSpec.IntValue logCookSpeed;
        public final ModConfigSpec.IntValue coalCookSpeed;

        public Server(ModConfigSpec.Builder builder) {
            builder.push("bricks");
            unfiredBrickDryingTimeTicks = builder
                    .comment("Number of ticks required for an unfired brick to dry into a baked brick in open daylight (default: 2000 ticks = 100 seconds)")
                    .defineInRange("unfiredBrickDryingTimeTicks", 2000, 100, 72000);
            builder.pop();

            builder.push("alloy_mixer");
            alloyMixerCookTimeTicks = builder
                    .comment("Number of ticks required for the Alloy Mixer to produce a Diamond Ingot (default: 600 ticks = 30 seconds)")
                    .defineInRange("alloyMixerCookTimeTicks", 600, 100, 24000);
            builder.pop();

            builder.push("brick_furnace");
            brickFurnaceDefaultCookTimeTicks = builder
                    .comment("Default cook time for brick furnace recipes in ticks (default: 200 ticks = 10 seconds)")
                    .defineInRange("brickFurnaceDefaultCookTimeTicks", 200, 20, 2400);

            builder.push("fuel_durations");
            dryGrassBurnTicks = builder.comment("Burn duration of dry grass in ticks (default: 400 ticks = 20s)").defineInRange("dryGrassBurnTicks", 400, 20, 72000);
            stickBurnTicks = builder.comment("Burn duration of sticks in ticks (default: 300 ticks = 15s)").defineInRange("stickBurnTicks", 300, 20, 72000);
            logBurnTicks = builder.comment("Burn duration of logs/wood in ticks (default: 800 ticks = 40s)").defineInRange("logBurnTicks", 800, 20, 72000);
            coalBurnTicks = builder.comment("Burn duration of coal/charcoal in ticks (default: 1600 ticks = 80s)").defineInRange("coalBurnTicks", 1600, 20, 72000);
            builder.pop();

            builder.push("fuel_cook_speeds");
            dryGrassCookSpeed = builder.comment("Smelting cook time when fueled with dry grass (default: 200 ticks)").defineInRange("dryGrassCookSpeed", 200, 20, 2400);
            stickCookSpeed = builder.comment("Smelting cook time when fueled with sticks (default: 250 ticks)").defineInRange("stickCookSpeed", 250, 20, 2400);
            logCookSpeed = builder.comment("Smelting cook time when fueled with logs (default: 160 ticks)").defineInRange("logCookSpeed", 160, 20, 2400);
            coalCookSpeed = builder.comment("Smelting cook time when fueled with coal (default: 100 ticks)").defineInRange("coalCookSpeed", 100, 20, 2400);
            builder.pop();

            builder.pop();
        }
    }
}
