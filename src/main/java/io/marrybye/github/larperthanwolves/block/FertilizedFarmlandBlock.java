package io.marrybye.github.larperthanwolves.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import io.marrybye.github.larperthanwolves.compat.IJeiDocumentationProvider;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class FertilizedFarmlandBlock extends FarmBlock implements IJeiDocumentationProvider {
    public static final MapCodec<FertilizedFarmlandBlock> CODEC = simpleCodec(FertilizedFarmlandBlock::new);
    public static final IntegerProperty CHARGES = IntegerProperty.create("charges", 1, 3);

    public FertilizedFarmlandBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(MOISTURE, 0).setValue(CHARGES, 1));
    }

    @Override
    public void registerJeiInfo(IRecipeRegistration registration) {
        registration.addIngredientInfo(new ItemStack(this), VanillaTypes.ITEM_STACK,
                Component.translatable("jei.larperthanwolves.info.fertilized_farmland"));
    }

    @Override
    @SuppressWarnings("unchecked")
    public MapCodec<FarmBlock> codec() {
        return (MapCodec<FarmBlock>) (MapCodec<?>) CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(MOISTURE, CHARGES);
    }

    /**
     * Decreases the fertilization charges after a crop harvest.
     * If charges remain (> 1), decreases charge level.
     * If this was the last charge (1), resets to unfertilized vanilla farmland.
     */
    public static BlockState getAfterHarvestState(BlockState state) {
        int moisture = state.hasProperty(MOISTURE) ? state.getValue(MOISTURE) : 0;
        int charges = state.hasProperty(CHARGES) ? state.getValue(CHARGES) : 1;

        if (charges > 1) {
            return state.setValue(CHARGES, charges - 1);
        } else {
            return Blocks.FARMLAND.defaultBlockState().setValue(MOISTURE, moisture);
        }
    }

    /**
     * Converts a fertilized farmland block back to regular farmland, preserving moisture.
     */
    public static BlockState getUnfertilizedState(BlockState state) {
        int moisture = state.hasProperty(MOISTURE) ? state.getValue(MOISTURE) : 0;
        return Blocks.FARMLAND.defaultBlockState().setValue(MOISTURE, moisture);
    }
}
