package com.daqem.arc.data.reward.effect;

import com.daqem.arc.data.ActionData;
import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.player.ArcPlayer;
import com.daqem.arc.api.reward.AbstractReward;
import com.daqem.arc.api.reward.IRewardSerializer;
import com.daqem.arc.api.reward.IRewardType;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;

public class EffectReward extends AbstractReward {

    private final MobEffectInstance effectInstance;

    public EffectReward(double chance, int priority, MobEffectInstance effectInstance) {
        super(chance, priority);
        this.effectInstance = effectInstance;
    }

    @Override
    public Component getDescription() {
        return getDescription(effectInstance.getEffect().value().getDisplayName(), MobEffectUtil.formatDuration(effectInstance, 1.0F, 20.0F), effectInstance.getAmplifier() + 1);
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        ArcPlayer player = actionData.getPlayer();
        player.arc$getPlayer().addEffect(new MobEffectInstance(effectInstance));
        return new ActionResult();
    }

    @Override
    public IRewardType<?> getType() {
        return IRewardType.EFFECT;
    }

    public MobEffect getEffect() {
        return effectInstance.getEffect().value();
    }

    public int getDuration() {
        return effectInstance.getDuration();
    }

    public int getAmplifier() {
        return effectInstance.getAmplifier();
    }

    public static class Serializer implements IRewardSerializer<EffectReward> {

        @Override
        public EffectReward fromJson(JsonObject jsonObject, double chance, int priority) {
            MobEffectInstance mobEffectInstance = getMobEffectInstance(jsonObject, "effect");
            return new EffectReward(
                    chance,
                    priority,
                    mobEffectInstance
            );
        }

        @Override
        public EffectReward fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf, double chance, int priority) {
            return new EffectReward(
                    chance,
                    priority,
                    MobEffectInstance.STREAM_CODEC.decode(friendlyByteBuf)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, EffectReward type) {
            IRewardSerializer.super.toNetwork(friendlyByteBuf, type);
            MobEffectInstance.STREAM_CODEC.encode(friendlyByteBuf, type.effectInstance);
        }
    }
}
