package com.daqem.arc.data.action.item;

import com.daqem.arc.api.action.AbstractAction;
import com.daqem.arc.api.action.IActionSerializer;
import com.daqem.arc.api.action.IActionType;
import com.daqem.arc.api.action.holder.IActionHolderType;
import com.daqem.arc.api.condition.ICondition;
import com.daqem.arc.api.reward.IReward;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.Identifier;

import java.util.List;

public class PickupItemAction extends AbstractAction {

    public PickupItemAction(Identifier location, Identifier actionHolderLocation, IActionHolderType<?> actionHolderType, List<IReward> rewards, List<ICondition> conditions) {
        super(location, actionHolderLocation, actionHolderType, rewards, conditions);
    }

    @Override
    public IActionType<?> getType() {
        return IActionType.PICKUP_ITEM;
    }

    public static class Serializer implements IActionSerializer<PickupItemAction> {

        @Override
        public PickupItemAction fromJson(Identifier location, JsonObject jsonObject, Identifier actionHolderLocation, IActionHolderType<?> actionHolderType, List<IReward> rewards, List<ICondition> conditions) {
            return new PickupItemAction(location, actionHolderLocation, actionHolderType, rewards, conditions);
        }

        @Override
        public PickupItemAction fromNetwork(Identifier location, RegistryFriendlyByteBuf friendlyByteBuf, Identifier actionHolderLocation, IActionHolderType<?> actionHolderType, List<IReward> rewards, List<ICondition> conditions) {
            return new PickupItemAction(location, actionHolderLocation, actionHolderType, rewards, conditions);
        }
    }
}