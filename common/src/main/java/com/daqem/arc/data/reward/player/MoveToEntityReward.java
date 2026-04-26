package com.daqem.arc.data.reward.player;

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

public class MoveToEntityReward extends AbstractReward {

    private final INumberProvider force;

    public MoveToEntityReward(double chance, int priority, INumberProvider force) {
        super(chance, priority);
        this.force = force;
    }

    @Override
    public Component getDescription() {
        return getDescription(force.toString());
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        Player player = actionData.getPlayer().arc$getPlayer();
        Entity entity = actionData.getData(IActionDataType.ENTITY);
        if (entity != null) {
            float resolvedForce = (float) force.resolve(actionData);
            player.setDeltaMovement((entity.position().x - player.position().x) / 2, resolvedForce, (entity.position().z - player.position().z) / 2);
            player.hurtMarked = true;
        }
        return new ActionResult();
    }

    public INumberProvider getForce() {
        return force;
    }

    @Override
    public IRewardType<?> getType() {
        return IRewardType.MOVE_TO_ENTITY;
    }

    public static class Serializer implements IRewardSerializer<MoveToEntityReward> {

        @Override
        public MoveToEntityReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new MoveToEntityReward(
                    chance,
                    priority,
                    getNumberProvider(jsonObject, "force", new ConstantNumberProvider(1.0))
            );
        }

        @Override
        public MoveToEntityReward fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf, double chance, int priority) {
            return new MoveToEntityReward(
                    chance,
                    priority,
                    INumberProviderSerializer.fromNetworkStatic(friendlyByteBuf)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, MoveToEntityReward type) {
            IRewardSerializer.super.toNetwork(friendlyByteBuf, type);
            INumberProviderSerializer.toNetwork(type.force, friendlyByteBuf);
        }
    }
}