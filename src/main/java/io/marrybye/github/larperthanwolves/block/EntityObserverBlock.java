package io.marrybye.github.larperthanwolves.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.AABB;

import io.marrybye.github.larperthanwolves.compat.IJeiDocumentationProvider;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class EntityObserverBlock extends DirectionalBlock implements IJeiDocumentationProvider {
    public static final MapCodec<EntityObserverBlock> CODEC = simpleCodec(EntityObserverBlock::new);
    public static final DirectionProperty FACING = DirectionalBlock.FACING;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public EntityObserverBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.SOUTH)
                .setValue(POWERED, Boolean.FALSE));
    }

    @Override
    public void registerJeiInfo(IRecipeRegistration registration) {
        registration.addIngredientInfo(new ItemStack(this), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.larperthanwolves.info.entity_observer"));
    }

    @Override
    protected MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getNearestLookingDirection().getOpposite().getOpposite());
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!state.is(oldState.getBlock())) {
            if (!level.isClientSide) {
                level.scheduleTick(pos, this, 2);
            }
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            if (!level.isClientSide && state.getValue(POWERED)) {
                updateNeighbors(level, pos, state);
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        Direction facing = state.getValue(FACING);

        if (state.getValue(POWERED)) {
            // Turn off pulse
            level.setBlock(pos, state.setValue(POWERED, false), 2);
            updateNeighbors(level, pos, state);
            level.scheduleTick(pos, this, 2);
        } else {
            // Scan for entities in front of the sensor eye
            BlockPos frontPos = pos.relative(facing);
            AABB scanBox = new AABB(frontPos);
            List<Entity> entities = level.getEntities((Entity) null, scanBox, e -> !e.isSpectator());

            if (!entities.isEmpty()) {
                // Entity detected! Emit redstone pulse for 4 ticks
                level.setBlock(pos, state.setValue(POWERED, true), 2);
                updateNeighbors(level, pos, state);
                level.playSound(null, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.3F, 1.4F);
                level.scheduleTick(pos, this, 4);
            } else {
                level.scheduleTick(pos, this, 2);
            }
        }
    }

    private void updateNeighbors(Level level, BlockPos pos, BlockState state) {
        Direction backDirection = state.getValue(FACING).getOpposite();
        BlockPos backPos = pos.relative(backDirection);
        level.neighborChanged(backPos, this, pos);
        level.updateNeighborsAtExceptFromFacing(backPos, this, state.getValue(FACING));
    }

    @Override
    protected boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction side) {
        return getSignal(state, level, pos, side);
    }

    @Override
    protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction side) {
        return state.getValue(POWERED) && state.getValue(FACING) == side ? 15 : 0;
    }
}
