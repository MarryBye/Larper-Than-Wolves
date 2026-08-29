package io.marrybye.github.larperthanwolves;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import io.marrybye.github.larperthanwolves.block.ModBlocks;
import io.marrybye.github.larperthanwolves.block.entity.ModBlockEntities;
import io.marrybye.github.larperthanwolves.item.ModCreativeTabs;
import io.marrybye.github.larperthanwolves.item.ModItems;
import io.marrybye.github.larperthanwolves.menu.ModMenuTypes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;

@Mod(LarperThanWolves.MODID)
public class LarperThanWolves {
    public static final String MODID = "larperthanwolves";
    public static final Logger LOGGER = LogUtils.getLogger();

    public LarperThanWolves(IEventBus modEventBus, ModContainer modContainer) {
        // Register items, blocks, menus, tabs, and other components
        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModMenuTypes.MENUS.register(modEventBus);
        ModCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
    }
}
