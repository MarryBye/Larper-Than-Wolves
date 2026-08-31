package io.marrybye.github.larperthanwolves.mixin;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(RandomizableContainer.class)
public interface RandomizableContainerMixin {

    @Shadow
    ResourceKey<LootTable> getLootTable();

    @Shadow
    void setLootTable(ResourceKey<LootTable> key);

    @Inject(method = "unpackLootTable", at = @At("HEAD"), cancellable = true)
    private void larperthanwolves$purgeNonBastionLoot(@Nullable Player player, CallbackInfo ci) {
        ResourceKey<LootTable> table = getLootTable();
        if (table != null) {
            String path = table.location().getPath();
            if (!path.contains("bastion")) {
                setLootTable(null);
                ci.cancel();
            }
        }
    }
}
