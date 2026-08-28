package io.marrybye.github.betterthangamers.mixin;

import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

// Mixin to change gravel drops to silicon shards instead of flint
// This will be handled through loot tables instead
@Mixin(Block.class)
public class GravelBlockMixin {
}

