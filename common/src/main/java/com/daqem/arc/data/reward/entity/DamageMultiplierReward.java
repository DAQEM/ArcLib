package com.daqem.arc.data.reward.entity;

import com.daqem.arc.api.action.data.ActionData;
import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.reward.AbstractReward;
import com.daqem.arc.api.reward.serializer.IRewardSerializer;
import com.daqem.arc.api.reward.serializer.RewardSerializer;
import com.daqem.arc.api.reward.type.IRewardType;
import com.daqem.arc.api.reward.type.RewardType;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;

public class DamageMultiplierReward extends AbstractReward {

    private final double multiplier;

    public DamageMultiplierReward(double chance, int priority, double multiplier) {
        super(chance, priority);
        this.multiplier = multiplier;
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        return new ActionResult().withDamageModifier((float) multiplier);
    }

    @Override
    public IRewardType<?> getType() {
        return RewardType.DAMAGE_MULTIPLIER;
    }

    @Override
    public IRewardSerializer<?> getSerializer() {
        return RewardSerializer.DAMAGE_MULTIPLIER;
    }

    public static class Serializer implements RewardSerializer<DamageMultiplierReward> {

        @Override
        public DamageMultiplierReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new DamageMultiplierReward(
                    chance,
                    priority,
                    GsonHelper.getAsDouble(jsonObject, "multiplier"));
        }

        @Override
        public DamageMultiplierReward fromNetwork(FriendlyByteBuf friendlyByteBuf, double chance, int priority) {
            return new DamageMultiplierReward(
                    chance,
                    priority,
                    friendlyByteBuf.readDouble());
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, DamageMultiplierReward type) {
            RewardSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeDouble(type.multiplier);
        }
    }
}
