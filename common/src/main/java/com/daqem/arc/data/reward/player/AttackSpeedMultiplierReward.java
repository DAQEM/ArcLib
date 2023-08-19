package com.daqem.arc.data.reward.player;

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

public class AttackSpeedMultiplierReward extends AbstractReward {

    private final float multiplier;

    public AttackSpeedMultiplierReward(double chance, float multiplier) {
        super(chance);
        this.multiplier = multiplier;
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        return new ActionResult().withAttackSpeedModifier(multiplier);
    }

    @Override
    public IRewardType<?> getType() {
        return RewardType.ATTACK_SPEED_MULTIPLIER;
    }

    @Override
    public IRewardSerializer<?> getSerializer() {
        return RewardSerializer.ATTACK_SPEED_MULTIPLIER;
    }

    public static class Serializer implements RewardSerializer<AttackSpeedMultiplierReward> {

        @Override
        public AttackSpeedMultiplierReward fromJson(JsonObject jsonObject, double chance) {
            return new AttackSpeedMultiplierReward(
                    chance,
                    GsonHelper.getAsFloat(jsonObject, "multiplier"));
        }

        @Override
        public AttackSpeedMultiplierReward fromNetwork(FriendlyByteBuf friendlyByteBuf, double chance) {
            return new AttackSpeedMultiplierReward(
                    chance,
                    friendlyByteBuf.readFloat());
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, AttackSpeedMultiplierReward type) {
            RewardSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeFloat(type.multiplier);
        }
    }
}
