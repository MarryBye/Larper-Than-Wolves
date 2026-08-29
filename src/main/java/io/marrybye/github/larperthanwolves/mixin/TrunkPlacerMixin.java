package io.marrybye.github.larperthanwolves.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
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

import java.util.function.BiConsumer;
import java.util.function.Function;

@Mixin(TrunkPlacer.class)
public abstract class TrunkPlacerMixin {

    @WrapOperation(
            method = "placeLog(Lnet/minecraft/world/level/LevelSimulatedReader;Ljava/util/function/BiConsumer;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration;Ljava/util/function/Function;)Z",
            at = @At(value = "INVOKE", target = "Ljava/util/function/BiConsumer;accept(Ljava/lang/Object;Ljava/lang/Object;)V")
    )
    private static void larperthanwolves$placeStumpAtRoot(
            BiConsumer<BlockPos, BlockState> biConsumer,
            Object posObj,
            Object stateObj,
            Operation<Void> original,
            LevelSimulatedReader level,
            BiConsumer<BlockPos, BlockState> blockSetter,
            RandomSource random,
            BlockPos pos,
            TreeConfiguration config,
            Function<BlockState, BlockState> propertySetter
    ) {
        BlockPos blockPos = (BlockPos) posObj;
        BlockState logState = (BlockState) stateObj;

        // Check if block below is soil / ground where the tree roots
        boolean isAboveSoil = level.isStateAtPosition(blockPos.below(), state ->
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
            BlockState stumpState = ModBlocks.getStumpForLog(logState);
            if (stumpState != null) {
                original.call(biConsumer, blockPos, stumpState);
                return;
            }
        }

        original.call(biConsumer, blockPos, logState);
    }
}

