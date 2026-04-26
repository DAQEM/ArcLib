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
import net.minecraft.world.item.ItemStackTemplate;

public class ItemReward extends AbstractReward {

    private final ItemStackTemplate itemStackTemplate;
    private final INumberProvider amount;
    private ItemStack cachedItemStack;

    public ItemReward(double chance, int priority, ItemStackTemplate itemStackTemplate, INumberProvider amount) {
        super(chance, priority);
        this.itemStackTemplate = itemStackTemplate;
        this.amount = amount;
        this.cachedItemStack = null;
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
            ItemStack stack = getItemStackTemplate().create();
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
        if (cachedItemStack != null) {
            return cachedItemStack;
        }
        this.cachedItemStack = itemStackTemplate.create();
        return cachedItemStack;
    }

    public ItemStackTemplate getItemStackTemplate() {
        return itemStackTemplate;
    }

    public static class Serializer implements IRewardSerializer<ItemReward> {

        @Override
        public ItemReward fromJson(JsonObject jsonObject, double chance, int priority) {
            ItemStackTemplate template = getItemStackTemplate(jsonObject, "item");
            int templateCount = template.count();

            return new ItemReward(
                    chance,
                    priority,
                    template,
                    getNumberProvider(jsonObject, "amount", new ConstantNumberProvider(templateCount))
            );
        }

        @Override
        public ItemReward fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf, double chance, int priority) {
            return new ItemReward(
                    chance,
                    priority,
                    ItemStackTemplate.STREAM_CODEC.decode(friendlyByteBuf),
                    INumberProviderSerializer.fromNetworkStatic(friendlyByteBuf)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, ItemReward type) {
            IRewardSerializer.super.toNetwork(friendlyByteBuf, type);
            ItemStackTemplate.STREAM_CODEC.encode(friendlyByteBuf, type.itemStackTemplate);
            INumberProviderSerializer.toNetwork(type.amount, friendlyByteBuf);
        }
    }
}