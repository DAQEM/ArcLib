package com.daqem.arc.data.reward.world;

import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.reward.AbstractReward;
import com.daqem.arc.api.reward.IRewardSerializer;
import com.daqem.arc.api.reward.IRewardType;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.model.ArcBlockState;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;

public class SetBlockReward extends AbstractReward {

    private final ArcBlockState blockState;

    public SetBlockReward(double chance, int priority, ArcBlockState blockState) {
        super(chance, priority);
        this.blockState = blockState;
    }

    @Override
    public Component getDescription() {
        return getDescription(blockState.block().getName());
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        BlockPos pos = actionData.getPlayer().arc$getPlayer().blockPosition();
        actionData.getPlayer().arc$getLevel().setBlock(pos, blockState.create(), 3);
        return new ActionResult();
    }

    @Override
    public IRewardType<?> getType() {
        return IRewardType.SET_BLOCK;
    }

    public static class Serializer implements IRewardSerializer<SetBlockReward> {

        @Override
        public SetBlockReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new SetBlockReward(
                    chance,
                    priority,
                    getBlockState(jsonObject, "block")
            );
        }

        @Override
        public SetBlockReward fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf, double chance, int priority) {
            return new SetBlockReward(
                    chance,
                    priority,
                    ArcBlockState.STREAM_CODEC.decode(friendlyByteBuf)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, SetBlockReward type) {
            IRewardSerializer.super.toNetwork(friendlyByteBuf, type);
            ArcBlockState.STREAM_CODEC.encode(friendlyByteBuf, type.blockState);
        }
    }
}