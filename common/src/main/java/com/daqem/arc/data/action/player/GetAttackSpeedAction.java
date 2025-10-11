package com.daqem.arc.data.action.player;

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

public class GetAttackSpeedAction extends AbstractAction {

    public GetAttackSpeedAction(ResourceLocation location, ResourceLocation actionHolderLocation, IActionHolderType<?> actionHolderType, List<IReward> rewards, List<ICondition> conditions) {
        super(location, actionHolderLocation, actionHolderType, rewards, conditions);
    }

    @Override
    public boolean shouldPerformOnClient() {
        return true;
    }

    @Override
    public IActionType<?> getType() {
        return IActionType.GET_ATTACK_SPEED;
    }

    public static class Serializer implements IActionSerializer<GetAttackSpeedAction> {

        @Override
        public GetAttackSpeedAction fromJson(ResourceLocation location, JsonObject jsonObject, ResourceLocation actionHolderLocation, IActionHolderType<?> actionHolderType, List<IReward> rewards, List<ICondition> conditions) {
            return new GetAttackSpeedAction(location, actionHolderLocation, actionHolderType, rewards, conditions);
        }

        @Override
        public GetAttackSpeedAction fromNetwork(ResourceLocation location, RegistryFriendlyByteBuf friendlyByteBuf, ResourceLocation actionHolderLocation, IActionHolderType<?> actionHolderType, List<IReward> rewards, List<ICondition> conditions) {
            return new GetAttackSpeedAction(location, actionHolderLocation, actionHolderType, rewards, conditions);
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, GetAttackSpeedAction type) {
            IActionSerializer.super.toNetwork(friendlyByteBuf, type);
        }
    }
}
