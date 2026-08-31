package io.marrybye.github.larperthanwolves.datagen;

import io.marrybye.github.larperthanwolves.LarperThanWolves;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = LarperThanWolves.MODID)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();

        if (event.includeServer()) {
            generator.addProvider(event.includeServer(), new ModRecipesProvider(packOutput, event.getLookupProvider()));
        }
        if (event.includeClient()) {
            generator.addProvider(event.includeClient(), new ModItemModelProvider(packOutput, event.getExistingFileHelper()));
        }
    }
}

