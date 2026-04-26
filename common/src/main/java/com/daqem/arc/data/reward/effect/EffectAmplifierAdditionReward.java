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

public class EffectAmplifierAdditionReward extends AbstractReward {

    private final INumberProvider addition;

    public EffectAmplifierAdditionReward(double chance, int priority, INumberProvider addition) {
        super(chance, priority);
        this.addition = addition;
    }

    @Override
    public Component getDescription() {
        return getDescription(addition.toString());
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        MobEffectInstance effect = actionData.getData(IActionDataType.MOB_EFFECT_INSTANCE);
        if (effect != null) {
            int resolvedAddition = (int) Math.round(addition.resolve(actionData));
            var newAmplifier = Mth.clamp(effect.getAmplifier() + resolvedAddition, 0, 255);
            ((MobEffectInstanceAccessor) effect).arc$setAmplifier(newAmplifier);
        }
        return new ActionResult();
    }

    public INumberProvider getAddition() {
        return addition;
    }

    @Override
    public IRewardType<?> getType() {
        return IRewardType.EFFECT_AMPLIFIER_ADDITION;
    }

    public static class Serializer implements IRewardSerializer<EffectAmplifierAdditionReward> {
        @Override
        public EffectAmplifierAdditionReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new EffectAmplifierAdditionReward(chance, priority, getNumberProvider(jsonObject, "addition", new ConstantNumberProvider(0)));
        }

        @Override
        public EffectAmplifierAdditionReward fromNetwork(RegistryFriendlyByteBuf buf, double chance, int priority) {
            return new EffectAmplifierAdditionReward(chance, priority, INumberProviderSerializer.fromNetworkStatic(buf));
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, EffectAmplifierAdditionReward type) {
            IRewardSerializer.super.toNetwork(buf, type);
            INumberProviderSerializer.toNetwork(type.addition, buf);
        }
    }
}