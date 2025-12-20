package com.daqem.arc.data.condition.movement;

import com.daqem.arc.api.action.data.IActionDataType;
import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.api.player.ArcServerPlayer;
import com.daqem.arc.data.ActionData;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.GsonHelper;

public class DistanceCondition extends AbstractCondition {

    private final int distanceInBlocks;

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
        if (actionData.getPlayer() instanceof ArcServerPlayer accessor) {
            Double totalDistanceMovedInCm = actionData.getData(IActionDataType.DISTANCE_IN_CM);
            if (totalDistanceMovedInCm == null || totalDistanceMovedInCm < 0) return false;
            double lastAccountedDistanceCm = accessor.arc$getActionLastMetDistances().getOrDefault(this, 0.0);
            double requiredDistanceInCm = (double) this.distanceInBlocks * 100.0;
            boolean hasMetCondition = false;

            while (totalDistanceMovedInCm - lastAccountedDistanceCm >= requiredDistanceInCm) {
                hasMetCondition = true;
                lastAccountedDistanceCm += requiredDistanceInCm;
            }

            if (hasMetCondition) {
                accessor.arc$setActionLastMetDistance(this, lastAccountedDistanceCm);
            }

            return hasMetCondition;
        }
        return false;
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.DISTANCE;
    }

    public int getDistanceInBlocks() {
        return distanceInBlocks;
    }

    public static class Serializer implements IConditionSerializer<DistanceCondition> {

        @Override
        public DistanceCondition fromJson(Identifier location, JsonObject jsonObject, boolean inverted) {
            return new DistanceCondition(
                    inverted,
                    GsonHelper.getAsInt(jsonObject, "distance_in_blocks")
            );
        }

        @Override
        public DistanceCondition fromNetwork(Identifier location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new DistanceCondition(
                    inverted,
                    friendlyByteBuf.readVarInt()
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, DistanceCondition type) {
            IConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeVarInt(type.distanceInBlocks);
        }
    }
}
