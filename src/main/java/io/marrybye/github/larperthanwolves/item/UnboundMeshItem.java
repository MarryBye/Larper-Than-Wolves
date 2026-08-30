package io.marrybye.github.larperthanwolves.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class UnboundMeshItem extends Item {
    public static final int MAX_PROGRESS_SECONDS = 15;

    public UnboundMeshItem(Properties properties) {
        super(properties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BRUSH;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged || oldStack.getItem() != newStack.getItem();
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        int progress = Math.min(MAX_PROGRESS_SECONDS, stack.getDamageValue());
        return Math.max(20, (MAX_PROGRESS_SECONDS - progress) * 20);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int count) {
        int initialProgress = Math.min(MAX_PROGRESS_SECONDS, stack.getDamageValue());
        int totalTicks = (MAX_PROGRESS_SECONDS - initialProgress) * 20;
        int ticksUsed = totalTicks - count;

        if (ticksUsed > 0 && ticksUsed % 20 == 0) {
            level.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),
                    SoundEvents.BRUSH_GENERIC, SoundSource.PLAYERS, 0.8F, 0.85F + level.random.nextFloat() * 0.3F);

            int secondsElapsed = ticksUsed / 20;
            int newProgress = Math.min(MAX_PROGRESS_SECONDS, initialProgress + secondsElapsed);
            if (newProgress != stack.getDamageValue()) {
                stack.setDamageValue(newProgress);
            }
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeLeft) {
        int initialProgress = Math.min(MAX_PROGRESS_SECONDS, stack.getDamageValue());
        int totalTicks = (MAX_PROGRESS_SECONDS - initialProgress) * 20;
        int ticksUsed = totalTicks - timeLeft;
        int secondsAdded = ticksUsed / 20;

        if (secondsAdded > 0) {
            int newProgress = Math.min(MAX_PROGRESS_SECONDS, initialProgress + secondsAdded);
            if (newProgress >= MAX_PROGRESS_SECONDS) {
                finishBinding(stack, level, livingEntity);
            } else {
                stack.setDamageValue(newProgress);
            }
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        return finishBinding(stack, level, livingEntity);
    }

    private ItemStack finishBinding(ItemStack stack, Level level, LivingEntity livingEntity) {
        level.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),
                SoundEvents.LEASH_KNOT_PLACE, SoundSource.PLAYERS, 1.0F, 1.2F);
        level.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),
                SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.3F, 1.8F);

        ItemStack finishedMesh = new ItemStack(ModItems.MESH.get());
        if (livingEntity instanceof Player player) {
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
                if (stack.isEmpty()) {
                    return finishedMesh;
                } else {
                    if (!player.getInventory().add(finishedMesh)) {
                        player.drop(finishedMesh, false);
                    }
                    return stack;
                }
            }
        }
        return finishedMesh;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        int progress = Math.min(MAX_PROGRESS_SECONDS, stack.getDamageValue());
        return Math.round(13.0F * progress / (float) MAX_PROGRESS_SECONDS);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        int progress = Math.min(MAX_PROGRESS_SECONDS, stack.getDamageValue());
        float fraction = (float) progress / (float) MAX_PROGRESS_SECONDS;
        return Mth.hsvToRgb(fraction / 3.0F, 1.0F, 1.0F);
    }
}
