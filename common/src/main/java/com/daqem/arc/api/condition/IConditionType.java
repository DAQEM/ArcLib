package com.daqem.arc.api.condition;

import com.daqem.arc.Arc;
import com.daqem.arc.data.condition.OrCondition;
import com.daqem.arc.data.condition.advancement.AdvancementCondition;
import com.daqem.arc.data.condition.block.BlockCondition;
import com.daqem.arc.data.condition.block.BlocksCondition;
import com.daqem.arc.data.condition.block.NotInBlockPosCacheCondition;
import com.daqem.arc.data.condition.block.CropAgeCondition;
import com.daqem.arc.data.condition.block.CropFullyGrownCondition;
import com.daqem.arc.data.condition.block.IsOreCondition;
import com.daqem.arc.data.condition.block.BlockHardnessCondition;
import com.daqem.arc.data.condition.combat.CriticalHitCondition;
import com.daqem.arc.data.condition.combat.DamageSourceCondition;
import com.daqem.arc.data.condition.combat.TargetHealthCondition;
import com.daqem.arc.data.condition.effect.EffectCategoryCondition;
import com.daqem.arc.data.condition.effect.EffectCondition;
import com.daqem.arc.data.condition.entity.*;
import com.daqem.arc.data.condition.experience.ExpDropCondition;
import com.daqem.arc.data.condition.experience.ExpLevelCondition;
import com.daqem.arc.data.condition.item.*;
import com.daqem.arc.data.condition.misc.ChanceCondition;
import com.daqem.arc.data.condition.movement.DistanceCondition;
import com.daqem.arc.data.condition.player.*;
import com.daqem.arc.data.condition.recipe.IsBlastingRecipeCondition;
import com.daqem.arc.data.condition.recipe.IsSmokingRecipeCondition;
import com.daqem.arc.data.condition.player.ScoreboardCondition;
import com.daqem.arc.data.condition.player.TeamCondition;
import com.daqem.arc.data.condition.world.*;
import com.daqem.arc.registry.ArcRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public interface IConditionType<T extends ICondition> {

    IConditionType<OrCondition> OR = register(Arc.getId("or"), new OrCondition.Serializer());

    IConditionType<AdvancementCondition> ADVANCEMENT = register(Arc.getId("advancement"), new AdvancementCondition.Serializer());
    IConditionType<CropFullyGrownCondition> CROP_FULLY_GROWN = register(Arc.getId("crop_fully_grown"), new CropFullyGrownCondition.Serializer());
    IConditionType<CropAgeCondition> CROP_AGE = register(Arc.getId("crop_age"), new CropAgeCondition.Serializer());
    IConditionType<BlockCondition> BLOCK = register(Arc.getId("block"), new BlockCondition.Serializer());
    IConditionType<BlocksCondition> BLOCKS = register(Arc.getId("blocks"), new BlocksCondition.Serializer());
    IConditionType<DistanceCondition> DISTANCE = register(Arc.getId("distance"), new DistanceCondition.Serializer());
    IConditionType<EntityTypeCondition> ENTITY_TYPE = register(Arc.getId("entity_type"), new EntityTypeCondition.Serializer());
    IConditionType<EntityTypesCondition> ENTITY_TYPES = register(Arc.getId("entity_types"), new EntityTypesCondition.Serializer());
    IConditionType<DimensionCondition> DIMENSION = register(Arc.getId("dimension"), new DimensionCondition.Serializer());
    IConditionType<ScoreboardCondition> SCOREBOARD = register(Arc.getId("scoreboard"), new ScoreboardCondition.Serializer());
    IConditionType<TeamCondition> TEAM = register(Arc.getId("team"), new TeamCondition.Serializer());
    IConditionType<ItemInHandCondition> ITEM_IN_HAND = register(Arc.getId("item_in_hand"), new ItemInHandCondition.Serializer());
    IConditionType<ItemInInventoryCondition> ITEM_IN_INVENTORY = register(Arc.getId("item_in_inventory"), new ItemInInventoryCondition.Serializer());
    IConditionType<ItemEquippedCondition> ITEM_EQUIPPED = register(Arc.getId("item_equipped"), new ItemEquippedCondition.Serializer());
    IConditionType<ItemCondition> ITEM = register(Arc.getId("item"), new ItemCondition.Serializer());
    IConditionType<ItemsCondition> ITEMS = register(Arc.getId("items"), new ItemsCondition.Serializer());
    IConditionType<ExpDropCondition> EXP_DROP = register(Arc.getId("exp_drop"), new ExpDropCondition.Serializer());
    IConditionType<ExpLevelCondition> EXP_LEVEL = register(Arc.getId("exp_level"), new ExpLevelCondition.Serializer());
    IConditionType<ReadyForShearingCondition> READY_FOR_SHEARING = register(Arc.getId("ready_for_shearing"), new ReadyForShearingCondition.Serializer());
    IConditionType<IsBlastingRecipeCondition> IS_BLASTING_RECIPE = register(Arc.getId("is_blasting_recipe"), new IsBlastingRecipeCondition.Serializer());
    IConditionType<IsSmokingRecipeCondition> IS_SMOKING_RECIPE = register(Arc.getId("is_smoking_recipe"), new IsSmokingRecipeCondition.Serializer());
    IConditionType<IsOreCondition> IS_ORE = register(Arc.getId("is_ore"), new IsOreCondition.Serializer());
    IConditionType<EffectCategoryCondition> EFFECT_CATEGORY = register(Arc.getId("effect_category"), new EffectCategoryCondition.Serializer());
    IConditionType<EffectCondition> EFFECT = register(Arc.getId("effect"), new EffectCondition.Serializer());
    IConditionType<BlockHardnessCondition> BLOCK_HARDNESS = register(Arc.getId("block_hardness"), new BlockHardnessCondition.Serializer());
    IConditionType<EntityInBlockCondition> ENTITY_IN_BLOCK = register(Arc.getId("entity_in_block"), new EntityInBlockCondition.Serializer());
    IConditionType<DamageSourceCondition> DAMAGE_SOURCE = register(Arc.getId("damage_source"), new DamageSourceCondition.Serializer());
    IConditionType<NotInBlockPosCacheCondition> NOT_IN_BLOCK_POS_CACHE = register(Arc.getId("not_in_block_pos_cache"), new NotInBlockPosCacheCondition.Serializer());
    IConditionType<HandCondition> HAND = register(Arc.getId("hand"), new HandCondition.Serializer());
    IConditionType<EntityDataCondition > ENTITY_DATA = register(Arc.getId("entity_data"), new EntityDataCondition.Serializer());
    IConditionType<OnFireCondition> ON_FIRE = register(Arc.getId("on_fire"), new OnFireCondition.Serializer());
    IConditionType<HealthCondition> HEALTH = register(Arc.getId("health"), new HealthCondition.Serializer());
    IConditionType<FoodLevelCondition> FOOD_LEVEL = register(Arc.getId("food_level"), new FoodLevelCondition.Serializer());
    IConditionType<SaturationLevelCondition> SATURATION_LEVEL = register(Arc.getId("saturation_level"), new SaturationLevelCondition.Serializer());
    IConditionType<IsWetCondition> IS_WET = register(Arc.getId("is_wet"), new IsWetCondition.Serializer());
    IConditionType<IsUnderwaterCondition> IS_UNDERWATER = register(Arc.getId("is_underwater"), new IsUnderwaterCondition.Serializer());
    IConditionType<IsSleepingCondition> IS_SLEEPING = register(Arc.getId("is_sleeping"), new IsSleepingCondition.Serializer());
    IConditionType<IsSneakingCondition> IS_SNEAKING = register(Arc.getId("is_sneaking"), new IsSneakingCondition.Serializer());
    IConditionType<IsBlockingCondition> IS_BLOCKING = register(Arc.getId("is_blocking"), new IsBlockingCondition.Serializer());
    IConditionType<InventoryFullCondition> INVENTORY_FULL = register(Arc.getId("inventory_full"), new InventoryFullCondition.Serializer());
    IConditionType<InventoryEmptyCondition> INVENTORY_EMPTY = register(Arc.getId("inventory_empty"), new InventoryEmptyCondition.Serializer());
    IConditionType<IsRidingCondition> IS_RIDING = register(Arc.getId("is_riding"), new IsRidingCondition.Serializer());
    IConditionType<RidingEntityTypeCondition> RIDING_ENTITY_TYPE = register(Arc.getId("riding_entity_type"), new RidingEntityTypeCondition.Serializer());
    IConditionType<YLevelCondition> Y_LEVEL = register(Arc.getId("y_level"), new YLevelCondition.Serializer());
    IConditionType<TimeOfDayCondition> TIME_OF_DAY = register(Arc.getId("time_of_day"), new TimeOfDayCondition.Serializer());
    IConditionType<WeatherCondition> WEATHER = register(Arc.getId("weather"), new WeatherCondition.Serializer());
    IConditionType<LightLevelCondition> LIGHT_LEVEL = register(Arc.getId("light_level"), new LightLevelCondition.Serializer());
    IConditionType<BiomeCondition> BIOME = register(Arc.getId("biome"), new BiomeCondition.Serializer());
    IConditionType<StructureCondition> STRUCTURE = register(Arc.getId("structure"), new StructureCondition.Serializer());
    IConditionType<CriticalHitCondition> CRITICAL_HIT = register(Arc.getId("critical_hit"), new CriticalHitCondition.Serializer());
    IConditionType<TargetHealthCondition> TARGET_HEALTH = register(Arc.getId("target_health"), new TargetHealthCondition.Serializer());
    IConditionType<ItemDurabilityCondition> ITEM_DURABILITY = register(Arc.getId("item_durability"), new ItemDurabilityCondition.Serializer());
    IConditionType<HasEnchantmentCondition> HAS_ENCHANTMENT = register(Arc.getId("has_enchantment"), new HasEnchantmentCondition.Serializer());
    IConditionType<FullArmorSetCondition> FULL_ARMOR_SET = register(Arc.getId("full_armor_set"), new FullArmorSetCondition.Serializer());
    IConditionType<ChanceCondition> CHANCE = register(Arc.getId("chance"), new ChanceCondition.Serializer());

    static <T extends ICondition> IConditionType<T> register(final ResourceLocation location, final IConditionSerializer<T> serializer) {
        return Registry.register(ArcRegistry.CONDITION, location, new IConditionType<T>(){

            @Override
            public ResourceLocation getLocation() {
                return location;
            }

            @Override
            public IConditionSerializer<T> getSerializer() {
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

    ResourceLocation getLocation();

    IConditionSerializer<T> getSerializer();
}