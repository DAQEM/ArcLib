package com.daqem.arc.api.action.serializer;

import com.daqem.arc.Arc;
import com.daqem.arc.api.action.IAction;
import com.daqem.arc.api.condition.ICondition;
import com.daqem.arc.api.condition.serializer.IConditionSerializer;
import com.daqem.arc.api.reward.IReward;
import com.daqem.arc.api.reward.serializer.IRewardSerializer;
import com.daqem.arc.data.action.advancement.AdvancementAction;
import com.daqem.arc.data.action.block.*;
import com.daqem.arc.data.action.entity.*;
import com.daqem.arc.data.action.item.*;
import com.daqem.arc.data.action.movement.*;
import com.daqem.arc.data.action.player.*;
import com.daqem.arc.registry.ArcRegistry;
import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

import java.util.ArrayList;
import java.util.List;

public interface ActionSerializer<T extends IAction> extends IActionSerializer<T> {

    IActionSerializer<SwimAction> SWIM = register(Arc.getId("on_swim"), new SwimAction.Serializer());
    IActionSerializer<SwimStartAction> SWIM_START = register(Arc.getId("on_swim_start"), new SwimStartAction.Serializer());
    IActionSerializer<SwimStopAction> SWIM_STOP = register(Arc.getId("on_swim_stop"), new SwimStopAction.Serializer());
    IActionSerializer<WalkAction> WALK = register(Arc.getId("on_walk"), new WalkAction.Serializer());
    IActionSerializer<WalkStartAction> WALK_START = register(Arc.getId("on_walk_start"), new WalkStartAction.Serializer());
    IActionSerializer<WalkStopAction> WALK_STOP = register(Arc.getId("on_walk_stop"), new WalkStopAction.Serializer());
    IActionSerializer<SprintAction> SPRINT = register(Arc.getId("on_sprint"), new SprintAction.Serializer());
    IActionSerializer<SprintStartAction> SPRINT_START = register(Arc.getId("on_sprint_start"), new SprintStartAction.Serializer());
    IActionSerializer<SprintStopAction> SPRINT_STOP = register(Arc.getId("on_sprint_stop"), new SprintStopAction.Serializer());
    IActionSerializer<CrouchAction> CROUCH = register(Arc.getId("on_crouch"), new CrouchAction.Serializer());
    IActionSerializer<CrouchStartAction> CROUCH_START = register(Arc.getId("on_crouch_start"), new CrouchStartAction.Serializer());
    IActionSerializer<CrouchStopAction> CROUCH_STOP = register(Arc.getId("on_crouch_stop"), new CrouchStopAction.Serializer());
    IActionSerializer<ElytraFlyAction> ELYTRA_FLY = register(Arc.getId("on_elytra_fly"), new ElytraFlyAction.Serializer());
    IActionSerializer<ElytraFlyStartAction> ELYTRA_FLY_START = register(Arc.getId("on_elytra_fly_start"), new ElytraFlyStartAction.Serializer());
    IActionSerializer<ElytraFlyStopAction> ELYTRA_FLY_STOP = register(Arc.getId("on_elytra_fly_stop"), new ElytraFlyStopAction.Serializer());

    IActionSerializer<PlaceBlockAction> PLACE_BLOCK = register(Arc.getId("on_place_block"), new PlaceBlockAction.Serializer());
    IActionSerializer<BreakBlockAction> BREAK_BLOCK = register(Arc.getId("on_break_block"), new BreakBlockAction.Serializer());
    IActionSerializer<InteractBlockAction> INTERACT_BLOCK = register(Arc.getId("on_interact_block"), new InteractBlockAction.Serializer());
    IActionSerializer<DeathAction> DEATH = register(Arc.getId("on_death"), new DeathAction.Serializer());
    IActionSerializer<GetHurtAction> GET_HURT = register(Arc.getId("on_get_hurt"), new GetHurtAction.Serializer());
    IActionSerializer<KillEntityAction> KILL_ENTITY = register(Arc.getId("on_kill_entity"), new KillEntityAction.Serializer());
    IActionSerializer<HurtEntityAction> HURT_ENTITY = register(Arc.getId("on_hurt_entity"), new HurtEntityAction.Serializer());
    IActionSerializer<CraftItemAction> CRAFT_ITEM = register(Arc.getId("on_craft_item"), new CraftItemAction.Serializer());
    IActionSerializer<DropItemAction> DROP_ITEM = register(Arc.getId("on_drop_item"), new DropItemAction.Serializer());
    IActionSerializer<UseItemAction> USE_ITEM = register(Arc.getId("on_use_item"), new UseItemAction.Serializer());
    IActionSerializer<AdvancementAction> ADVANCEMENT = register(Arc.getId("on_advancement"), new AdvancementAction.Serializer());
    IActionSerializer<EatAction> EAT = register(Arc.getId("on_eat"), new EatAction.Serializer());
    IActionSerializer<DrinkAction> DRINK = register(Arc.getId("on_drink"), new DrinkAction.Serializer());
    IActionSerializer<ThrowItemAction> THROW_ITEM = register(Arc.getId("on_throw_item"), new ThrowItemAction.Serializer());
    IActionSerializer<ShootProjectileAction> SHOOT_PROJECTILE = register(Arc.getId("on_shoot_projectile"), new ShootProjectileAction.Serializer());
    IActionSerializer<BrewPotionAction> BREW_POTION = register(Arc.getId("on_brew_potion"), new BrewPotionAction.Serializer());
    IActionSerializer<EffectAddedAction> EFFECT_ADDED = register(Arc.getId("on_effect_added"), new EffectAddedAction.Serializer());
    IActionSerializer<SmeltItemAction> SMELT_ITEM = register(Arc.getId("on_smelt_item"), new SmeltItemAction.Serializer());
    IActionSerializer<EnchantItemAction> ENCHANT_ITEM = register(Arc.getId("on_enchant_item"), new EnchantItemAction.Serializer());
    IActionSerializer<PlantCropAction> PLANT_CROP = register(Arc.getId("on_plant_crop"), new PlantCropAction.Serializer());
    IActionSerializer<HarvestCropAction> HARVEST_CROP = register(Arc.getId("on_harvest_crop"), new HarvestCropAction.Serializer());
    IActionSerializer<TameAnimalAction> TAME_ANIMAL = register(Arc.getId("on_tame_animal"), new TameAnimalAction.Serializer());
    IActionSerializer<InteractEntityAction> INTERACT_ENTITY = register(Arc.getId("on_interact_entity"), new InteractEntityAction.Serializer());
    IActionSerializer<BreedAnimalAction> BREED_ANIMAL = register(Arc.getId("on_breed_animal"), new BreedAnimalAction.Serializer());
    IActionSerializer<FishedUpItemAction> FISHED_UP_ITEM = register(Arc.getId("on_fished_up_item"), new FishedUpItemAction.Serializer());
    IActionSerializer<StripLogAction> STRIP_LOG = register(Arc.getId("on_strip_log"), new StripLogAction.Serializer());
    IActionSerializer<GrindItemAction> GRIND_ITEM = register(Arc.getId("on_grind_item"), new GrindItemAction.Serializer());
    IActionSerializer<UseAnvilAction> USE_ANVIL = register(Arc.getId("on_use_anvil"), new UseAnvilAction.Serializer());
    IActionSerializer<HurtItemAction> HURT_ITEM = register(Arc.getId("on_hurt_item"), new HurtItemAction.Serializer());
    IActionSerializer<GetDestroySpeedAction> GET_DESTROY_SPEED = register(Arc.getId("on_get_destroy_speed"), new GetDestroySpeedAction.Serializer());

    @Override
    default T fromJson(ResourceLocation location, JsonObject jsonObject) {
        List<IReward> rewards = new ArrayList<>();
        if (jsonObject.has("rewards")) {
            jsonObject.getAsJsonArray("rewards").forEach(jsonElement -> {
                ResourceLocation type = getResourceLocation(jsonElement.getAsJsonObject(), "type");
                ArcRegistry.REWARD_SERIALIZER.getOptional(type).ifPresent(serializer -> {
                    rewards.add(serializer.fromJson(location, jsonElement.getAsJsonObject()));
                });
            });
        }

        List<ICondition> conditions = new ArrayList<>();
        if (jsonObject.has("conditions")) {
            jsonObject.getAsJsonArray("conditions").forEach(jsonElement -> {
                ResourceLocation type = getResourceLocation(jsonElement.getAsJsonObject(), "type");
                ArcRegistry.CONDITION_SERIALIZER.getOptional(type).ifPresent(serializer -> {
                    conditions.add(serializer.fromJson(location, jsonElement.getAsJsonObject()));
                });
            });
        }

        JsonObject holderObject = GsonHelper.getAsJsonObject(jsonObject, "holder");

        return fromJson(location, jsonObject,
                getResourceLocation(holderObject, "id"),
                getHolderType(holderObject, "type"),
                false,
                rewards, conditions);
    }

    @Override
    default T fromNetwork(ResourceLocation location, FriendlyByteBuf friendlyByteBuf) {
        return fromNetwork(location, friendlyByteBuf,
                friendlyByteBuf.readResourceLocation(),
                ArcRegistry.ACTION_HOLDER.getOptional(friendlyByteBuf.readResourceLocation()).orElse(null),
                friendlyByteBuf.readBoolean(),
                friendlyByteBuf.readList(IRewardSerializer::fromNetwork),
                friendlyByteBuf.readList(IConditionSerializer::fromNetwork));
    }

    @Override
    default void toNetwork(FriendlyByteBuf friendlyByteBuf, T type) {
        friendlyByteBuf.writeResourceLocation(type.getActionHolderLocation());
        friendlyByteBuf.writeResourceLocation(type.getActionHolderType().getLocation());
        friendlyByteBuf.writeBoolean(type.shouldPerformOnClient());
        friendlyByteBuf.writeCollection(type.getRewards(),
                (friendlyByteBuf1, reward) -> IRewardSerializer.toNetwork(reward, friendlyByteBuf1, type.getLocation()));
        friendlyByteBuf.writeCollection(type.getConditions(),
                (friendlyByteBuf1, condition) -> IConditionSerializer.toNetwork(condition, friendlyByteBuf1, type.getLocation()));
    }

    static <T extends IAction> IActionSerializer<T> register(ResourceLocation location, IActionSerializer<T> serializer) {
        return Registry.register(ArcRegistry.ACTION_SERIALIZER, location, serializer);
    }

    static void init() {
    }
}
