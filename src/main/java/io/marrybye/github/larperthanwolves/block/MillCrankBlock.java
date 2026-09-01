package io.marrybye.github.larperthanwolves.block;

import com.mojang.serialization.MapCodec;
import io.marrybye.github.larperthanwolves.block.entity.MillBlockEntity;
import io.marrybye.github.larperthanwolves.block.entity.MillCrankBlockEntity;
import io.marrybye.github.larperthanwolves.block.entity.ModBlockEntities;
import io.marrybye.github.larperthanwolves.compat.CreateCompatHelper;
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
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import io.marrybye.github.larperthanwolves.compat.IJeiDocumentationProvider;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class MillCrankBlock extends BaseEntityBlock implements IJeiDocumentationProvider {
    public static final DirectionProperty FACING = DirectionalBlock.FACING;
    public static final MapCodec<MillCrankBlock> CODEC = simpleCodec(MillCrankBlock::new);

    @Override
    public void registerJeiInfo(IRecipeRegistration registration) {
        registration.addIngredientInfo(new ItemStack(this), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.larperthanwolves.info.mill_crank"));
    }

    private static final VoxelShape SHAPE_UP = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 8.0D, 14.0D);
    private static final VoxelShape SHAPE_DOWN = Block.box(2.0D, 8.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    private static final VoxelShape SHAPE_NORTH = Block.box(2.0D, 2.0D, 8.0D, 14.0D, 14.0D, 16.0D);
    private static final VoxelShape SHAPE_SOUTH = Block.box(2.0D, 2.0D, 0.0D, 14.0D, 14.0D, 8.0D);
    private static final VoxelShape SHAPE_WEST = Block.box(8.0D, 2.0D, 2.0D, 16.0D, 14.0D, 14.0D);
    private static final VoxelShape SHAPE_EAST = Block.box(0.0D, 2.0D, 2.0D, 8.0D, 14.0D, 14.0D);

    public MillCrankBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getClickedFace());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction facing = state.hasProperty(FACING) ? state.getValue(FACING) : Direction.UP;
        return switch (facing) {
            case DOWN -> SHAPE_DOWN;
            case NORTH -> SHAPE_NORTH;
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_WEST;
            case EAST -> SHAPE_EAST;
            default -> SHAPE_UP;
        };
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction facing = state.hasProperty(FACING) ? state.getValue(FACING) : Direction.UP;
        BlockPos attachedPos = pos.relative(facing.getOpposite());
        BlockState attachedState = level.getBlockState(attachedPos);
        BlockEntity attachedBe = level.getBlockEntity(attachedPos);

        return (attachedBe instanceof io.marrybye.github.larperthanwolves.api.IKineticReceiver receiver && receiver.acceptsKineticRotationFrom(facing))
                || attachedState.isFaceSturdy(level, attachedPos, facing)
                || (level instanceof Level lvl && CreateCompatHelper.isKineticBlockEntity(attachedBe));
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

        Direction facing = state.hasProperty(FACING) ? state.getValue(FACING) : Direction.UP;
        BlockPos attachedPos = pos.relative(facing.getOpposite());
        BlockEntity attachedBe = level.getBlockEntity(attachedPos);

        if (attachedBe instanceof io.marrybye.github.larperthanwolves.api.IKineticReceiver receiver && receiver.acceptsKineticRotationFrom(facing)) {
            if (receiver.hasWorkAvailable()) {
                crankBe.startRotation();
                level.playSound(null, pos, SoundEvents.WOODEN_TRAPDOOR_CLOSE, SoundSource.BLOCKS, 0.5f, 1.4f);
                level.playSound(null, pos, SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS, 0.7f, 1.1f + level.random.nextFloat() * 0.2f);

                receiver.onManualCrank(facing, player);
                return InteractionResult.sidedSuccess(level.isClientSide);
            } else {
                // Empty or cannot process: play subtle stuck sound
                level.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 0.5f, 1.8f);
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        } else if (CreateCompatHelper.isKineticBlockEntity(attachedBe)) {
            float speed = player.isShiftKeyDown() ? -32.0f : 32.0f;
            crankBe.startRotation(speed);
            if (!level.isClientSide) {
                CreateCompatHelper.applyKineticRotation(level, pos, attachedPos, attachedBe, speed);
            }
            level.playSound(null, pos, SoundEvents.WOODEN_TRAPDOOR_CLOSE, SoundSource.BLOCKS, 0.5f, 1.4f);
            level.playSound(null, pos, SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS, 0.6f, 1.3f);
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            // Free rotation when placed on another surface
            crankBe.startRotation();
            level.playSound(null, pos, SoundEvents.WOODEN_TRAPDOOR_CLOSE, SoundSource.BLOCKS, 0.5f, 1.3f);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
    }
}
