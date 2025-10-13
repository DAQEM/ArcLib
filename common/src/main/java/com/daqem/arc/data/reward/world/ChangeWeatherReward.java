package com.daqem.arc.data.reward.world;

import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.reward.AbstractReward;
import com.daqem.arc.api.reward.IRewardSerializer;
import com.daqem.arc.api.reward.IRewardType;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.model.ArcWeatherType;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.GsonHelper;

public class ChangeWeatherReward extends AbstractReward {

    private final ArcWeatherType weatherType;
    private final int duration; // Duration in ticks

    public ChangeWeatherReward(double chance, int priority, ArcWeatherType weatherType, int duration) {
        super(chance, priority);
        this.weatherType = weatherType;
        this.duration = duration;
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        if (actionData.getPlayer().arc$getLevel() instanceof ServerLevel serverLevel) {
            switch (weatherType) {
                case CLEAR -> serverLevel.setWeatherParameters(duration, 0, false, false);
                case RAIN -> serverLevel.setWeatherParameters(0, duration, true, false);
                case THUNDER -> serverLevel.setWeatherParameters(0, duration, true, true);
            }
        }
        return new ActionResult();
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
                    GsonHelper.getAsInt(jsonObject, "duration", 6000) // Default to 5 minutes (6000 ticks)
            );
        }

        @Override
        public ChangeWeatherReward fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf, double chance, int priority) {
            return new ChangeWeatherReward(
                    chance,
                    priority,
                    friendlyByteBuf.readEnum(ArcWeatherType.class),
                    friendlyByteBuf.readVarInt()
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, ChangeWeatherReward type) {
            IRewardSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeEnum(type.weatherType);
            friendlyByteBuf.writeVarInt(type.duration);
        }
    }
}