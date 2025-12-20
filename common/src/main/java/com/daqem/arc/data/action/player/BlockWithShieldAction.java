package com.daqem.arc.data.action.player;

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

public class BlockWithShieldAction extends AbstractAction {

    public BlockWithShieldAction(Identifier location, Identifier actionHolderLocation, IActionHolderType<?> actionHolderType, List<IReward> rewards, List<ICondition> conditions) {
        super(location, actionHolderLocation, actionHolderType, rewards, conditions);
    }

    @Override
    public IActionType<?> getType() {
        return IActionType.BLOCK_WITH_SHIELD;
    }

    public static class Serializer implements IActionSerializer<BlockWithShieldAction> {

        @Override
        public BlockWithShieldAction fromJson(Identifier location, JsonObject jsonObject, Identifier actionHolderLocation, IActionHolderType<?> actionHolderType, List<IReward> rewards, List<ICondition> conditions) {
            return new BlockWithShieldAction(location, actionHolderLocation, actionHolderType, rewards, conditions);
        }

        @Override
        public BlockWithShieldAction fromNetwork(Identifier location, RegistryFriendlyByteBuf friendlyByteBuf, Identifier actionHolderLocation, IActionHolderType<?> actionHolderType, List<IReward> rewards, List<ICondition> conditions) {
            return new BlockWithShieldAction(location, actionHolderLocation, actionHolderType, rewards, conditions);
        }
    }
}