package io.marrybye.github.larperthanwolves.block.entity;

import io.marrybye.github.larperthanwolves.block.UnfiredBrickBlock;
import io.marrybye.github.larperthanwolves.config.ModConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class UnfiredBrickBlockEntity extends BlockEntity {
    private int dryingProgress = 0;

    public UnfiredBrickBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.UNFIRED_BRICK.get(), pos, state);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("DryingProgress", dryingProgress);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("DryingProgress")) {
            dryingProgress = tag.getInt("DryingProgress");
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, UnfiredBrickBlockEntity entity) {
        if (level.isClientSide) return;

        int currentStage = state.getValue(UnfiredBrickBlock.STAGE);
        if (currentStage == 3) {
            return;
        }

        // Requirements from task:
        // 1. Day time only (level.isDay())
        // 2. No blocks above (level.canSeeSky(pos.above()))
        // 3. Not raining at this spot (!level.isRainingAt(pos.above()))
        if (level.isDay() && level.canSeeSky(pos.above()) && !level.isRainingAt(pos.above())) {
            entity.dryingProgress++;
            int totalTicks = ModConfig.SERVER != null ? ModConfig.SERVER.unfiredBrickDryingTimeTicks.get() : 2000;
            if (totalTicks <= 0) totalTicks = 2000;

            int targetStage = Math.clamp((int) ((long) entity.dryingProgress * 3 / totalTicks), 0, 3);
            if (targetStage != currentStage) {
                level.setBlock(pos, state.setValue(UnfiredBrickBlock.STAGE, targetStage), 3);
                if (targetStage == 3) {
                    level.playSound(null, pos, SoundEvents.SAND_PLACE, SoundSource.BLOCKS, 0.8f, 1.2f);
                }
                setChanged(level, pos, state);
            }
        }
    }

    public int getDryingProgress() {
        return dryingProgress;
    }
}
