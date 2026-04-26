package com.daqem.arc.data.reward.entity;

import com.daqem.arc.api.action.data.IActionDataType;
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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class FreezeEntityReward extends AbstractReward {

    private final INumberProvider duration;

    public FreezeEntityReward(double chance, int priority, INumberProvider duration) {
        super(chance, priority);
        this.duration = duration;
    }

    @Override
    public Component getDescription() {
        return super.getDescription(duration.getDescription());
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        if (actionData.getData(IActionDataType.ENTITY) instanceof LivingEntity target) {
            int resolvedDuration = (int) Math.round(duration.resolve(actionData));
            target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, resolvedDuration, 255, false, false));
        }
        return new ActionResult();
    }

    @Override
    public IRewardType<?> getType() {
        return IRewardType.FREEZE_ENTITY;
    }

    public static class Serializer implements IRewardSerializer<FreezeEntityReward> {

        @Override
        public FreezeEntityReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new FreezeEntityReward(
                    chance,
                    priority,
                    getNumberProvider(jsonObject, "duration", new ConstantNumberProvider(100.0))
            );
        }

        @Override
        public FreezeEntityReward fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf, double chance, int priority) {
            return new FreezeEntityReward(
                    chance,
                    priority,
                    INumberProviderSerializer.fromNetworkStatic(friendlyByteBuf)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, FreezeEntityReward type) {
            IRewardSerializer.super.toNetwork(friendlyByteBuf, type);
            INumberProviderSerializer.toNetwork(type.duration, friendlyByteBuf);
        }
    }
}