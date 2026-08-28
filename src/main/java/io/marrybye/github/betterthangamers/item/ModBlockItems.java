package io.marrybye.github.betterthangamers.item;

import io.marrybye.github.betterthangamers.block.ModBlocks;
import net.neoforged.neoforge.registries.DeferredItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

public class ModBlockItems {
    public static final DeferredItem<BlockItem> BRICK_FURNACE = ModItems.ITEMS.register("brick_furnace",
            () -> new BlockItem(ModBlocks.BRICK_FURNACE.get(), new Item.Properties()));

    public static final DeferredItem<BlockItem> UNFIRED_BRICK = ModItems.ITEMS.register("unfired_brick",
            () -> new BlockItem(ModBlocks.UNFIRED_BRICK.get(), new Item.Properties()));
}

