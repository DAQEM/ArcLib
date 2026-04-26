package com.daqem.arc.data.reward.block;

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

public class DestroySpeedMultiplierReward extends AbstractReward {

    private final INumberProvider multiplier;

    public DestroySpeedMultiplierReward(double chance, int priority, INumberProvider multiplier) {
        super(chance, priority);
        this.multiplier = multiplier;
    }

    @Override
    public Component getDescription() {
        return getDescription(multiplier.getDescription());
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        return new ActionResult().withDestroySpeedModifier((float) multiplier.resolve(actionData));
    }

    public INumberProvider getMultiplier() {
        return multiplier;
    }

    @Override
    public IRewardType<?> getType() {
        return IRewardType.DESTROY_SPEED_MULTIPLIER;
    }

    public static class Serializer implements IRewardSerializer<DestroySpeedMultiplierReward> {
        @Override
        public DestroySpeedMultiplierReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new DestroySpeedMultiplierReward(chance, priority, getNumberProvider(jsonObject, "multiplier", new ConstantNumberProvider(1.0)));
        }

        @Override
        public DestroySpeedMultiplierReward fromNetwork(RegistryFriendlyByteBuf buf, double chance, int priority) {
            return new DestroySpeedMultiplierReward(chance, priority, INumberProviderSerializer.fromNetworkStatic(buf));
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, DestroySpeedMultiplierReward type) {
            IRewardSerializer.super.toNetwork(buf, type);
            INumberProviderSerializer.toNetwork(type.multiplier, buf);
        }
    }
}