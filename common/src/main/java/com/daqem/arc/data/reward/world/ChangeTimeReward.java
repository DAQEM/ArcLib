package com.daqem.arc.data.reward.world;

import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.reward.AbstractReward;
import com.daqem.arc.api.reward.IRewardSerializer;
import com.daqem.arc.api.reward.IRewardType;
import com.daqem.arc.data.ActionData;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.GsonHelper;

public class ChangeTimeReward extends AbstractReward {

    private final int time;
    private final boolean addTime;

    public ChangeTimeReward(double chance, int priority, int time, boolean addTime) {
        super(chance, priority);
        this.time = time;
        this.addTime = addTime;
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        if (actionData.getPlayer().arc$getLevel() instanceof ServerLevel serverLevel) {
            if (addTime) {
                serverLevel.setDayTime(serverLevel.getDayTime() + time);
            } else {
                serverLevel.setDayTime(time);
            }
        }
        return new ActionResult();
    }

    @Override
    public IRewardType<?> getType() {
        return IRewardType.CHANGE_TIME;
    }

    public static class Serializer implements IRewardSerializer<ChangeTimeReward> {

        @Override
        public ChangeTimeReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new ChangeTimeReward(
                    chance,
                    priority,
                    GsonHelper.getAsInt(jsonObject, "time"),
                    GsonHelper.getAsBoolean(jsonObject, "add", false)
            );
        }

        @Override
        public ChangeTimeReward fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf, double chance, int priority) {
            return new ChangeTimeReward(
                    chance,
                    priority,
                    friendlyByteBuf.readVarInt(),
                    friendlyByteBuf.readBoolean()
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, ChangeTimeReward type) {
            IRewardSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeVarInt(type.time);
            friendlyByteBuf.writeBoolean(type.addTime);
        }
    }
}