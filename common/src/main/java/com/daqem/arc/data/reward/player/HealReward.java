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

public class HealReward extends AbstractReward {

    private final float amount;
    private final boolean isPercentage;

    public HealReward(double chance, int priority, float amount, boolean isPercentage) {
        super(chance, priority);
        this.amount = amount;
        this.isPercentage = isPercentage;
    }

    @Override
    public Component getDescription() {
        return getDescription(amount + (isPercentage ? "%" : " hearts"));
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        Player player = actionData.getPlayer().arc$getPlayer();
        float healAmount = amount;
        if (isPercentage) {
            healAmount = player.getMaxHealth() * (amount / 100F);
        }
        player.heal(healAmount);
        return new ActionResult();
    }

    @Override
    public IRewardType<?> getType() {
        return IRewardType.HEAL;
    }

    public static class Serializer implements IRewardSerializer<HealReward> {

        @Override
        public HealReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new HealReward(
                    chance,
                    priority,
                    GsonHelper.getAsFloat(jsonObject, "amount"),
                    GsonHelper.getAsBoolean(jsonObject, "is_percentage", false)
            );
        }

        @Override
        public HealReward fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf, double chance, int priority) {
            return new HealReward(
                    chance,
                    priority,
                    friendlyByteBuf.readFloat(),
                    friendlyByteBuf.readBoolean()
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, HealReward type) {
            IRewardSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeFloat(type.amount);
            friendlyByteBuf.writeBoolean(type.isPercentage);
        }
    }
}