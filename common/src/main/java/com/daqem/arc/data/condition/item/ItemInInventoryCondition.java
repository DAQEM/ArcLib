package com.daqem.arc.data.condition.item;

import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.data.ActionData;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;

public class ItemInInventoryCondition extends AbstractCondition {

    private final ItemStackTemplate itemStackTemplate;
    private ItemStack cachedItemStack;

    public ItemInInventoryCondition(boolean inverted, ItemStackTemplate itemStackTemplate) {
        super(inverted);
        this.itemStackTemplate = itemStackTemplate;
        this.cachedItemStack = null;
    }

    @Override
    public Component getDescription() {
        return getDescription(getItemStack().getHoverName());
    }

    @Override
    public boolean isMet(ActionData actionData) {
        Player player = actionData.getPlayer().arc$getPlayer();
        return player.getInventory().getNonEquipmentItems().stream().anyMatch(stack -> stack.getItem() == getItemStack().getItem());
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.ITEM_IN_INVENTORY;
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

    public static class Serializer implements IConditionSerializer<ItemInInventoryCondition> {

        @Override
        public ItemInInventoryCondition fromJson(Identifier location, JsonObject jsonObject, boolean inverted) {
            return new ItemInInventoryCondition(
                    inverted,
                    getItemStackTemplate(jsonObject, "item"));
        }

        @Override
        public ItemInInventoryCondition fromNetwork(Identifier location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new ItemInInventoryCondition(
                    inverted,
                    ItemStackTemplate.STREAM_CODEC.decode(friendlyByteBuf));
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, ItemInInventoryCondition type) {
            IConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            ItemStackTemplate.STREAM_CODEC.encode(friendlyByteBuf, type.itemStackTemplate);
        }
    }
}
