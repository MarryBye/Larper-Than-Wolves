package io.marrybye.github.betterthangamers.mixin;

import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.world.level.block.TallGrassBlock;

// Mixin to change tall grass drops (handled through loot tables)
@Mixin(TallGrassBlock.class)
public class TallGrassBlockMixin {
}


