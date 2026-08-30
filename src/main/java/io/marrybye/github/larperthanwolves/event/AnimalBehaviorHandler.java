package io.marrybye.github.larperthanwolves.event;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

@EventBusSubscriber(modid = "larperthanwolves")
public class AnimalBehaviorHandler {

    private static final String NBT_KICKING = "larperthanwolves:kicking";

    /**
     * Wild wolves hunt ALL peaceful animals (cows, pigs, sheep, chickens, rabbits, etc.)
     */
    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) return;

        if (event.getEntity() instanceof Wolf wolf) {
            // Priority 5: Hunt any untamed animal
            wolf.targetSelector.addGoal(5, new NonTameRandomTargetGoal<>(wolf, Animal.class, false,
                    target -> !(target instanceof Wolf) && !(target instanceof TamableAnimal tamable && tamable.isTame())));
        }
    }

    /**
     * Enhanced animal fleeing and Cow defensive kick mechanics when attacked.
     */
    @SubscribeEvent
    public static void onAnimalDamaged(LivingDamageEvent.Post event) {
        LivingEntity victim = event.getEntity();
        if (victim.level().isClientSide()) return;

        if (!(victim instanceof Animal animal)) return;

        DamageSource source = event.getSource();
        Entity attacker = source.getEntity();

        // 1. Cow Defensive Kick: If attacker is within close melee range (<= 3.0 blocks), cow kicks hard!
        if (animal instanceof Cow cow && attacker instanceof LivingEntity livingAttacker) {
            if (!cow.getPersistentData().getBoolean(NBT_KICKING) && cow.isAlive()) {
                double distanceSq = cow.distanceToSqr(livingAttacker);
                if (distanceSq <= 9.0) { // 3.0 blocks distance
                    try {
                        cow.getPersistentData().putBoolean(NBT_KICKING, true);

                        // Deal 5.0 damage (2.5 hearts) to attacker
                        livingAttacker.hurt(cow.damageSources().mobAttack(cow), 5.0F);

                        // Strong knockback pushing attacker away
                        double dx = livingAttacker.getX() - cow.getX();
                        double dz = livingAttacker.getZ() - cow.getZ();
                        livingAttacker.knockback(1.4D, -dx, -dz);

                        // Audio and visual kick effects
                        cow.level().playSound(null, livingAttacker.blockPosition(), SoundEvents.RAVAGER_ATTACK, SoundSource.NEUTRAL, 1.0F, 1.2F);
                        cow.level().playSound(null, cow.blockPosition(), SoundEvents.COW_HURT, SoundSource.NEUTRAL, 1.0F, 0.8F);

                        if (cow.level() instanceof ServerLevel serverLevel) {
                            serverLevel.sendParticles(ParticleTypes.CRIT,
                                    livingAttacker.getX(), livingAttacker.getY() + 1.0D, livingAttacker.getZ(),
                                    15, 0.3D, 0.3D, 0.3D, 0.1D);
                            serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK,
                                    livingAttacker.getX(), livingAttacker.getY() + 0.8D, livingAttacker.getZ(),
                                    2, 0.1D, 0.1D, 0.1D, 0.0D);
                        }
                    } finally {
                        cow.getPersistentData().remove(NBT_KICKING);
                    }

                    // Cow gets high speed boost (Speed III) to immediately flee ("даёт дёру")
                    cow.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 160, 2, false, false, true));
                }
            }
        }

        // 2. All Animals: Increased fleeing speed (Speed II) when attacked
        animal.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 140, 1, false, false, true));

        // Actively navigate away from the attacker
        if (attacker != null && animal.isAlive()) {
            Vec3 fleeVec = animal.position().subtract(attacker.position()).normalize();
            Vec3 targetPos = animal.position().add(fleeVec.x * 20.0D, 0.0D, fleeVec.z * 20.0D);
            animal.getNavigation().moveTo(targetPos.x, targetPos.y, targetPos.z, 2.0D);
        }
    }
}
