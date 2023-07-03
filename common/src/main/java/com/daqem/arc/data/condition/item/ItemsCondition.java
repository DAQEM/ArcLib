package com.daqem.arc.data.condition.item;

import com.daqem.arc.api.action.data.ActionData;
import com.daqem.arc.api.action.data.type.ActionDataType;
import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.serializer.ConditionSerializer;
import com.daqem.arc.api.condition.serializer.IConditionSerializer;
import com.daqem.arc.api.condition.type.ConditionType;
import com.daqem.arc.api.condition.type.IConditionType;
import com.google.gson.*;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemsCondition extends AbstractCondition {

    private final List<Item> items;
    private final List<TagKey<Item>> itemTags;


    public ItemsCondition(boolean inverted, List<Item> items, List<TagKey<Item>> itemTags) {
        super(inverted);
        this.items = items;
        this.itemTags = itemTags;
    }

    @Override
    public boolean isMet(ActionData actionData) {
        ItemStack itemStack = actionData.getData(ActionDataType.ITEM_STACK);
        if (itemStack == null) {
            Item item = actionData.getData(ActionDataType.ITEM);
            if (item != null) {
                itemStack = item.getDefaultInstance();
            }
        }
        return itemStack != null && (isItem(itemStack) || isItemByTag(itemStack));
    }

    @Override
    public IConditionType<?> getType() {
        return ConditionType.ITEMS;
    }

    @Override
    public IConditionSerializer<?> getSerializer() {
        return ConditionSerializer.ITEMS;
    }

    private boolean isItem(ItemStack itemStack) {
        return this.items.contains(itemStack.getItem());
    }

    private boolean isItemByTag(ItemStack itemStack) {
        return this.itemTags.stream().anyMatch(itemStack::is);
    }

    public static class Serializer implements ConditionSerializer<ItemsCondition> {

        @Override
        public ItemsCondition fromJson(ResourceLocation location, JsonObject jsonObject, boolean inverted) {
            return new ItemsCondition(
                    inverted,
                    getItems(jsonObject, "items"),
                    getItemTags(jsonObject, "items"));
        }

        @Override
        public ItemsCondition fromNetwork(ResourceLocation location, FriendlyByteBuf friendlyByteBuf, boolean inverted) {
            int itemCount = friendlyByteBuf.readVarInt();
            int tagCount = friendlyByteBuf.readVarInt();

            List<Item> items = new ArrayList<>();
            List<TagKey<Item>> itemTags = new ArrayList<>();

            for (int i = 0; i < itemCount; i++) {
                items.add(friendlyByteBuf.readById(Registry.ITEM));
            }

            for (int i = 0; i < tagCount; i++) {
                itemTags.add(TagKey.create(Registry.ITEM.key(), friendlyByteBuf.readResourceLocation()));
            }


            return new ItemsCondition(
                    inverted,
                    items,
                    itemTags);
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, ItemsCondition type) {
            ConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeVarInt(type.items.size());
            friendlyByteBuf.writeVarInt(type.itemTags.size());
            type.items.forEach(item -> friendlyByteBuf.writeId(Registry.ITEM, item));
            type.itemTags.forEach(tag -> friendlyByteBuf.writeResourceLocation(tag.location()));
        }
    }
}
