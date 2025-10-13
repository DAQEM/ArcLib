package com.daqem.arc.data.reward.player;

import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.reward.AbstractReward;
import com.daqem.arc.api.reward.IRewardSerializer;
import com.daqem.arc.api.reward.IRewardType;
import com.daqem.arc.data.ActionData;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;

public class TeleportPlayerReward extends AbstractReward {

    private final int radius;

    public TeleportPlayerReward(double chance, int priority, int radius) {
        super(chance, priority);
        this.radius = radius;
    }

    @Override
    public Component getDescription() {
        return getDescription(radius);
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        if (actionData.getPlayer().arc$getPlayer() instanceof ServerPlayer player) {
            RandomSource random = player.getRandom();
            double x = player.getX() + (random.nextDouble() - 0.5D) * radius * 2.0D;
            double y = player.getY() + (random.nextInt(radius * 2) - radius);
            double z = player.getZ() + (random.nextDouble() - 0.5D) * radius * 2.0D;
            player.teleportTo(x, y, z);
        }
        return new ActionResult();
    }

    @Override
    public IRewardType<?> getType() {
        return IRewardType.TELEPORT_PLAYER;
    }

    public static class Serializer implements IRewardSerializer<TeleportPlayerReward> {

        @Override
        public TeleportPlayerReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new TeleportPlayerReward(
                    chance,
                    priority,
                    GsonHelper.getAsInt(jsonObject, "radius")
            );
        }

        @Override
        public TeleportPlayerReward fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf, double chance, int priority) {
            return new TeleportPlayerReward(
                    chance,
                    priority,
                    friendlyByteBuf.readVarInt()
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, TeleportPlayerReward type) {
            IRewardSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeVarInt(type.radius);
        }
    }
}