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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;

import java.util.function.Consumer;

public class UnboundMeshItem extends Item {
    public static final int MAX_PROGRESS_TICKS = 300; // 15 seconds at 20 ticks/sec
    public static final int USE_DURATION = 72000;

    public UnboundMeshItem(Properties properties) {
        super(properties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged || oldStack.getItem() != newStack.getItem();
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return Math.max(1, MAX_PROGRESS_TICKS - stack.getDamageValue());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getDamageValue() >= MAX_PROGRESS_TICKS) {
            if (!level.isClientSide) {
                playCompleteSounds(level, player);
                ItemStack finishedMesh = new ItemStack(ModItems.MESH.get());
                player.setItemInHand(hand, finishedMesh);
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
        }

        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int count) {
        int duration = getUseDuration(stack, livingEntity);
        int elapsedInSession = duration - count;
        int currentTotalProgress = stack.getDamageValue() + elapsedInSession;

        if (elapsedInSession % 4 == 0) {
            level.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),
                    SoundEvents.BRUSH_GENERIC, SoundSource.PLAYERS, 0.7F, 0.85F + level.random.nextFloat() * 0.3F);
        }

        if (currentTotalProgress >= MAX_PROGRESS_TICKS || count <= 1) {
            if (!level.isClientSide && livingEntity instanceof Player player) {
                playCompleteSounds(level, player);
                InteractionHand hand = player.getUsedItemHand();
                player.stopUsingItem();
                player.setItemInHand(hand, new ItemStack(ModItems.MESH.get()));
            } else if (level.isClientSide) {
                livingEntity.stopUsingItem();
            }
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if (!level.isClientSide && livingEntity instanceof Player player) {
            playCompleteSounds(level, player);
        }
        return new ItemStack(ModItems.MESH.get());
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeLeft) {
        int duration = getUseDuration(stack, livingEntity);
        int elapsedInSession = duration - timeLeft;
        int newTotalProgress = Math.min(MAX_PROGRESS_TICKS, stack.getDamageValue() + elapsedInSession);
        stack.setDamageValue(newTotalProgress);

        if (newTotalProgress >= MAX_PROGRESS_TICKS) {
            if (!level.isClientSide && livingEntity instanceof Player player) {
                playCompleteSounds(level, player);
                InteractionHand hand = player.getUsedItemHand();
                player.setItemInHand(hand, new ItemStack(ModItems.MESH.get()));
            }
        }
    }

    private static void playCompleteSounds(Level level, LivingEntity entity) {
        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                SoundEvents.LEASH_KNOT_PLACE, SoundSource.PLAYERS, 1.0F, 1.2F);
        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.4F, 1.8F);
    }

    @Override
    public void initializeClient(Consumer<net.neoforged.neoforge.client.extensions.common.IClientItemExtensions> consumer) {
        io.marrybye.github.larperthanwolves.client.UnboundMeshClientExtension.register(consumer);
    }

    private static int getCurrentProgress(ItemStack stack) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            return io.marrybye.github.larperthanwolves.client.ClientProgressHelper.getClientVisualProgress(stack);
        }
        return Math.min(MAX_PROGRESS_TICKS, stack.getDamageValue());
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round(13.0F * getCurrentProgress(stack) / (float) MAX_PROGRESS_TICKS);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        float fraction = (float) getCurrentProgress(stack) / (float) MAX_PROGRESS_TICKS;
        return Mth.hsvToRgb(fraction / 3.0F, 1.0F, 1.0F);
    }
}
