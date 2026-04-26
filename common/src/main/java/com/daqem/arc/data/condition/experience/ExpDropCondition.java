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

public class ExpDropCondition extends AbstractCondition {

    private final INumberProvider min;
    private final INumberProvider max;

    public ExpDropCondition(boolean inverted, INumberProvider min, INumberProvider max) {
        super(inverted);
        this.min = min;
        this.max = max;
    }

    @Override
    public Component getDescription() {
        return getDescription(min.toString(), max.toString());
    }

    @Override
    public boolean isMet(ActionData actionData) {
        Integer expDrop = actionData.getData(IActionDataType.EXP_DROP);
        if (expDrop == null) return false;
        double minVal = min.resolve(actionData);
        double maxVal = max.resolve(actionData);
        return expDrop >= minVal && expDrop <= maxVal;
    }

    public INumberProvider getMin() {
        return min;
    }

    public INumberProvider getMax() {
        return max;
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.EXP_DROP;
    }

    public static class Serializer implements IConditionSerializer<ExpDropCondition> {
        @Override
        public ExpDropCondition fromJson(Identifier location, JsonObject jsonObject, boolean inverted) {
            return new ExpDropCondition(
                    inverted,
                    getNumberProvider(jsonObject, "min", new ConstantNumberProvider(Integer.MIN_VALUE)),
                    getNumberProvider(jsonObject, "max", new ConstantNumberProvider(Integer.MAX_VALUE))
            );
        }

        @Override
        public ExpDropCondition fromNetwork(Identifier location, RegistryFriendlyByteBuf buf, boolean inverted) {
            return new ExpDropCondition(
                    inverted,
                    INumberProviderSerializer.fromNetworkStatic(buf),
                    INumberProviderSerializer.fromNetworkStatic(buf)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, ExpDropCondition type) {
            IConditionSerializer.super.toNetwork(buf, type);
            INumberProviderSerializer.toNetwork(type.min, buf);
            INumberProviderSerializer.toNetwork(type.max, buf);
        }
    }
}