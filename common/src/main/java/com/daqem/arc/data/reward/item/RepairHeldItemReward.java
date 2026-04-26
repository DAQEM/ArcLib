package com.daqem.arc.data.reward.item;

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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class RepairHeldItemReward extends AbstractReward {

    private final INumberProvider amount;
    private final boolean isPercentage;
    private final InteractionHand hand;

    public RepairHeldItemReward(double chance, int priority, INumberProvider amount, boolean isPercentage, InteractionHand hand) {
        super(chance, priority);
        this.amount = amount;
        this.isPercentage = isPercentage;
        this.hand = hand;
    }

    @Override
    public Component getDescription() {
        return getDescription(amount.getDescription(), (isPercentage ? "%" : ""), hand.name());
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        Player player = actionData.getPlayer().arc$getPlayer();
        ItemStack stack = player.getItemInHand(hand);

        if (stack.isDamageableItem()) {
            double resolvedAmount = amount.resolve(actionData);
            int repairAmount = (int) Math.round(resolvedAmount);
            if (isPercentage) {
                repairAmount = (int) (stack.getMaxDamage() * (resolvedAmount / 100.0));
            }
            stack.setDamageValue(Math.max(0, stack.getDamageValue() - repairAmount));
        }
        return new ActionResult();
    }

    public INumberProvider getAmount() {
        return amount;
    }

    public InteractionHand getHand() {
        return hand;
    }

    public boolean isPercentage() {
        return isPercentage;
    }

    @Override
    public IRewardType<?> getType() {
        return IRewardType.REPAIR_HELD_ITEM;
    }

    public static class Serializer implements IRewardSerializer<RepairHeldItemReward> {
        @Override
        public RepairHeldItemReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new RepairHeldItemReward(
                    chance, priority,
                    getNumberProvider(jsonObject, "amount", new ConstantNumberProvider(1.0)),
                    GsonHelper.getAsBoolean(jsonObject, "is_percentage", false),
                    getHand(jsonObject, "hand", InteractionHand.MAIN_HAND)
            );
        }

        @Override
        public RepairHeldItemReward fromNetwork(RegistryFriendlyByteBuf buf, double chance, int priority) {
            return new RepairHeldItemReward(
                    chance, priority,
                    INumberProviderSerializer.fromNetworkStatic(buf),
                    buf.readBoolean(),
                    buf.readEnum(InteractionHand.class)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, RepairHeldItemReward type) {
            IRewardSerializer.super.toNetwork(buf, type);
            INumberProviderSerializer.toNetwork(type.amount, buf);
            buf.writeBoolean(type.isPercentage);
            buf.writeEnum(type.hand);
        }
    }
}