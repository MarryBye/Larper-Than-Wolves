package io.marrybye.github.larperthanwolves.block;

import com.mojang.serialization.MapCodec;
import io.marrybye.github.larperthanwolves.block.entity.FuelRegistry;
import io.marrybye.github.larperthanwolves.block.entity.MithrilFurnaceBlockEntity;
import io.marrybye.github.larperthanwolves.block.entity.ModBlockEntities;
import io.marrybye.github.larperthanwolves.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import org.jetbrains.annotations.Nullable;

import io.marrybye.github.larperthanwolves.compat.IJeiMachineStation;
import io.marrybye.github.larperthanwolves.compat.MachineFuelRecipeCategory;
import io.marrybye.github.larperthanwolves.compat.MithrilFurnaceJeiRecipe;
import io.marrybye.github.larperthanwolves.compat.MithrilFurnaceRecipeCategory;
import io.marrybye.github.larperthanwolves.client.MithrilFurnaceScreen;
import io.marrybye.github.larperthanwolves.menu.MithrilFurnaceMenu;
import io.marrybye.github.larperthanwolves.menu.ModMenuTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class MithrilFurnaceBlock extends BaseEntityBlock implements IJeiMachineStation {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    // 0: Empty, 1: Fueled, 2: Lit (Strong), 3: Lit Low (Dying embers)
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 3);
    public static final MapCodec<MithrilFurnaceBlock> CODEC = simpleCodec(MithrilFurnaceBlock::new);

    public MithrilFurnaceBlock(Properties properties) {
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
        return new MithrilFurnaceBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModBlockEntities.MITHRIL_FURNACE.get(),
                (level, pos, state, entity) -> MithrilFurnaceBlockEntity.tick(level, pos, state, (MithrilFurnaceBlockEntity) entity));
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof MithrilFurnaceBlockEntity furnace) {
            // 1. Check if holding valid fuel
            if (FuelRegistry.isValidFuel(stack)) {
                if (!level.isClientSide) {
                    ItemStack remainder = stack.getCraftingRemainingItem();
                    if (furnace.addFuel(stack)) {
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
                        level.setBlock(pos, state.setValue(STAGE, furnace.isLit() ? (furnace.getBurnTime() <= furnace.getMaxBurnTime() / 4 ? 3 : 2) : 1), 3);
                        level.playSound(null, pos, SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 1.0f, 1.0f);
                    }
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }

            // 2. Check if holding lighter or flint & steel
            if (stack.is(ModItems.LIGHTER.get()) || stack.is(Items.FLINT_AND_STEEL)) {
                if (!level.isClientSide) {
                    if (!furnace.isLit() && furnace.lightFurnace()) {
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
            if (be instanceof MithrilFurnaceBlockEntity furnace) {
                player.openMenu(furnace, pos);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof MithrilFurnaceBlockEntity furnace) {
                Containers.dropContents(level, pos, furnace);
                level.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Override
    public void registerJeiCategories(IRecipeCategoryRegistration registration, IGuiHelper guiHelper) {
        registration.addRecipeCategories(new MithrilFurnaceRecipeCategory(guiHelper));
    }

    @Override
    public void registerJeiRecipes(IRecipeRegistration registration) {
        List<MithrilFurnaceJeiRecipe> mithrilFurnaceRecipes = new ArrayList<>(List.of(
                new MithrilFurnaceJeiRecipe(new ItemStack(ModItems.RAW_MITHRIL.get()), new ItemStack(ModItems.MITHRIL_INGOT.get()), 200),
                new MithrilFurnaceJeiRecipe(new ItemStack(Items.RAW_IRON), new ItemStack(Items.IRON_INGOT), 200),
                new MithrilFurnaceJeiRecipe(new ItemStack(Items.RAW_COPPER), new ItemStack(Items.COPPER_INGOT), 200),
                new MithrilFurnaceJeiRecipe(new ItemStack(Items.RAW_GOLD), new ItemStack(Items.GOLD_INGOT), 200),
                new MithrilFurnaceJeiRecipe(new ItemStack(ModItems.RAW_TIN.get()), new ItemStack(ModItems.TIN_INGOT.get()), 200),
                new MithrilFurnaceJeiRecipe(new ItemStack(Items.IRON_ORE), new ItemStack(Items.IRON_INGOT), 200),
                new MithrilFurnaceJeiRecipe(new ItemStack(Items.DEEPSLATE_IRON_ORE), new ItemStack(Items.IRON_INGOT), 200),
                new MithrilFurnaceJeiRecipe(new ItemStack(Items.COPPER_ORE), new ItemStack(Items.COPPER_INGOT), 200),
                new MithrilFurnaceJeiRecipe(new ItemStack(Items.DEEPSLATE_COPPER_ORE), new ItemStack(Items.COPPER_INGOT), 200),
                new MithrilFurnaceJeiRecipe(new ItemStack(Items.GOLD_ORE), new ItemStack(Items.GOLD_INGOT), 200),
                new MithrilFurnaceJeiRecipe(new ItemStack(Items.DEEPSLATE_GOLD_ORE), new ItemStack(Items.GOLD_INGOT), 200),
                new MithrilFurnaceJeiRecipe(new ItemStack(Items.NETHER_GOLD_ORE), new ItemStack(Items.GOLD_INGOT), 200),
                new MithrilFurnaceJeiRecipe(new ItemStack(ModBlocks.TIN_ORE.get()), new ItemStack(ModItems.TIN_INGOT.get()), 200),
                new MithrilFurnaceJeiRecipe(new ItemStack(ModBlocks.DEEPSLATE_TIN_ORE.get()), new ItemStack(ModItems.TIN_INGOT.get()), 200),
                new MithrilFurnaceJeiRecipe(new ItemStack(Items.COBBLESTONE), new ItemStack(Items.STONE), 200),
                new MithrilFurnaceJeiRecipe(new ItemStack(Items.SAND), new ItemStack(Items.GLASS), 200),
                new MithrilFurnaceJeiRecipe(new ItemStack(ModBlocks.UNFIRED_BRICK.asItem()), new ItemStack(Items.BRICK), 200),
                new MithrilFurnaceJeiRecipe(new ItemStack(Items.CLAY), new ItemStack(Items.TERRACOTTA), 200),
                new MithrilFurnaceJeiRecipe(new ItemStack(Items.WET_SPONGE), new ItemStack(Items.SPONGE), 200)
        ));
        net.minecraft.world.item.Item zincIngotItem = net.minecraft.core.registries.BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("create", "zinc_ingot"));
        if (zincIngotItem != Items.AIR) {
            net.minecraft.world.item.Item rawZincItem = net.minecraft.core.registries.BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("create", "raw_zinc"));
            if (rawZincItem != Items.AIR) {
                mithrilFurnaceRecipes.add(new MithrilFurnaceJeiRecipe(new ItemStack(rawZincItem), new ItemStack(zincIngotItem), 200));
            }
        }
        registration.addRecipes(MithrilFurnaceRecipeCategory.TYPE, mithrilFurnaceRecipes);
    }

    @Override
    public void registerJeiCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(this), MithrilFurnaceRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(this), MachineFuelRecipeCategory.TYPE);
    }

    @Override
    public void registerJeiGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(MithrilFurnaceScreen.class, 79, 34, 24, 17, MithrilFurnaceRecipeCategory.TYPE, MachineFuelRecipeCategory.TYPE);
    }

    @Override
    public void registerJeiRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(MithrilFurnaceMenu.class, ModMenuTypes.MITHRIL_FURNACE.get(), MithrilFurnaceRecipeCategory.TYPE, 0, 3, 6, 36);
    }
}
