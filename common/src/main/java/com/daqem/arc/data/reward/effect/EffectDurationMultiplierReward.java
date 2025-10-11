package com.daqem.arc.data.reward.effect;

import com.daqem.arc.data.ActionData;
import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.action.data.IActionDataType;
import com.daqem.arc.api.player.ArcServerPlayer;
import com.daqem.arc.api.reward.AbstractReward;
import com.daqem.arc.api.reward.IRewardSerializer;
import com.daqem.arc.api.reward.IRewardType;
import com.google.gson.*;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;

public class EffectDurationMultiplierReward extends AbstractReward {

    private final double multiplier;

    public EffectDurationMultiplierReward(double chance, int priority, double multiplier) {
        super(chance, priority);
        this.multiplier = multiplier;
    }

    @Override
    public Component getDescription() {
        return getDescription(multiplier);
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        MobEffectInstance effect = actionData.getData(IActionDataType.MOB_EFFECT_INSTANCE);
        if (effect != null) {
            if (actionData.getPlayer() instanceof ArcServerPlayer player) {
                MobEffectInstance newEffect = new MobEffectInstance(effect.getEffect(), Mth.floor(effect.getDuration() * multiplier), effect.getAmplifier(), effect.isAmbient(), effect.isVisible());
                try {
                    player.arc$setApplyingRewardEffect(true);
                    player.arc$getPlayer().addEffect(newEffect);
                } finally {
                    player.arc$setApplyingRewardEffect(false);
                }
            }
        }
        return new ActionResult();
    }

    @Override
    public IRewardType<?> getType() {
        return IRewardType.EFFECT_DURATION_MULTIPLIER;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public static class Serializer implements IRewardSerializer<EffectDurationMultiplierReward> {

        @Override
        public EffectDurationMultiplierReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new EffectDurationMultiplierReward(
                    chance,
                    priority,
                    GsonHelper.getAsDouble(jsonObject, "multiplier"));
        }

        @Override
        public EffectDurationMultiplierReward fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf, double chance, int priority) {
            return new EffectDurationMultiplierReward(
                    chance,
                    priority,
                    friendlyByteBuf.readDouble());
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, EffectDurationMultiplierReward type) {
            IRewardSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeDouble(type.multiplier);
        }
    }
}
