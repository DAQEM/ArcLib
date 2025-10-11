package com.daqem.arc.data.condition.entity;

import com.daqem.arc.data.ActionData;
import com.daqem.arc.api.action.data.IActionDataType;
import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;

public class HandCondition extends AbstractCondition {

    private final InteractionHand hand;

    public HandCondition(boolean inverted, InteractionHand hand) {
        super(inverted);
        this.hand = hand;
    }

    @Override
    public Component getDescription() {
        return getDescription(hand.name());
    }

    @Override
    public boolean isMet(ActionData actionData) {
        InteractionHand hand = actionData.getData(IActionDataType.HAND);
        return hand != null && this.hand == hand;
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.HAND;
    }

    public InteractionHand getHand() {
        return hand;
    }

    public static class Serializer implements IConditionSerializer<HandCondition> {

        @Override
        public HandCondition fromJson(ResourceLocation location, JsonObject jsonObject, boolean inverted) {
            return new HandCondition(
                    inverted,
                    getHand(jsonObject, "hand"));
        }

        @Override
        public HandCondition fromNetwork(ResourceLocation location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new HandCondition(
                    inverted,
                    friendlyByteBuf.readEnum(InteractionHand.class));
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, HandCondition type) {
            IConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeEnum(type.hand);
        }
    }
}
