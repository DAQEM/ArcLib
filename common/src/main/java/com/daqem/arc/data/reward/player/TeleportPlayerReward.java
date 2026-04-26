package com.daqem.arc.data.reward.player;

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
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;

public class TeleportPlayerReward extends AbstractReward {

    private final INumberProvider radius;

    public TeleportPlayerReward(double chance, int priority, INumberProvider radius) {
        super(chance, priority);
        this.radius = radius;
    }

    @Override
    public Component getDescription() {
        return getDescription(radius.getDescription());
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        if (actionData.getPlayer().arc$getPlayer() instanceof ServerPlayer player) {
            RandomSource random = player.getRandom();
            double resRadius = radius.resolve(actionData);
            double x = player.getX() + (random.nextDouble() - 0.5D) * resRadius * 2.0D;
            double y = player.getY() + (random.nextInt((int)Math.round(resRadius * 2)) - resRadius);
            double z = player.getZ() + (random.nextDouble() - 0.5D) * resRadius * 2.0D;
            player.teleportTo(x, y, z);
        }
        return new ActionResult();
    }

    public INumberProvider getRadius() {
        return radius;
    }

    @Override
    public IRewardType<?> getType() {
        return IRewardType.TELEPORT_PLAYER;
    }

    public static class Serializer implements IRewardSerializer<TeleportPlayerReward> {
        @Override
        public TeleportPlayerReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new TeleportPlayerReward(chance, priority, getNumberProvider(jsonObject, "radius", new ConstantNumberProvider(0)));
        }

        @Override
        public TeleportPlayerReward fromNetwork(RegistryFriendlyByteBuf buf, double chance, int priority) {
            return new TeleportPlayerReward(chance, priority, INumberProviderSerializer.fromNetworkStatic(buf));
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, TeleportPlayerReward type) {
            IRewardSerializer.super.toNetwork(buf, type);
            INumberProviderSerializer.toNetwork(type.radius, buf);
        }
    }
}