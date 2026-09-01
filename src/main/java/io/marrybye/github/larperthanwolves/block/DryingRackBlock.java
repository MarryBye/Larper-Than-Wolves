package io.marrybye.github.larperthanwolves.block;

import com.mojang.serialization.MapCodec;
import io.marrybye.github.larperthanwolves.block.entity.DryingRackBlockEntity;
import io.marrybye.github.larperthanwolves.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import io.marrybye.github.larperthanwolves.compat.DryingRackRecipe;
import io.marrybye.github.larperthanwolves.compat.DryingRackRecipeCategory;
import io.marrybye.github.larperthanwolves.compat.IJeiMachineStation;
import io.marrybye.github.larperthanwolves.config.ModConfig;
import io.marrybye.github.larperthanwolves.item.ModItems;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DryingRackBlock extends BaseEntityBlock implements IJeiMachineStation {
    public static final MapCodec<DryingRackBlock> CODEC = simpleCodec(DryingRackBlock::new);

    public enum Content implements StringRepresentable {
        NONE("none"),
        GRASS("grass"),
        DRY_GRASS("dry_grass"),
        LEATHER("leather"),
        TANNED_LEATHER("tanned_leather");

        private final String name;

        Content(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<Content> CONTENT = EnumProperty.create("content", Content.class);

    protected static final VoxelShape SHAPE_NORTH_SOUTH = Block.box(1.0, 0.0, 6.0, 15.0, 16.0, 10.0);
    protected static final VoxelShape SHAPE_EAST_WEST = Block.box(6.0, 0.0, 1.0, 10.0, 16.0, 15.0);

    public DryingRackBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(CONTENT, Content.NONE));
    }

    @Override
    public MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, CONTENT);
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
                .setValue(CONTENT, Content.NONE);
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

    public static boolean isDryableGrass(ItemStack stack) {
        return stack.is(Items.SHORT_GRASS) ||
                stack.is(Items.TALL_GRASS) ||
                stack.is(Items.FERN) ||
                stack.is(Items.LARGE_FERN) ||
                stack.is(Items.SEAGRASS);
    }

    public static boolean isDryableLeather(ItemStack stack) {
        return stack.is(Items.LEATHER);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack held, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof DryingRackBlockEntity entity)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (entity.isEmpty()) {
            if (isDryableGrass(held)) {
                if (!level.isClientSide) {
                    ItemStack inserted = held.split(1);
                    entity.setItem(0, inserted);
                    level.setBlock(pos, state.setValue(CONTENT, Content.GRASS), 3);
                    level.playSound(null, pos, SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 1.0f, 1.0f);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            } else if (isDryableLeather(held)) {
                if (!level.isClientSide) {
                    ItemStack inserted = held.split(1);
                    entity.setItem(0, inserted);
                    level.setBlock(pos, state.setValue(CONTENT, Content.LEATHER), 3);
                    level.playSound(null, pos, SoundEvents.ARMOR_EQUIP_LEATHER.value(), SoundSource.BLOCKS, 1.0f, 1.0f);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        } else {
            // Retrieve existing item
            if (!level.isClientSide) {
                ItemStack stored = entity.removeItem(0, 1);
                level.setBlock(pos, state.setValue(CONTENT, Content.NONE), 3);
                if (!player.addItem(stored)) {
                    player.drop(stored, false);
                }
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.8f, 1.0f);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof DryingRackBlockEntity entity && !entity.isEmpty()) {
            if (!level.isClientSide) {
                ItemStack stored = entity.removeItem(0, 1);
                level.setBlock(pos, state.setValue(CONTENT, Content.NONE), 3);
                if (!player.addItem(stored)) {
                    player.drop(stored, false);
                }
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.8f, 1.0f);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof DryingRackBlockEntity rack) {
                Containers.dropContents(level, pos, rack);
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DryingRackBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : createTickerHelper(type, ModBlockEntities.DRYING_RACK.get(), DryingRackBlockEntity::tick);
    }

    @Override
    public void registerJeiCategories(IRecipeCategoryRegistration registration, IGuiHelper guiHelper) {
        registration.addRecipeCategories(new DryingRackRecipeCategory(guiHelper));
    }

    @Override
    public void registerJeiRecipes(IRecipeRegistration registration) {
        int dryingRackTime = ModConfig.SERVER != null ? ModConfig.SERVER.dryingRackTimeTicks.get() : 1200;
        List<ItemStack> grassInputs = List.of(
                new ItemStack(Items.SHORT_GRASS),
                new ItemStack(Items.TALL_GRASS),
                new ItemStack(Items.FERN),
                new ItemStack(Items.LARGE_FERN),
                new ItemStack(Items.SEAGRASS)
        );
        registration.addRecipes(DryingRackRecipeCategory.TYPE, List.of(
                new DryingRackRecipe(grassInputs, new ItemStack(ModItems.DRY_GRASS.get()), dryingRackTime),
                new DryingRackRecipe(List.of(new ItemStack(Items.LEATHER)), new ItemStack(ModItems.TANNED_LEATHER.get()), dryingRackTime)
        ));
    }

    @Override
    public void registerJeiCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(this), DryingRackRecipeCategory.TYPE);
    }

    @Override
    public void registerJeiGuiHandlers(IGuiHandlerRegistration registration) {}

    @Override
    public void registerJeiRecipeTransferHandlers(IRecipeTransferRegistration registration) {}

    @Override
    public void registerJeiInfo(IRecipeRegistration registration) {
        registration.addIngredientInfo(new ItemStack(this), mezz.jei.api.constants.VanillaTypes.ITEM_STACK,
                net.minecraft.network.chat.Component.translatable("jei.larperthanwolves.info.drying_rack"));
    }
}
