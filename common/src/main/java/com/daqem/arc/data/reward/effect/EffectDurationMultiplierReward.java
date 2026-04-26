package com.daqem.arc.data.reward.effect;

import com.daqem.arc.api.action.data.IActionDataType;
import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.math.INumberProvider;
import com.daqem.arc.api.math.INumberProviderSerializer;
import com.daqem.arc.api.reward.AbstractReward;
import com.daqem.arc.api.reward.IRewardSerializer;
import com.daqem.arc.api.reward.IRewardType;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.data.math.ConstantNumberProvider;
import com.daqem.arc.mixin.MobEffectInstanceAccessor;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;

public class EffectDurationMultiplierReward extends AbstractReward {

    private final INumberProvider multiplier;

    public EffectDurationMultiplierReward(double chance, int priority, INumberProvider multiplier) {
        super(chance, priority);
        this.multiplier = multiplier;
    }

    @Override
    public Component getDescription() {
        return getDescription(multiplier.getDescription());
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        MobEffectInstance effect = actionData.getData(IActionDataType.MOB_EFFECT_INSTANCE);
        if (effect != null) {
            int newDuration = Mth.ceil(effect.getDuration() * multiplier.resolve(actionData));
            ((MobEffectInstanceAccessor) effect).arc$setDuration(newDuration);
        }
        return new ActionResult();
    }

    public INumberProvider getMultiplier() {
        return multiplier;
    }

    @Override
    public IRewardType<?> getType() {
        return IRewardType.EFFECT_DURATION_MULTIPLIER;
    }

    public static class Serializer implements IRewardSerializer<EffectDurationMultiplierReward> {
        @Override
        public EffectDurationMultiplierReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new EffectDurationMultiplierReward(chance, priority, getNumberProvider(jsonObject, "multiplier", new ConstantNumberProvider(1.0)));
        }

        @Override
        public EffectDurationMultiplierReward fromNetwork(RegistryFriendlyByteBuf buf, double chance, int priority) {
            return new EffectDurationMultiplierReward(chance, priority, INumberProviderSerializer.fromNetworkStatic(buf));
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, EffectDurationMultiplierReward type) {
            IRewardSerializer.super.toNetwork(buf, type);
            INumberProviderSerializer.toNetwork(type.multiplier, buf);
        }
    }
}