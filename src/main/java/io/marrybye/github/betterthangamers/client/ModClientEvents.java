package io.marrybye.github.betterthangamers.client;

import io.marrybye.github.betterthangamers.BetterThanGamers;
import io.marrybye.github.betterthangamers.menu.ModMenuTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = BetterThanGamers.MODID, value = Dist.CLIENT)
public class ModClientEvents {
    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.BRICK_FURNACE.get(), BrickFurnaceScreen::new);
    }
}
