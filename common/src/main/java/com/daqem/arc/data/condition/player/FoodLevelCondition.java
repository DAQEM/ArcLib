package com.daqem.arc.data.condition.player;

import com.daqem.arc.api.ComparisonType;
import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.data.ActionData;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;

public class FoodLevelCondition extends AbstractCondition {

    private final int foodLevel;
    private final ComparisonType comparisonType;

    public FoodLevelCondition(boolean inverted, int foodLevel, ComparisonType comparisonType) {
        super(inverted);
        this.foodLevel = foodLevel;
        this.comparisonType = comparisonType;
    }

    @Override
    public Component getDescription() {
        return getDescription(comparisonType.getSymbol(), foodLevel);
    }

    @Override
    public boolean isMet(ActionData actionData) {
        Player player = actionData.getPlayer().arc$getPlayer();
        return comparisonType.compare(player.getFoodData().getFoodLevel(), this.foodLevel);
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.FOOD_LEVEL;
    }

    public static class Serializer implements IConditionSerializer<FoodLevelCondition> {

        @Override
        public FoodLevelCondition fromJson(ResourceLocation location, JsonObject jsonObject, boolean inverted) {
            return new FoodLevelCondition(
                    inverted,
                    GsonHelper.getAsInt(jsonObject, "food_level"),
                    getComparisonType(jsonObject, "comparison", ComparisonType.EQUAL)
            );
        }

        @Override
        public FoodLevelCondition fromNetwork(ResourceLocation location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new FoodLevelCondition(
                    inverted,
                    friendlyByteBuf.readVarInt(),
                    friendlyByteBuf.readEnum(ComparisonType.class)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, FoodLevelCondition type) {
            IConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeVarInt(type.foodLevel);
            friendlyByteBuf.writeEnum(type.comparisonType);
        }
    }
}