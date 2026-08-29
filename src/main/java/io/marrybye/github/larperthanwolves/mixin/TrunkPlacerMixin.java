package io.marrybye.github.larperthanwolves.mixin;

import io.marrybye.github.larperthanwolves.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.BiConsumer;
import java.util.function.Function;

@Mixin(TrunkPlacer.class)
public abstract class TrunkPlacerMixin {

    @Inject(
            method = "placeLog(Lnet/minecraft/world/level/LevelSimulatedReader;Ljava/util/function/BiConsumer;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration;Ljava/util/function/Function;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void larperthanwolves$placeStumpAtRoot(
            LevelSimulatedReader level,
            BiConsumer<BlockPos, BlockState> blockSetter,
            RandomSource random,
            BlockPos pos,
            TreeConfiguration config,
            Function<BlockState, BlockState> propertySetter,
            CallbackInfoReturnable<Boolean> cir
    ) {
        // Check if block below is soil / ground where the tree roots
        boolean isAboveSoil = level.isStateAtPosition(pos.below(), state ->
                state.is(BlockTags.DIRT) ||
                state.is(Blocks.FARMLAND) ||
                state.is(Blocks.DIRT_PATH) ||
                state.is(Blocks.MUD) ||
                state.is(Blocks.ROOTED_DIRT) ||
                state.is(Blocks.MUDDY_MANGROVE_ROOTS) ||
                state.is(Blocks.CRIMSON_NYLIUM) ||
                state.is(Blocks.WARPED_NYLIUM) ||
                state.is(Blocks.SAND) ||
                state.is(Blocks.RED_SAND) ||
                state.is(Blocks.MOSS_BLOCK)
        );

        if (isAboveSoil) {
            BlockState logState = propertySetter.apply(config.trunkProvider.getState(random, pos));
            BlockState stumpState = ModBlocks.getStumpForLog(logState);
            if (stumpState != null) {
                blockSetter.accept(pos, stumpState);
                cir.setReturnValue(true);
            }
        }
    }
}
