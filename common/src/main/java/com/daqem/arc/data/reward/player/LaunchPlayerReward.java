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
import net.minecraft.world.entity.player.Player;

public class LaunchPlayerReward extends AbstractReward {

    private final INumberProvider forceX;
    private final INumberProvider forceY;
    private final INumberProvider forceZ;

    public LaunchPlayerReward(double chance, int priority, INumberProvider forceX, INumberProvider forceY, INumberProvider forceZ) {
        super(chance, priority);
        this.forceX = forceX;
        this.forceY = forceY;
        this.forceZ = forceZ;
    }

    @Override
    public Component getDescription() {
        return getDescription(String.format("X:%s, Y:%s, Z:%s", forceX.toString(), forceY.toString(), forceZ.toString()));
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        Player player = actionData.getPlayer().arc$getPlayer();
        player.push(forceX.resolve(actionData), forceY.resolve(actionData), forceZ.resolve(actionData));
        player.hurtMarked = true;
        return new ActionResult();
    }

    public INumberProvider getForceX() {
        return forceX;
    }

    public INumberProvider getForceY() {
        return forceY;
    }

    public INumberProvider getForceZ() {
        return forceZ;
    }

    @Override
    public IRewardType<?> getType() {
        return IRewardType.LAUNCH_PLAYER;
    }

    public static class Serializer implements IRewardSerializer<LaunchPlayerReward> {
        @Override
        public LaunchPlayerReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new LaunchPlayerReward(
                    chance, priority,
                    getNumberProvider(jsonObject, "x", new ConstantNumberProvider(0.0)),
                    getNumberProvider(jsonObject, "y", new ConstantNumberProvider(0.0)),
                    getNumberProvider(jsonObject, "z", new ConstantNumberProvider(0.0))
            );
        }

        @Override
        public LaunchPlayerReward fromNetwork(RegistryFriendlyByteBuf buf, double chance, int priority) {
            return new LaunchPlayerReward(chance, priority,
                    INumberProviderSerializer.fromNetworkStatic(buf),
                    INumberProviderSerializer.fromNetworkStatic(buf),
                    INumberProviderSerializer.fromNetworkStatic(buf)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, LaunchPlayerReward type) {
            IRewardSerializer.super.toNetwork(buf, type);
            INumberProviderSerializer.toNetwork(type.forceX, buf);
            INumberProviderSerializer.toNetwork(type.forceY, buf);
            INumberProviderSerializer.toNetwork(type.forceZ, buf);
        }
    }
}