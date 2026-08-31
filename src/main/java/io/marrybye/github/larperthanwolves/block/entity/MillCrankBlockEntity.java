package io.marrybye.github.larperthanwolves.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MillCrankBlockEntity extends BlockEntity {
    public static final int EVENT_ROTATE = 1;
    public static final int ROTATION_DURATION_TICKS = 10; // 0.5 seconds at 20 tps

    private int rotationTicksRemaining = 0;
    private float currentRotationAngle = 0.0f;
    private float prevRotationAngle = 0.0f;

    public MillCrankBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MILL_CRANK.get(), pos, state);
    }

    public boolean isRotating() {
        return rotationTicksRemaining > 0;
    }

    public void startRotation() {
        if (rotationTicksRemaining <= 0) {
            rotationTicksRemaining = ROTATION_DURATION_TICKS;
            if (this.level != null && !this.level.isClientSide) {
                this.level.blockEvent(this.worldPosition, this.getBlockState().getBlock(), EVENT_ROTATE, 0);
            }
        }
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, MillCrankBlockEntity be) {
        if (be.rotationTicksRemaining > 0) {
            be.prevRotationAngle = be.currentRotationAngle;
            be.currentRotationAngle += (360.0f / ROTATION_DURATION_TICKS);
            be.rotationTicksRemaining--;
            if (be.rotationTicksRemaining == 0) {
                be.currentRotationAngle = be.currentRotationAngle % 360.0f;
                be.prevRotationAngle = be.currentRotationAngle;
            }
        } else {
            be.prevRotationAngle = be.currentRotationAngle;
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, MillCrankBlockEntity be) {
        if (be.rotationTicksRemaining > 0) {
            be.rotationTicksRemaining--;
            if (be.rotationTicksRemaining == 0) {
                net.minecraft.core.Direction facing = state.hasProperty(io.marrybye.github.larperthanwolves.block.MillCrankBlock.FACING)
                        ? state.getValue(io.marrybye.github.larperthanwolves.block.MillCrankBlock.FACING)
                        : net.minecraft.core.Direction.UP;
                BlockPos attachedPos = pos.relative(facing.getOpposite());
                BlockEntity attachedBe = level.getBlockEntity(attachedPos);
                if (io.marrybye.github.larperthanwolves.compat.CreateCompatHelper.isKineticBlockEntity(attachedBe)) {
                    io.marrybye.github.larperthanwolves.compat.CreateCompatHelper.applyKineticRotation(level, pos, attachedPos, attachedBe, 0.0f);
                }
            }
        }
    }

    @Override
    public boolean triggerEvent(int id, int type) {
        if (id == EVENT_ROTATE) {
            this.rotationTicksRemaining = ROTATION_DURATION_TICKS;
            return true;
        }
        return super.triggerEvent(id, type);
    }

    public float getInterpolatedAngle(float partialTick) {
        if (rotationTicksRemaining > 0) {
            return Mth.lerp(partialTick, prevRotationAngle, currentRotationAngle);
        }
        return currentRotationAngle;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.currentRotationAngle = tag.getFloat("Angle");
        this.prevRotationAngle = this.currentRotationAngle;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putFloat("Angle", this.currentRotationAngle);
    }
}
