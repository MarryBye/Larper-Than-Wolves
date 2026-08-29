package io.marrybye.github.larperthanwolves.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class WorkStumpBlock extends Block {
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
}
