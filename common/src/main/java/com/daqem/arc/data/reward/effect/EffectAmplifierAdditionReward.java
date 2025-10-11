package com.daqem.arc.data.reward.effect;

import com.daqem.arc.data.ActionData;
import com.daqem.arc.api.action.data.IActionDataType;
import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.player.ArcServerPlayer;
import com.daqem.arc.api.reward.AbstractReward;
import com.daqem.arc.api.reward.IRewardSerializer;
import com.daqem.arc.api.reward.IRewardType;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;

public class EffectAmplifierAdditionReward extends AbstractReward {

    private final int addition;

    public EffectAmplifierAdditionReward(double chance, int priority, int addition) {
        super(chance, priority);
        this.addition = addition;
    }

    @Override
    public Component getDescription() {
        return getDescription(addition);
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        MobEffectInstance effect = actionData.getData(IActionDataType.MOB_EFFECT_INSTANCE);
        if (effect != null) {
            if (actionData.getPlayer() instanceof ArcServerPlayer player) {
                MobEffectInstance newEffect = new MobEffectInstance(effect.getEffect(), effect.getDuration(), Mth.floor(effect.getAmplifier() + addition), effect.isAmbient(), effect.isVisible());
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
        return IRewardType.EFFECT_AMPLIFIER_ADDITION;
    }

    public int getAddition() {
        return addition;
    }

    public static class Serializer implements IRewardSerializer<EffectAmplifierAdditionReward> {

        @Override
        public EffectAmplifierAdditionReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new EffectAmplifierAdditionReward(
                    chance,
                    priority,
                    GsonHelper.getAsInt(jsonObject, "addition"));
        }

        @Override
        public EffectAmplifierAdditionReward fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf, double chance, int priority) {
            return new EffectAmplifierAdditionReward(
                    chance,
                    priority,
                    friendlyByteBuf.readInt());
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, EffectAmplifierAdditionReward type) {
            IRewardSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeInt(type.addition);
        }
    }
}
