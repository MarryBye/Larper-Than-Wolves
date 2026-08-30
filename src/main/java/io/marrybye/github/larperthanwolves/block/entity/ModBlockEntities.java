package io.marrybye.github.larperthanwolves.block.entity;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, "larperthanwolves");

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BrickFurnaceBlockEntity>> BRICK_FURNACE =
            BLOCK_ENTITIES.register("brick_furnace", () ->
                    BlockEntityType.Builder.of(BrickFurnaceBlockEntity::new,
                            io.marrybye.github.larperthanwolves.block.ModBlocks.BRICK_FURNACE.get())
                            .build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<OvenBlockEntity>> OVEN =
            BLOCK_ENTITIES.register("oven", () ->
                    BlockEntityType.Builder.of(OvenBlockEntity::new,
                            io.marrybye.github.larperthanwolves.block.ModBlocks.OVEN.get())
                            .build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<UnfiredBrickBlockEntity>> UNFIRED_BRICK =
            BLOCK_ENTITIES.register("unfired_brick", () ->
                    BlockEntityType.Builder.of(UnfiredBrickBlockEntity::new,
                            io.marrybye.github.larperthanwolves.block.ModBlocks.UNFIRED_BRICK.get())
                            .build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AlloyMixerBlockEntity>> ALLOY_MIXER =
            BLOCK_ENTITIES.register("alloy_mixer", () ->
                    BlockEntityType.Builder.of(AlloyMixerBlockEntity::new,
                            io.marrybye.github.larperthanwolves.block.ModBlocks.ALLOY_MIXER.get())
                            .build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SieveBlockEntity>> SIEVE =
            BLOCK_ENTITIES.register("sieve", () ->
                    BlockEntityType.Builder.of(SieveBlockEntity::new,
                            io.marrybye.github.larperthanwolves.block.ModBlocks.SIEVE.get())
                            .build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BasketBlockEntity>> BASKET =
            BLOCK_ENTITIES.register("basket", () ->
                    BlockEntityType.Builder.of(BasketBlockEntity::new,
                            io.marrybye.github.larperthanwolves.block.ModBlocks.BASKET.get())
                            .build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DryingRackBlockEntity>> DRYING_RACK =
            BLOCK_ENTITIES.register("drying_rack", () ->
                    BlockEntityType.Builder.of(DryingRackBlockEntity::new,
                            io.marrybye.github.larperthanwolves.block.ModBlocks.DRYING_RACK.get())
                            .build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MillBlockEntity>> MILL =
            BLOCK_ENTITIES.register("mill", () ->
                    BlockEntityType.Builder.of(MillBlockEntity::new,
                            io.marrybye.github.larperthanwolves.block.ModBlocks.MILL.get())
                            .build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MillCrankBlockEntity>> MILL_CRANK =
            BLOCK_ENTITIES.register("mill_crank", () ->
                    BlockEntityType.Builder.of(MillCrankBlockEntity::new,
                            io.marrybye.github.larperthanwolves.block.ModBlocks.MILL_CRANK.get())
                            .build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FilterGrateBlockEntity>> FILTER_GRATE =
            BLOCK_ENTITIES.register("filter_grate", () ->
                    BlockEntityType.Builder.of(FilterGrateBlockEntity::new,
                            io.marrybye.github.larperthanwolves.block.ModBlocks.FILTER_GRATE.get())
                            .build(null));
}

