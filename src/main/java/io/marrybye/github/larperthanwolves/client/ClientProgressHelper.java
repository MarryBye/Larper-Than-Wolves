package io.marrybye.github.larperthanwolves.client;

import net.minecraft.world.item.ItemStack;

public class ClientProgressHelper {
    public static int getClientVisualProgress(ItemStack stack) {
        return stack.getDamageValue();
    }
}
