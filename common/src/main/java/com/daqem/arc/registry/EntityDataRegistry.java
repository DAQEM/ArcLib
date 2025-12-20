package com.daqem.arc.registry;

import com.daqem.arc.Arc;
import com.daqem.arc.api.entity.IEntityDataResolver;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.illager.SpellcasterIllager;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class EntityDataRegistry {

    private static final Map<Identifier, IEntityDataResolver<?>> RESOLVERS = new HashMap<>();

    public static <T> void register(IEntityDataResolver<T> resolver) {
        RESOLVERS.put(resolver.getId(), resolver);
    }

    public static Optional<IEntityDataResolver<?>> get(Identifier id) {
        return Optional.ofNullable(RESOLVERS.get(id));
    }

    public static void init() {
        register(new SimpleEntityDataResolver<>(Arc.getId("is_on_fire"), Boolean.class, Entity::isOnFire));
        register(new SimpleEntityDataResolver<>(Arc.getId("is_crouching"), Boolean.class, Entity::isCrouching));
        register(new SimpleEntityDataResolver<>(Arc.getId("is_sprinting"), Boolean.class, Entity::isSprinting));
        register(new SimpleEntityDataResolver<>(Arc.getId("is_swimming"), Boolean.class, Entity::isSwimming));
        register(new SimpleEntityDataResolver<>(Arc.getId("is_glowing"), Boolean.class, Entity::isCurrentlyGlowing));
        register(new SimpleEntityDataResolver<>(Arc.getId("is_invisible"), Boolean.class, Entity::isInvisible));
        register(new SimpleEntityDataResolver<>(Arc.getId("is_on_ground"), Boolean.class, Entity::onGround));
        register(new SimpleEntityDataResolver<>(Arc.getId("is_in_water"), Boolean.class, Entity::isInWater));
        register(new SimpleEntityDataResolver<>(Arc.getId("is_in_lava"), Boolean.class, Entity::isInLava));
        register(new SimpleEntityDataResolver<>(Arc.getId("is_in_rain"), Boolean.class, Entity::isInRain));
        register(new SimpleEntityDataResolver<>(Arc.getId("air_supply"), Integer.class, Entity::getAirSupply));
        register(new SimpleEntityDataResolver<>(Arc.getId("fall_distance"), Float.class, entity -> (float) entity.fallDistance));
        register(new SimpleEntityDataResolver<>(Arc.getId("ticks_frozen"), Integer.class, Entity::getTicksFrozen));
        register(new SimpleEntityDataResolver<>(Arc.getId("has_no_gravity"), Boolean.class, Entity::isNoGravity));
        register(new SimpleEntityDataResolver<>(Arc.getId("is_silent"), Boolean.class, Entity::isSilent));
        register(new SimpleEntityDataResolver<>(Arc.getId("pose"), String.class, entity -> entity.getPose().name()));
        //endregion

        //region LivingEntity Data
        register(new SimpleEntityDataResolver<>(Arc.getId("health"), Float.class,
                entity -> entity instanceof LivingEntity le ? le.getHealth() : 0F));
        register(new SimpleEntityDataResolver<>(Arc.getId("max_health"), Float.class,
                entity -> entity instanceof LivingEntity le ? le.getMaxHealth() : 0F));
        register(new SimpleEntityDataResolver<>(Arc.getId("absorption_amount"), Float.class,
                entity -> entity instanceof LivingEntity le ? le.getAbsorptionAmount() : 0F));
        register(new SimpleEntityDataResolver<>(Arc.getId("is_baby"), Boolean.class,
                entity -> entity instanceof LivingEntity le && le.isBaby()));
        register(new SimpleEntityDataResolver<>(Arc.getId("is_using_item"), Boolean.class,
                entity -> entity instanceof LivingEntity le && le.isUsingItem()));
        register(new SimpleEntityDataResolver<>(Arc.getId("is_blocking"), Boolean.class,
                entity -> entity instanceof LivingEntity le && le.isBlocking()));
        register(new SimpleEntityDataResolver<>(Arc.getId("is_sleeping"), Boolean.class,
                entity -> entity instanceof LivingEntity le && le.isSleeping()));
        register(new SimpleEntityDataResolver<>(Arc.getId("arrow_count"), Integer.class,
                entity -> entity instanceof LivingEntity le ? le.getArrowCount() : 0));
        register(new SimpleEntityDataResolver<>(Arc.getId("stinger_count"), Integer.class,
                entity -> entity instanceof LivingEntity le ? le.getStingerCount() : 0));
        register(new SimpleEntityDataResolver<>(Arc.getId("main_arm"), String.class,
                entity -> entity instanceof LivingEntity le ? le.getMainArm().name() : ""));
        register(new SimpleEntityDataResolver<>(Arc.getId("is_fall_flying"), Boolean.class,
                entity -> entity instanceof LivingEntity le && le.isFallFlying()));;
        //endregion

        //region Player Data
        register(new SimpleEntityDataResolver<>(Arc.getId("score"), Integer.class,
                entity -> entity instanceof Player p ? p.getScore() : 0));
        register(new SimpleEntityDataResolver<>(Arc.getId("experience_level"), Integer.class,
                entity -> entity instanceof Player p ? p.experienceLevel : 0));
        register(new SimpleEntityDataResolver<>(Arc.getId("total_experience"), Integer.class,
                entity -> entity instanceof Player p ? p.totalExperience : 0));
        register(new SimpleEntityDataResolver<>(Arc.getId("experience_progress"), Float.class,
                entity -> entity instanceof Player p ? p.experienceProgress : 0F));
        register(new SimpleEntityDataResolver<>(Arc.getId("is_creative"), Boolean.class,
                entity -> entity instanceof Player p && p.isCreative()));
        register(new SimpleEntityDataResolver<>(Arc.getId("is_spectator"), Boolean.class,
                entity -> entity instanceof Player p && p.isSpectator()));
        register(new SimpleEntityDataResolver<>(Arc.getId("food_level"), Integer.class,
                entity -> entity instanceof Player p ? p.getFoodData().getFoodLevel() : 0));
        register(new SimpleEntityDataResolver<>(Arc.getId("saturation_level"), Float.class,
                entity -> entity instanceof Player p ? p.getFoodData().getSaturationLevel() : 0F));
        //endregion

        //region Mob Data
        register(new SimpleEntityDataResolver<>(Arc.getId("has_no_ai"), Boolean.class,
                entity -> entity instanceof Mob m && m.isNoAi()));
        register(new SimpleEntityDataResolver<>(Arc.getId("is_left_handed"), Boolean.class,
                entity -> entity instanceof Mob m && m.isLeftHanded()));
        register(new SimpleEntityDataResolver<>(Arc.getId("is_aggressive"), Boolean.class,
                entity -> entity instanceof Mob m && m.isAggressive()));
        register(new SimpleEntityDataResolver<>(Arc.getId("can_pickup_loot"), Boolean.class,
                entity -> entity instanceof Mob m && m.canPickUpLoot()));
        register(new SimpleEntityDataResolver<>(Arc.getId("is_leashed"), Boolean.class,
                entity -> entity instanceof Mob m && m.isLeashed()));
        register(new SimpleEntityDataResolver<>(Arc.getId("has_target"), Boolean.class,
                entity -> entity instanceof Mob m && m.getTarget() != null));
        //endregion

        //region Animal & Tameable Data
        register(new SimpleEntityDataResolver<>(Arc.getId("in_love_time"), Integer.class,
                entity -> entity instanceof Animal a ? a.getInLoveTime() : 0));
        register(new SimpleEntityDataResolver<>(Arc.getId("is_tame"), Boolean.class,
                entity -> entity instanceof TamableAnimal t && t.isTame()));
        register(new SimpleEntityDataResolver<>(Arc.getId("is_sitting"), Boolean.class,
                entity -> entity instanceof TamableAnimal t && t.isInSittingPose()));
        register(new SimpleEntityDataResolver<>(Arc.getId("is_ordered_to_sit"), Boolean.class,
                entity -> entity instanceof TamableAnimal t && t.isOrderedToSit()));
        register(new SimpleEntityDataResolver<>(Arc.getId("has_owner"), Boolean.class,
                entity -> entity instanceof TamableAnimal t && t.getOwner() != null));
        //endregion

        //region Specific Mob Data
        register(new SimpleEntityDataResolver<>(Arc.getId("villager_unhappy_counter"), Integer.class,
                entity -> entity instanceof AbstractVillager v ? v.getUnhappyCounter() : 0));
        register(new SimpleEntityDataResolver<>(Arc.getId("is_trading"), Boolean.class,
                entity -> entity instanceof AbstractVillager v && v.isTrading()));
        register(new SimpleEntityDataResolver<>(Arc.getId("villager_profession"), String.class,
                entity -> entity instanceof Villager v ? BuiltInRegistries.VILLAGER_PROFESSION.getKey(v.getVillagerData().profession().value()).toString() : "none"));
        register(new SimpleEntityDataResolver<>(Arc.getId("is_casting_spell"), Boolean.class,
                entity -> entity instanceof SpellcasterIllager si && si.isCastingSpell()));
        register(new SimpleEntityDataResolver<>(Arc.getId("current_spell"), String.class,
                entity -> entity instanceof SpellcasterIllager si ? ((Enum<?>) si.getCurrentSpell()).name() : "NONE"));
        //endregion
    }

    private record SimpleEntityDataResolver<T>(Identifier id, Class<T> type, Function<Entity, T> dataFetcher) implements IEntityDataResolver<T> {
        @Override
        public Identifier getId() {
            return id;
        }

        @Override
        public Function<Entity, T> getDataFetcher() {
            return dataFetcher;
        }

        @Override
        public Class<T> getType() {
            return type;
        }
    }
}