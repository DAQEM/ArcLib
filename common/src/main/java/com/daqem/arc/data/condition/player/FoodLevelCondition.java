package com.daqem.arc.data.condition.player;

import com.daqem.arc.api.ComparisonType;
import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.api.math.INumberProvider;
import com.daqem.arc.api.math.INumberProviderSerializer;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.data.math.ConstantNumberProvider;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

public class FoodLevelCondition extends AbstractCondition {

    private final INumberProvider foodLevel;
    private final ComparisonType comparisonType;

    public FoodLevelCondition(boolean inverted, INumberProvider foodLevel, ComparisonType comparisonType) {
        super(inverted);
        this.foodLevel = foodLevel;
        this.comparisonType = comparisonType;
    }

    @Override
    public Component getDescription() {
        return getDescription(comparisonType.getSymbol(), foodLevel.getDescription());
    }

    @Override
    public boolean isMet(ActionData actionData) {
        Player player = actionData.getPlayer().arc$getPlayer();
        return comparisonType.compare(player.getFoodData().getFoodLevel(), this.foodLevel.resolve(actionData));
    }

    public ComparisonType getComparisonType() {
        return comparisonType;
    }

    public INumberProvider getFoodLevel() {
        return foodLevel;
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.FOOD_LEVEL;
    }

    public static class Serializer implements IConditionSerializer<FoodLevelCondition> {

        @Override
        public FoodLevelCondition fromJson(Identifier location, JsonObject jsonObject, boolean inverted) {
            return new FoodLevelCondition(
                    inverted,
                    getNumberProvider(jsonObject, "food_level", new ConstantNumberProvider(0.0)),
                    getComparisonType(jsonObject, "comparison", ComparisonType.EQUAL)
            );
        }

        @Override
        public FoodLevelCondition fromNetwork(Identifier location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new FoodLevelCondition(
                    inverted,
                    INumberProviderSerializer.fromNetworkStatic(friendlyByteBuf),
                    friendlyByteBuf.readEnum(ComparisonType.class)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, FoodLevelCondition type) {
            IConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            INumberProviderSerializer.toNetwork(type.foodLevel, friendlyByteBuf);
            friendlyByteBuf.writeEnum(type.comparisonType);
        }
    }
}