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

public class FeedReward extends AbstractReward {

    private final INumberProvider foodLevel;
    private final INumberProvider saturation;

    public FeedReward(double chance, int priority, INumberProvider foodLevel, INumberProvider saturation) {
        super(chance, priority);
        this.foodLevel = foodLevel;
        this.saturation = saturation;
    }

    @Override
    public Component getDescription() {
        return getDescription(foodLevel.toString(), saturation.toString());
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        Player player = actionData.getPlayer().arc$getPlayer();
        player.getFoodData().eat((int) Math.round(foodLevel.resolve(actionData)), (float) saturation.resolve(actionData));
        return new ActionResult();
    }

    public INumberProvider getFoodLevel() {
        return foodLevel;
    }

    public INumberProvider getSaturation() {
        return saturation;
    }

    @Override
    public IRewardType<?> getType() {
        return IRewardType.FEED;
    }

    public static class Serializer implements IRewardSerializer<FeedReward> {
        @Override
        public FeedReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new FeedReward(
                    chance, priority,
                    getNumberProvider(jsonObject, "food_level", new ConstantNumberProvider(0)),
                    getNumberProvider(jsonObject, "saturation", new ConstantNumberProvider(0.0))
            );
        }

        @Override
        public FeedReward fromNetwork(RegistryFriendlyByteBuf buf, double chance, int priority) {
            return new FeedReward(chance, priority, INumberProviderSerializer.fromNetworkStatic(buf), INumberProviderSerializer.fromNetworkStatic(buf));
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, FeedReward type) {
            IRewardSerializer.super.toNetwork(buf, type);
            INumberProviderSerializer.toNetwork(type.foodLevel, buf);
            INumberProviderSerializer.toNetwork(type.saturation, buf);
        }
    }
}