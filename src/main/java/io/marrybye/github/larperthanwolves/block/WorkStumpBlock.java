package io.marrybye.github.larperthanwolves.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import io.marrybye.github.larperthanwolves.compat.ChiselRecipe;
import io.marrybye.github.larperthanwolves.compat.ChiselRecipeCategory;
import io.marrybye.github.larperthanwolves.compat.IJeiMachineStation;
import io.marrybye.github.larperthanwolves.item.ModItems;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public class WorkStumpBlock extends Block implements IJeiMachineStation {
    // 0: Initial carving, 1: Half carved, 2: Final carving before crafting table
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 2);

    public WorkStumpBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(STAGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STAGE);
    }

    @Override
    public void registerJeiCategories(IRecipeCategoryRegistration registration, IGuiHelper guiHelper) {
        registration.addRecipeCategories(new ChiselRecipeCategory(guiHelper));
    }

    @Override
    public void registerJeiRecipes(IRecipeRegistration registration) {
        List<ItemStack> carvableStumps = List.of(
                new ItemStack(ModBlocks.OAK_STUMP.get()),
                new ItemStack(ModBlocks.BIRCH_STUMP.get()),
                new ItemStack(ModBlocks.SPRUCE_STUMP.get()),
                new ItemStack(ModBlocks.JUNGLE_STUMP.get()),
                new ItemStack(ModBlocks.ACACIA_STUMP.get()),
                new ItemStack(ModBlocks.DARK_OAK_STUMP.get()),
                new ItemStack(ModBlocks.MANGROVE_STUMP.get()),
                new ItemStack(ModBlocks.CHERRY_STUMP.get())
        );

        registration.addRecipes(ChiselRecipeCategory.TYPE, List.of(
                new ChiselRecipe(
                        carvableStumps,
                        new ItemStack(ModItems.CHISEL.get()),
                        new ItemStack(this),
                        new ItemStack(Items.CRAFTING_TABLE),
                        4
                )
        ));
    }

    @Override
    public void registerJeiCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(this), ChiselRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModItems.CHISEL.get()), ChiselRecipeCategory.TYPE);
    }

    @Override
    public void registerJeiGuiHandlers(IGuiHandlerRegistration registration) {}

    @Override
    public void registerJeiRecipeTransferHandlers(IRecipeTransferRegistration registration) {}
}
