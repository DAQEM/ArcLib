package com.daqem.arc.data.condition.block;

import com.daqem.arc.api.action.data.IActionDataType;
import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.model.ArcBlockState;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BlockCondition extends AbstractCondition {

    private final ArcBlockState blockState;

    public BlockCondition(boolean inverted, ArcBlockState blockState) {
        super(inverted);
        this.blockState = blockState;
    }

    @Override
    public Component getDescription() {
        return getDescription(blockState.block().getName());
    }

    @Override
    public boolean isMet(ActionData actionData) {
        BlockState blockState = actionData.getData(IActionDataType.BLOCK_STATE);
        return blockState != null && this.blockState.matches(blockState);
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.BLOCK;
    }

    public ArcBlockState getBlockState() {
        return blockState;
    }

    public Block getBlock() {
        return blockState.block();
    }

    public static class Serializer implements IConditionSerializer<BlockCondition> {

        @Override
        public BlockCondition fromJson(Identifier location, JsonObject jsonObject, boolean inverted) {
            return new BlockCondition(
                    inverted,
                    getBlockState(jsonObject, "block")
            );
        }

        @Override
        public BlockCondition fromNetwork(Identifier location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new BlockCondition(
                    inverted,
                    ArcBlockState.STREAM_CODEC.decode(friendlyByteBuf)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, BlockCondition type) {
            IConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            ArcBlockState.STREAM_CODEC.encode(friendlyByteBuf, type.blockState);
        }
    }
}
