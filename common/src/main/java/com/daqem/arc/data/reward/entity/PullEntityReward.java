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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class PullEntityReward extends AbstractReward {

    private final INumberProvider force;

    public PullEntityReward(double chance, int priority, INumberProvider force) {
        super(chance, priority);
        this.force = force;
    }

    @Override
    public Component getDescription() {
        return super.getDescription(force.getDescription());
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        Player player = actionData.getPlayer().arc$getPlayer();
        Entity target = actionData.getData(IActionDataType.ENTITY);
        if (target != null) {
            double resolvedForce = force.resolve(actionData);
            Vec3 pullVector = player.position().subtract(target.position()).normalize().scale(resolvedForce);
            target.push(pullVector.x, pullVector.y, pullVector.z);
        }
        return new ActionResult();
    }

    public INumberProvider getForce() {
        return force;
    }

    @Override
    public IRewardType<?> getType() {
        return IRewardType.PULL_ENTITY;
    }

    public static class Serializer implements IRewardSerializer<PullEntityReward> {

        @Override
        public PullEntityReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new PullEntityReward(
                    chance,
                    priority,
                    getNumberProvider(jsonObject, "force", new ConstantNumberProvider(1.0))
            );
        }

        @Override
        public PullEntityReward fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf, double chance, int priority) {
            return new PullEntityReward(
                    chance,
                    priority,
                    INumberProviderSerializer.fromNetworkStatic(friendlyByteBuf)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, PullEntityReward type) {
            IRewardSerializer.super.toNetwork(friendlyByteBuf, type);
            INumberProviderSerializer.toNetwork(type.force, friendlyByteBuf);
        }
    }
}