package com.daqem.arc.data.condition.item;

import com.daqem.arc.Arc;
import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.data.ActionData;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import org.jetbrains.annotations.Nullable;

public class ItemInHandCondition extends AbstractCondition {

    private final ItemStackTemplate itemStackTemplate;
    private ItemStack cachedItemStack;
    @Nullable
    private final InteractionHand hand;

    public ItemInHandCondition(boolean inverted, ItemStackTemplate itemStackTemplate, @Nullable InteractionHand hand) {
        super(inverted);
        this.itemStackTemplate = itemStackTemplate;
        this.cachedItemStack = null;
        this.hand = hand;
    }

    @Override
    public Component getDescription() {
        return getDescription(getItemStack().getHoverName(), hand == null ? Arc.API.translatable("hand.any") : Arc.API.translatable("hand." + hand.name().toLowerCase()));
    }

    @Override
    public boolean isMet(ActionData actionData) {
        Player player = actionData.getPlayer().arc$getPlayer();
        Item targetItem = getItemStack().getItem();

        if (hand == null) {
            // Check both hands when no specific hand is defined
            return player.getMainHandItem().getItem() == targetItem
                    || player.getOffhandItem().getItem() == targetItem;
        }

        // Check only the specified hand
        return player.getItemInHand(hand).getItem() == targetItem;
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.ITEM_IN_HAND;
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

    @Nullable
    public InteractionHand getHand() {
        return hand;
    }

    public static class Serializer implements IConditionSerializer<ItemInHandCondition> {

        @Override
        public ItemInHandCondition fromJson(Identifier location, JsonObject jsonObject, boolean inverted) {
            return new ItemInHandCondition(
                    inverted,
                    getItemStackTemplate(jsonObject,"item"),
                    getOptionalHand(jsonObject, "hand")
            );
        }

        @Override
        public ItemInHandCondition fromNetwork(Identifier location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            ItemStackTemplate itemStackTemplate = ItemStackTemplate.STREAM_CODEC.decode(friendlyByteBuf);
            InteractionHand hand = friendlyByteBuf.readBoolean() ? friendlyByteBuf.readEnum(InteractionHand.class) : null;
            return new ItemInHandCondition(
                    inverted,
                    itemStackTemplate,
                    hand
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, ItemInHandCondition type) {
            IConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            ItemStackTemplate.STREAM_CODEC.encode(friendlyByteBuf, type.itemStackTemplate);
            friendlyByteBuf.writeBoolean(type.hand != null);
            if (type.hand != null) {
                friendlyByteBuf.writeEnum(type.hand);
            }
        }
    }
}
