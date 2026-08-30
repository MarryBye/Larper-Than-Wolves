package io.marrybye.github.larperthanwolves.client;

import io.marrybye.github.larperthanwolves.item.UnboundMeshItem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ClientProgressHelper {
    public static int getClientVisualProgress(ItemStack stack) {
        Player player = Minecraft.getInstance().player;
        int saved = stack.getDamageValue();
        if (player != null && player.isUsingItem() && (player.getMainHandItem() == stack || player.getOffhandItem() == stack)) {
            int maxTicks = player.getUseItem().getUseDuration(player);
            int ticksLeft = player.getUseItemRemainingTicks();
            int ticksUsed = maxTicks - ticksLeft;
            return Math.min(UnboundMeshItem.MAX_PROGRESS_SECONDS, saved + (ticksUsed / 20));
        }
        return saved;
    }
}
