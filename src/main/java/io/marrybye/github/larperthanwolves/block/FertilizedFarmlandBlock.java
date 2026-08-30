package io.marrybye.github.larperthanwolves.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class FertilizedFarmlandBlock extends FarmBlock {
    public static final MapCodec<FertilizedFarmlandBlock> CODEC = simpleCodec(FertilizedFarmlandBlock::new);

    public FertilizedFarmlandBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(MOISTURE, 0));
    }

    @Override
    @SuppressWarnings("unchecked")
    public MapCodec<FarmBlock> codec() {
        return (MapCodec<FarmBlock>) (MapCodec<?>) CODEC;
    }

    /**
     * Converts a fertilized farmland block back to regular farmland, preserving moisture.
     */
    public static BlockState getUnfertilizedState(BlockState state) {
        int moisture = state.hasProperty(MOISTURE) ? state.getValue(MOISTURE) : 0;
        return Blocks.FARMLAND.defaultBlockState().setValue(MOISTURE, moisture);
    }
}
