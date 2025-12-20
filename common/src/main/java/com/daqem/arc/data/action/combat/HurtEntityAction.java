package com.daqem.arc.data.action.combat;

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

public class HurtEntityAction extends AbstractAction {

    public HurtEntityAction(Identifier location, Identifier actionHolderLocation, IActionHolderType<?> actionHolderType, List<IReward> rewards, List<ICondition> conditions) {
        super(location, actionHolderLocation, actionHolderType, rewards, conditions);
    }

    @Override
    public IActionType<?> getType() {
        return IActionType.HURT_ENTITY;
    }

    public static class Serializer implements IActionSerializer<HurtEntityAction> {

        @Override
        public HurtEntityAction fromJson(Identifier location, JsonObject jsonObject, Identifier actionHolderLocation, IActionHolderType<?> actionHolderType, List<IReward> rewards, List<ICondition> conditions) {
            return new HurtEntityAction(location, actionHolderLocation, actionHolderType, rewards, conditions);
        }

        @Override
        public HurtEntityAction fromNetwork(Identifier location, RegistryFriendlyByteBuf friendlyByteBuf, Identifier actionHolderLocation, IActionHolderType<?> actionHolderType, List<IReward> rewards, List<ICondition> conditions) {
            return new HurtEntityAction(location, actionHolderLocation, actionHolderType, rewards, conditions);
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, HurtEntityAction type) {
            IActionSerializer.super.toNetwork(friendlyByteBuf, type);
        }
    }
}
