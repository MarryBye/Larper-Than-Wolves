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
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import java.util.EnumSet;
import java.util.UUID;

@EventBusSubscriber(modid = "larperthanwolves")
public class AnimalBehaviorHandler {

    private static final String NBT_KICKING = "larperthanwolves:kicking";
    public static final String NBT_FLEE_TARGET = "larperthanwolves:flee_target";
    public static final double FLEE_DISTANCE_THRESHOLD = 30.0D;
    public static final double FLEE_DISTANCE_SQ = FLEE_DISTANCE_THRESHOLD * FLEE_DISTANCE_THRESHOLD; // 900.0

    /**
     * Wild wolves hunt ALL peaceful animals (cows, pigs, sheep, chickens, rabbits, etc.)
     * and peaceful animals are registered with persistent fleeing AI goal.
     */
    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) return;

        if (event.getEntity() instanceof Wolf wolf) {
            // Priority 5: Hunt any untamed animal
            wolf.targetSelector.addGoal(5, new NonTameRandomTargetGoal<>(wolf, Animal.class, false,
                    target -> !(target instanceof Wolf) && !(target instanceof TamableAnimal tamable && tamable.isTame())));
        } else if (event.getEntity() instanceof Animal animal) {
            // Priority 1: Persistent fleeing until 30 blocks away from attacker/player
            animal.goalSelector.addGoal(1, new PersistentFleeGoal(animal, 1.8D, FLEE_DISTANCE_THRESHOLD));
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

        if (attacker instanceof LivingEntity livingAttacker) {
            // Mark persistent flee target UUID so the animal persistently runs until 30 blocks away
            animal.getPersistentData().putUUID(NBT_FLEE_TARGET, livingAttacker.getUUID());
            animal.setLastHurtByMob(livingAttacker);
        }

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

        // Actively navigate away from the attacker immediately
        if (attacker != null && animal.isAlive()) {
            Vec3 fleeVec = animal.position().subtract(attacker.position()).normalize();
            Vec3 targetPos = animal.position().add(fleeVec.x * 25.0D, 0.0D, fleeVec.z * 25.0D);
            animal.getNavigation().moveTo(targetPos.x, targetPos.y, targetPos.z, 2.0D);
        }
    }

    /**
     * AI Goal: Continuously flees away from the player or attacker until at least 30 blocks away.
     */
    public static class PersistentFleeGoal extends Goal {
        private final PathfinderMob mob;
        private final double speedModifier;
        private final double maxDistanceSq;
        private LivingEntity fleeTarget;
        private double posX;
        private double posY;
        private double posZ;
        private int rePathCooldown = 0;

        public PersistentFleeGoal(PathfinderMob mob, double speedModifier, double maxDistance) {
            this.mob = mob;
            this.speedModifier = speedModifier;
            this.maxDistanceSq = maxDistance * maxDistance;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (this.mob instanceof TamableAnimal tamable && tamable.isTame()) {
                return false;
            }

            LivingEntity target = findTarget();
            if (target != null && target.isAlive() && this.mob.distanceToSqr(target) < this.maxDistanceSq) {
                this.fleeTarget = target;
                return findFleePosition();
            }

            return false;
        }

        @Override
        public boolean canContinueToUse() {
            if (this.fleeTarget == null || !this.fleeTarget.isAlive() || !this.fleeTarget.level().equals(this.mob.level())) {
                clearTarget();
                return false;
            }

            if (this.mob.distanceToSqr(this.fleeTarget) >= this.maxDistanceSq) {
                // Successfully escaped 30+ blocks away!
                clearTarget();
                return false;
            }

            return true;
        }

        @Override
        public void start() {
            this.mob.getNavigation().moveTo(this.posX, this.posY, this.posZ, this.speedModifier);
            this.mob.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 80, 1, false, false, true));
        }

        @Override
        public void tick() {
            if (this.fleeTarget == null) return;

            // Keep Speed II active during active flight
            if (!this.mob.hasEffect(MobEffects.MOVEMENT_SPEED)) {
                this.mob.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 80, 1, false, false, true));
            }

            if (--this.rePathCooldown <= 0 || this.mob.getNavigation().isDone()) {
                this.rePathCooldown = 15;
                if (findFleePosition()) {
                    this.mob.getNavigation().moveTo(this.posX, this.posY, this.posZ, this.speedModifier);
                }
            }
        }

        @Override
        public void stop() {
            this.fleeTarget = null;
        }

        private LivingEntity findTarget() {
            LivingEntity lastHurt = this.mob.getLastHurtByMob();
            if (lastHurt != null && lastHurt.isAlive() && this.mob.distanceToSqr(lastHurt) < this.maxDistanceSq) {
                return lastHurt;
            }

            if (this.mob.getPersistentData().hasUUID(NBT_FLEE_TARGET)) {
                UUID targetUuid = this.mob.getPersistentData().getUUID(NBT_FLEE_TARGET);
                if (this.mob.level() instanceof ServerLevel serverLevel) {
                    Entity entity = serverLevel.getEntity(targetUuid);
                    if (entity instanceof LivingEntity living && living.isAlive() && this.mob.distanceToSqr(living) < this.maxDistanceSq) {
                        return living;
                    } else {
                        this.mob.getPersistentData().remove(NBT_FLEE_TARGET);
                    }
                }
            }

            return null;
        }

        private boolean findFleePosition() {
            if (this.fleeTarget == null) return false;

            Vec3 fleePos = DefaultRandomPos.getPosAway(this.mob, 16, 7, this.fleeTarget.position());
            if (fleePos == null) {
                Vec3 dir = this.mob.position().subtract(this.fleeTarget.position()).normalize();
                if (dir.lengthSqr() < 1e-4) {
                    dir = new Vec3(1, 0, 0);
                }
                fleePos = this.mob.position().add(dir.x * 16.0D, 0, dir.z * 16.0D);
            }

            this.posX = fleePos.x;
            this.posY = fleePos.y;
            this.posZ = fleePos.z;
            return true;
        }

        private void clearTarget() {
            if (this.mob.getPersistentData().hasUUID(NBT_FLEE_TARGET)) {
                this.mob.getPersistentData().remove(NBT_FLEE_TARGET);
            }
            this.fleeTarget = null;
        }
    }
}
