package io.marrybye.github.betterthangamers.event;

import io.marrybye.github.betterthangamers.item.ModItems;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber(modid = "betterthangamers")
public class GrassBlockHandler {

    @SubscribeEvent
    public static void onBlockHarvested(BlockEvent.BreakEvent event) {
        Block block = event.getState().getBlock();
        Player player = event.getPlayer();

        if (player == null || player.level().isClientSide) return;

        // Handle grass block with hoe - replace with dirt and have chance to drop seeds
        if (block == Blocks.GRASS_BLOCK) {
            if (player.getMainHandItem().getItem() instanceof net.minecraft.world.item.HoeItem) {
                // This is handled through custom logic
                // Will be improved with Mixin if needed
            }
        }
    }
}

