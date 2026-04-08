package com.daqem.arc.api.action;

import com.daqem.arc.Arc;
import com.daqem.arc.data.action.block.*;
import com.daqem.arc.data.action.combat.GetHurtAction;
import com.daqem.arc.data.action.combat.HurtEntityAction;
import com.daqem.arc.data.action.combat.HurtPlayerAction;
import com.daqem.arc.data.action.combat.KillEntityAction;
import com.daqem.arc.data.action.entity.BreedAnimalAction;
import com.daqem.arc.data.action.entity.InteractEntityAction;
import com.daqem.arc.data.action.entity.TameAnimalAction;
import com.daqem.arc.data.action.entity.TradeWithVillagerAction;
import com.daqem.arc.data.action.item.*;
import com.daqem.arc.data.action.movement.*;
import com.daqem.arc.data.action.player.*;
import com.daqem.arc.registry.ArcRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

public interface IActionType<T extends IAction> {

    IActionType<SwimAction> SWIM = register(Arc.API.getId("on_swim"), new SwimAction.Serializer());
    IActionType<SwimStartAction> SWIM_START = register(Arc.API.getId("on_swim_start"), new SwimStartAction.Serializer());
    IActionType<SwimStopAction> SWIM_STOP = register(Arc.API.getId("on_swim_stop"), new SwimStopAction.Serializer());
    IActionType<WalkAction> WALK = register(Arc.API.getId("on_walk"), new WalkAction.Serializer());
    IActionType<WalkStartAction> WALK_START = register(Arc.API.getId("on_walk_start"), new WalkStartAction.Serializer());
    IActionType<WalkStopAction> WALK_STOP = register(Arc.API.getId("on_walk_stop"), new WalkStopAction.Serializer());
    IActionType<SprintAction> SPRINT = register(Arc.API.getId("on_sprint"), new SprintAction.Serializer());
    IActionType<SprintStartAction> SPRINT_START = register(Arc.API.getId("on_sprint_start"), new SprintStartAction.Serializer());
    IActionType<SprintStopAction> SPRINT_STOP = register(Arc.API.getId("on_sprint_stop"), new SprintStopAction.Serializer());
    IActionType<CrouchAction> CROUCH = register(Arc.API.getId("on_crouch"), new CrouchAction.Serializer());
    IActionType<CrouchStartAction> CROUCH_START = register(Arc.API.getId("on_crouch_start"), new CrouchStartAction.Serializer());
    IActionType<CrouchStopAction> CROUCH_STOP = register(Arc.API.getId("on_crouch_stop"), new CrouchStopAction.Serializer());
    IActionType<ElytraFlyAction> ELYTRA_FLY = register(Arc.API.getId("on_elytra_fly"), new ElytraFlyAction.Serializer());
    IActionType<ElytraFlyStartAction> ELYTRA_FLY_START = register(Arc.API.getId("on_elytra_fly_start"), new ElytraFlyStartAction.Serializer());
    IActionType<ElytraFlyStopAction> ELYTRA_FLY_STOP = register(Arc.API.getId("on_elytra_fly_stop"), new ElytraFlyStopAction.Serializer());
    IActionType<HorseRideAction> HORSE_RIDE = register(Arc.API.getId("on_horse_ride"), new HorseRideAction.Serializer());
    IActionType<HorseRideStartAction> HORSE_RIDE_START = register(Arc.API.getId("on_horse_ride_start"), new HorseRideStartAction.Serializer());
    IActionType<HorseRideStopAction> HORSE_RIDE_STOP = register(Arc.API.getId("on_horse_ride_stop"), new HorseRideStopAction.Serializer());

    IActionType<PlaceBlockAction> PLACE_BLOCK = register(Arc.API.getId("on_place_block"), new PlaceBlockAction.Serializer());
    IActionType<BreakBlockAction> BREAK_BLOCK = register(Arc.API.getId("on_break_block"), new BreakBlockAction.Serializer());
    IActionType<InteractBlockAction> INTERACT_BLOCK = register(Arc.API.getId("on_interact_block"), new InteractBlockAction.Serializer());
    IActionType<DeathAction> DEATH = register(Arc.API.getId("on_death"), new DeathAction.Serializer());
    IActionType<GetHurtAction> GET_HURT = register(Arc.API.getId("on_get_hurt"), new GetHurtAction.Serializer());
    IActionType<KillEntityAction> KILL_ENTITY = register(Arc.API.getId("on_kill_entity"), new KillEntityAction.Serializer());
    IActionType<HurtEntityAction> HURT_ENTITY = register(Arc.API.getId("on_hurt_entity"), new HurtEntityAction.Serializer());
    IActionType<HurtPlayerAction> HURT_PLAYER = register(Arc.API.getId("on_hurt_player"), new HurtPlayerAction.Serializer());
    IActionType<CraftItemAction> CRAFT_ITEM = register(Arc.API.getId("on_craft_item"), new CraftItemAction.Serializer());
    IActionType<DropItemAction> DROP_ITEM = register(Arc.API.getId("on_drop_item"), new DropItemAction.Serializer());
    IActionType<UseItemAction> USE_ITEM = register(Arc.API.getId("on_use_item"), new UseItemAction.Serializer());
    IActionType<AdvancementAction> ADVANCEMENT = register(Arc.API.getId("on_advancement"), new AdvancementAction.Serializer());
    IActionType<EatAction> EAT = register(Arc.API.getId("on_eat"), new EatAction.Serializer());
    IActionType<DrinkAction> DRINK = register(Arc.API.getId("on_drink"), new DrinkAction.Serializer());
    IActionType<ThrowItemAction> THROW_ITEM = register(Arc.API.getId("on_throw_item"), new ThrowItemAction.Serializer());
    IActionType<ShootProjectileAction> SHOOT_PROJECTILE = register(Arc.API.getId("on_shoot_projectile"), new ShootProjectileAction.Serializer());
    IActionType<BrewPotionAction> BREW_POTION = register(Arc.API.getId("on_brew_potion"), new BrewPotionAction.Serializer());
    IActionType<AddEffectAction> ADD_EFFECT = register(Arc.API.getId("on_add_effect"), new AddEffectAction.Serializer());
    IActionType<SmeltItemAction> SMELT_ITEM = register(Arc.API.getId("on_smelt_item"), new SmeltItemAction.Serializer());
    IActionType<EnchantItemAction> ENCHANT_ITEM = register(Arc.API.getId("on_enchant_item"), new EnchantItemAction.Serializer());
    IActionType<PlantCropAction> PLANT_CROP = register(Arc.API.getId("on_plant_crop"), new PlantCropAction.Serializer());
    IActionType<HarvestCropAction> HARVEST_CROP = register(Arc.API.getId("on_harvest_crop"), new HarvestCropAction.Serializer());
    IActionType<TameAnimalAction> TAME_ANIMAL = register(Arc.API.getId("on_tame_animal"), new TameAnimalAction.Serializer());
    IActionType<InteractEntityAction> INTERACT_ENTITY = register(Arc.API.getId("on_interact_entity"), new InteractEntityAction.Serializer());
    IActionType<BreedAnimalAction> BREED_ANIMAL = register(Arc.API.getId("on_breed_animal"), new BreedAnimalAction.Serializer());
    IActionType<FishedUpItemAction> FISHED_UP_ITEM = register(Arc.API.getId("on_fished_up_item"), new FishedUpItemAction.Serializer());
    IActionType<StripLogAction> STRIP_LOG = register(Arc.API.getId("on_strip_log"), new StripLogAction.Serializer());
    IActionType<GrindItemAction> GRIND_ITEM = register(Arc.API.getId("on_grind_item"), new GrindItemAction.Serializer());
    IActionType<UseAnvilAction> USE_ANVIL = register(Arc.API.getId("on_use_anvil"), new UseAnvilAction.Serializer());
    IActionType<HurtItemAction> HURT_ITEM = register(Arc.API.getId("on_hurt_item"), new HurtItemAction.Serializer());
    IActionType<GetDestroySpeedAction> GET_DESTROY_SPEED = register(Arc.API.getId("on_get_destroy_speed"), new GetDestroySpeedAction.Serializer());
    IActionType<GetAttackSpeedAction> GET_ATTACK_SPEED = register(Arc.API.getId("on_get_attack_speed"), new GetAttackSpeedAction.Serializer());
    IActionType<RodReelInAction> ROD_REEL_IN = register(Arc.API.getId("on_rod_reel_in"), new RodReelInAction.Serializer());
    IActionType<JumpAction> JUMP = register(Arc.API.getId("on_jump"), new JumpAction.Serializer());
    IActionType<LandOnGroundAction> LAND_ON_GROUND = register(Arc.API.getId("on_land"), new LandOnGroundAction.Serializer());
    IActionType<PickupItemAction> PICKUP_ITEM = register(Arc.API.getId("on_pickup_item"), new PickupItemAction.Serializer());
    IActionType<BlockWithShieldAction> BLOCK_WITH_SHIELD = register(Arc.API.getId("on_block_with_shield"), new BlockWithShieldAction.Serializer());
    IActionType<ChangeDimensionAction> CHANGE_DIMENSION = register(Arc.API.getId("on_change_dimension"), new ChangeDimensionAction.Serializer());
    IActionType<FillBucketAction> FILL_BUCKET = register(Arc.API.getId("on_fill_bucket"), new FillBucketAction.Serializer());
    IActionType<EmptyBucketAction> EMPTY_BUCKET = register(Arc.API.getId("on_empty_bucket"), new EmptyBucketAction.Serializer());
    IActionType<TillSoilAction> TILL_SOIL = register(Arc.API.getId("on_till_soil"), new TillSoilAction.Serializer());
    IActionType<ItemBreakAction> ITEM_BREAK = register(Arc.API.getId("on_item_break"), new ItemBreakAction.Serializer());
    IActionType<TradeWithVillagerAction> TRADE_WITH_VILLAGER = register(Arc.API.getId("on_trade_with_villager"), new TradeWithVillagerAction.Serializer());

    static <T extends IAction> IActionType<T> register(final Identifier location, final IActionSerializer<T> serializer) {
        return Registry.register(ArcRegistry.ACTION, location, new IActionType<T>(){

            @Override
            public Identifier getIdentifier() {
                return location;
            }

            @Override
            public IActionSerializer<T> getSerializer() {
                return serializer;
            }

            @Override
            public String toString() {
                return location.toString();
            }
        });
    }

    static void init() {
    }

    Identifier getIdentifier();

    IActionSerializer<T> getSerializer();
}