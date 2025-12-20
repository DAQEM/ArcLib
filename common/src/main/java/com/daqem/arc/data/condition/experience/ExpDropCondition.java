package com.daqem.arc.data.condition.experience;

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

public class ExpDropCondition extends AbstractCondition {

    private final int min;
    private final int max;

    public ExpDropCondition(boolean inverted, int min, int max) {
        super(inverted);
        this.min = min;
        this.max = max;

        if (min > max) {
            throw new IllegalArgumentException("min cannot be greater than max for ExpDropCondition.");
        }
    }

    @Override
    public Component getDescription() {
        return getDescription(min, max);
    }

    @Override
    public boolean isMet(ActionData actionData) {
        Integer expDrop = actionData.getData(IActionDataType.EXP_DROP);
        return expDrop != null && expDrop >= min && expDrop <= max;
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.EXP_DROP;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public static class Serializer implements IConditionSerializer<ExpDropCondition> {

        @Override
        public ExpDropCondition fromJson(Identifier location, JsonObject jsonObject, boolean inverted) {
            return new ExpDropCondition(
                    inverted,
                    GsonHelper.getAsInt(jsonObject, "min"),
                    GsonHelper.getAsInt(jsonObject, "max"));
        }

        @Override
        public ExpDropCondition fromNetwork(Identifier location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new ExpDropCondition(
                    inverted,
                    friendlyByteBuf.readVarInt(),
                    friendlyByteBuf.readVarInt());
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, ExpDropCondition type) {
            IConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeVarInt(type.min);
            friendlyByteBuf.writeVarInt(type.max);
        }
    }
}
