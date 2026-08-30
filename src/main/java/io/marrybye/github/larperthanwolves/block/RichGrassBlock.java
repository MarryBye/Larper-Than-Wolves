package io.marrybye.github.larperthanwolves.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.lighting.LightEngine;

public class RichGrassBlock extends Block {
    public static final MapCodec<RichGrassBlock> CODEC = simpleCodec(RichGrassBlock::new);
    public static final BooleanProperty SNOWY = BlockStateProperties.SNOWY;

    public RichGrassBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(SNOWY, false));
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    private static boolean canBeGrass(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos abovePos = pos.above();
        BlockState aboveState = level.getBlockState(abovePos);
        if (aboveState.is(Blocks.SNOW) && aboveState.getValue(SnowLayerBlock.LAYERS) == 1) {
            return true;
        } else if (aboveState.getFluidState().getAmount() == 8) {
            return false;
        } else {
            int light = LightEngine.getLightBlockInto(level, state, pos, aboveState, abovePos, Direction.UP, aboveState.getLightBlock(level, abovePos));
            return light < level.getMaxLightLevel();
        }
    }

    private static boolean canPropagate(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos abovePos = pos.above();
        return canBeGrass(state, level, pos) && !level.getFluidState(abovePos).is(net.minecraft.tags.FluidTags.WATER);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!canBeGrass(state, level, pos)) {
            level.setBlockAndUpdate(pos, ModBlocks.RICH_DIRT.get().defaultBlockState());
        } else {
            if (level.getMaxLocalRawBrightness(pos.above()) >= 9) {
                BlockState defaultState = this.defaultBlockState();
                for (int i = 0; i < 4; ++i) {
                    BlockPos targetPos = pos.offset(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
                    if (level.getBlockState(targetPos).is(ModBlocks.RICH_DIRT.get()) && canPropagate(defaultState, level, targetPos)) {
                        level.setBlockAndUpdate(targetPos, defaultState.setValue(SNOWY, level.getBlockState(targetPos.above()).is(Blocks.SNOW)));
                    }
                }
            }
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState aboveState = context.getLevel().getBlockState(context.getClickedPos().above());
        return this.defaultBlockState().setValue(SNOWY, isSnowySetting(aboveState));
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        return direction == Direction.UP ? state.setValue(SNOWY, isSnowySetting(neighborState)) : super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    private static boolean isSnowySetting(BlockState state) {
        return state.is(Blocks.SNOW) || state.is(Blocks.SNOW_BLOCK);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SNOWY);
    }
}
