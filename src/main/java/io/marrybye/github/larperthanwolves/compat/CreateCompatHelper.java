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
    private static Class<?> handCrankBeClass = null;
    private static Class<?> rotationPropagatorClass = null;
    private static Method getSpeedMethod = null;
    private static Method setSpeedMethod = null;
    private static Method setSourceMethod = null;
    private static Method removeSourceMethod = null;
    private static Method sendDataMethod = null;
    private static Method turnMethod = null;
    private static Method handleAddedMethod = null;
    private static Method handleRemovedMethod = null;
    private static boolean initialized = false;

    private static void init() {
        if (initialized) return;
        initialized = true;
        try {
            kineticBeClass = Class.forName("com.simibubi.create.content.kinetics.base.KineticBlockEntity");
            getSpeedMethod = kineticBeClass.getMethod("getSpeed");
            setSpeedMethod = kineticBeClass.getMethod("setSpeed", float.class);
            setSourceMethod = kineticBeClass.getMethod("setSource", BlockPos.class);
            removeSourceMethod = kineticBeClass.getMethod("removeSource");
        } catch (Throwable ignored) {
            kineticBeClass = null;
        }

        try {
            Class<?> syncedBeClass = Class.forName("com.simibubi.create.foundation.blockEntity.SyncedBlockEntity");
            sendDataMethod = syncedBeClass.getMethod("sendData");
        } catch (Throwable ignored) {
            sendDataMethod = null;
        }

        try {
            rotationPropagatorClass = Class.forName("com.simibubi.create.content.kinetics.RotationPropagator");
            handleAddedMethod = rotationPropagatorClass.getMethod("handleAdded", Level.class, BlockPos.class, kineticBeClass);
            handleRemovedMethod = rotationPropagatorClass.getMethod("handleRemoved", Level.class, BlockPos.class, kineticBeClass);
        } catch (Throwable ignored) {
            rotationPropagatorClass = null;
        }

        try {
            handCrankBeClass = Class.forName("com.simibubi.create.content.kinetics.crank.HandCrankBlockEntity");
            turnMethod = handCrankBeClass.getMethod("turn", boolean.class);
        } catch (Throwable ignored) {
            handCrankBeClass = null;
        }
    }

    /**
     * Checks if the given BlockEntity is a Create kinetic component.
     */
    public static boolean isKineticBlockEntity(BlockEntity be) {
        init();
        return be != null && kineticBeClass != null && kineticBeClass.isInstance(be);
    }

    /**
     * Applies manual kinetic rotation to a connected Create block entity.
     */
    public static void applyKineticRotation(Level level, BlockPos crankPos, BlockPos attachedPos, BlockEntity attachedBe, float speed) {
        init();
        if (level == null || level.isClientSide() || attachedBe == null || kineticBeClass == null) return;
        if (!kineticBeClass.isInstance(attachedBe)) return;

        try {
            if (handCrankBeClass != null && handCrankBeClass.isInstance(attachedBe) && turnMethod != null) {
                if (speed != 0.0f) {
                    turnMethod.invoke(attachedBe, speed < 0);
                }
                return;
            }

            if (speed != 0.0f) {
                if (setSpeedMethod != null) {
                    setSpeedMethod.invoke(attachedBe, speed);
                }
                if (setSourceMethod != null) {
                    setSourceMethod.invoke(attachedBe, crankPos);
                }
                if (handleAddedMethod != null) {
                    handleAddedMethod.invoke(null, level, attachedPos, attachedBe);
                }
                if (sendDataMethod != null) {
                    sendDataMethod.invoke(attachedBe);
                }
            } else {
                if (handleRemovedMethod != null) {
                    handleRemovedMethod.invoke(null, level, attachedPos, attachedBe);
                }
                if (setSpeedMethod != null) {
                    setSpeedMethod.invoke(attachedBe, 0.0f);
                }
                if (removeSourceMethod != null) {
                    removeSourceMethod.invoke(attachedBe);
                }
                if (sendDataMethod != null) {
                    sendDataMethod.invoke(attachedBe);
                }
            }
        } catch (Throwable ignored) {
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
