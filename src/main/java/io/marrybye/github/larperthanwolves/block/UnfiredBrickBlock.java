package io.marrybye.github.larperthanwolves.block;

import com.mojang.serialization.MapCodec;
import io.marrybye.github.larperthanwolves.block.entity.ModBlockEntities;
import io.marrybye.github.larperthanwolves.block.entity.UnfiredBrickBlockEntity;
import io.marrybye.github.larperthanwolves.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class UnfiredBrickBlock extends BaseEntityBlock {
    public static final MapCodec<UnfiredBrickBlock> CODEC = simpleCodec(UnfiredBrickBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty DRIED = BooleanProperty.create("dried");

    // 8x4x6 small brick shape on ground
    protected static final VoxelShape SHAPE_NORTH_SOUTH = Block.box(4.0, 0.0, 5.0, 12.0, 4.0, 11.0);
    protected static final VoxelShape SHAPE_EAST_WEST = Block.box(5.0, 0.0, 4.0, 11.0, 4.0, 12.0);

    public UnfiredBrickBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(DRIED, false));
    }

    @Override
    public MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, DRIED);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction dir = state.getValue(FACING);
        return (dir.getAxis() == Direction.Axis.X) ? SHAPE_EAST_WEST : SHAPE_NORTH_SOUTH;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(DRIED, false);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return canSupportCenter(level, pos.below(), Direction.UP);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (!state.canSurvive(level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (state.getValue(DRIED)) {
            if (!level.isClientSide) {
                ItemEntity drop = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.2, pos.getZ() + 0.5, new ItemStack(Items.BRICK));
                drop.setDefaultPickUpDelay();
                level.addFreshEntity(drop);
                level.removeBlock(pos, false);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new UnfiredBrickBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.UNFIRED_BRICK.get(), UnfiredBrickBlockEntity::tick);
    }
}
