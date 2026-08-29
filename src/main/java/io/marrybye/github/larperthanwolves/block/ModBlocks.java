package io.marrybye.github.larperthanwolves.block;

import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks("larperthanwolves");

    public static final DeferredBlock<BrickFurnaceBlock> BRICK_FURNACE = BLOCKS.register("brick_furnace",
            () -> new BrickFurnaceBlock(BlockBehaviour.Properties.of()
                    .requiresCorrectToolForDrops()
                    .strength(3.0F, 10.0F)
                    .lightLevel(state -> {
                        int stage = state.hasProperty(BrickFurnaceBlock.STAGE) ? state.getValue(BrickFurnaceBlock.STAGE) : 0;
                        return stage == 2 ? 14 : (stage == 3 ? 8 : 0);
                    })));

    public static final DeferredBlock<UnfiredBrickBlock> UNFIRED_BRICK = BLOCKS.register("unfired_brick",
            () -> new UnfiredBrickBlock(BlockBehaviour.Properties.of()
                    .strength(0.3F, 0.3F)
                    .sound(net.minecraft.world.level.block.SoundType.GRAVEL)
                    .noOcclusion()));
}

