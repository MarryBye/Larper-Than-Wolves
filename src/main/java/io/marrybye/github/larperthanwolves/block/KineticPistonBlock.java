package io.marrybye.github.larperthanwolves.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import io.marrybye.github.larperthanwolves.compat.IJeiDocumentationProvider;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class KineticPistonBlock extends DirectionalBlock implements IJeiDocumentationProvider {
    public static final MapCodec<KineticPistonBlock> CODEC = simpleCodec(KineticPistonBlock::new);
    public static final DirectionProperty FACING = DirectionalBlock.FACING;
    public static final BooleanProperty EXTENDED = BlockStateProperties.EXTENDED;

    protected static final VoxelShape BASE_UP = Block.box(0, 0, 0, 16, 12, 16);
    protected static final VoxelShape BASE_DOWN = Block.box(0, 4, 0, 16, 16, 16);
    protected static final VoxelShape BASE_NORTH = Block.box(0, 0, 4, 16, 16, 16);
    protected static final VoxelShape BASE_SOUTH = Block.box(0, 0, 0, 16, 16, 12);
    protected static final VoxelShape BASE_WEST = Block.box(4, 0, 0, 16, 16, 16);
    protected static final VoxelShape BASE_EAST = Block.box(0, 0, 0, 12, 16, 16);

    public KineticPistonBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(EXTENDED, Boolean.FALSE));
    }

    @Override
    protected MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, EXTENDED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getNearestLookingDirection().getOpposite())
                .setValue(EXTENDED, Boolean.FALSE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (!state.getValue(EXTENDED)) {
            return Shapes.block();
        }
        return switch (state.getValue(FACING)) {
            case DOWN -> BASE_DOWN;
            case UP -> BASE_UP;
            case SOUTH -> BASE_SOUTH;
            case WEST -> BASE_WEST;
            case EAST -> BASE_EAST;
            default -> BASE_NORTH;
        };
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
        if (level.isClientSide) return;

        boolean hasSignal = level.hasNeighborSignal(pos);
        boolean isExtended = state.getValue(EXTENDED);

        if (hasSignal && !isExtended) {
            // Redstone activated -> extend and launch
            triggerPiston(level, pos, state);
        } else if (!hasSignal && isExtended) {
            // Redstone deactivated -> retract
            retractPiston(level, pos, state);
        }
    }

    private void triggerPiston(Level level, BlockPos pos, BlockState state) {
        Direction facing = state.getValue(FACING);
        BlockPos targetPos = pos.relative(facing);
        BlockState targetState = level.getBlockState(targetPos);

        // 1. Launch single block as physical falling block projectile
        if (!targetState.isAir() && targetState.getDestroySpeed(level, targetPos) >= 0
                && targetState.getPistonPushReaction() != PushReaction.BLOCK
                && targetState.getPistonPushReaction() != PushReaction.DESTROY
                && level.getBlockEntity(targetPos) == null) {

            // Rule: If there is MORE than 1 block in front, it does NOT trigger
            BlockPos beyondPos = targetPos.relative(facing);
            BlockState beyondState = level.getBlockState(beyondPos);

            if (beyondState.isAir() || beyondState.canBeReplaced()) {
                level.setBlock(targetPos, Blocks.AIR.defaultBlockState(), 3);

                FallingBlockEntity falling = FallingBlockEntity.fall(level, targetPos, targetState);
                falling.dropItem = true;

                Vec3 blockVel;
                if (facing == Direction.UP) {
                    blockVel = new Vec3(0, 1.35D, 0);
                } else if (facing == Direction.DOWN) {
                    blockVel = new Vec3(0, -1.2D, 0);
                } else {
                    blockVel = new Vec3(facing.getStepX() * 1.55D, 0.28D, facing.getStepZ() * 1.55D);
                }
                falling.setDeltaMovement(blockVel);
                falling.hurtMarked = true;
            } else {
                // Obstructed by 2+ blocks: cannot extend
                return;
            }
        }

        // 2. Launch entities in front (~10 blocks velocity)
        AABB entityBox = new AABB(targetPos).inflate(0.3D);
        List<Entity> entities = level.getEntities((Entity) null, entityBox, e -> !e.isSpectator());
        for (Entity entity : entities) {
            Vec3 launchVel;
            if (facing == Direction.UP) {
                launchVel = new Vec3(0, 1.45D, 0);
            } else if (facing == Direction.DOWN) {
                launchVel = new Vec3(0, -1.45D, 0);
            } else {
                launchVel = new Vec3(facing.getStepX() * 1.85D, 0.38D, facing.getStepZ() * 1.85D);
            }
            entity.setDeltaMovement(launchVel);
            entity.hurtMarked = true;
            if (entity instanceof ServerPlayer serverPlayer) {
                serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(serverPlayer));
            }
        }

        // 3. Physically extend base and place extending piston head block in front
        level.setBlock(pos, state.setValue(EXTENDED, true), 3);
        if (level.getBlockState(targetPos).isAir() || level.getBlockState(targetPos).canBeReplaced()) {
            level.setBlock(targetPos, ModBlocks.KINETIC_PISTON_HEAD.get().defaultBlockState()
                    .setValue(KineticPistonHeadBlock.FACING, facing), 3);
        }

        // Sound effects & particle bursts ONCE on activation
        level.playSound(null, pos, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 1.0F, 1.2F);
        level.playSound(null, pos, SoundEvents.WIND_CHARGE_BURST.value(), SoundSource.BLOCKS, 0.8F, 0.9F);

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.POOF,
                    targetPos.getX() + 0.5D, targetPos.getY() + 0.5D, targetPos.getZ() + 0.5D,
                    15, 0.2D, 0.2D, 0.2D, 0.08D);
            serverLevel.sendParticles(ParticleTypes.CRIT,
                    targetPos.getX() + 0.5D, targetPos.getY() + 0.5D, targetPos.getZ() + 0.5D,
                    8, 0.2D, 0.2D, 0.2D, 0.1D);
        }
    }

    private void retractPiston(Level level, BlockPos pos, BlockState state) {
        Direction facing = state.getValue(FACING);
        BlockPos targetPos = pos.relative(facing);

        level.setBlock(pos, state.setValue(EXTENDED, false), 3);
        if (level.getBlockState(targetPos).is(ModBlocks.KINETIC_PISTON_HEAD.get())) {
            level.setBlock(targetPos, Blocks.AIR.defaultBlockState(), 3);
        }
        level.playSound(null, pos, SoundEvents.PISTON_CONTRACT, SoundSource.BLOCKS, 0.8F, 1.1F);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            super.onRemove(state, level, pos, newState, isMoving);
            if (state.getValue(EXTENDED)) {
                BlockPos targetPos = pos.relative(state.getValue(FACING));
                if (level.getBlockState(targetPos).is(ModBlocks.KINETIC_PISTON_HEAD.get())) {
                    level.setBlock(targetPos, Blocks.AIR.defaultBlockState(), 3);
                }
            }
        }
    }

    @Override
    public void registerJeiInfo(IRecipeRegistration registration) {
        registration.addIngredientInfo(new ItemStack(this), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.larperthanwolves.info.kinetic_piston"));
    }
}
