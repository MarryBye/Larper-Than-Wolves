package io.marrybye.github.larperthanwolves.item;

import io.marrybye.github.larperthanwolves.block.ModBlocks;
import io.marrybye.github.larperthanwolves.block.WorkStumpBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class ChiselItem extends Item {
    public ChiselItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();

        // 1. Interacting ONLY with StumpBlock! Regular logs cannot be chiseled into work stumps!
        if (ModBlocks.isStump(state)) {
            if (!level.isClientSide) {
                level.setBlock(pos, ModBlocks.WORK_STUMP.get().defaultBlockState().setValue(WorkStumpBlock.STAGE, 0), 3);
                level.playSound(null, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0f, 1.0f);
                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.CRIT, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 10, 0.2, 0.1, 0.2, 0.05);
                }
                if (player != null && !player.getAbilities().instabuild) {
                    stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        // 2. Interacting with an ongoing WorkStumpBlock
        if (state.is(ModBlocks.WORK_STUMP.get())) {
            int currentStage = state.getValue(WorkStumpBlock.STAGE);
            if (!level.isClientSide) {
                if (currentStage == 0) {
                    level.setBlock(pos, state.setValue(WorkStumpBlock.STAGE, 1), 3);
                    level.playSound(null, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0f, 1.1f);
                } else if (currentStage == 1) {
                    level.setBlock(pos, state.setValue(WorkStumpBlock.STAGE, 2), 3);
                    level.playSound(null, pos, SoundEvents.WOOD_HIT, SoundSource.BLOCKS, 1.0f, 1.2f);
                } else if (currentStage == 2) {
                    // 4th hit: Turn into Crafting Table!
                    level.setBlock(pos, Blocks.CRAFTING_TABLE.defaultBlockState(), 3);
                    level.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1.2f, 1.0f);
                    if (level instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5, 15, 0.3, 0.3, 0.3, 0.1);
                    }
                }

                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.CRIT, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 8, 0.2, 0.1, 0.2, 0.05);
                }
                if (player != null && !player.getAbilities().instabuild) {
                    stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return InteractionResult.PASS;
    }
}
