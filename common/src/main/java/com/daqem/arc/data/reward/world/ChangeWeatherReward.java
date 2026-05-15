package com.daqem.arc.data.reward.world;

import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.math.INumberProvider;
import com.daqem.arc.api.math.INumberProviderSerializer;
import com.daqem.arc.api.reward.AbstractReward;
import com.daqem.arc.api.reward.IRewardSerializer;
import com.daqem.arc.api.reward.IRewardType;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.data.math.ConstantNumberProvider;
import com.daqem.arc.model.ArcWeatherType;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

public class ChangeWeatherReward extends AbstractReward {

    private final ArcWeatherType weatherType;
    private final INumberProvider duration;

    public ChangeWeatherReward(double chance, int priority, ArcWeatherType weatherType, INumberProvider duration) {
        super(chance, priority);
        this.weatherType = weatherType;
        this.duration = duration;
    }

    @Override
    public Component getDescription() {
        return super.getDescription(weatherType.toString(), duration.getDescription());
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        if (actionData.getPlayer().arc$getLevel() instanceof ServerLevel serverLevel) {
            int resolvedDuration = (int) Math.round(duration.resolve(actionData));
            switch (weatherType) {
                case CLEAR -> serverLevel.getServer().overworld().setWeatherParameters(resolvedDuration, 0, false, false);
                case RAIN -> serverLevel.getServer().overworld().setWeatherParameters(0, resolvedDuration, true, false);
                case THUNDER -> serverLevel.getServer().overworld().setWeatherParameters(0, resolvedDuration, true, true);
            }
        }
        return new ActionResult();
    }

    public ArcWeatherType getWeatherType() {
        return weatherType;
    }

    public INumberProvider getDuration() {
        return duration;
    }

    @Override
    public IRewardType<?> getType() {
        return IRewardType.CHANGE_WEATHER;
    }

    public static class Serializer implements IRewardSerializer<ChangeWeatherReward> {

        @Override
        public ChangeWeatherReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new ChangeWeatherReward(
                    chance,
                    priority,
                    getWeatherType(jsonObject, "weather"),
                    getNumberProvider(jsonObject, "duration", new ConstantNumberProvider(6000.0)) // Default 5 mins
            );
        }

        @Override
        public ChangeWeatherReward fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf, double chance, int priority) {
            return new ChangeWeatherReward(
                    chance,
                    priority,
                    friendlyByteBuf.readEnum(ArcWeatherType.class),
                    INumberProviderSerializer.fromNetworkStatic(friendlyByteBuf)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, ChangeWeatherReward type) {
            IRewardSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeEnum(type.weatherType);
            INumberProviderSerializer.toNetwork(type.duration, friendlyByteBuf);
        }
    }
}