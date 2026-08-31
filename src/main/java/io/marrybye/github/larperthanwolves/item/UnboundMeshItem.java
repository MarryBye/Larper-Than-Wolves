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
    public static final int MAX_PROGRESS_TICKS = 300; // 15 seconds at 20 ticks/sec
    public static final int EATING_CYCLE_TICKS = 32;  // standard food eating animation cycle

    public UnboundMeshItem(Properties properties) {
        super(properties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.EAT;
    }

    @Override
    public net.minecraft.sounds.SoundEvent getEatingSound() {
        return SoundEvents.BRUSH_GENERIC;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged || oldStack.getItem() != newStack.getItem();
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return EATING_CYCLE_TICKS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int count) {
        int currentTicks = stack.getDamageValue() + 1;
        stack.setDamageValue(Math.min(MAX_PROGRESS_TICKS, currentTicks));

        if (currentTicks % 4 == 0) {
            level.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),
                    SoundEvents.BRUSH_GENERIC, SoundSource.PLAYERS, 0.7F, 0.85F + level.random.nextFloat() * 0.3F);
        }

        if (currentTicks >= MAX_PROGRESS_TICKS) {
            finishBinding(stack, level, livingEntity);
            livingEntity.stopUsingItem();
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if (stack.getDamageValue() >= MAX_PROGRESS_TICKS) {
            return finishBinding(stack, level, livingEntity);
        }
        if (livingEntity instanceof Player player) {
            player.startUsingItem(player.getUsedItemHand());
        }
        return stack;
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
        return Math.round(13.0F * stack.getDamageValue() / (float) MAX_PROGRESS_TICKS);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        float fraction = (float) stack.getDamageValue() / (float) MAX_PROGRESS_TICKS;
        return Mth.hsvToRgb(fraction / 3.0F, 1.0F, 1.0F);
    }
}
