package io.marrybye.github.larperthanwolves.client;

import io.marrybye.github.larperthanwolves.LarperThanWolves;
import io.marrybye.github.larperthanwolves.menu.ModMenuTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = LarperThanWolves.MODID, value = Dist.CLIENT)
public class ModClientEvents {
    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.BRICK_FURNACE.get(), BrickFurnaceScreen::new);
        event.register(ModMenuTypes.OVEN.get(), OvenScreen::new);
        event.register(ModMenuTypes.ALLOY_MIXER.get(), AlloyMixerScreen::new);
        event.register(ModMenuTypes.SIEVE.get(), SieveScreen::new);
        event.register(ModMenuTypes.BASKET.get(), BasketScreen::new);
    }

    @SubscribeEvent
    public static void registerBlockColors(net.neoforged.neoforge.client.event.RegisterColorHandlersEvent.Block event) {
        event.register((state, level, pos, tintIndex) -> {
            if (tintIndex == 0) {
                if (level != null && pos != null) {
                    return net.minecraft.client.renderer.BiomeColors.getAverageGrassColor(level, pos);
                }
                return net.minecraft.world.level.GrassColor.getDefaultColor();
            }
            return -1;
        }, io.marrybye.github.larperthanwolves.block.ModBlocks.RICH_GRASS_BLOCK.get());
    }

    @SubscribeEvent
    public static void registerItemColors(net.neoforged.neoforge.client.event.RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> tintIndex == 0 ? net.minecraft.world.level.GrassColor.getDefaultColor() : -1,
                io.marrybye.github.larperthanwolves.block.ModBlocks.RICH_GRASS_BLOCK.get());
    }
}
