package io.marrybye.github.larperthanwolves.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class KineticPistonHeadBlock extends DirectionalBlock {
    public static final MapCodec<KineticPistonHeadBlock> CODEC = simpleCodec(KineticPistonHeadBlock::new);
    public static final DirectionProperty FACING = DirectionalBlock.FACING;

    protected static final VoxelShape UP_SHAPE = Shapes.or(Block.box(0, 12, 0, 16, 16, 16), Block.box(6, 0, 6, 10, 12, 10));
    protected static final VoxelShape DOWN_SHAPE = Shapes.or(Block.box(0, 0, 0, 16, 4, 16), Block.box(6, 4, 6, 10, 16, 10));
    protected static final VoxelShape NORTH_SHAPE = Shapes.or(Block.box(0, 0, 0, 16, 16, 4), Block.box(6, 6, 4, 10, 10, 16));
    protected static final VoxelShape SOUTH_SHAPE = Shapes.or(Block.box(0, 0, 12, 16, 16, 16), Block.box(6, 6, 0, 10, 10, 12));
    protected static final VoxelShape WEST_SHAPE = Shapes.or(Block.box(0, 0, 0, 4, 16, 16), Block.box(4, 6, 6, 16, 10, 10));
    protected static final VoxelShape EAST_SHAPE = Shapes.or(Block.box(12, 0, 0, 16, 16, 16), Block.box(0, 6, 6, 12, 10, 10));

    public KineticPistonHeadBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case UP -> UP_SHAPE;
            case DOWN -> DOWN_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
            case EAST -> EAST_SHAPE;
            default -> NORTH_SHAPE;
        };
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide) {
            BlockPos basePos = pos.relative(state.getValue(FACING).getOpposite());
            BlockState baseState = level.getBlockState(basePos);
            if (baseState.is(ModBlocks.KINETIC_PISTON.get())) {
                level.destroyBlock(basePos, !player.isCreative());
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            super.onRemove(state, level, pos, newState, isMoving);
            BlockPos basePos = pos.relative(state.getValue(FACING).getOpposite());
            BlockState baseState = level.getBlockState(basePos);
            if (baseState.is(ModBlocks.KINETIC_PISTON.get()) && baseState.getValue(KineticPistonBlock.EXTENDED)) {
                level.setBlock(basePos, baseState.setValue(KineticPistonBlock.EXTENDED, false), 3);
            }
        }
    }
}
