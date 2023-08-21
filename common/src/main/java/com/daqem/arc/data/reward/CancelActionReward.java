package com.daqem.arc.data.reward;

import com.daqem.arc.api.action.data.ActionData;
import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.reward.AbstractReward;
import com.daqem.arc.api.reward.serializer.IRewardSerializer;
import com.daqem.arc.api.reward.serializer.RewardSerializer;
import com.daqem.arc.api.reward.type.IRewardType;
import com.daqem.arc.api.reward.type.RewardType;
import com.google.gson.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class CancelActionReward extends AbstractReward {

    public CancelActionReward(double chance, int priority) {
        super(chance, priority);
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        return new ActionResult().withCancelAction(true);
    }

    @Override
    public IRewardType<?> getType() {
        return RewardType.CANCEL_ACTION;
    }

    @Override
    public IRewardSerializer<?> getSerializer() {
        return RewardSerializer.CANCEL_ACTION;
    }

    public static class Serializer implements RewardSerializer<CancelActionReward> {

        @Override
        public CancelActionReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new CancelActionReward(chance, priority);
        }

        @Override
        public CancelActionReward fromNetwork(FriendlyByteBuf friendlyByteBuf, double chance, int priority) {
            return new CancelActionReward(chance, priority);
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, CancelActionReward type) {
            RewardSerializer.super.toNetwork(friendlyByteBuf, type);
        }
    }
}
