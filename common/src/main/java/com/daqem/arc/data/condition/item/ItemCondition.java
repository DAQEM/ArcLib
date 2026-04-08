package com.daqem.arc.data.condition.item;

import com.daqem.arc.api.action.data.IActionDataType;
import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.data.ActionData;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;

public class ItemCondition extends AbstractCondition {

    private final ItemStackTemplate itemStackTemplate;
    private ItemStack cachedItemStack;
    private final boolean checkComponents;

    public ItemCondition(boolean inverted, ItemStackTemplate itemStackTemplate, boolean checkComponents) {
        super(inverted);
        this.itemStackTemplate = itemStackTemplate;
        this.cachedItemStack = null;
        this.checkComponents = checkComponents;
    }

    @Override
    public Component getDescription() {
        return getDescription(getItemStack().getHoverName());
    }

    @Override
    public boolean isMet(ActionData actionData) {
        Item item = actionData.getData(IActionDataType.ITEM);
        ItemStack itemStack = actionData.getData(IActionDataType.ITEM_STACK);
        boolean hasItem = item != null || itemStack != null;
        boolean passOnItem = item != null && item == getItemStack().getItem();
        boolean passOnItemStack = itemStack != null && testItemStack(itemStack);
        return hasItem && (passOnItem || passOnItemStack);
    }

    private boolean testItemStack(ItemStack itemStack) {
        boolean sameItem = ItemStack.isSameItem(getItemStack(), itemStack);
        boolean hasCount = getItemStack().getCount() > 1;
        boolean sameCount = itemStack.getCount() == getItemStack().getCount();
        boolean passOnCount = !hasCount || sameCount;
        boolean hasComponents = !checkComponents || !getItemStack().getComponents().isEmpty();
        boolean sameComponents = !checkComponents || ItemStack.isSameItemSameComponents(getItemStack(), itemStack);
        boolean passOnComponents = !checkComponents || !hasComponents || sameComponents;
        return sameItem && passOnCount && passOnComponents;
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.ITEM;
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

    public boolean isCheckComponents() {
        return checkComponents;
    }

    public static class Serializer implements IConditionSerializer<ItemCondition> {

        @Override
        public ItemCondition fromJson(Identifier location, JsonObject jsonObject, boolean inverted) {
            return new ItemCondition(
                    inverted,
                    getItemStackTemplate(jsonObject, "item"),
                    GsonHelper.getAsBoolean(jsonObject, "check_components", true));
        }

        @Override
        public ItemCondition fromNetwork(Identifier location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new ItemCondition(
                    inverted,
                    ItemStackTemplate.STREAM_CODEC.decode(friendlyByteBuf),
                    friendlyByteBuf.readBoolean());
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, ItemCondition type) {
            IConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            ItemStackTemplate.STREAM_CODEC.encode(friendlyByteBuf, type.itemStackTemplate);
            friendlyByteBuf.writeBoolean(type.checkComponents);
        }
    }
}
