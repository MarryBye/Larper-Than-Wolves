package io.marrybye.github.larperthanwolves.event;

import io.marrybye.github.larperthanwolves.item.ModItems;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber(modid = "larperthanwolves")
public class AnimalDungHandler {

    public static final String NBT_DUNG_TIMER = "larperthanwolves:dung_timer";
    public static final int DIGESTION_TICKS = 3600; // 3 minutes = 180 seconds * 20 ticks

    /**
     * Trigger digestion timer when an animal or wolf is fed by the player.
     */
    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getLevel().isClientSide) return;

        Entity target = event.getTarget();
        if (!(target instanceof LivingEntity living)) return;

        ItemStack held = event.getItemStack();
        if (held.isEmpty()) return;

        boolean isFed = false;

        if (living instanceof Wolf wolf) {
            if (held.is(ItemTags.MEAT) || held.is(Items.BONE)) {
                isFed = true;
            }
        } else if (living instanceof Animal animal) {
            if (animal.isFood(held) || held.is(Items.WHEAT) || held.is(Items.CARROT) ||
                    held.is(Items.POTATO) || held.is(Items.BEETROOT) || held.is(Items.HAY_BLOCK) ||
                    held.is(Items.APPLE) || held.is(Items.GOLDEN_CARROT) || held.is(Items.CACTUS) ||
                    held.is(Items.TORCHFLOWER_SEEDS)) {
                isFed = true;
            }
        }

        if (isFed) {
            startDigestion(living);
        }
    }

    /**
     * Trigger digestion when a wolf attacks and kills prey in the wild.
     */
    @SubscribeEvent
    public static void onWolfKill(LivingDeathEvent event) {
        if (event.getEntity().level().isClientSide) return;

        if (event.getSource().getEntity() instanceof Wolf wolf) {
            startDigestion(wolf);
        }
    }

    /**
     * Start the 3-minute digestion countdown timer on the entity.
     */
    public static void startDigestion(LivingEntity entity) {
        CompoundTag data = entity.getPersistentData();
        data.putInt(NBT_DUNG_TIMER, DIGESTION_TICKS);
    }

    /**
     * Tick digestion timers on living entities and drop dung after 3 minutes.
     */
    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Pre event) {
        Entity entity = event.getEntity();
        if (entity.level().isClientSide || !(entity instanceof LivingEntity living)) return;

        // Check if sheep is grazing
        if (living instanceof Sheep sheep) {
            if (sheep.getHeadEatPositionScale(0) > 0.8f) {
                if (!living.getPersistentData().contains(NBT_DUNG_TIMER)) {
                    startDigestion(sheep);
                }
            }
        }

        CompoundTag data = living.getPersistentData();
        if (data.contains(NBT_DUNG_TIMER)) {
            int timer = data.getInt(NBT_DUNG_TIMER);
            if (timer > 1) {
                data.putInt(NBT_DUNG_TIMER, timer - 1);
            } else {
                data.remove(NBT_DUNG_TIMER);
                dropDung(living);
            }
        }
    }

    /**
     * Drop 1 dung item at the animal's position with effects.
     */
    private static void dropDung(LivingEntity entity) {
        if (entity.level() instanceof ServerLevel serverLevel) {
            double x = entity.getX();
            double y = entity.getY() + 0.2;
            double z = entity.getZ();

            ItemEntity dungItem = new ItemEntity(serverLevel, x, y, z, new ItemStack(ModItems.DUNG.get(), 1));
            dungItem.setDefaultPickUpDelay();
            serverLevel.addFreshEntity(dungItem);

            // Pop sound and particle effect
            serverLevel.playSound(null, entity.blockPosition(), SoundEvents.CHICKEN_EGG, SoundSource.NEUTRAL, 0.8f, 0.6f);
            serverLevel.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(ModItems.DUNG.get())),
                    x, y, z, 6, 0.2, 0.1, 0.2, 0.05);
        }
    }
}
