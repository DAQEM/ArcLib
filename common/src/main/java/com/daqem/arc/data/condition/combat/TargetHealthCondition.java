package com.daqem.arc.data.condition.combat;

import com.daqem.arc.api.ComparisonType;
import com.daqem.arc.api.action.data.IActionDataType;
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
import net.minecraft.world.entity.LivingEntity;

public class TargetHealthCondition extends AbstractCondition {
    private final INumberProvider health;
    private final ComparisonType comparisonType;
    private final boolean isPercentage;

    public TargetHealthCondition(boolean inverted, INumberProvider health, ComparisonType comparisonType, boolean isPercentage) {
        super(inverted);
        this.health = health;
        this.comparisonType = comparisonType;
        this.isPercentage = isPercentage;
    }

    @Override
    public Component getDescription() {
        return getDescription(comparisonType.getSymbol(), health.toString() + (isPercentage ? "%" : ""));
    }

    @Override
    public boolean isMet(ActionData actionData) {
        if (actionData.getData(IActionDataType.ENTITY) instanceof LivingEntity target) {
            double targetHealth = target.getHealth();
            if (isPercentage) {
                targetHealth = (targetHealth / target.getMaxHealth()) * 100.0;
            }
            return comparisonType.compare(targetHealth, this.health.resolve(actionData));
        }
        return false;
    }

    public ComparisonType getComparisonType() {
        return comparisonType;
    }

    public INumberProvider getHealth() {
        return health;
    }

    public boolean isPercentage() {
        return isPercentage;
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.TARGET_HEALTH;
    }

    public static class Serializer implements IConditionSerializer<TargetHealthCondition> {
        @Override
        public TargetHealthCondition fromJson(Identifier location, JsonObject jsonObject, boolean inverted) {
            return new TargetHealthCondition(
                    inverted,
                    getNumberProvider(jsonObject, "health", new ConstantNumberProvider(0.0)),
                    getComparisonType(jsonObject, "comparison", ComparisonType.EQUAL),
                    GsonHelper.getAsBoolean(jsonObject, "is_percentage", false)
            );
        }

        @Override
        public TargetHealthCondition fromNetwork(Identifier location, RegistryFriendlyByteBuf buf, boolean inverted) {
            return new TargetHealthCondition(
                    inverted,
                    INumberProviderSerializer.fromNetworkStatic(buf),
                    buf.readEnum(ComparisonType.class),
                    buf.readBoolean()
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, TargetHealthCondition type) {
            IConditionSerializer.super.toNetwork(buf, type);
            INumberProviderSerializer.toNetwork(type.health, buf);
            buf.writeEnum(type.comparisonType);
            buf.writeBoolean(type.isPercentage);
        }
    }
}