package com.daqem.arc.data.condition.world;

import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.data.ActionData;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.Level;

public class TimeOfDayCondition extends AbstractCondition {

    private final int minTime;
    private final int maxTime;

    public TimeOfDayCondition(boolean inverted, int minTime, int maxTime) {
        super(inverted);
        this.minTime = minTime;
        this.maxTime = maxTime;
    }

    @Override
    public Component getDescription() {
        return getDescription(minTime, maxTime);
    }

    @Override
    public boolean isMet(ActionData actionData) {
        Level level = actionData.getPlayer().arc$getLevel();
        long time = level.getDayTime() % 24000;
        return time >= minTime && time <= maxTime;
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.TIME_OF_DAY;
    }

    public static class Serializer implements IConditionSerializer<TimeOfDayCondition> {

        @Override
        public TimeOfDayCondition fromJson(Identifier location, JsonObject jsonObject, boolean inverted) {
            return new TimeOfDayCondition(
                    inverted,
                    GsonHelper.getAsInt(jsonObject, "min_time"),
                    GsonHelper.getAsInt(jsonObject, "max_time")
            );
        }

        @Override
        public TimeOfDayCondition fromNetwork(Identifier location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new TimeOfDayCondition(
                    inverted,
                    friendlyByteBuf.readVarInt(),
                    friendlyByteBuf.readVarInt()
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, TimeOfDayCondition type) {
            IConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeVarInt(type.minTime);
            friendlyByteBuf.writeVarInt(type.maxTime);
        }
    }
}