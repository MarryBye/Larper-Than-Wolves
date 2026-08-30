package io.marrybye.github.larperthanwolves.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

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

    public static final DeferredBlock<FertilizedFarmlandBlock> FERTILIZED_FARMLAND = BLOCKS.register("fertilized_farmland",
            () -> new FertilizedFarmlandBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FARMLAND)));

    public static final DeferredBlock<OvenBlock> OVEN = BLOCKS.register("oven",
            () -> new OvenBlock(BlockBehaviour.Properties.of()
                    .requiresCorrectToolForDrops()
                    .strength(3.0F, 10.0F)
                    .lightLevel(state -> {
                        int stage = state.hasProperty(OvenBlock.STAGE) ? state.getValue(OvenBlock.STAGE) : 0;
                        return stage == 2 ? 14 : (stage == 3 ? 8 : 0);
                    })));

    public static final DeferredBlock<UnfiredBrickBlock> UNFIRED_BRICK = BLOCKS.register("unfired_brick",
            () -> new UnfiredBrickBlock(BlockBehaviour.Properties.of()
                    .strength(0.3F, 0.3F)
                    .sound(SoundType.GRAVEL)
                    .noOcclusion()));

    public static final DeferredBlock<AlloyMixerBlock> ALLOY_MIXER = BLOCKS.register("alloy_mixer",
            () -> new AlloyMixerBlock(BlockBehaviour.Properties.of()
                    .requiresCorrectToolForDrops()
                    .strength(3.5F, 12.0F)
                    .sound(SoundType.METAL)
                    .lightLevel(state -> {
                        int stage = state.hasProperty(AlloyMixerBlock.STAGE) ? state.getValue(AlloyMixerBlock.STAGE) : 0;
                        return stage == 2 ? 13 : (stage == 3 ? 7 : 0);
                    })));

    public static final DeferredBlock<WorkStumpBlock> WORK_STUMP = BLOCKS.register("work_stump",
            () -> new WorkStumpBlock(BlockBehaviour.Properties.of()
                    .strength(2.0F, 2.0F)
                    .sound(SoundType.WOOD)));

    public static final DeferredBlock<SieveBlock> SIEVE = BLOCKS.register("sieve",
            () -> new SieveBlock(BlockBehaviour.Properties.of()
                    .mapColor(net.minecraft.world.level.material.MapColor.WOOD)
                    .instrument(net.minecraft.world.level.block.state.properties.NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()));

    public static final DeferredBlock<BasketBlock> BASKET = BLOCKS.register("basket",
            () -> new BasketBlock(BlockBehaviour.Properties.of()
                    .mapColor(net.minecraft.world.level.material.MapColor.WOOD)
                    .strength(0.8F, 1.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()));

    public static final DeferredBlock<DryingRackBlock> DRYING_RACK = BLOCKS.register("drying_rack",
            () -> new DryingRackBlock(BlockBehaviour.Properties.of()
                    .mapColor(net.minecraft.world.level.material.MapColor.WOOD)
                    .instrument(net.minecraft.world.level.block.state.properties.NoteBlockInstrument.BASS)
                    .strength(1.5F, 2.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()));

    public static final DeferredBlock<MillBlock> MILL = BLOCKS.register("mill",
            () -> new MillBlock(BlockBehaviour.Properties.of()
                    .requiresCorrectToolForDrops()
                    .strength(3.5F, 10.0F)
                    .sound(SoundType.STONE)
                    .noOcclusion()));

    public static final DeferredBlock<MillCrankBlock> MILL_CRANK = BLOCKS.register("mill_crank",
            () -> new MillCrankBlock(BlockBehaviour.Properties.of()
                    .strength(1.5F, 2.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()));

    public static final DeferredBlock<KineticPistonBlock> KINETIC_PISTON = BLOCKS.register("kinetic_piston",
            () -> new KineticPistonBlock(BlockBehaviour.Properties.of()
                    .requiresCorrectToolForDrops()
                    .strength(3.5F, 12.0F)
                    .sound(SoundType.STONE)));

    public static final DeferredBlock<KineticPistonHeadBlock> KINETIC_PISTON_HEAD = BLOCKS.register("kinetic_piston_head",
            () -> new KineticPistonHeadBlock(BlockBehaviour.Properties.of()
                    .strength(1.5F, 12.0F)
                    .sound(SoundType.STONE)
                    .noOcclusion()
                    .noLootTable()));

    public static final DeferredBlock<FilterGrateBlock> FILTER_GRATE = BLOCKS.register("filter_grate",
            () -> new FilterGrateBlock(BlockBehaviour.Properties.of()
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()));

    public static final DeferredBlock<EntityObserverBlock> ENTITY_OBSERVER = BLOCKS.register("entity_observer",
            () -> new EntityObserverBlock(BlockBehaviour.Properties.of()
                    .requiresCorrectToolForDrops()
                    .strength(3.0F, 3.0F)
                    .sound(SoundType.STONE)));

    public static final DeferredBlock<Block> TIN_ORE = BLOCKS.register("tin_ore",
            () -> new Block(BlockBehaviour.Properties.of()
                    .requiresCorrectToolForDrops()
                    .strength(3.0F, 3.0F)
                    .sound(SoundType.STONE)));

    public static final DeferredBlock<Block> DEEPSLATE_TIN_ORE = BLOCKS.register("deepslate_tin_ore",
            () -> new Block(BlockBehaviour.Properties.of()
                    .requiresCorrectToolForDrops()
                    .strength(4.5F, 3.0F)
                    .sound(SoundType.DEEPSLATE)));

    public static final DeferredBlock<Block> RAW_TIN_BLOCK = BLOCKS.register("raw_tin_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.STONE)));

    public static final DeferredBlock<Block> TIN_BLOCK = BLOCKS.register("tin_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.METAL)));

    public static final DeferredBlock<Block> BRONZE_BLOCK = BLOCKS.register("bronze_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.METAL)));

    // --- Rich Soils (Hardness 1.0f: 2x slower break than regular, mined with copper+ shovel) ---
    public static final DeferredBlock<RichGrassBlock> RICH_GRASS_BLOCK = BLOCKS.register("rich_grass_block",
            () -> new RichGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK)
                    .strength(1.2F, 1.2F)
                    .randomTicks()
                    .sound(SoundType.GRASS)));

    public static final DeferredBlock<Block> RICH_DIRT = BLOCKS.register("rich_dirt",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT)
                    .strength(1.0F, 1.0F)
                    .sound(SoundType.GRAVEL)));

    public static final DeferredBlock<Block> RICH_GRAVEL = BLOCKS.register("rich_gravel",
            () -> new RichFallingBlock(-8356741, BlockBehaviour.Properties.ofFullCopy(Blocks.GRAVEL)
                    .strength(1.0F, 1.0F)
                    .sound(SoundType.GRAVEL)));

    public static final DeferredBlock<Block> RICH_SAND = BLOCKS.register("rich_sand",
            () -> new RichFallingBlock(14406560, BlockBehaviour.Properties.ofFullCopy(Blocks.SAND)
                    .strength(1.0F, 1.0F)
                    .sound(SoundType.SAND)));

    public static final DeferredBlock<Block> RICH_RED_SAND = BLOCKS.register("rich_red_sand",
            () -> new RichFallingBlock(11098145, BlockBehaviour.Properties.ofFullCopy(Blocks.RED_SAND)
                    .strength(1.0F, 1.0F)
                    .sound(SoundType.SAND)));

    // --- Ground Forest Flora / Twigs ---
    public static final DeferredBlock<TwigBlock> TWIG = BLOCKS.register("twig",
            () -> new TwigBlock(BlockBehaviour.Properties.of()
                    .instabreak()
                    .noCollission()
                    .noOcclusion()
                    .sound(SoundType.WOOD)));

    // --- Tree Stumps (Hardness 25.0f: Ultra slow break, requires axe) ---
    private static BlockBehaviour.Properties stumpProps() {
        return BlockBehaviour.Properties.of()
                .requiresCorrectToolForDrops()
                .strength(25.0F, 25.0F)
                .sound(SoundType.WOOD);
    }

    public static final DeferredBlock<StumpBlock> OAK_STUMP = BLOCKS.register("oak_stump",
            () -> new StumpBlock(() -> Blocks.OAK_LOG, stumpProps()));

    public static final DeferredBlock<StumpBlock> BIRCH_STUMP = BLOCKS.register("birch_stump",
            () -> new StumpBlock(() -> Blocks.BIRCH_LOG, stumpProps()));

    public static final DeferredBlock<StumpBlock> SPRUCE_STUMP = BLOCKS.register("spruce_stump",
            () -> new StumpBlock(() -> Blocks.SPRUCE_LOG, stumpProps()));

    public static final DeferredBlock<StumpBlock> JUNGLE_STUMP = BLOCKS.register("jungle_stump",
            () -> new StumpBlock(() -> Blocks.JUNGLE_LOG, stumpProps()));

    public static final DeferredBlock<StumpBlock> ACACIA_STUMP = BLOCKS.register("acacia_stump",
            () -> new StumpBlock(() -> Blocks.ACACIA_LOG, stumpProps()));

    public static final DeferredBlock<StumpBlock> DARK_OAK_STUMP = BLOCKS.register("dark_oak_stump",
            () -> new StumpBlock(() -> Blocks.DARK_OAK_LOG, stumpProps()));

    public static final DeferredBlock<StumpBlock> MANGROVE_STUMP = BLOCKS.register("mangrove_stump",
            () -> new StumpBlock(() -> Blocks.MANGROVE_LOG, stumpProps()));

    public static final DeferredBlock<StumpBlock> CHERRY_STUMP = BLOCKS.register("cherry_stump",
            () -> new StumpBlock(() -> Blocks.CHERRY_LOG, stumpProps()));

    public static final DeferredBlock<StumpBlock> CRIMSON_STUMP = BLOCKS.register("crimson_stump",
            () -> new StumpBlock(() -> Blocks.CRIMSON_STEM, stumpProps()));

    public static final DeferredBlock<StumpBlock> WARPED_STUMP = BLOCKS.register("warped_stump",
            () -> new StumpBlock(() -> Blocks.WARPED_STEM, stumpProps()));

    public static BlockState getStumpForLog(BlockState logState) {
        Block b = logState.getBlock();
        if (b == Blocks.OAK_LOG || b == Blocks.STRIPPED_OAK_LOG || b == Blocks.OAK_WOOD) return copyAxis(OAK_STUMP.get().defaultBlockState(), logState);
        if (b == Blocks.BIRCH_LOG || b == Blocks.STRIPPED_BIRCH_LOG || b == Blocks.BIRCH_WOOD) return copyAxis(BIRCH_STUMP.get().defaultBlockState(), logState);
        if (b == Blocks.SPRUCE_LOG || b == Blocks.STRIPPED_SPRUCE_LOG || b == Blocks.SPRUCE_WOOD) return copyAxis(SPRUCE_STUMP.get().defaultBlockState(), logState);
        if (b == Blocks.JUNGLE_LOG || b == Blocks.STRIPPED_JUNGLE_LOG || b == Blocks.JUNGLE_WOOD) return copyAxis(JUNGLE_STUMP.get().defaultBlockState(), logState);
        if (b == Blocks.ACACIA_LOG || b == Blocks.STRIPPED_ACACIA_LOG || b == Blocks.ACACIA_WOOD) return copyAxis(ACACIA_STUMP.get().defaultBlockState(), logState);
        if (b == Blocks.DARK_OAK_LOG || b == Blocks.STRIPPED_DARK_OAK_LOG || b == Blocks.DARK_OAK_WOOD) return copyAxis(DARK_OAK_STUMP.get().defaultBlockState(), logState);
        if (b == Blocks.MANGROVE_LOG || b == Blocks.STRIPPED_MANGROVE_LOG || b == Blocks.MANGROVE_WOOD) return copyAxis(MANGROVE_STUMP.get().defaultBlockState(), logState);
        if (b == Blocks.CHERRY_LOG || b == Blocks.STRIPPED_CHERRY_LOG || b == Blocks.CHERRY_WOOD) return copyAxis(CHERRY_STUMP.get().defaultBlockState(), logState);
        if (b == Blocks.CRIMSON_STEM || b == Blocks.STRIPPED_CRIMSON_STEM || b == Blocks.CRIMSON_HYPHAE) return copyAxis(CRIMSON_STUMP.get().defaultBlockState(), logState);
        if (b == Blocks.WARPED_STEM || b == Blocks.STRIPPED_WARPED_STEM || b == Blocks.WARPED_HYPHAE) return copyAxis(WARPED_STUMP.get().defaultBlockState(), logState);
        return null;
    }

    private static BlockState copyAxis(BlockState stump, BlockState log) {
        if (log.hasProperty(RotatedPillarBlock.AXIS) && stump.hasProperty(RotatedPillarBlock.AXIS)) {
            return stump.setValue(RotatedPillarBlock.AXIS, log.getValue(RotatedPillarBlock.AXIS));
        }
        return stump;
    }

    public static boolean isStump(BlockState state) {
        return state.getBlock() instanceof StumpBlock;
    }

    public static boolean isCarvableStump(BlockState state) {
        if (!(state.getBlock() instanceof StumpBlock)) return false;
        return !state.is(CRIMSON_STUMP.get()) && !state.is(WARPED_STUMP.get());
    }
}
