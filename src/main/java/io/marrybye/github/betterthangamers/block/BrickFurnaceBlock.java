package io.marrybye.github.betterthangamers.block;

import io.marrybye.github.betterthangamers.block.entity.BrickFurnaceBlockEntity;
import io.marrybye.github.betterthangamers.block.entity.ModBlockEntities;
import io.marrybye.github.betterthangamers.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.Level;
import net.minecraft.network.chat.Component;
import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.Nullable;

public class BrickFurnaceBlock extends BaseEntityBlock {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final MapCodec<BrickFurnaceBlock> CODEC = simpleCodec(BrickFurnaceBlock::new);

    public BrickFurnaceBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false));
    }

    @Override
    public MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BrickFurnaceBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, net.minecraft.world.level.block.entity.BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModBlockEntities.BRICK_FURNACE.get(),
                (level, pos, state, entity) -> BrickFurnaceBlockEntity.tick(level, pos, state, (BrickFurnaceBlockEntity) entity));
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof BrickFurnaceBlockEntity furnace) {
            // 1. Check if holding valid fuel
            if (BrickFurnaceBlockEntity.isValidFuel(stack)) {
                if (!level.isClientSide) {
                    if (furnace.addFuel(stack)) {
                        if (!player.getAbilities().instabuild) {
                            stack.shrink(1);
                        }
                        level.playSound(null, pos, SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 1.0f, 1.0f);
                        player.displayClientMessage(Component.literal("§aТопливо загружено в печь."), true);
                    } else if (furnace.isLit()) {
                        player.displayClientMessage(Component.literal("§eПечь уже горит с максимальной длительностью."), true);
                    } else {
                        player.displayClientMessage(Component.literal("§eТопливо уже загружено. Подожгите печь зажигалкой!"), true);
                    }
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }

            // 2. Check if holding lighter or flint & steel
            if (stack.is(ModItems.LIGHTER.get()) || stack.is(Items.FLINT_AND_STEEL)) {
                if (!level.isClientSide) {
                    if (furnace.isLit()) {
                        player.displayClientMessage(Component.literal("§eПечь уже горит!"), true);
                    } else if (furnace.lightFurnace()) {
                        level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0f, 1.0f);
                        stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
                        player.displayClientMessage(Component.literal("§6Кирпичная печь успешно зажжена!"), true);
                    } else {
                        player.displayClientMessage(Component.literal("§cВ печи нет топлива! Загрузите сухую траву, дерево или уголь."), true);
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
            if (be instanceof BrickFurnaceBlockEntity furnace) {
                player.openMenu(furnace, pos);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}

