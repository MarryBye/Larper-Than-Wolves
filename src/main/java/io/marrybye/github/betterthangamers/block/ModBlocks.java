package io.marrybye.github.betterthangamers.block;

import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks("betterthangamers");

    public static final DeferredBlock<BrickFurnaceBlock> BRICK_FURNACE = BLOCKS.register("brick_furnace",
            () -> new BrickFurnaceBlock(BlockBehaviour.Properties.of()
                    .requiresCorrectToolForDrops()
                    .strength(3.0F, 10.0F)));

    public static final DeferredBlock<Block> BRICK_SLAB = BLOCKS.register("brick_slab",
            () -> new net.minecraft.world.level.block.SlabBlock(BlockBehaviour.Properties.of()
                    .requiresCorrectToolForDrops()
                    .strength(2.0F, 6.0F)));
}

