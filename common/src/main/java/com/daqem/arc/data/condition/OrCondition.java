package com.daqem.arc.data.condition;

import com.daqem.arc.api.action.data.ActionData;
import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.ICondition;
import com.daqem.arc.api.condition.serializer.ConditionSerializer;
import com.daqem.arc.api.condition.serializer.IConditionSerializer;
import com.daqem.arc.api.condition.type.ConditionType;
import com.daqem.arc.api.condition.type.IConditionType;
import com.google.gson.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class OrCondition extends AbstractCondition {

    private final List<ICondition> conditions;

    public OrCondition(boolean inverted, List<ICondition> conditions) {
        super(inverted);
        this.conditions = conditions;
    }

    @Override
    public boolean isMet(ActionData actionData) {
        return conditions.stream().anyMatch(condition -> condition.isMet(actionData));
    }

    @Override
    public IConditionType<?> getType() {
        return ConditionType.OR;
    }

    @Override
    public IConditionSerializer<?> getSerializer() {
        return ConditionSerializer.OR;
    }

    public static class Serializer implements ConditionSerializer<OrCondition> {

        @Override
        @SuppressWarnings("unchecked")
        public OrCondition fromJson(ResourceLocation location, JsonObject jsonObject, boolean inverted) {
            return new OrCondition(
                    inverted, null);
//                    processJsonArray(jsonObject, "conditions", (Registry<? extends IArcSerializer<ICondition>>) ArcRegistry.CONDITION_SERIALIZER, location));
        }

        @Override
        @SuppressWarnings("unchecked")
        public OrCondition fromNetwork(ResourceLocation location, FriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new OrCondition(
                    inverted, null);
//                    processFriendlyByteBuf(friendlyByteBuf, (Registry<? extends IArcSerializer<ICondition>>) ArcRegistry.CONDITION_SERIALIZER, location));
        }

        @Override
        @SuppressWarnings("unchecked")
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, OrCondition type) {
            ConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeVarInt(type.conditions.size());
            type.conditions.forEach(condition ->
                    ((IConditionSerializer<ICondition>) condition.getSerializer()).toNetwork(friendlyByteBuf, condition)
            );
        }
    }
}
