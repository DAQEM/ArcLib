package com.daqem.arc.api.condition.type;

import com.daqem.arc.Arc;
import com.daqem.arc.api.condition.ICondition;
import com.daqem.arc.data.condition.OrCondition;
import com.daqem.arc.data.condition.block.BlockCondition;
import com.daqem.arc.data.condition.block.BlocksCondition;
import com.daqem.arc.data.condition.block.crop.CropAgeCondition;
import com.daqem.arc.data.condition.block.crop.CropFullyGrownCondition;
import com.daqem.arc.data.condition.block.ore.IsOreCondition;
import com.daqem.arc.data.condition.block.properties.BlockHardnessCondition;
import com.daqem.arc.data.condition.block.properties.BlockMaterialColorCondition;
import com.daqem.arc.data.condition.effect.EffectCategoryCondition;
import com.daqem.arc.data.condition.effect.EffectCondition;
import com.daqem.arc.data.condition.entity.EntityTypeCondition;
import com.daqem.arc.data.condition.entity.EntityTypesCondition;
import com.daqem.arc.data.condition.entity.ReadyForShearingCondition;
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
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public interface ConditionType<T extends ICondition> extends IConditionType<T> {

    ConditionType<OrCondition> OR = register(Arc.getId("or"));

    ConditionType<CropFullyGrownCondition> CROP_FULLY_GROWN = register(Arc.getId("crop_fully_grown"));
    ConditionType<CropAgeCondition> CROP_AGE = register(Arc.getId("crop_age"));
    ConditionType<BlockCondition> BLOCK = register(Arc.getId("block"));
    ConditionType<BlocksCondition> BLOCKS = register(Arc.getId("blocks"));
    ConditionType<DistanceCondition> DISTANCE = register(Arc.getId("swimming_distance"));
    ConditionType<EntityTypeCondition> ENTITY_TYPE = register(Arc.getId("entity_type"));
    ConditionType<EntityTypesCondition> ENTITY_TYPES = register(Arc.getId("entity_types"));
    ConditionType<DimensionCondition> DIMENSION = register(Arc.getId("dimension"));
    ConditionType<ScoreboardCondition> SCOREBOARD = register(Arc.getId("scoreboard"));
    ConditionType<TeamCondition> TEAM = register(Arc.getId("team"));
    ConditionType<ItemInHandCondition> ITEM_IN_HAND = register(Arc.getId("item_in_hand"));
    ConditionType<ItemInInventoryCondition> ITEM_IN_INVENTORY = register(Arc.getId("item_in_inventory"));
    ConditionType<ItemEquippedCondition> ITEM_EQUIPPED = register(Arc.getId("item_equipped"));
    ConditionType<ItemCondition> ITEM = register(Arc.getId("item"));
    ConditionType<ItemsCondition> ITEMS = register(Arc.getId("items"));
    ConditionType<ExpDropCondition> EXP_DROP = register(Arc.getId("exp_drop"));
    ConditionType<ExpLevelCondition> EXP_LEVEL = register(Arc.getId("exp_level"));
    ConditionType<ReadyForShearingCondition> READY_FOR_SHEARING = register(Arc.getId("ready_for_shearing"));
    ConditionType<IsBlastingRecipeCondition> IS_BLASTING_RECIPE = register(Arc.getId("is_blasting_recipe"));
    ConditionType<IsSmokingRecipeCondition> IS_SMOKING_RECIPE = register(Arc.getId("is_smoking_recipe"));
    ConditionType<IsOreCondition> IS_ORE = register(Arc.getId("is_ore"));
    ConditionType<EffectCategoryCondition> EFFECT_CATEGORY = register(Arc.getId("effect_category"));
    ConditionType<EffectCondition> EFFECT = register(Arc.getId("effect"));
    ConditionType<BlockHardnessCondition> BLOCK_HARDNESS = register(Arc.getId("block_hardness"));
    ConditionType<BlockMaterialColorCondition> BLOCK_MATERIAL_COLOR = register(Arc.getId("block_material_color"));

    static <T extends ICondition> ConditionType<T> register(final ResourceLocation location) {
        return Registry.register(ArcRegistry.CONDITION, location, new ConditionType<T>(){

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
        RegistryEvent.REGISTER_CONDITION_TYPE.invoker().registerConditionType();
    }
}
