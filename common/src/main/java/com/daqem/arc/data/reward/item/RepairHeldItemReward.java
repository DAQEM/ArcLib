package com.daqem.arc.data.reward.item;

import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.reward.AbstractReward;
import com.daqem.arc.api.reward.IRewardSerializer;
import com.daqem.arc.api.reward.IRewardType;
import com.daqem.arc.data.ActionData;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class RepairHeldItemReward extends AbstractReward {

    private final int amount;
    private final boolean isPercentage;
    private final InteractionHand hand;

    public RepairHeldItemReward(double chance, int priority, int amount, boolean isPercentage, InteractionHand hand) {
        super(chance, priority);
        this.amount = amount;
        this.isPercentage = isPercentage;
        this.hand = hand;
    }

    @Override
    public Component getDescription() {
        return getDescription(amount + (isPercentage ? "%" : ""), hand.name());
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        Player player = actionData.getPlayer().arc$getPlayer();
        ItemStack stack = player.getItemInHand(hand);

        if (stack.isDamageableItem()) {
            int repairAmount = amount;
            if (isPercentage) {
                repairAmount = (int) (stack.getMaxDamage() * (amount / 100.0));
            }
            stack.setDamageValue(Math.max(0, stack.getDamageValue() - repairAmount));
        }
        return new ActionResult();
    }

    @Override
    public IRewardType<?> getType() {
        return IRewardType.REPAIR_HELD_ITEM;
    }

    public static class Serializer implements IRewardSerializer<RepairHeldItemReward> {

        @Override
        public RepairHeldItemReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new RepairHeldItemReward(
                    chance,
                    priority,
                    GsonHelper.getAsInt(jsonObject, "amount"),
                    GsonHelper.getAsBoolean(jsonObject, "is_percentage", false),
                    getHand(jsonObject, "hand", InteractionHand.MAIN_HAND)
            );
        }

        @Override
        public RepairHeldItemReward fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf, double chance, int priority) {
            return new RepairHeldItemReward(
                    chance,
                    priority,
                    friendlyByteBuf.readVarInt(),
                    friendlyByteBuf.readBoolean(),
                    friendlyByteBuf.readEnum(InteractionHand.class)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, RepairHeldItemReward type) {
            IRewardSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeVarInt(type.amount);
            friendlyByteBuf.writeBoolean(type.isPercentage);
            friendlyByteBuf.writeEnum(type.hand);
        }
    }
}