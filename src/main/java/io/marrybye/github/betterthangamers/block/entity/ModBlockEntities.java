package io.marrybye.github.betterthangamers.block.entity;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, "betterthangamers");

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BrickFurnaceBlockEntity>> BRICK_FURNACE =
            BLOCK_ENTITIES.register("brick_furnace", () ->
                    BlockEntityType.Builder.of(BrickFurnaceBlockEntity::new,
                            io.marrybye.github.betterthangamers.block.ModBlocks.BRICK_FURNACE.get())
                            .build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<UnfiredBrickBlockEntity>> UNFIRED_BRICK =
            BLOCK_ENTITIES.register("unfired_brick", () ->
                    BlockEntityType.Builder.of(UnfiredBrickBlockEntity::new,
                            io.marrybye.github.betterthangamers.block.ModBlocks.UNFIRED_BRICK.get())
                            .build(null));
}

