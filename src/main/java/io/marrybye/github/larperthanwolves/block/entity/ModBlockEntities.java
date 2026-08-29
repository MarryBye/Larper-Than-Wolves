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
}

