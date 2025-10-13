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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class PushEntityReward extends AbstractReward {

    private final double force;

    public PushEntityReward(double chance, int priority, double force) {
        super(chance, priority);
        this.force = force;
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        Player player = actionData.getPlayer().arc$getPlayer();
        Entity target = actionData.getData(IActionDataType.ENTITY);
        if (target != null) {
            Vec3 pushVector = target.position().subtract(player.position()).normalize().scale(force);
            target.push(pushVector.x, pushVector.y, pushVector.z);
        }
        return new ActionResult();
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
                    GsonHelper.getAsDouble(jsonObject, "force", 1.0)
            );
        }

        @Override
        public PushEntityReward fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf, double chance, int priority) {
            return new PushEntityReward(
                    chance,
                    priority,
                    friendlyByteBuf.readDouble()
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, PushEntityReward type) {
            IRewardSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeDouble(type.force);
        }
    }
}