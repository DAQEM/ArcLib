package com.daqem.arc.data.reward.effect;

import com.daqem.arc.api.action.data.IActionDataType;
import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.reward.AbstractReward;
import com.daqem.arc.api.reward.IRewardSerializer;
import com.daqem.arc.api.reward.IRewardType;
import com.daqem.arc.data.ActionData;
import com.google.gson.JsonObject;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;

public class RemoveEffectReward extends AbstractReward {

    public RemoveEffectReward(double chance, int priority) {
        super(chance, priority);
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        ActionResult result = new ActionResult();
        MobEffectInstance effectInstance = actionData.getData(IActionDataType.MOB_EFFECT_INSTANCE);
        if (effectInstance == null) return result;
        Holder<MobEffect> effect = effectInstance.getEffect();
        Player player = actionData.getPlayer().arc$getPlayer();

        player.getActiveEffectsMap().keySet()
                .stream()
                .filter(e -> e.is(effect))
                .findFirst()
                .ifPresent(player::removeEffect);

        return result.withCancelEffect(effect);

    }

    @Override
    public IRewardType<?> getType() {
        return IRewardType.REMOVE_EFFECT;
    }

    public static class Serializer implements IRewardSerializer<RemoveEffectReward> {

        @Override
        public RemoveEffectReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new RemoveEffectReward(chance, priority);
        }

        @Override
        public RemoveEffectReward fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf, double chance, int priority) {
            return new RemoveEffectReward(chance, priority);
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, RemoveEffectReward type) {
            IRewardSerializer.super.toNetwork(friendlyByteBuf, type);
        }
    }
}
