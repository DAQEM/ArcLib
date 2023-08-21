package com.daqem.arc.api.action.type;

import com.daqem.arc.Arc;
import com.daqem.arc.api.action.IAction;
import com.daqem.arc.data.action.advancement.AdvancementAction;
import com.daqem.arc.data.action.block.*;
import com.daqem.arc.data.action.entity.*;
import com.daqem.arc.data.action.item.*;
import com.daqem.arc.data.action.movement.*;
import com.daqem.arc.data.action.player.*;
import com.daqem.arc.event.events.RegistryEvent;
import com.daqem.arc.registry.ArcRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public interface ActionType<T extends IAction> extends IActionType<T> {

    ActionType<SwimAction> SWIM = register(Arc.getId("on_swim"));
    ActionType<SwimStartAction> SWIM_START = register(Arc.getId("on_swim_start"));
    ActionType<SwimStopAction> SWIM_STOP = register(Arc.getId("on_swim_stop"));
    ActionType<WalkAction> WALK = register(Arc.getId("on_walk"));
    ActionType<WalkStartAction> WALK_START = register(Arc.getId("on_walk_start"));
    ActionType<WalkStopAction> WALK_STOP = register(Arc.getId("on_walk_stop"));
    ActionType<SprintAction> SPRINT = register(Arc.getId("on_sprint"));
    ActionType<SprintStartAction> SPRINT_START = register(Arc.getId("on_sprint_start"));
    ActionType<SprintStopAction> SPRINT_STOP = register(Arc.getId("on_sprint_stop"));
    ActionType<CrouchAction> CROUCH = register(Arc.getId("on_crouch"));
    ActionType<CrouchStartAction> CROUCH_START = register(Arc.getId("on_crouch_start"));
    ActionType<CrouchStopAction> CROUCH_STOP = register(Arc.getId("on_crouch_stop"));
    ActionType<ElytraFlyAction> ELYTRA_FLY = register(Arc.getId("on_elytra_fly"));
    ActionType<ElytraFlyStartAction> ELYTRA_FLY_START = register(Arc.getId("on_elytra_fly_start"));
    ActionType<ElytraFlyStopAction> ELYTRA_FLY_STOP = register(Arc.getId("on_elytra_fly_stop"));

    ActionType<PlaceBlockAction> PLACE_BLOCK = register(Arc.getId("on_place_block"));
    ActionType<BreakBlockAction> BREAK_BLOCK = register(Arc.getId("on_break_block"));
    ActionType<InteractBlockAction> INTERACT_BLOCK = register(Arc.getId("on_interact_block"));
    ActionType<DeathAction> DEATH = register(Arc.getId("on_death"));
    ActionType<GetHurtAction> GET_HURT = register(Arc.getId("on_get_hurt"));
    ActionType<KillEntityAction> KILL_ENTITY = register(Arc.getId("on_kill_entity"));
    ActionType<HurtEntityAction> HURT_ENTITY = register(Arc.getId("on_hurt_entity"));
    ActionType<CraftItemAction> CRAFT_ITEM = register(Arc.getId("on_craft_item"));
    ActionType<DropItemAction> DROP_ITEM = register(Arc.getId("on_drop_item"));
    ActionType<UseItemAction> USE_ITEM = register(Arc.getId("on_use_item"));
    ActionType<AdvancementAction> ADVANCEMENT = register(Arc.getId("on_advancement"));
    ActionType<EatAction> EAT = register(Arc.getId("on_eat"));
    ActionType<DrinkAction> DRINK = register(Arc.getId("on_drink"));
    ActionType<ThrowItemAction> THROW_ITEM = register(Arc.getId("on_throw_item"));
    ActionType<ShootProjectileAction> SHOOT_PROJECTILE = register(Arc.getId("on_shoot_projectile"));
    ActionType<BrewPotionAction> BREW_POTION = register(Arc.getId("on_brew_potion"));
    ActionType<EffectAddedAction> EFFECT_ADDED = register(Arc.getId("on_effect_added"));
    ActionType<SmeltItemAction> SMELT_ITEM = register(Arc.getId("on_smelt_item"));
    ActionType<EnchantItemAction> ENCHANT_ITEM = register(Arc.getId("on_enchant_item"));
    ActionType<PlantCropAction> PLANT_CROP = register(Arc.getId("on_plant_crop"));
    ActionType<HarvestCropAction> HARVEST_CROP = register(Arc.getId("on_harvest_crop"));
    ActionType<TameAnimalAction> TAME_ANIMAL = register(Arc.getId("on_tame_animal"));
    ActionType<InteractEntityAction> INTERACT_ENTITY = register(Arc.getId("on_interact_entity"));
    ActionType<BreedAnimalAction> BREED_ANIMAL = register(Arc.getId("on_breed_animal"));
    ActionType<FishedUpItemAction> FISHED_UP_ITEM = register(Arc.getId("on_fished_up_item"));
    ActionType<StripLogAction> STRIP_LOG = register(Arc.getId("on_strip_log"));
    ActionType<GrindItemAction> GRIND_ITEM = register(Arc.getId("on_grind_item"));
    ActionType<UseAnvilAction> USE_ANVIL = register(Arc.getId("on_use_anvil"));
    ActionType<HurtItemAction> HURT_ITEM = register(Arc.getId("on_hurt_item"));
    ActionType<GetDestroySpeedAction> GET_DESTROY_SPEED = register(Arc.getId("on_get_destroy_speed"));
    ActionType<GetAttackSpeedAction> GET_ATTACK_SPEED = register(Arc.getId("on_get_attack_speed"));
    ActionType<RodReelInAction> ROD_REEL_IN = register(Arc.getId("on_rod_reel_in"));

    static <T extends IAction> ActionType<T> register(final ResourceLocation location) {
        return Registry.register(ArcRegistry.ACTION, location, new ActionType<T>(){

            @Override
            public ResourceLocation getLocation() {
                return location;
            }

            @Override
            public String toString() {
                return location.toString();
            }
        });
    }

    static void init() {
        RegistryEvent.REGISTER_ACTION_TYPE.invoker().registerActionType();
    }
}
