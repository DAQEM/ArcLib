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
import net.minecraft.world.item.ItemStackTemplate;

public class ItemReward extends AbstractReward {

    private final ItemStackTemplate itemStackTemplate;
    private ItemStack cachedItemStack;

    public ItemReward(double chance, int priority, ItemStackTemplate itemStackTemplate) {
        super(chance, priority);
        this.itemStackTemplate = itemStackTemplate;
        this.cachedItemStack = null;
    }

    @Override
    public Component getDescription() {
        return getDescription(getItemStack().getCount(), getItemStack().getHoverName());
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        ArcPlayer player = actionData.getPlayer();
        player.arc$getPlayer().addItem(getItemStack().copy());
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

    public int getAmount() {
        return getItemStack().getCount();
    }

    public static class Serializer implements IRewardSerializer<ItemReward> {

        @Override
        public ItemReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new ItemReward(chance, priority, getItemStackTemplate(jsonObject, "item"));
        }

        @Override
        public ItemReward fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf, double chance, int priority) {
            return new ItemReward(chance, priority, ItemStackTemplate.STREAM_CODEC.decode(friendlyByteBuf));
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, ItemReward type) {
            IRewardSerializer.super.toNetwork(friendlyByteBuf, type);
            ItemStackTemplate.STREAM_CODEC.encode(friendlyByteBuf, type.itemStackTemplate);
        }
    }
}
