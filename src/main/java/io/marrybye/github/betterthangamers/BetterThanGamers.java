package io.marrybye.github.betterthangamers;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import io.marrybye.github.betterthangamers.block.ModBlocks;
import io.marrybye.github.betterthangamers.block.entity.ModBlockEntities;
import io.marrybye.github.betterthangamers.item.ModItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;

@Mod(BetterThanGamers.MODID)
public class BetterThanGamers {
    public static final String MODID = "betterthangamers";
    public static final Logger LOGGER = LogUtils.getLogger();

    public BetterThanGamers(IEventBus modEventBus, ModContainer modContainer) {
        // Register items, blocks, and other components
        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
    }
}
