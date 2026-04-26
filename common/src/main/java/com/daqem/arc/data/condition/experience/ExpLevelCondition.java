package com.daqem.arc.data.condition.experience;

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

public class ExpLevelCondition extends AbstractCondition {

    private final INumberProvider level;

    public ExpLevelCondition(boolean inverted, INumberProvider level) {
        super(inverted);
        this.level = level;
    }

    @Override
    public Component getDescription() {
        return getDescription(level.getDescription());
    }

    @Override
    public boolean isMet(ActionData actionData) {
        Integer expLevel = actionData.getData(IActionDataType.EXP_LEVEL);
        return expLevel != null && expLevel == Math.round(this.level.resolve(actionData));
    }

    public INumberProvider getLevel() {
        return level;
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.EXP_LEVEL;
    }

    public static class Serializer implements IConditionSerializer<ExpLevelCondition> {

        @Override
        public ExpLevelCondition fromJson(Identifier location, JsonObject jsonObject, boolean inverted) {
            return new ExpLevelCondition(
                    inverted,
                    getNumberProvider(jsonObject, "level", new ConstantNumberProvider(0.0))
            );
        }

        @Override
        public ExpLevelCondition fromNetwork(Identifier location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new ExpLevelCondition(
                    inverted,
                    INumberProviderSerializer.fromNetworkStatic(friendlyByteBuf)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, ExpLevelCondition type) {
            IConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            INumberProviderSerializer.toNetwork(type.level, friendlyByteBuf);
        }
    }
}