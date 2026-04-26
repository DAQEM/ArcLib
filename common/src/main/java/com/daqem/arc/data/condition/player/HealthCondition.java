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
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;

public class HealthCondition extends AbstractCondition {

    private final INumberProvider health;
    private final ComparisonType comparisonType;
    private final boolean isPercentage;

    public HealthCondition(boolean inverted, INumberProvider health, ComparisonType comparisonType, boolean isPercentage) {
        super(inverted);
        this.health = health;
        this.comparisonType = comparisonType;
        this.isPercentage = isPercentage;
    }

    @Override
    public Component getDescription() {
        return getDescription(comparisonType.getSymbol(), health.getDescription(), (isPercentage ? "%" : ""));
    }

    @Override
    public boolean isMet(ActionData actionData) {
        Player player = actionData.getPlayer().arc$getPlayer();
        double playerHealth = player.getHealth();
        if (isPercentage) {
            playerHealth = (playerHealth / player.getMaxHealth()) * 100.0;
        }
        return comparisonType.compare(playerHealth, this.health.resolve(actionData));
    }

    public INumberProvider getHealth() {
        return health;
    }

    public ComparisonType getComparisonType() {
        return comparisonType;
    }

    public boolean isPercentage() {
        return isPercentage;
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
                    getNumberProvider(jsonObject, "health", new ConstantNumberProvider(0.0)),
                    getComparisonType(jsonObject, "comparison", ComparisonType.EQUAL),
                    GsonHelper.getAsBoolean(jsonObject, "is_percentage", false)
            );
        }

        @Override
        public HealthCondition fromNetwork(Identifier location, RegistryFriendlyByteBuf buf, boolean inverted) {
            return new HealthCondition(
                    inverted,
                    INumberProviderSerializer.fromNetworkStatic(buf),
                    buf.readEnum(ComparisonType.class),
                    buf.readBoolean()
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, HealthCondition type) {
            IConditionSerializer.super.toNetwork(buf, type);
            INumberProviderSerializer.toNetwork(type.health, buf);
            buf.writeEnum(type.comparisonType);
            buf.writeBoolean(type.isPercentage);
        }
    }
}