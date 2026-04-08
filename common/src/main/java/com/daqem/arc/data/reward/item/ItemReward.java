package com.daqem.arc.data.reward.item;

import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.player.ArcPlayer;
import com.daqem.arc.api.reward.AbstractReward;
import com.daqem.arc.api.reward.IRewardSerializer;
import com.daqem.arc.api.reward.IRewardType;
import com.daqem.arc.data.ActionData;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class ItemReward extends AbstractReward {

    private final ItemStack itemStack;

    public ItemReward(double chance, int priority, ItemStack itemStack) {
        super(chance, priority);
        this.itemStack = itemStack;
    }

    @Override
    public Component getDescription() {
        return getDescription(itemStack.getCount(), itemStack.getHoverName());
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        ArcPlayer player = actionData.getPlayer();
        player.arc$getPlayer().addItem(itemStack.copy());
        return new ActionResult();
    }

    @Override
    public IRewardType<?> getType() {
        return IRewardType.ITEM;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public int getAmount() {
        return itemStack.getCount();
    }

    public static class Serializer implements IRewardSerializer<ItemReward> {

        @Override
        public ItemReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new ItemReward(chance, priority, getItemStack(jsonObject, "item"));
        }

        @Override
        public ItemReward fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf, double chance, int priority) {
            return new ItemReward(chance, priority, ItemStack.STREAM_CODEC.decode(friendlyByteBuf));
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, ItemReward type) {
            IRewardSerializer.super.toNetwork(friendlyByteBuf, type);
            ItemStack.STREAM_CODEC.encode(friendlyByteBuf, type.itemStack);
        }
    }
}
