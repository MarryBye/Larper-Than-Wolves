package io.marrybye.github.betterthangamers.event;

import io.marrybye.github.betterthangamers.item.ModItems;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber(modid = "betterthangamers")
public class BlockBreakHandler {

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (player == null || player.level().isClientSide) return;

        // Handle gravel drops (change flint to silicon shard with lower chance)
        if (event.getState().getBlock() == Blocks.GRAVEL) {
            // Will be handled by loot table
        }

        // Handle tall grass - drop nothing
        if (event.getState().getBlock() == Blocks.TALL_GRASS) {
            // Will be handled by loot table
        }

        // Handle seagrass - drop nothing
        if (event.getState().getBlock() == Blocks.SEAGRASS) {
            // Will be handled by loot table
        }
    }
}

