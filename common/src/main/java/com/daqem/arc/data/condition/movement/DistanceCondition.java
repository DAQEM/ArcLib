package com.daqem.arc.data.condition.movement;

import com.daqem.arc.api.action.data.IActionDataType;
import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.api.math.INumberProvider;
import com.daqem.arc.api.math.INumberProviderSerializer;
import com.daqem.arc.api.player.ArcServerPlayer;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.data.math.ConstantNumberProvider;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class DistanceCondition extends AbstractCondition {

    private final INumberProvider distanceInBlocks;

    public DistanceCondition(boolean inverted, INumberProvider distanceInBlocks) {
        super(inverted);
        this.distanceInBlocks = distanceInBlocks;
    }

    @Override
    public Component getDescription() {
        return getDescription(distanceInBlocks.getDescription());
    }

    @Override
    public boolean isMet(ActionData actionData) {
        if (actionData.getPlayer() instanceof ArcServerPlayer accessor) {
            Double totalDistanceMovedInCm = actionData.getData(IActionDataType.DISTANCE_IN_CM);
            if (totalDistanceMovedInCm == null || totalDistanceMovedInCm < 0) return false;

            double resolvedDistance = this.distanceInBlocks.resolve(actionData);
            if (resolvedDistance <= 0) return false; // Prevent infinite loop on edge cases

            double lastAccountedDistanceCm = accessor.arc$getActionLastMetDistances().getOrDefault(this, 0.0);
            double requiredDistanceInCm = resolvedDistance * 100.0;
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

    public INumberProvider getDistanceInBlocks() {
        return distanceInBlocks;
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.DISTANCE;
    }

    public static class Serializer implements IConditionSerializer<DistanceCondition> {

        @Override
        public DistanceCondition fromJson(ResourceLocation location, JsonObject jsonObject, boolean inverted) {
            return new DistanceCondition(
                    inverted,
                    getNumberProvider(jsonObject, "distance_in_blocks", new ConstantNumberProvider(1.0))
            );
        }

        @Override
        public DistanceCondition fromNetwork(ResourceLocation location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new DistanceCondition(
                    inverted,
                    INumberProviderSerializer.fromNetworkStatic(friendlyByteBuf)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, DistanceCondition type) {
            IConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            INumberProviderSerializer.toNetwork(type.distanceInBlocks, friendlyByteBuf);
        }
    }
}