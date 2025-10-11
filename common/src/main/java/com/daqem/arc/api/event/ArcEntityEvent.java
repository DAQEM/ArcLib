package com.daqem.arc.api.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import org.apache.commons.lang3.mutable.MutableFloat;

public interface ArcEntityEvent {

    Event<PlayerDeath> PLAYER_DEATH = EventFactory.createEventResult(PlayerDeath.class);
    Event<PlayerKillEntity> PLAYER_KILL_ENTITY = EventFactory.createEventResult(PlayerKillEntity.class);
    Event<PlayerHurtEntity> PLAYER_HURT_ENTITY = EventFactory.createEventResult(PlayerHurtEntity.class);

    Event<BreedAnimal> BREED_ANIMAL = EventFactory.createEventResult(BreedAnimal.class);
    Event<TameAnimal> TAME_ANIMAL = EventFactory.createEventResult(TameAnimal.class);

    Event<InteractWithEntity> INTERACT_WITH_ENTITY = EventFactory.createEventResult(InteractWithEntity.class);

    interface PlayerDeath {
        EventResult onPlayerDeath(ServerPlayer serverPlayer, DamageSource damageSource);
    }

    interface PlayerKillEntity {
        EventResult onPlayerKillEntity(ServerPlayer serverPlayer, LivingEntity entity, DamageSource damageSource);
    }

    interface PlayerHurtEntity {
        EventResult onPlayerHurtEntity(ServerPlayer serverPlayer, LivingEntity entity, DamageSource damageSource, MutableFloat damage);
    }

    interface BreedAnimal {
        EventResult onBreedAnimal(ServerLevel serverLevel, ServerPlayer serverPlayer, AgeableMob baby);
    }

    interface TameAnimal {
        EventResult onTameAnimal(Animal animal, Player player);
    }

    interface InteractWithEntity {
        EventResult onInteractWithEntity(Player player, Entity entity, InteractionHand hand);
    }
}
