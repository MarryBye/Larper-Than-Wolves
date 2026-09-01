package io.marrybye.github.larperthanwolves.block;

import com.mojang.serialization.MapCodec;
import io.marrybye.github.larperthanwolves.block.entity.ModBlockEntities;
import io.marrybye.github.larperthanwolves.block.entity.SieveBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import io.marrybye.github.larperthanwolves.compat.IJeiDocumentationProvider;
import io.marrybye.github.larperthanwolves.compat.IJeiMachineStation;
import io.marrybye.github.larperthanwolves.compat.SieveJeiRecipe;
import io.marrybye.github.larperthanwolves.compat.SieveRecipeCategory;
import io.marrybye.github.larperthanwolves.client.SieveScreen;
import io.marrybye.github.larperthanwolves.config.ModConfig;
import io.marrybye.github.larperthanwolves.item.ModItems;
import io.marrybye.github.larperthanwolves.menu.ModMenuTypes;
import io.marrybye.github.larperthanwolves.menu.SieveMenu;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class SieveBlock extends BaseEntityBlock implements IJeiMachineStation, IJeiDocumentationProvider {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final MapCodec<SieveBlock> CODEC = simpleCodec(SieveBlock::new);

    public SieveBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SieveBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide
                ? createTickerHelper(blockEntityType, ModBlockEntities.SIEVE.get(), SieveBlockEntity::clientTick)
                : createTickerHelper(blockEntityType, ModBlockEntities.SIEVE.get(), SieveBlockEntity::serverTick);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (player.isShiftKeyDown() || player.isCrouching()) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof SieveBlockEntity sieve) {
                InteractionResult result = sieve.performManualShake(player, level, pos);
                if (result.consumesAction()) {
                    return ItemInteractionResult.sidedSuccess(level.isClientSide);
                }
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (player.isShiftKeyDown() || player.isCrouching()) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof SieveBlockEntity sieve) {
                InteractionResult result = sieve.performManualShake(player, level, pos);
                if (result.consumesAction()) {
                    return result;
                }
            }
        }

        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof MenuProvider menuProvider) {
                player.openMenu(menuProvider, pos);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof SieveBlockEntity sieve) {
                Containers.dropContents(level, pos, sieve);
                level.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Override
    public void registerJeiCategories(IRecipeCategoryRegistration registration, IGuiHelper guiHelper) {
        registration.addRecipeCategories(new SieveRecipeCategory(guiHelper));
    }

    @Override
    public void registerJeiRecipes(IRecipeRegistration registration) {
        int sieveTime = ModConfig.SERVER != null ? ModConfig.SERVER.sieveProcessTimeTicks.get() : 100;
        List<ItemStack> regularSoilOutputs = List.of(
                new ItemStack(ModItems.SILICON_SHARD.get()),
                new ItemStack(Items.FLINT),
                new ItemStack(ModItems.COPPER_DUST.get()),
                new ItemStack(ModItems.TIN_DUST.get()),
                new ItemStack(ModItems.IRON_DUST.get())
        );

        List<ItemStack> suspGravelOutputs = List.of(
                new ItemStack(ModItems.SILICON_SHARD.get()),
                new ItemStack(Items.FLINT),
                new ItemStack(ModItems.COPPER_DUST.get()),
                new ItemStack(ModItems.TIN_DUST.get()),
                new ItemStack(ModItems.IRON_DUST.get()),
                new ItemStack(Items.EMERALD),
                new ItemStack(Items.WHEAT),
                new ItemStack(Items.BURN_POTTERY_SHERD)
        );

        List<ItemStack> suspSandOutputs = List.of(
                new ItemStack(ModItems.SILICON_SHARD.get()),
                new ItemStack(Items.FLINT),
                new ItemStack(ModItems.COPPER_DUST.get()),
                new ItemStack(ModItems.TIN_DUST.get()),
                new ItemStack(ModItems.IRON_DUST.get()),
                new ItemStack(Items.DIAMOND),
                new ItemStack(Items.EMERALD),
                new ItemStack(Items.SNIFFER_EGG),
                new ItemStack(Items.ARCHER_POTTERY_SHERD)
        );

        List<ItemStack> richSoilOutputs = List.of(
                new ItemStack(ModItems.COPPER_DUST.get()),
                new ItemStack(ModItems.TIN_DUST.get()),
                new ItemStack(ModItems.IRON_DUST.get()),
                new ItemStack(Items.FLINT)
        );

        registration.addRecipes(SieveRecipeCategory.TYPE, List.of(
                new SieveJeiRecipe(new ItemStack(Blocks.GRAVEL), regularSoilOutputs, sieveTime),
                new SieveJeiRecipe(new ItemStack(Blocks.SAND), regularSoilOutputs, sieveTime),
                new SieveJeiRecipe(new ItemStack(Blocks.RED_SAND), regularSoilOutputs, sieveTime),
                new SieveJeiRecipe(new ItemStack(Blocks.DIRT), regularSoilOutputs, sieveTime),
                new SieveJeiRecipe(new ItemStack(Blocks.GRASS_BLOCK), regularSoilOutputs, sieveTime),
                new SieveJeiRecipe(new ItemStack(Blocks.SUSPICIOUS_GRAVEL), suspGravelOutputs, sieveTime),
                new SieveJeiRecipe(new ItemStack(Blocks.SUSPICIOUS_SAND), suspSandOutputs, sieveTime),
                new SieveJeiRecipe(new ItemStack(ModBlocks.RICH_GRASS_BLOCK.get()), richSoilOutputs, sieveTime),
                new SieveJeiRecipe(new ItemStack(ModBlocks.RICH_DIRT.get()), richSoilOutputs, sieveTime),
                new SieveJeiRecipe(new ItemStack(ModBlocks.RICH_GRAVEL.get()), richSoilOutputs, sieveTime),
                new SieveJeiRecipe(new ItemStack(ModBlocks.RICH_SAND.get()), richSoilOutputs, sieveTime),
                new SieveJeiRecipe(new ItemStack(ModBlocks.RICH_RED_SAND.get()), richSoilOutputs, sieveTime)
        ));
    }

    @Override
    public void registerJeiCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(this), SieveRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.MILL_CRANK.get()), SieveRecipeCategory.TYPE);
    }

    @Override
    public void registerJeiGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(SieveScreen.class, 76, 34, 24, 17, SieveRecipeCategory.TYPE);
    }

    @Override
    public void registerJeiRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(SieveMenu.class, ModMenuTypes.SIEVE.get(), SieveRecipeCategory.TYPE, 0, 9, 18, 36);
    }

    @Override
    public void registerJeiInfo(IRecipeRegistration registration) {
        registration.addIngredientInfo(new ItemStack(this), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.larperthanwolves.info.sieve"));
    }
}
