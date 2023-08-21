package com.daqem.arc.data.reward.effect;

import com.daqem.arc.api.action.data.ActionData;
import com.daqem.arc.api.player.ArcPlayer;
import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.reward.AbstractReward;
import com.daqem.arc.api.reward.serializer.IRewardSerializer;
import com.daqem.arc.api.reward.serializer.RewardSerializer;
import com.daqem.arc.api.reward.type.IRewardType;
import com.daqem.arc.api.reward.type.RewardType;
import com.google.gson.*;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

public class EffectReward extends AbstractReward {

    private final MobEffect effect;
    private final int duration;

    public EffectReward(double chance, int priority, MobEffect effect, int duration) {
        super(chance, priority);
        this.effect = effect;
        this.duration = duration;
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        ArcPlayer player = actionData.getPlayer();
        player.arc$getPlayer().addEffect(new MobEffectInstance(effect, duration));
        return new ActionResult();
    }

    @Override
    public IRewardType<?> getType() {
        return RewardType.EFFECT;
    }

    @Override
    public IRewardSerializer<?> getSerializer() {
        return RewardSerializer.EFFECT;
    }

    public static class Serializer implements RewardSerializer<EffectReward> {

        @Override
        public EffectReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new EffectReward(
                    chance,
                    priority,
                    getMobEffect(jsonObject, "effect"),
                    GsonHelper.getAsInt(jsonObject, "duration"));
        }

        @Override
        public EffectReward fromNetwork(FriendlyByteBuf friendlyByteBuf, double chance, int priority) {
            return new EffectReward(
                    chance,
                    priority,
                    Registry.MOB_EFFECT.byId(friendlyByteBuf.readVarInt()),
                    friendlyByteBuf.readVarInt());
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, EffectReward type) {
            RewardSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeVarInt(Registry.MOB_EFFECT.getId(type.effect));
            friendlyByteBuf.writeVarInt(type.duration);
        }
    }
}
