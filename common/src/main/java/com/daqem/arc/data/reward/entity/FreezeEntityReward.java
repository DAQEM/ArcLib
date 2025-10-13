package com.daqem.arc.data.reward.entity;

import com.daqem.arc.api.action.data.IActionDataType;
import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.reward.AbstractReward;
import com.daqem.arc.api.reward.IRewardSerializer;
import com.daqem.arc.api.reward.IRewardType;
import com.daqem.arc.data.ActionData;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class FreezeEntityReward extends AbstractReward {

    private final int duration; // Duration in ticks

    public FreezeEntityReward(double chance, int priority, int duration) {
        super(chance, priority);
        this.duration = duration;
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        if (actionData.getData(IActionDataType.ENTITY) instanceof LivingEntity target) {
            target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, duration, 255, false, false));
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
                    GsonHelper.getAsInt(jsonObject, "duration", 100)
            );
        }

        @Override
        public FreezeEntityReward fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf, double chance, int priority) {
            return new FreezeEntityReward(
                    chance,
                    priority,
                    friendlyByteBuf.readVarInt()
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, FreezeEntityReward type) {
            IRewardSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeVarInt(type.duration);
        }
    }
}