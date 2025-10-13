package com.daqem.arc.data.reward.player;

import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.reward.AbstractReward;
import com.daqem.arc.api.reward.IRewardSerializer;
import com.daqem.arc.api.reward.IRewardType;
import com.daqem.arc.data.ActionData;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;

public class LaunchPlayerReward extends AbstractReward {

    private final float forceX;
    private final float forceY;
    private final float forceZ;

    public LaunchPlayerReward(double chance, int priority, float forceX, float forceY, float forceZ) {
        super(chance, priority);
        this.forceX = forceX;
        this.forceY = forceY;
        this.forceZ = forceZ;
    }

    @Override
    public Component getDescription() {
        return getDescription(String.format("X:%.1f, Y:%.1f, Z:%.1f", forceX, forceY, forceZ));
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        Player player = actionData.getPlayer().arc$getPlayer();
        player.push(forceX, forceY, forceZ);
        player.hurtMarked = true;
        return new ActionResult();
    }

    @Override
    public IRewardType<?> getType() {
        return IRewardType.LAUNCH_PLAYER;
    }

    public static class Serializer implements IRewardSerializer<LaunchPlayerReward> {

        @Override
        public LaunchPlayerReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new LaunchPlayerReward(
                    chance,
                    priority,
                    GsonHelper.getAsFloat(jsonObject, "x", 0F),
                    GsonHelper.getAsFloat(jsonObject, "y", 0F),
                    GsonHelper.getAsFloat(jsonObject, "z", 0F)
            );
        }

        @Override
        public LaunchPlayerReward fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf, double chance, int priority) {
            return new LaunchPlayerReward(
                    chance,
                    priority,
                    friendlyByteBuf.readFloat(),
                    friendlyByteBuf.readFloat(),
                    friendlyByteBuf.readFloat()
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, LaunchPlayerReward type) {
            IRewardSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeFloat(type.forceX);
            friendlyByteBuf.writeFloat(type.forceY);
            friendlyByteBuf.writeFloat(type.forceZ);
        }
    }
}