package com.daqem.arc.data.condition.combat;

import com.daqem.arc.api.ComparisonType;
import com.daqem.arc.api.action.data.IActionDataType;
import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.data.ActionData;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.LivingEntity;

public class TargetHealthCondition extends AbstractCondition {

    private final double health;
    private final ComparisonType comparisonType;
    private final boolean isPercentage;

    public TargetHealthCondition(boolean inverted, double health, ComparisonType comparisonType, boolean isPercentage) {
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
        if (actionData.getData(IActionDataType.ENTITY) instanceof LivingEntity target) {
            double targetHealth = target.getHealth();
            if (isPercentage) {
                targetHealth = (targetHealth / target.getMaxHealth()) * 100.0;
            }
            return comparisonType.compare(targetHealth, this.health);
        }
        return false;
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
                    GsonHelper.getAsDouble(jsonObject, "health"),
                    getComparisonType(jsonObject, "comparison", ComparisonType.EQUAL),
                    GsonHelper.getAsBoolean(jsonObject, "is_percentage", false)
            );
        }

        @Override
        public TargetHealthCondition fromNetwork(Identifier location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new TargetHealthCondition(
                    inverted,
                    friendlyByteBuf.readDouble(),
                    friendlyByteBuf.readEnum(ComparisonType.class),
                    friendlyByteBuf.readBoolean()
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, TargetHealthCondition type) {
            IConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeDouble(type.health);
            friendlyByteBuf.writeEnum(type.comparisonType);
            friendlyByteBuf.writeBoolean(type.isPercentage);
        }
    }
}