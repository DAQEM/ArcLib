package com.daqem.arc.api.condition.serializer;

import com.daqem.arc.Arc;
import com.daqem.arc.api.condition.ICondition;
import com.daqem.arc.data.condition.NotCondition;
import com.daqem.arc.data.condition.OrCondition;
import com.daqem.arc.data.condition.block.BlockCondition;
import com.daqem.arc.data.condition.block.BlocksCondition;
import com.daqem.arc.data.condition.block.crop.CropAgeCondition;
import com.daqem.arc.data.condition.block.crop.CropFullyGrownCondition;
import com.daqem.arc.data.condition.block.ore.IsOreCondition;
import com.daqem.arc.data.condition.block.properties.BlockHardnessCondition;
import com.daqem.arc.data.condition.effect.EffectCategoryCondition;
import com.daqem.arc.data.condition.effect.EffectCondition;
import com.daqem.arc.data.condition.entity.*;
import com.daqem.arc.data.condition.experience.ExpDropCondition;
import com.daqem.arc.data.condition.experience.ExpLevelCondition;
import com.daqem.arc.data.condition.item.*;
import com.daqem.arc.data.condition.movement.DistanceCondition;
import com.daqem.arc.data.condition.recipe.IsBlastingRecipeCondition;
import com.daqem.arc.data.condition.recipe.IsSmokingRecipeCondition;
import com.daqem.arc.data.condition.scoreboard.ScoreboardCondition;
import com.daqem.arc.data.condition.team.TeamCondition;
import com.daqem.arc.data.condition.world.DimensionCondition;
import com.daqem.arc.event.events.RegistryEvent;
import com.daqem.arc.registry.ArcRegistry;
import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

public interface ConditionSerializer<T extends ICondition> extends IConditionSerializer<T> {

    IConditionSerializer<OrCondition> OR = register(Arc.getId("or"), new OrCondition.Serializer());
    IConditionSerializer<NotCondition> NOT = register(Arc.getId("not"), new NotCondition.Serializer());

    IConditionSerializer<CropFullyGrownCondition> CROP_FULLY_GROWN = register(Arc.getId("crop_fully_grown"), new CropFullyGrownCondition.Serializer());
    IConditionSerializer<CropAgeCondition> CROP_AGE = register(Arc.getId("crop_age"), new CropAgeCondition.Serializer());
    IConditionSerializer<BlockCondition> BLOCK = register(Arc.getId("block"), new BlockCondition.Serializer());
    IConditionSerializer<BlocksCondition> BLOCKS = register(Arc.getId("blocks"), new BlocksCondition.Serializer());
    IConditionSerializer<DistanceCondition> DISTANCE = register(Arc.getId("swimming_distance"), new DistanceCondition.Serializer());
    IConditionSerializer<EntityTypeCondition> ENTITY_TYPE = register(Arc.getId("entity_type"), new EntityTypeCondition.Serializer());
    IConditionSerializer<EntityTypesCondition> ENTITY_TYPES = register(Arc.getId("entity_types"), new EntityTypesCondition.Serializer());
    IConditionSerializer<DimensionCondition> DIMENSION = register(Arc.getId("dimension"), new DimensionCondition.Serializer());
    IConditionSerializer<ScoreboardCondition> SCOREBOARD = register(Arc.getId("scoreboard"), new ScoreboardCondition.Serializer());
    IConditionSerializer<TeamCondition> TEAM = register(Arc.getId("team"), new TeamCondition.Serializer());
    IConditionSerializer<ItemInHandCondition> ITEM_IN_HAND = register(Arc.getId("item_in_hand"), new ItemInHandCondition.Serializer());
    IConditionSerializer<ItemInInventoryCondition> ITEM_IN_INVENTORY = register(Arc.getId("item_in_inventory"), new ItemInInventoryCondition.Serializer());
    IConditionSerializer<ItemEquippedCondition> ITEM_EQUIPPED = register(Arc.getId("item_equipped"), new ItemEquippedCondition.Serializer());
    IConditionSerializer<ItemCondition> ITEM = register(Arc.getId("item"), new ItemCondition.Serializer());
    IConditionSerializer<ItemsCondition> ITEMS = register(Arc.getId("items"), new ItemsCondition.Serializer());
    IConditionSerializer<ExpDropCondition> EXP_DROP = register(Arc.getId("exp_drop"), new ExpDropCondition.Serializer());
    IConditionSerializer<ExpLevelCondition> EXP_LEVEL = register(Arc.getId("exp_level"), new ExpLevelCondition.Serializer());
    IConditionSerializer<ReadyForShearingCondition> READY_FOR_SHEARING = register(Arc.getId("ready_for_shearing"), new ReadyForShearingCondition.Serializer());
    IConditionSerializer<IsBlastingRecipeCondition> IS_BLASTING_RECIPE = register(Arc.getId("is_blasting_recipe"), new IsBlastingRecipeCondition.Serializer());
    IConditionSerializer<IsSmokingRecipeCondition> IS_SMOKING_RECIPE = register(Arc.getId("is_smoking_recipe"), new IsSmokingRecipeCondition.Serializer());
    IConditionSerializer<IsOreCondition> IS_ORE = register(Arc.getId("is_ore"), new IsOreCondition.Serializer());
    IConditionSerializer<EffectCategoryCondition> EFFECT_CATEGORY = register(Arc.getId("effect_category"), new EffectCategoryCondition.Serializer());
    IConditionSerializer<EffectCondition> EFFECT = register(Arc.getId("effect"), new EffectCondition.Serializer());
    IConditionSerializer<BlockHardnessCondition> BLOCK_HARDNESS = register(Arc.getId("block_hardness"), new BlockHardnessCondition.Serializer());
    IConditionSerializer<EntityInBlockCondition> ENTITY_IN_BLOCK = register(Arc.getId("entity_in_block"), new EntityInBlockCondition.Serializer());
    IConditionSerializer<DamageSourceCondition> DAMAGE_SOURCE = register(Arc.getId("damage_source"), new DamageSourceCondition.Serializer());

    @Override
    default T fromJson(ResourceLocation location, JsonObject jsonObject) {
        return fromJson(location, jsonObject, GsonHelper.getAsBoolean(jsonObject, "inverted", false));
    }

    @Override
    default T fromNetwork(ResourceLocation location, FriendlyByteBuf friendlyByteBuf) {
        return fromNetwork(location, friendlyByteBuf, friendlyByteBuf.readBoolean());
    }

    @Override
    default void toNetwork(FriendlyByteBuf friendlyByteBuf, T type) {
        friendlyByteBuf.writeBoolean(type.isInverted());
    }

    static <T extends ICondition> IConditionSerializer<T> register(final ResourceLocation location, final ConditionSerializer<T> serializer) {
        return Registry.register(ArcRegistry.CONDITION_SERIALIZER, location, serializer);
    }

    static void init() {
        RegistryEvent.REGISTER_CONDITION_SERIALIZER.invoker().registerConditionSerializer();
    }
}
