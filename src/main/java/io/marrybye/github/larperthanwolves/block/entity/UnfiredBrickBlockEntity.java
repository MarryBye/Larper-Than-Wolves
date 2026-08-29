package io.marrybye.github.larperthanwolves.block.entity;

import io.marrybye.github.larperthanwolves.block.UnfiredBrickBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class UnfiredBrickBlockEntity extends BlockEntity {
    // 100 seconds = 2000 ticks
    public static final int TOTAL_DRYING_TIME = 2000;
    private int dryingTimer = TOTAL_DRYING_TIME;

    public UnfiredBrickBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.UNFIRED_BRICK.get(), pos, state);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("DryingTimer", dryingTimer);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("DryingTimer")) {
            dryingTimer = tag.getInt("DryingTimer");
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, UnfiredBrickBlockEntity entity) {
        if (level.isClientSide) return;

        if (state.getValue(UnfiredBrickBlock.DRIED)) {
            return;
        }

        entity.dryingTimer--;

        if (entity.dryingTimer <= 0) {
            level.setBlock(pos, state.setValue(UnfiredBrickBlock.DRIED, true), 3);
            level.playSound(null, pos, SoundEvents.SAND_PLACE, SoundSource.BLOCKS, 0.8f, 1.2f);
            setChanged(level, pos, state);
        }
    }

    public int getDryingTimer() {
        return dryingTimer;
    }
}
