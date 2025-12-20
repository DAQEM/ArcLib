package com.daqem.arc.data.action.movement;

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

public class SprintAction extends AbstractAction {

    public SprintAction(Identifier location, Identifier actionHolderLocation, IActionHolderType<?> actionHolderType, List<IReward> rewards, List<ICondition> conditions) {
        super(location, actionHolderLocation, actionHolderType, rewards, conditions);
    }

    @Override
    public IActionType<?> getType() {
        return IActionType.SPRINT;
    }

    public static class Serializer implements IActionSerializer<SprintAction> {

        @Override
        public SprintAction fromJson(Identifier location, JsonObject jsonObject, Identifier actionHolderLocation, IActionHolderType<?> actionHolderType, List<IReward> rewards, List<ICondition> conditions) {
            return new SprintAction(location, actionHolderLocation, actionHolderType, rewards, conditions);
        }

        @Override
        public SprintAction fromNetwork(Identifier location, RegistryFriendlyByteBuf friendlyByteBuf, Identifier actionHolderLocation, IActionHolderType<?> actionHolderType, List<IReward> rewards, List<ICondition> conditions) {
            return new SprintAction(location, actionHolderLocation, actionHolderType, rewards, conditions);
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, SprintAction type) {
            IActionSerializer.super.toNetwork(friendlyByteBuf, type);
        }
    }
}
