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

public class FeedReward extends AbstractReward {

    private final int foodLevel;
    private final float saturation;

    public FeedReward(double chance, int priority, int foodLevel, float saturation) {
        super(chance, priority);
        this.foodLevel = foodLevel;
        this.saturation = saturation;
    }

    @Override
    public Component getDescription() {
        return getDescription(foodLevel, String.format("%.1f", saturation));
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        Player player = actionData.getPlayer().arc$getPlayer();
        player.getFoodData().eat(foodLevel, saturation);
        return new ActionResult();
    }

    @Override
    public IRewardType<?> getType() {
        return IRewardType.FEED;
    }

    public static class Serializer implements IRewardSerializer<FeedReward> {

        @Override
        public FeedReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new FeedReward(
                    chance,
                    priority,
                    GsonHelper.getAsInt(jsonObject, "food_level", 0),
                    GsonHelper.getAsFloat(jsonObject, "saturation", 0F)
            );
        }

        @Override
        public FeedReward fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf, double chance, int priority) {
            return new FeedReward(
                    chance,
                    priority,
                    friendlyByteBuf.readVarInt(),
                    friendlyByteBuf.readFloat()
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, FeedReward type) {
            IRewardSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeVarInt(type.foodLevel);
            friendlyByteBuf.writeFloat(type.saturation);
        }
    }
}