package com.daqem.arc.data.action.movement;

import com.daqem.arc.api.action.AbstractAction;
import com.daqem.arc.api.action.holder.type.IActionHolderType;
import com.daqem.arc.api.action.serializer.ActionSerializer;
import com.daqem.arc.api.action.serializer.IActionSerializer;
import com.daqem.arc.api.action.type.ActionType;
import com.daqem.arc.api.action.type.IActionType;
import com.daqem.arc.api.condition.ICondition;
import com.daqem.arc.api.reward.IReward;
import com.google.gson.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class ElytraFlyStartAction extends AbstractAction {

    public ElytraFlyStartAction(ResourceLocation location, ResourceLocation actionHolderLocation, IActionHolderType<?> actionHolderType, boolean performOnClient, List<IReward> rewards, List<ICondition> conditions) {
        super(location, actionHolderLocation, actionHolderType, performOnClient, rewards, conditions);
    }

    @Override
    public IActionType<?> getType() {
        return ActionType.ELYTRA_FLY_START;
    }

    @Override
    public IActionSerializer<?> getSerializer() {
        return ActionSerializer.ELYTRA_FLY_START;
    }

    public static class Serializer implements ActionSerializer<ElytraFlyStartAction> {

        @Override
        public ElytraFlyStartAction fromJson(ResourceLocation location, JsonObject jsonObject, ResourceLocation actionHolderLocation, IActionHolderType<?> actionHolderType, boolean performOnClient, List<IReward> rewards, List<ICondition> conditions) {
            return new ElytraFlyStartAction(location, actionHolderLocation, actionHolderType, performOnClient, rewards, conditions);
        }

        @Override
        public ElytraFlyStartAction fromNetwork(ResourceLocation location, FriendlyByteBuf friendlyByteBuf, ResourceLocation actionHolderLocation, IActionHolderType<?> actionHolderType, boolean performOnClient, List<IReward> rewards, List<ICondition> conditions) {
            return new ElytraFlyStartAction(location, actionHolderLocation, actionHolderType, performOnClient, rewards, conditions);
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, ElytraFlyStartAction type) {
            ActionSerializer.super.toNetwork(friendlyByteBuf, type);
        }
    }
}
