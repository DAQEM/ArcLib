package com.daqem.arc.data.reward.world;

import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.math.INumberProvider;
import com.daqem.arc.api.math.INumberProviderSerializer;
import com.daqem.arc.api.reward.AbstractReward;
import com.daqem.arc.api.reward.IRewardSerializer;
import com.daqem.arc.api.reward.IRewardType;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.data.math.ConstantNumberProvider;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.GsonHelper;

public class ChangeTimeReward extends AbstractReward {

    private final INumberProvider time;
    private final boolean addTime;

    public ChangeTimeReward(double chance, int priority, INumberProvider time, boolean addTime) {
        super(chance, priority);
        this.time = time;
        this.addTime = addTime;
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        if (actionData.getPlayer().arc$getLevel() instanceof ServerLevel serverLevel) {
            int resolvedTime = (int) Math.round(time.resolve(actionData));
            if (addTime) {
                serverLevel.dimensionType().defaultClock().ifPresent(clock ->
                        serverLevel.clockManager().setTotalTicks(clock, serverLevel.getOverworldClockTime() + resolvedTime));
            } else {
                serverLevel.dimensionType().defaultClock().ifPresent(clock ->
                        serverLevel.clockManager().setTotalTicks(clock, resolvedTime));
            }
        }
        return new ActionResult();
    }

    public INumberProvider getTime() {
        return time;
    }

    public boolean shouldAddTime() {
        return addTime;
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
                    getNumberProvider(jsonObject, "time", new ConstantNumberProvider(0.0)),
                    GsonHelper.getAsBoolean(jsonObject, "add", false)
            );
        }

        @Override
        public ChangeTimeReward fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf, double chance, int priority) {
            return new ChangeTimeReward(
                    chance,
                    priority,
                    INumberProviderSerializer.fromNetworkStatic(friendlyByteBuf),
                    friendlyByteBuf.readBoolean()
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, ChangeTimeReward type) {
            IRewardSerializer.super.toNetwork(friendlyByteBuf, type);
            INumberProviderSerializer.toNetwork(type.time, friendlyByteBuf);
            friendlyByteBuf.writeBoolean(type.addTime);
        }
    }
}