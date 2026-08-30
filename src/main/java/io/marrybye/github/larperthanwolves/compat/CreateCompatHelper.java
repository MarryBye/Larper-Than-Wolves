package io.marrybye.github.larperthanwolves.compat;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.lang.reflect.Method;

/**
 * Safe optional integration helper for Create mod (6.0.10+).
 * Uses cached reflection to interact with Create kinetic components without
 * requiring transitive Ponder/Catnip compile dependencies or risking runtime crashes.
 */
public class CreateCompatHelper {
    private static Class<?> kineticBeClass = null;
    private static Method getSpeedMethod = null;
    private static boolean initialized = false;

    private static void init() {
        if (initialized) return;
        initialized = true;
        try {
            kineticBeClass = Class.forName("com.simibubi.create.content.kinetics.base.KineticBlockEntity");
            getSpeedMethod = kineticBeClass.getMethod("getSpeed");
        } catch (Throwable ignored) {
            kineticBeClass = null;
            getSpeedMethod = null;
        }
    }

    /**
     * Checks adjacent faces of the mill (top, sides, bottom)
     * for any active Create kinetic block entity delivering rotational speed.
     * Returns the maximum absolute RPM speed found, or 0.0f if none/stopped.
     */
    public static float getKineticSpeed(Level level, BlockPos millPos) {
        init();
        if (kineticBeClass == null || getSpeedMethod == null) return 0.0f;

        float maxSpeed = 0.0f;

        // 1. Check top face first (where shafts, gears, or cranks are placed)
        BlockPos abovePos = millPos.above();
        BlockEntity aboveBe = level.getBlockEntity(abovePos);
        maxSpeed = Math.max(maxSpeed, extractSpeed(aboveBe));

        // 2. Check all surrounding faces for kinetic connections
        for (Direction dir : Direction.values()) {
            if (dir == Direction.UP) continue;
            BlockPos neighborPos = millPos.relative(dir);
            BlockEntity neighborBe = level.getBlockEntity(neighborPos);
            maxSpeed = Math.max(maxSpeed, extractSpeed(neighborBe));
        }

        return maxSpeed;
    }

    private static float extractSpeed(BlockEntity be) {
        if (be != null && kineticBeClass != null && kineticBeClass.isInstance(be)) {
            try {
                Object result = getSpeedMethod.invoke(be);
                if (result instanceof Number num) {
                    return Math.abs(num.floatValue());
                }
            } catch (Throwable ignored) {
            }
        }
        return 0.0f;
    }
}
