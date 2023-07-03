package com.daqem.arc.data.condition.recipe;

import com.daqem.arc.api.action.data.ActionData;
import com.daqem.arc.api.action.data.type.ActionDataType;
import com.daqem.arc.api.condition.serializer.ConditionSerializer;
import com.daqem.arc.api.condition.serializer.IConditionSerializer;
import com.daqem.arc.api.condition.type.ConditionType;
import com.daqem.arc.api.condition.type.IConditionType;
import com.google.gson.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.crafting.SmokingRecipe;

public class IsSmokingRecipeCondition extends IsRecipeCondition<SmokingRecipe> {

    public IsSmokingRecipeCondition(boolean inverted) {
        super(inverted);
    }

    @Override
    public boolean isMet(ActionData actionData) {
        Recipe<?> recipe = actionData.getData(ActionDataType.RECIPE);
        if (recipe != null) {
            if (recipe instanceof SmokingRecipe) {
                return true;
            }
            if (recipe instanceof SmeltingRecipe smeltingRecipe) {
                return isSmeltingRecipeVersion(actionData, smeltingRecipe, SmokingRecipe.class);
            }
        }
        return false;
    }

    @Override
    public IConditionType<?> getType() {
        return ConditionType.IS_SMOKING_RECIPE;
    }

    @Override
    public IConditionSerializer<?> getSerializer() {
        return ConditionSerializer.IS_SMOKING_RECIPE;
    }

    public static class Serializer implements ConditionSerializer<IsSmokingRecipeCondition> {

        @Override
        public IsSmokingRecipeCondition fromJson(ResourceLocation location, JsonObject jsonObject, boolean inverted) {
            return new IsSmokingRecipeCondition(inverted);
        }

        @Override
        public IsSmokingRecipeCondition fromNetwork(ResourceLocation location, FriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new IsSmokingRecipeCondition(inverted);
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, IsSmokingRecipeCondition type) {
            ConditionSerializer.super.toNetwork(friendlyByteBuf, type);
        }
    }
}
