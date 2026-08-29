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

    public static final DeferredBlock<AlloyMixerBlock> ALLOY_MIXER = BLOCKS.register("alloy_mixer",
            () -> new AlloyMixerBlock(BlockBehaviour.Properties.of()
                    .requiresCorrectToolForDrops()
                    .strength(3.5F, 12.0F)
                    .sound(net.minecraft.world.level.block.SoundType.METAL)
                    .lightLevel(state -> {
                        int stage = state.hasProperty(AlloyMixerBlock.STAGE) ? state.getValue(AlloyMixerBlock.STAGE) : 0;
                        return stage == 2 ? 13 : (stage == 3 ? 7 : 0);
                    })));

    public static final DeferredBlock<WorkStumpBlock> WORK_STUMP = BLOCKS.register("work_stump",
            () -> new WorkStumpBlock(BlockBehaviour.Properties.of()
                    .strength(2.0F, 2.0F)
                    .sound(net.minecraft.world.level.block.SoundType.WOOD)));

    public static final DeferredBlock<SieveBlock> SIEVE = BLOCKS.register("sieve",
            () -> new SieveBlock(BlockBehaviour.Properties.of()
                    .mapColor(net.minecraft.world.level.material.MapColor.WOOD)
                    .instrument(net.minecraft.world.level.block.state.properties.NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F)
                    .sound(net.minecraft.world.level.block.SoundType.WOOD)
                    .noOcclusion()));

    public static final DeferredBlock<Block> TIN_ORE = BLOCKS.register("tin_ore",
            () -> new Block(BlockBehaviour.Properties.of()
                    .requiresCorrectToolForDrops()
                    .strength(3.0F, 3.0F)
                    .sound(net.minecraft.world.level.block.SoundType.STONE)));

    public static final DeferredBlock<Block> DEEPSLATE_TIN_ORE = BLOCKS.register("deepslate_tin_ore",
            () -> new Block(BlockBehaviour.Properties.of()
                    .requiresCorrectToolForDrops()
                    .strength(4.5F, 3.0F)
                    .sound(net.minecraft.world.level.block.SoundType.DEEPSLATE)));

    public static final DeferredBlock<Block> RAW_TIN_BLOCK = BLOCKS.register("raw_tin_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(net.minecraft.world.level.block.SoundType.STONE)));

    public static final DeferredBlock<Block> TIN_BLOCK = BLOCKS.register("tin_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(net.minecraft.world.level.block.SoundType.METAL)));

    public static final DeferredBlock<Block> BRONZE_BLOCK = BLOCKS.register("bronze_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(net.minecraft.world.level.block.SoundType.METAL)));
}

