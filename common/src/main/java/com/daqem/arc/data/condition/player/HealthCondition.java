package com.daqem.arc.data.condition.player;

import com.daqem.arc.api.ComparisonType;
import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.data.ActionData;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;

public class HealthCondition extends AbstractCondition {

    private final double health;
    private final ComparisonType comparisonType;
    private final boolean isPercentage;

    public HealthCondition(boolean inverted, double health, ComparisonType comparisonType, boolean isPercentage) {
        super(inverted);
        this.health = health;
        this.comparisonType = comparisonType;
        this.isPercentage = isPercentage;
    }

    @Override
    public Component getDescription() {
        return getDescription(comparisonType.getSymbol(), health + (isPercentage ? "%" : ""));
    }

    @Override
    public boolean isMet(ActionData actionData) {
        Player player = actionData.getPlayer().arc$getPlayer();
        double playerHealth = player.getHealth();
        if (isPercentage) {
            playerHealth = (playerHealth / player.getMaxHealth()) * 100.0;
        }
        return comparisonType.compare(playerHealth, this.health);
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.HEALTH;
    }

    public static class Serializer implements IConditionSerializer<HealthCondition> {

        @Override
        public HealthCondition fromJson(Identifier location, JsonObject jsonObject, boolean inverted) {
            return new HealthCondition(
                    inverted,
                    GsonHelper.getAsDouble(jsonObject, "health"),
                    getComparisonType(jsonObject, "comparison", ComparisonType.EQUAL),
                    GsonHelper.getAsBoolean(jsonObject, "is_percentage", false)
            );
        }

        @Override
        public HealthCondition fromNetwork(Identifier location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new HealthCondition(
                    inverted,
                    friendlyByteBuf.readDouble(),
                    friendlyByteBuf.readEnum(ComparisonType.class),
                    friendlyByteBuf.readBoolean()
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, HealthCondition type) {
            IConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeDouble(type.health);
            friendlyByteBuf.writeEnum(type.comparisonType);
            friendlyByteBuf.writeBoolean(type.isPercentage);
        }
    }
}