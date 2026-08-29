package io.marrybye.github.larperthanwolves.mixin;

import io.marrybye.github.larperthanwolves.config.ModConfig;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.StructureTags;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(Structure.class)
public class StructureMixin {

    @Inject(
            method = "findValidGenerationPoint(Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationContext;)Ljava/util/Optional;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void larperthanwolves$enforceMinDistance(Structure.GenerationContext context, CallbackInfoReturnable<Optional<Structure.GenerationStub>> cir) {
        Structure self = (Structure) (Object) this;

        // Check if this structure is a village
        boolean isVillage = false;
        try {
            var registry = context.registryAccess().registry(Registries.STRUCTURE);
            if (registry.isPresent()) {
                var holder = registry.get().wrapAsHolder(self);
                if (holder.is(StructureTags.VILLAGE)) {
                    isVillage = true;
                } else {
                    var key = registry.get().getResourceKey(self);
                    if (key.isPresent() && key.get().location().getPath().contains("village")) {
                        isVillage = true;
                    }
                }
            }
        } catch (Exception ignored) {
        }

        if (isVillage) {
            ChunkPos chunkPos = context.chunkPos();
            long blockX = chunkPos.getMiddleBlockX();
            long blockZ = chunkPos.getMiddleBlockZ();
            double distSq = (double) blockX * blockX + (double) blockZ * blockZ;

            double minDist = ModConfig.SERVER != null ? ModConfig.SERVER.villageMinDistanceFromSpawn.get() : 3000.0;
            if (distSq < minDist * minDist) {
                cir.setReturnValue(Optional.empty());
            }
        }
    }
}
