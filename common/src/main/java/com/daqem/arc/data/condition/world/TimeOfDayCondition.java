package com.daqem.arc.data.condition.world;

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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class TimeOfDayCondition extends AbstractCondition {

    private final INumberProvider minTime;
    private final INumberProvider maxTime;

    public TimeOfDayCondition(boolean inverted, INumberProvider minTime, INumberProvider maxTime) {
        super(inverted);
        this.minTime = minTime;
        this.maxTime = maxTime;
    }

    @Override
    public Component getDescription() {
        return getDescription(minTime.getDescription(), maxTime.getDescription());
    }

    @Override
    public boolean isMet(ActionData actionData) {
        Level level = actionData.getPlayer().arc$getLevel();
        long time = level.getDayTime();

        double resolvedMin = minTime.resolve(actionData);
        double resolvedMax = maxTime.resolve(actionData);

        if (resolvedMin > resolvedMax) {
            double temp = resolvedMin;
            resolvedMin = resolvedMax;
            resolvedMax = temp;
        }

        return time >= resolvedMin && time <= resolvedMax;
    }

    public INumberProvider getMaxTime() {
        return maxTime;
    }

    public INumberProvider getMinTime() {
        return minTime;
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.TIME_OF_DAY;
    }

    public static class Serializer implements IConditionSerializer<TimeOfDayCondition> {

        @Override
        public TimeOfDayCondition fromJson(ResourceLocation location, JsonObject jsonObject, boolean inverted) {
            return new TimeOfDayCondition(
                    inverted,
                    getNumberProvider(jsonObject, "min_time", new ConstantNumberProvider(0.0)),
                    getNumberProvider(jsonObject, "max_time", new ConstantNumberProvider(24000.0))
            );
        }

        @Override
        public TimeOfDayCondition fromNetwork(ResourceLocation location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new TimeOfDayCondition(
                    inverted,
                    INumberProviderSerializer.fromNetworkStatic(friendlyByteBuf),
                    INumberProviderSerializer.fromNetworkStatic(friendlyByteBuf)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, TimeOfDayCondition type) {
            IConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            INumberProviderSerializer.toNetwork(type.minTime, friendlyByteBuf);
            INumberProviderSerializer.toNetwork(type.maxTime, friendlyByteBuf);
        }
    }
}