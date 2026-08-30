package io.marrybye.github.larperthanwolves.block;

import com.mojang.serialization.MapCodec;
import io.marrybye.github.larperthanwolves.block.entity.MillBlockEntity;
import io.marrybye.github.larperthanwolves.block.entity.MillCrankBlockEntity;
import io.marrybye.github.larperthanwolves.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class MillCrankBlock extends BaseEntityBlock {
    public static final MapCodec<MillCrankBlock> CODEC = simpleCodec(MillCrankBlock::new);
    private static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 8.0D, 14.0D);

    public MillCrankBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);
        return belowState.getBlock() instanceof MillBlock || belowState.isFaceSturdy(level, belowPos, Direction.UP);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (!state.canSurvive(level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MillCrankBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, ModBlockEntities.MILL_CRANK.get(),
                level.isClientSide ? MillCrankBlockEntity::clientTick : MillCrankBlockEntity::serverTick);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof MillCrankBlockEntity crankBe)) {
            return InteractionResult.PASS;
        }

        // Ignore clicks if crank is currently in the middle of a rotation (0.5s lockout)
        if (crankBe.isRotating()) {
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        BlockPos millPos = pos.below();
        BlockEntity millBe = level.getBlockEntity(millPos);

        if (millBe instanceof MillBlockEntity mill) {
            if (mill.canGrind()) {
                crankBe.startRotation();
                level.playSound(null, pos, SoundEvents.WOODEN_TRAPDOOR_CLOSE, SoundSource.BLOCKS, 0.5f, 1.4f);
                level.playSound(null, pos, SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS, 0.7f, 1.1f + level.random.nextFloat() * 0.2f);

                if (!level.isClientSide) {
                    ItemStack inputBefore = mill.getItem(MillBlockEntity.SLOT_INPUT).copy();
                    mill.addGrindProgress(5);

                    // If grinding finished (progress reset to 0 and input shrank)
                    if (mill.getProgress() == 0 && (mill.getItem(MillBlockEntity.SLOT_INPUT).getCount() < inputBefore.getCount() || mill.getItem(MillBlockEntity.SLOT_INPUT).isEmpty())) {
                        level.playSound(null, pos, SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 0.8f, 1.3f);
                        level.playSound(null, pos, SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS, 1.0f, 0.85f);
                        if (level instanceof ServerLevel serverLevel) {
                            serverLevel.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, inputBefore),
                                    millPos.getX() + 0.5D, millPos.getY() + 0.9D, millPos.getZ() + 0.5D,
                                    12, 0.2D, 0.1D, 0.2D, 0.05D);
                        }
                    }
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            } else {
                // Empty or cannot grind: play subtle stuck sound
                level.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 0.5f, 1.8f);
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        } else {
            // Free rotation when placed on another surface
            crankBe.startRotation();
            level.playSound(null, pos, SoundEvents.WOODEN_TRAPDOOR_CLOSE, SoundSource.BLOCKS, 0.5f, 1.3f);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
    }
}
