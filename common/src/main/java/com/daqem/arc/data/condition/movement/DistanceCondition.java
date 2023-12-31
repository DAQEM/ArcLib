package com.daqem.arc.data.condition.movement;

import com.daqem.arc.api.action.data.ActionData;
import com.daqem.arc.api.action.data.type.ActionDataType;
import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.serializer.ConditionSerializer;
import com.daqem.arc.api.condition.serializer.IConditionSerializer;
import com.daqem.arc.api.condition.type.ConditionType;
import com.daqem.arc.api.condition.type.IConditionType;
import com.google.gson.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

public class DistanceCondition extends AbstractCondition {

    private final int distanceInBlocks;
    private int lastDistanceInBlocks = 0;

    public DistanceCondition(boolean inverted, int distanceInBlocks) {
        super(inverted);
        this.distanceInBlocks = distanceInBlocks;
    }

    @Override
    public Component getDescription() {
        return getDescription(distanceInBlocks);
    }

    @Override
    public boolean isMet(ActionData actionData) {
        Integer distanceInCm = actionData.getData(ActionDataType.DISTANCE_IN_CM);
        if (distanceInCm != null) {
            int currentDistanceInBlocks = distanceInCm / 100;
            if (currentDistanceInBlocks != lastDistanceInBlocks) {
                if (currentDistanceInBlocks % distanceInBlocks == 0) {
                    lastDistanceInBlocks = currentDistanceInBlocks;
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public IConditionType<?> getType() {
        return ConditionType.DISTANCE;
    }

    @Override
    public IConditionSerializer<?> getSerializer() {
        return ConditionSerializer.DISTANCE;
    }

    public static class Serializer implements ConditionSerializer<DistanceCondition> {

        @Override
        public DistanceCondition fromJson(ResourceLocation location, JsonObject jsonObject, boolean inverted) {
            return new DistanceCondition(
                    inverted,
                    GsonHelper.getAsInt(jsonObject, "distance_in_blocks"));
        }

        @Override
        public DistanceCondition fromNetwork(ResourceLocation location, FriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new DistanceCondition(
                    inverted,
                    friendlyByteBuf.readVarInt());
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, DistanceCondition type) {
            ConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeVarInt(type.distanceInBlocks);
        }
    }
}
