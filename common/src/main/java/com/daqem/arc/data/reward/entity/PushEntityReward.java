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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class PushEntityReward extends AbstractReward {

    private final INumberProvider force;

    public PushEntityReward(double chance, int priority, INumberProvider force) {
        super(chance, priority);
        this.force = force;
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        Player player = actionData.getPlayer().arc$getPlayer();
        Entity target = actionData.getData(IActionDataType.ENTITY);
        if (target != null) {
            double resolvedForce = force.resolve(actionData);
            Vec3 pushVector = target.position().subtract(player.position()).normalize().scale(resolvedForce);
            target.push(pushVector.x, pushVector.y, pushVector.z);
        }
        return new ActionResult();
    }

    public INumberProvider getForce() {
        return force;
    }

    @Override
    public IRewardType<?> getType() {
        return IRewardType.PUSH_ENTITY;
    }

    public static class Serializer implements IRewardSerializer<PushEntityReward> {

        @Override
        public PushEntityReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new PushEntityReward(
                    chance,
                    priority,
                    getNumberProvider(jsonObject, "force", new ConstantNumberProvider(1.0))
            );
        }

        @Override
        public PushEntityReward fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf, double chance, int priority) {
            return new PushEntityReward(
                    chance,
                    priority,
                    INumberProviderSerializer.fromNetworkStatic(friendlyByteBuf)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, PushEntityReward type) {
            IRewardSerializer.super.toNetwork(friendlyByteBuf, type);
            INumberProviderSerializer.toNetwork(type.force, friendlyByteBuf);
        }
    }
}