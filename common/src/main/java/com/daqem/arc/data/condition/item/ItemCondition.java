package com.daqem.arc.data.condition.item;

import com.daqem.arc.api.action.data.ActionData;
import com.daqem.arc.api.action.data.type.ActionDataType;
import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.serializer.IConditionSerializer;
import com.daqem.arc.api.condition.type.ConditionType;
import com.daqem.arc.api.condition.type.IConditionType;
import com.google.gson.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemCondition extends AbstractCondition {

    private final ItemStack itemStack;

    public ItemCondition(boolean inverted, ItemStack itemStack) {
        super(inverted);
        this.itemStack = itemStack;
    }

    @Override
    public Component getDescription() {
        return getDescription(itemStack.getHoverName());
    }

    @Override
    public boolean isMet(ActionData actionData) {
        Item item = actionData.getData(ActionDataType.ITEM);
        if (item == null) {
            ItemStack itemStack = actionData.getData(ActionDataType.ITEM_STACK);
            if (itemStack != null) {
                item = itemStack.getItem();
            }
        }
        return item != null && item == this.itemStack.getItem();
    }

    @Override
    public IConditionType<?> getType() {
        return ConditionType.ITEM;
    }

    public static class Serializer implements IConditionSerializer<ItemCondition> {

        @Override
        public ItemCondition fromJson(ResourceLocation location, JsonObject jsonObject, boolean inverted) {
            return new ItemCondition(
                    inverted,
                    getItemStack(jsonObject, "item"));
        }

        @Override
        public ItemCondition fromNetwork(ResourceLocation location, FriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new ItemCondition(
                    inverted,
                    friendlyByteBuf.readItem());
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, ItemCondition type) {
            IConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeItem(type.itemStack);
        }
    }
}
