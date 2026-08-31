package io.marrybye.github.larperthanwolves.compat;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Safe optional integration helper for Create mod (6.0.10+).
 * Uses cached reflection to interact with Create kinetic components without
 * requiring transitive Ponder/Catnip compile dependencies or risking runtime crashes.
 */
public class CreateCompatHelper {
    private static Class<?> kineticBeClass = null;
    private static Class<?> handCrankBeClass = null;
    private static Class<?> rotationPropagatorClass = null;
    private static Class<?> iRotateClass = null;
    private static Class<?> kineticNetworkClass = null;
    private static Method getSpeedMethod = null;
    private static Method setSpeedMethod = null;
    private static Method onSpeedChangedMethod = null;
    private static Method getOrCreateNetworkMethod = null;
    private static Method setSourceMethod = null;
    private static Method removeSourceMethod = null;
    private static Method sendDataMethod = null;
    private static Method turnMethod = null;
    private static Method handleAddedMethod = null;
    private static Method handleRemovedMethod = null;
    private static Method hasShaftTowardsMethod = null;
    private static Method updateNetworkMethod = null;
    private static Method syncMethod = null;
    private static Field membersField = null;
    private static Field sourcesField = null;
    private static boolean initialized = false;

    private static void init() {
        if (initialized) return;
        initialized = true;
        try {
            kineticBeClass = Class.forName("com.simibubi.create.content.kinetics.base.KineticBlockEntity");
            getSpeedMethod = kineticBeClass.getMethod("getSpeed");
            setSpeedMethod = kineticBeClass.getMethod("setSpeed", float.class);
            onSpeedChangedMethod = kineticBeClass.getMethod("onSpeedChanged", float.class);
            getOrCreateNetworkMethod = kineticBeClass.getMethod("getOrCreateNetwork");
            setSourceMethod = kineticBeClass.getMethod("setSource", BlockPos.class);
            removeSourceMethod = kineticBeClass.getMethod("removeSource");
        } catch (Throwable ignored) {
            kineticBeClass = null;
        }

        try {
            kineticNetworkClass = Class.forName("com.simibubi.create.content.kinetics.KineticNetwork");
            updateNetworkMethod = kineticNetworkClass.getMethod("updateNetwork");
            syncMethod = kineticNetworkClass.getMethod("sync");
            membersField = kineticNetworkClass.getField("members");
            sourcesField = kineticNetworkClass.getField("sources");
        } catch (Throwable ignored) {
            kineticNetworkClass = null;
        }

        try {
            iRotateClass = Class.forName("com.simibubi.create.content.kinetics.base.IRotate");
            hasShaftTowardsMethod = iRotateClass.getMethod("hasShaftTowards", LevelReader.class, BlockPos.class, BlockState.class, Direction.class);
        } catch (Throwable ignored) {
            hasShaftTowardsMethod = null;
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
     * Checks if an adjacent neighbor block has an active rotating shaft or component
     * pointing directly towards target block in the given direction.
     */
    public static boolean hasShaftPointingTowards(Level level, BlockPos neighborPos, BlockState neighborState, Direction towardsTarget) {
        init();
        if (level == null || neighborPos == null || neighborState == null || towardsTarget == null) return false;

        // 1. Create IRotate interface (Shafts, Cogwheels, Gearboxes, Motors, Drills, Water Wheels, etc.)
        if (hasShaftTowardsMethod != null && iRotateClass != null && iRotateClass.isInstance(neighborState.getBlock())) {
            try {
                Object result = hasShaftTowardsMethod.invoke(neighborState.getBlock(), level, neighborPos, neighborState, towardsTarget);
                if (result instanceof Boolean b) {
                    return b;
                }
            } catch (Throwable ignored) {
            }
        }

        // 2. Standard BlockState AXIS property (Shafts, Cogwheels, Kinetic Pillars)
        if (neighborState.hasProperty(BlockStateProperties.AXIS)) {
            return neighborState.getValue(BlockStateProperties.AXIS) == towardsTarget.getAxis();
        }

        // 3. Standard BlockState FACING property (Motors, Cranks, Gearboxes)
        if (neighborState.hasProperty(BlockStateProperties.FACING)) {
            return neighborState.getValue(BlockStateProperties.FACING) == towardsTarget;
        }

        // 4. Standard BlockState HORIZONTAL_FACING property
        if (neighborState.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            return neighborState.getValue(BlockStateProperties.HORIZONTAL_FACING) == towardsTarget;
        }

        return false;
    }

    /**
     * Applies manual kinetic rotation to a connected Create block entity.
     * Updates the full connected KineticNetwork and synchronizes all member blocks.
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

            Object network = null;
            if (getOrCreateNetworkMethod != null) {
                network = getOrCreateNetworkMethod.invoke(attachedBe);
            }

            if (network != null && membersField != null) {
                @SuppressWarnings("unchecked")
                Map<Object, Float> members = (Map<Object, Float>) membersField.get(network);
                @SuppressWarnings("unchecked")
                Map<Object, Float> sources = (sourcesField != null) ? (Map<Object, Float>) sourcesField.get(network) : null;

                if (speed != 0.0f) {
                    if (sources != null) {
                        sources.put(attachedBe, speed);
                    }
                    if (members != null && !members.isEmpty()) {
                        for (Map.Entry<Object, Float> entry : members.entrySet()) {
                            Object memberBe = entry.getKey();
                            float modifier = entry.getValue() != null ? entry.getValue() : 1.0f;
                            float targetSpeed = speed * modifier;
                            if (setSpeedMethod != null) setSpeedMethod.invoke(memberBe, targetSpeed);
                            if (onSpeedChangedMethod != null) onSpeedChangedMethod.invoke(memberBe, 0.0f);
                            if (sendDataMethod != null) sendDataMethod.invoke(memberBe);
                        }
                    } else {
                        if (setSpeedMethod != null) setSpeedMethod.invoke(attachedBe, speed);
                        if (onSpeedChangedMethod != null) onSpeedChangedMethod.invoke(attachedBe, 0.0f);
                        if (sendDataMethod != null) sendDataMethod.invoke(attachedBe);
                    }
                    if (updateNetworkMethod != null) updateNetworkMethod.invoke(network);
                    if (syncMethod != null) syncMethod.invoke(network);
                } else {
                    if (sources != null) {
                        sources.remove(attachedBe);
                    }
                    if (members != null && !members.isEmpty()) {
                        for (Object memberBe : members.keySet()) {
                            if (setSpeedMethod != null) setSpeedMethod.invoke(memberBe, 0.0f);
                            if (onSpeedChangedMethod != null) onSpeedChangedMethod.invoke(memberBe, speed);
                            if (sendDataMethod != null) sendDataMethod.invoke(memberBe);
                        }
                    } else {
                        if (setSpeedMethod != null) setSpeedMethod.invoke(attachedBe, 0.0f);
                        if (onSpeedChangedMethod != null) onSpeedChangedMethod.invoke(attachedBe, speed);
                        if (sendDataMethod != null) sendDataMethod.invoke(attachedBe);
                    }
                    if (updateNetworkMethod != null) updateNetworkMethod.invoke(network);
                    if (syncMethod != null) syncMethod.invoke(network);
                }
            } else {
                if (setSpeedMethod != null) setSpeedMethod.invoke(attachedBe, speed);
                if (onSpeedChangedMethod != null) onSpeedChangedMethod.invoke(attachedBe, 0.0f);
                if (sendDataMethod != null) sendDataMethod.invoke(attachedBe);
            }
        } catch (Throwable ignored) {
        }
    }

    /**
     * Checks specified adjacent faces of the block for any active Create kinetic block entity
     * whose output shaft is physically connected and pointing towards the target block.
     * Returns the maximum absolute RPM speed found, or 0.0f if none/disconnected.
     */
    public static float getKineticSpeed(Level level, BlockPos targetPos, @Nullable Direction[] allowedFaces) {
        init();
        if (level == null || targetPos == null || kineticBeClass == null || getSpeedMethod == null) return 0.0f;

        Direction[] facesToCheck = (allowedFaces != null && allowedFaces.length > 0) ? allowedFaces : Direction.values();
        float maxSpeed = 0.0f;

        for (Direction dir : facesToCheck) {
            BlockPos neighborPos = targetPos.relative(dir);
            BlockState neighborState = level.getBlockState(neighborPos);
            Direction towardsTarget = dir.getOpposite();

            // Strictly verify that the neighbor has a shaft or gear connected towards our target block
            if (!hasShaftPointingTowards(level, neighborPos, neighborState, towardsTarget)) {
                continue;
            }

            BlockEntity neighborBe = level.getBlockEntity(neighborPos);
            maxSpeed = Math.max(maxSpeed, extractSpeed(neighborBe));
        }

        return maxSpeed;
    }

    public static float getKineticSpeed(Level level, BlockPos targetPos) {
        return getKineticSpeed(level, targetPos, null);
    }

    /**
     * Finds active kinetic rotation from any valid face accepted by an IKineticReceiver.
     */
    public static float getKineticSpeedForReceiver(Level level, BlockPos targetPos, io.marrybye.github.larperthanwolves.api.IKineticReceiver receiver) {
        init();
        if (level == null || targetPos == null || receiver == null || kineticBeClass == null || getSpeedMethod == null) return 0.0f;

        float maxSpeed = 0.0f;
        for (Direction dir : Direction.values()) {
            if (!receiver.acceptsKineticRotationFrom(dir)) {
                continue;
            }

            BlockPos neighborPos = targetPos.relative(dir);
            BlockState neighborState = level.getBlockState(neighborPos);
            Direction towardsTarget = dir.getOpposite();

            if (!hasShaftPointingTowards(level, neighborPos, neighborState, towardsTarget)) {
                continue;
            }

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