package com.daqem.arc.data.condition.recipe;

import com.daqem.arc.api.IArcAbstractCookingRecipe;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.api.action.data.IActionDataType;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.google.gson.*;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.SmokingRecipe;

public class IsSmokingRecipeCondition extends IsRecipeCondition {

    public IsSmokingRecipeCondition(boolean inverted) {
        super(inverted);
    }

    @Override
    public boolean isMet(ActionData actionData) {
        Recipe<?> recipe = actionData.getData(IActionDataType.RECIPE);
        if (recipe != null) {
            if (recipe instanceof SmokingRecipe) {
                return true;
            }
            if (recipe instanceof IArcAbstractCookingRecipe smeltingRecipe) {
                return RecipeCache.isRecipeType(actionData, smeltingRecipe, SmokingRecipe.class);
            }
        }
        return false;
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.IS_SMOKING_RECIPE;
    }

    public static class Serializer implements IConditionSerializer<IsSmokingRecipeCondition> {

        @Override
        public IsSmokingRecipeCondition fromJson(ResourceLocation location, JsonObject jsonObject, boolean inverted) {
            return new IsSmokingRecipeCondition(inverted);
        }

        @Override
        public IsSmokingRecipeCondition fromNetwork(ResourceLocation location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new IsSmokingRecipeCondition(inverted);
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, IsSmokingRecipeCondition type) {
            IConditionSerializer.super.toNetwork(friendlyByteBuf, type);
        }
    }
}
