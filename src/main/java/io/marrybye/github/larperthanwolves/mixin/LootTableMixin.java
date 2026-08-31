package io.marrybye.github.larperthanwolves.mixin;

import io.marrybye.github.larperthanwolves.event.DisabledItemsHandler;
import io.marrybye.github.larperthanwolves.loot.ChestLootModifier;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LootTable.class)
public class LootTableMixin {

    @Inject(
            method = "getRandomItems(Lnet/minecraft/world/level/storage/loot/LootParams;J)Lit/unimi/dsi/fastutil/objects/ObjectArrayList;",
            at = @At("RETURN"),
            cancellable = true
    )
    private void larperthanwolves$rebalanceGeneratedLoot(
            LootParams params,
            long seed,
            CallbackInfoReturnable<ObjectArrayList<ItemStack>> cir
    ) {
        ObjectArrayList<ItemStack> list = cir.getReturnValue();
        if (list == null || list.isEmpty()) {
            return;
        }

        ObjectArrayList<ItemStack> modified = new ObjectArrayList<>(list.size());

        for (ItemStack stack : list) {
            if (stack == null || stack.isEmpty()) {
                continue;
            }

            ItemStack rebalanced = ChestLootModifier.rebalanceLootItem(stack);
            if (rebalanced != null && !rebalanced.isEmpty()) {
                if (!DisabledItemsHandler.isDisabled(rebalanced.getItem())) {
                    modified.add(rebalanced);
                }
            }
        }

        cir.setReturnValue(modified);
    }
}
