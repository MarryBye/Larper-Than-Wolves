package io.marrybye.github.larperthanwolves.block;

import com.mojang.serialization.MapCodec;
import io.marrybye.github.larperthanwolves.block.entity.MillBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import io.marrybye.github.larperthanwolves.compat.IJeiMachineStation;
import io.marrybye.github.larperthanwolves.compat.MillJeiRecipe;
import io.marrybye.github.larperthanwolves.compat.MillRecipeCategory;
import io.marrybye.github.larperthanwolves.client.MillScreen;
import io.marrybye.github.larperthanwolves.menu.MillMenu;
import io.marrybye.github.larperthanwolves.menu.ModMenuTypes;
import io.marrybye.github.larperthanwolves.recipe.MillRecipe;
import io.marrybye.github.larperthanwolves.recipe.MillRegistry;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MillBlock extends BaseEntityBlock implements IJeiMachineStation {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final MapCodec<MillBlock> CODEC = simpleCodec(MillBlock::new);
    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 14, 16);

    public MillBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
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
        return new MillBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> getTicker(Level level, BlockState state, net.minecraft.world.level.block.entity.BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, io.marrybye.github.larperthanwolves.block.entity.ModBlockEntities.MILL.get(), MillBlockEntity::serverTick);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
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
            if (blockEntity instanceof MillBlockEntity mill) {
                Containers.dropContents(level, pos, mill);
                level.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Override
    public void registerJeiCategories(IRecipeCategoryRegistration registration, IGuiHelper guiHelper) {
        registration.addRecipeCategories(new MillRecipeCategory(guiHelper));
    }

    @Override
    public void registerJeiRecipes(IRecipeRegistration registration) {
        List<MillJeiRecipe> jeiMillRecipes = new ArrayList<>();
        for (MillRecipe recipe : MillRegistry.getRecipes()) {
            ItemStack[] matching = recipe.getIngredient().getItems();
            if (matching.length > 0) {
                ItemStack display = matching[0].copy();
                display.setCount(recipe.getInputCount());
                jeiMillRecipes.add(new MillJeiRecipe(display, recipe.getResults(), 20));
            }
        }
        registration.addRecipes(MillRecipeCategory.TYPE, jeiMillRecipes);
    }

    @Override
    public void registerJeiCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(this), MillRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.MILL_CRANK.get()), MillRecipeCategory.TYPE);
    }

    @Override
    public void registerJeiGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(MillScreen.class, 74, 34, 24, 17, MillRecipeCategory.TYPE);
    }

    @Override
    public void registerJeiRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(MillMenu.class, ModMenuTypes.MILL.get(), MillRecipeCategory.TYPE, 0, 1, 4, 36);
    }

    @Override
    public void registerJeiInfo(IRecipeRegistration registration) {
        registration.addIngredientInfo(new ItemStack(this), mezz.jei.api.constants.VanillaTypes.ITEM_STACK,
                net.minecraft.network.chat.Component.translatable("jei.larperthanwolves.info.mill"));
    }
}
