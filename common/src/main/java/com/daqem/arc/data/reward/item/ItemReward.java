package com.daqem.arc.data.reward.item;

import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.math.INumberProvider;
import com.daqem.arc.api.math.INumberProviderSerializer;
import com.daqem.arc.api.player.ArcPlayer;
import com.daqem.arc.api.reward.AbstractReward;
import com.daqem.arc.api.reward.IRewardSerializer;
import com.daqem.arc.api.reward.IRewardType;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.data.math.ConstantNumberProvider;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class ItemReward extends AbstractReward {

    private final ItemStack itemStack;
    private final INumberProvider amount;

    public ItemReward(double chance, int priority, ItemStack itemStack, INumberProvider amount) {
        super(chance, priority);
        this.itemStack = itemStack;
        this.amount = amount;
    }

    @Override
    public Component getDescription() {
        return getDescription(amount.getDescription(), getItemStack().getHoverName());
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        ArcPlayer player = actionData.getPlayer();

        int resolvedAmount = (int) Math.round(amount.resolve(actionData));
        if (resolvedAmount > 0) {
            ItemStack stack = getItemStack();
            stack.setCount(resolvedAmount);
            player.arc$getPlayer().addItem(stack);
        }

        return new ActionResult();
    }

    @Override
    public IRewardType<?> getType() {
        return IRewardType.ITEM;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public static class Serializer implements IRewardSerializer<ItemReward> {

        @Override
        public ItemReward fromJson(JsonObject jsonObject, double chance, int priority) {
            ItemStack itemStack = getItemStack(jsonObject, "item");
            int itemStackCount = itemStack.getCount();

            return new ItemReward(
                    chance,
                    priority,
                    itemStack,
                    getNumberProvider(jsonObject, "amount", new ConstantNumberProvider(itemStackCount))
            );
        }

        @Override
        public ItemReward fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf, double chance, int priority) {
            return new ItemReward(
                    chance,
                    priority,
                    ItemStack.STREAM_CODEC.decode(friendlyByteBuf),
                    INumberProviderSerializer.fromNetworkStatic(friendlyByteBuf)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, ItemReward type) {
            IRewardSerializer.super.toNetwork(friendlyByteBuf, type);
            ItemStack.STREAM_CODEC.encode(friendlyByteBuf, type.itemStack);
            INumberProviderSerializer.toNetwork(type.amount, friendlyByteBuf);
        }
    }
}