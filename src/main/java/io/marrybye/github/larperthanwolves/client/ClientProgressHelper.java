package io.marrybye.github.larperthanwolves.client;

import io.marrybye.github.larperthanwolves.item.UnboundMeshItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;

public class ClientProgressHelper {
    public static int getClientVisualProgress(ItemStack stack) {
        int progress = stack.getDamageValue();
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null && player.isUsingItem()) {
            ItemStack using = player.getUseItem();
            if (using == stack || (ItemStack.isSameItem(using, stack) && (player.getMainHandItem() == stack || player.getOffhandItem() == stack))) {
                int elapsed = stack.getUseDuration(player) - player.getUseItemRemainingTicks();
                progress += Math.max(0, elapsed);
            }
        }
        return Math.min(UnboundMeshItem.MAX_PROGRESS_TICKS, progress);
    }
}
