package io.marrybye.github.larperthanwolves.mixin;

import io.marrybye.github.larperthanwolves.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * Mixin into TreeFeature to place Twig blocks on the ground beneath newly generated trees.
 * Hooks into the private doPlace() method at RETURN, which runs after the trunk, foliage,
 * and decorators have all been placed. Uses the tree origin position to scan outward and
 * downward, placing 1-3 twig blocks on valid soil surfaces under the canopy.
 */
@Mixin(TreeFeature.class)
public abstract class TreeFeatureMixin {

    /**
     * After a tree is successfully placed, scatter twig blocks on the ground beneath the canopy.
     * The doPlace method is private but Mixin can inject into it since we target the same class.
     */
    @Inject(
            method = "doPlace",
            at = @At("RETURN")
    )
    private void larperthanwolves$placeGroundTwigs(
            WorldGenLevel level,
            RandomSource random,
            BlockPos origin,
            BiConsumer<BlockPos, BlockState> rootBlockSetter,
            BiConsumer<BlockPos, BlockState> trunkBlockSetter,
            FoliagePlacer.FoliageSetter foliageSetter,
            TreeConfiguration config,
            CallbackInfoReturnable<Boolean> cir
    ) {
        // Only place twigs if the tree was actually placed successfully
        if (!cir.getReturnValue()) return;

        // Determine how many twig patches to scatter (1 to 3)
        int numTwigs = 1 + random.nextInt(3);
        int placed = 0;
        Set<BlockPos> usedPositions = new HashSet<>();

        // Try placing twigs in a radius around the tree trunk base
        int maxAttempts = numTwigs * 6;
        for (int attempt = 0; attempt < maxAttempts && placed < numTwigs; attempt++) {
            // Random offset within canopy range from the trunk
            int dx = random.nextInt(7) - 3; // -3 to +3
            int dz = random.nextInt(7) - 3;

            // Skip positions too close to the trunk itself (within 1 block)
            if (Math.abs(dx) <= 0 && Math.abs(dz) <= 0) continue;

            BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos(
                    origin.getX() + dx,
                    origin.getY() + 8, // Start scanning from above trunk
                    origin.getZ() + dz
            );

            // Raycast downward to find the ground surface
            boolean foundGround = false;
            for (int step = 0; step < 16; step++) {
                if (cursor.getY() <= level.getMinBuildHeight()) break;

                BlockState state = level.getBlockState(cursor);
                if (state.isAir() || state.is(BlockTags.LEAVES) || state.is(BlockTags.LOGS)
                        || state.is(Blocks.VINE) || state.is(Blocks.HANGING_ROOTS)
                        || state.is(Blocks.MANGROVE_ROOTS)) {
                    cursor.move(Direction.DOWN);
                    continue;
                }

                // Found a solid block - check if it's valid ground
                if (larperthanwolves$isValidGround(state)) {
                    BlockPos twigPos = cursor.above();

                    if (!usedPositions.contains(twigPos)) {
                        BlockState aboveState = level.getBlockState(twigPos);
                        if (aboveState.isAir()) {
                            BlockState twigState = ModBlocks.TWIG.get().defaultBlockState();
                            level.setBlock(twigPos, twigState, 2);
                            usedPositions.add(twigPos.immutable());
                            placed++;
                        }
                    }
                }
                foundGround = true;
                break;
            }
        }
    }

    /**
     * Check if a block state represents valid ground for placing twigs.
     */
    private static boolean larperthanwolves$isValidGround(BlockState state) {
        return state.is(BlockTags.DIRT) ||
                state.is(Blocks.FARMLAND) ||
                state.is(Blocks.DIRT_PATH) ||
                state.is(Blocks.MUD) ||
                state.is(Blocks.MOSS_BLOCK) ||
                state.is(Blocks.ROOTED_DIRT) ||
                state.is(Blocks.PODZOL) ||
                state.is(Blocks.COARSE_DIRT) ||
                state.is(BlockTags.SAND) ||
                state.is(Blocks.GRAVEL) ||
                state.is(ModBlocks.RICH_GRASS_BLOCK.get()) ||
                state.is(ModBlocks.RICH_DIRT.get()) ||
                state.is(ModBlocks.RICH_SAND.get());
    }
}
