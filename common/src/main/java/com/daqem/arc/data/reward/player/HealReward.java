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
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;

public class HealReward extends AbstractReward {
    private final INumberProvider amount;
    private final boolean isPercentage;

    public HealReward(double chance, int priority, INumberProvider amount, boolean isPercentage) {
        super(chance, priority);
        this.amount = amount;
        this.isPercentage = isPercentage;
    }

    @Override
    public Component getDescription() {
        return getDescription(amount.getDescription(), (isPercentage ? "%" : " hearts"));
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        Player player = actionData.getPlayer().arc$getPlayer();
        float resolvedAmount = (float) amount.resolve(actionData);
        if (isPercentage) {
            resolvedAmount = player.getMaxHealth() * (resolvedAmount / 100F);
        }
        player.heal(resolvedAmount);
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
                    chance, priority,
                    getNumberProvider(jsonObject, "amount", new ConstantNumberProvider(1.0)),
                    GsonHelper.getAsBoolean(jsonObject, "is_percentage", false)
            );
        }

        @Override
        public HealReward fromNetwork(RegistryFriendlyByteBuf buf, double chance, int priority) {
            return new HealReward(chance, priority, INumberProviderSerializer.fromNetworkStatic(buf), buf.readBoolean());
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, HealReward type) {
            IRewardSerializer.super.toNetwork(buf, type);
            INumberProviderSerializer.toNetwork(type.amount, buf);
            buf.writeBoolean(type.isPercentage);
        }
    }
}