package io.marrybye.github.larperthanwolves.block;

import com.mojang.serialization.MapCodec;
import io.marrybye.github.larperthanwolves.block.entity.FuelRegistry;
import io.marrybye.github.larperthanwolves.block.entity.ModBlockEntities;
import io.marrybye.github.larperthanwolves.block.entity.OvenBlockEntity;
import io.marrybye.github.larperthanwolves.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class OvenBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    // 0: Empty, 1: Fueled, 2: Lit (Strong), 3: Lit Low (Dying embers)
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 3);
    public static final MapCodec<OvenBlock> CODEC = simpleCodec(OvenBlock::new);

    public OvenBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(STAGE, 0));
    }

    @Override
    public MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(STAGE, 0);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, STAGE);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new OvenBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModBlockEntities.OVEN.get(),
                (level, pos, state, entity) -> OvenBlockEntity.tick(level, pos, state, (OvenBlockEntity) entity));
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof OvenBlockEntity oven) {
            // 1. Check if holding valid fuel
            if (FuelRegistry.isValidFuel(stack)) {
                if (!level.isClientSide) {
                    if (oven.addFuel(stack)) {
                        if (!player.getAbilities().instabuild) {
                            stack.shrink(1);
                        }
                        level.setBlock(pos, state.setValue(STAGE, oven.isLit() ? (oven.getBurnTime() <= oven.getMaxBurnTime() / 4 ? 3 : 2) : 1), 3);
                        level.playSound(null, pos, SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 1.0f, 1.0f);
                    }
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }

            // 2. Check if holding lighter or flint & steel
            if (stack.is(ModItems.LIGHTER.get()) || stack.is(Items.FLINT_AND_STEEL)) {
                if (!level.isClientSide) {
                    if (!oven.isLit() && oven.lightOven()) {
                        level.setBlock(pos, state.setValue(STAGE, 2), 3);
                        level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0f, 1.0f);
                        EquipmentSlot equipSlot = hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
                        stack.hurtAndBreak(1, player, equipSlot);
                    }
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof OvenBlockEntity oven) {
                player.openMenu(oven, pos);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof OvenBlockEntity oven) {
                Containers.dropContents(level, pos, oven);
                level.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        int stage = state.getValue(STAGE);
        if (stage >= 2) {
            double x = (double) pos.getX() + 0.5;
            double y = (double) pos.getY();
            double z = (double) pos.getZ() + 0.5;

            if (random.nextDouble() < 0.1) {
                level.playLocalSound(x, y, z, SoundEvents.SMOKER_SMOKE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
            }

            Direction direction = state.getValue(FACING);
            Direction.Axis axis = direction.getAxis();
            double offset = random.nextDouble() * 0.6 - 0.3;
            double xOffset = axis == Direction.Axis.X ? (double) direction.getStepX() * 0.52 : offset;
            double yOffset = random.nextDouble() * 6.0 / 16.0;
            double zOffset = axis == Direction.Axis.Z ? (double) direction.getStepZ() * 0.52 : offset;

            level.addParticle(ParticleTypes.SMOKE, x + xOffset, y + yOffset + 0.2, z + zOffset, 0.0, 0.0, 0.0);
            if (stage == 2) {
                level.addParticle(ParticleTypes.FLAME, x + xOffset, y + yOffset + 0.2, z + zOffset, 0.0, 0.0, 0.0);
            }
        }
    }
}
