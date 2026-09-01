package io.marrybye.github.larperthanwolves.block;

import com.mojang.serialization.MapCodec;
import io.marrybye.github.larperthanwolves.block.entity.AlloyMixerBlockEntity;
import io.marrybye.github.larperthanwolves.block.entity.FuelRegistry;
import io.marrybye.github.larperthanwolves.block.entity.ModBlockEntities;
import io.marrybye.github.larperthanwolves.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import io.marrybye.github.larperthanwolves.compat.AlloyMixerRecipeCategory;
import io.marrybye.github.larperthanwolves.compat.IJeiMachineStation;
import io.marrybye.github.larperthanwolves.compat.MachineFuelRecipeCategory;
import io.marrybye.github.larperthanwolves.client.AlloyMixerScreen;
import io.marrybye.github.larperthanwolves.config.ModConfig;
import io.marrybye.github.larperthanwolves.menu.AlloyMixerMenu;
import io.marrybye.github.larperthanwolves.menu.ModMenuTypes;
import io.marrybye.github.larperthanwolves.recipe.AlloyRegistry;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import org.jetbrains.annotations.Nullable;

public class AlloyMixerBlock extends BaseEntityBlock implements IJeiMachineStation {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    // 0: Empty, 1: Fueled, 2: Lit Strong, 3: Lit Low (Embers)
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 3);
    public static final MapCodec<AlloyMixerBlock> CODEC = simpleCodec(AlloyMixerBlock::new);

    public AlloyMixerBlock(Properties properties) {
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
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AlloyMixerBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.ALLOY_MIXER.get(),
                (lvl, pos, st, entity) -> AlloyMixerBlockEntity.tick(lvl, pos, st, (AlloyMixerBlockEntity) entity));
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof AlloyMixerBlockEntity mixer) {
            // 1. Loading fuel
            if (FuelRegistry.isValidFuel(stack)) {
                if (!level.isClientSide) {
                    ItemStack remainder = stack.getCraftingRemainingItem();
                    if (mixer.addFuel(stack)) {
                        if (!player.getAbilities().instabuild) {
                            stack.shrink(1);
                            if (!remainder.isEmpty()) {
                                if (stack.isEmpty()) {
                                    player.setItemInHand(hand, remainder);
                                } else if (!player.getInventory().add(remainder)) {
                                    player.drop(remainder, false);
                                }
                            }
                        }
                        int targetStage = mixer.isLit() ? (mixer.getBurnTime() <= mixer.getMaxBurnTime() / 4 ? 3 : 2) : 1;
                        level.setBlock(pos, state.setValue(STAGE, targetStage), 3);
                        level.playSound(null, pos, SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 1.0f, 1.0f);
                    }
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }

            // 2. Igniting with lighter or flint & steel
            if (stack.is(ModItems.LIGHTER.get()) || stack.is(Items.FLINT_AND_STEEL)) {
                if (!level.isClientSide) {
                    if (!mixer.isLit() && mixer.lightMixer()) {
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
            if (be instanceof AlloyMixerBlockEntity mixer) {
                player.openMenu(mixer, pos);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof AlloyMixerBlockEntity mixer) {
                Containers.dropContents(level, pos, mixer);
                level.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Override
    public void registerJeiCategories(IRecipeCategoryRegistration registration, IGuiHelper guiHelper) {
        registration.addRecipeCategories(new AlloyMixerRecipeCategory(guiHelper));
    }

    @Override
    public void registerJeiRecipes(IRecipeRegistration registration) {
        int cookTime = ModConfig.SERVER != null ? ModConfig.SERVER.alloyMixerCookTimeTicks.get() : 600;
        registration.addRecipes(AlloyMixerRecipeCategory.TYPE, AlloyRegistry.getJeiRecipes(cookTime));
    }

    @Override
    public void registerJeiCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(this), AlloyMixerRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(this), MachineFuelRecipeCategory.TYPE);
    }

    @Override
    public void registerJeiGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(AlloyMixerScreen.class, 79, 24, 24, 17, AlloyMixerRecipeCategory.TYPE, MachineFuelRecipeCategory.TYPE);
    }

    @Override
    public void registerJeiRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(AlloyMixerMenu.class, ModMenuTypes.ALLOY_MIXER.get(), AlloyMixerRecipeCategory.TYPE, 0, 3, 4, 36);
    }

    @Override
    public void registerJeiInfo(IRecipeRegistration registration) {
        registration.addIngredientInfo(new ItemStack(this), mezz.jei.api.constants.VanillaTypes.ITEM_STACK,
                Component.translatable("jei.larperthanwolves.info.alloy_mixer"));
    }
}
