package com.daqem.arc.data.reward.item;

import com.daqem.arc.api.action.data.ActionData;
import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.player.ArcPlayer;
import com.daqem.arc.api.reward.AbstractReward;
import com.daqem.arc.api.reward.serializer.IRewardSerializer;
import com.daqem.arc.api.reward.type.IRewardType;
import com.daqem.arc.api.reward.type.RewardType;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;

public class ItemReward extends AbstractReward {

    private final ItemStack itemStack;
    private final int amount;

    public ItemReward(double chance, int priority, ItemStack itemStack, int amount) {
        super(chance, priority);
        this.itemStack = itemStack;
        this.amount = amount;
    }

    @Override
    public Component getDescription() {
        return getDescription(amount, itemStack.getHoverName());
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        ArcPlayer player = actionData.getPlayer();
        player.arc$getPlayer().addItem(itemStack.copy());
        return new ActionResult();
    }

    @Override
    public IRewardType<?> getType() {
        return RewardType.ITEM;
    }

    public static class Serializer implements IRewardSerializer<ItemReward> {

        @Override
        public ItemReward fromJson(JsonObject jsonObject, double chance, int priority) {
            ItemStack itemStack = getItemStack(jsonObject, "item");
            int amount = GsonHelper.getAsInt(jsonObject, "amount", 1);
            itemStack.setCount(amount);
            CompoundTag tag = getCompoundTag(jsonObject);
            if (tag != null) itemStack.setTag(tag);
            return new ItemReward(chance, priority, itemStack, amount);
        }

        @Override
        public ItemReward fromNetwork(FriendlyByteBuf friendlyByteBuf, double chance, int priority) {
            return new ItemReward(chance, priority, friendlyByteBuf.readItem(), friendlyByteBuf.readInt());
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, ItemReward type) {
            IRewardSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeItem(type.itemStack);
            friendlyByteBuf.writeInt(type.amount);
        }
    }
}
