package com.daqem.arc.data.reward.combat;

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
import net.minecraft.network.chat.Component;

public class DamageMultiplierReward extends AbstractReward {

    private final INumberProvider multiplier;

    public DamageMultiplierReward(double chance, int priority, INumberProvider multiplier) {
        super(chance, priority);
        this.multiplier = multiplier;
    }

    @Override
    public Component getDescription() {
        return getDescription(multiplier.toString());
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        return new ActionResult().withDamageModifier((float) multiplier.resolve(actionData));
    }

    public INumberProvider getMultiplier() {
        return multiplier;
    }

    @Override
    public IRewardType<?> getType() {
        return IRewardType.DAMAGE_MULTIPLIER;
    }

    public static class Serializer implements IRewardSerializer<DamageMultiplierReward> {
        @Override
        public DamageMultiplierReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new DamageMultiplierReward(chance, priority, getNumberProvider(jsonObject, "multiplier", new ConstantNumberProvider(1.0)));
        }

        @Override
        public DamageMultiplierReward fromNetwork(RegistryFriendlyByteBuf buf, double chance, int priority) {
            return new DamageMultiplierReward(chance, priority, INumberProviderSerializer.fromNetworkStatic(buf));
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, DamageMultiplierReward type) {
            IRewardSerializer.super.toNetwork(buf, type);
            INumberProviderSerializer.toNetwork(type.multiplier, buf);
        }
    }
}