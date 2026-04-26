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
import net.minecraft.world.entity.Entity;

public class EntityOnFireReward extends AbstractReward {

    private final INumberProvider fireTicks;

    public EntityOnFireReward(double chance, int priority, INumberProvider fireTicks) {
        super(chance, priority);
        this.fireTicks = fireTicks;
    }

    @Override
    public Component getDescription() {
        return getDescription(fireTicks.getDescription());
    }

    @Override
    public IRewardType<?> getType() {
        return IRewardType.ENTITY_ON_FIRE;
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        Entity entity = actionData.getData(IActionDataType.ENTITY);
        if (entity != null) {
            entity.setRemainingFireTicks((int) Math.round(fireTicks.resolve(actionData)));
        }
        return new ActionResult();
    }

    public INumberProvider getFireTicks() {
        return fireTicks;
    }

    public static class Serializer implements IRewardSerializer<EntityOnFireReward> {
        @Override
        public EntityOnFireReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new EntityOnFireReward(chance, priority, getNumberProvider(jsonObject, "ticks", new ConstantNumberProvider(0)));
        }

        @Override
        public EntityOnFireReward fromNetwork(RegistryFriendlyByteBuf buf, double chance, int priority) {
            return new EntityOnFireReward(chance, priority, INumberProviderSerializer.fromNetworkStatic(buf));
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, EntityOnFireReward type) {
            IRewardSerializer.super.toNetwork(buf, type);
            INumberProviderSerializer.toNetwork(type.fireTicks, buf);
        }
    }
}