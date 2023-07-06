package com.daqem.arc.data.reward.item;

import com.daqem.arc.api.action.data.ActionData;
import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.player.ArcPlayer;
import com.daqem.arc.api.reward.AbstractReward;
import com.daqem.arc.api.reward.serializer.IRewardSerializer;
import com.daqem.arc.api.reward.serializer.RewardSerializer;
import com.daqem.arc.api.reward.type.IRewardType;
import com.daqem.arc.api.reward.type.RewardType;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

public class ItemReward extends AbstractReward {

    private final ItemStack itemStack;

    public ItemReward(double chance, ItemStack itemStack) {
        super(chance);
        this.itemStack = itemStack;
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

    @Override
    public IRewardSerializer<?> getSerializer() {
        return RewardSerializer.ITEM;
    }

    public static class Serializer implements RewardSerializer<ItemReward> {

        @Override
        public ItemReward fromJson(JsonObject jsonObject, double chance) {
            ItemStack itemStack = getItemStack(jsonObject, "item");
            CompoundTag tag = getCompoundTag(jsonObject);
            if (tag != null) itemStack.setTag(tag);
            return new ItemReward(chance, itemStack);
        }

        @Override
        public ItemReward fromNetwork(FriendlyByteBuf friendlyByteBuf, double chance) {
            return new ItemReward(chance, friendlyByteBuf.readItem());
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, ItemReward type) {
            RewardSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeItem(type.itemStack);
        }
    }
}
