package com.daqem.arc.data.action.entity;

import com.daqem.arc.api.action.AbstractAction;
import com.daqem.arc.api.action.IActionSerializer;
import com.daqem.arc.api.action.IActionType;
import com.daqem.arc.api.action.holder.IActionHolderType;
import com.daqem.arc.api.condition.ICondition;
import com.daqem.arc.api.reward.IReward;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class TradeWithVillagerAction extends AbstractAction {

    public TradeWithVillagerAction(ResourceLocation location, ResourceLocation actionHolderLocation, IActionHolderType<?> actionHolderType, List<IReward> rewards, List<ICondition> conditions) {
        super(location, actionHolderLocation, actionHolderType, rewards, conditions);
    }

    @Override
    public IActionType<?> getType() {
        return IActionType.TRADE_WITH_VILLAGER;
    }

    public static class Serializer implements IActionSerializer<TradeWithVillagerAction> {

        @Override
        public TradeWithVillagerAction fromJson(ResourceLocation location, JsonObject jsonObject, ResourceLocation actionHolderLocation, IActionHolderType<?> actionHolderType, List<IReward> rewards, List<ICondition> conditions) {
            return new TradeWithVillagerAction(location, actionHolderLocation, actionHolderType, rewards, conditions);
        }

        @Override
        public TradeWithVillagerAction fromNetwork(ResourceLocation location, RegistryFriendlyByteBuf friendlyByteBuf, ResourceLocation actionHolderLocation, IActionHolderType<?> actionHolderType, List<IReward> rewards, List<ICondition> conditions) {
            return new TradeWithVillagerAction(location, actionHolderLocation, actionHolderType, rewards, conditions);
        }
    }
}