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

import java.util.List;

public class KineticPistonBlock extends DirectionalBlock {
    public static final MapCodec<KineticPistonBlock> CODEC = simpleCodec(KineticPistonBlock::new);
    public static final DirectionProperty FACING = DirectionalBlock.FACING;
    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;

    public KineticPistonBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(TRIGGERED, Boolean.FALSE));
    }

    @Override
    protected MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, TRIGGERED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getNearestLookingDirection().getOpposite())
                .setValue(TRIGGERED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
        if (level.isClientSide) return;

        boolean hasSignal = level.hasNeighborSignal(pos);
        boolean isTriggered = state.getValue(TRIGGERED);

        if (hasSignal && !isTriggered) {
            level.setBlock(pos, state.setValue(TRIGGERED, true), 2);
            triggerPiston(level, pos, state.getValue(FACING));
        } else if (!hasSignal && isTriggered) {
            level.setBlock(pos, state.setValue(TRIGGERED, false), 2);
        }
    }

    private void triggerPiston(Level level, BlockPos pos, Direction facing) {
        BlockPos targetPos = pos.relative(facing);
        BlockState targetState = level.getBlockState(targetPos);

        boolean didAction = false;

        // 1. Launch entities in front (~10 blocks velocity)
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
            didAction = true;
        }

        // 2. Launch single block as physical falling block projectile
        if (!targetState.isAir() && targetState.getDestroySpeed(level, targetPos) >= 0
                && targetState.getPistonPushReaction() != PushReaction.BLOCK
                && targetState.getPistonPushReaction() != PushReaction.DESTROY
                && level.getBlockEntity(targetPos) == null) {

            // Rule: If there is MORE than 1 block in front, it does NOT trigger ("Если перед ним больше 1 блока - он не сработает")
            BlockPos beyondPos = targetPos.relative(facing);
            BlockState beyondState = level.getBlockState(beyondPos);

            if (beyondState.isAir() || beyondState.canBeReplaced()) {
                // Exactly 1 block in front! Launch it as physical FallingBlock projectile
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
                didAction = true;
            }
        }

        // Sound effects & particle bursts
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
}
