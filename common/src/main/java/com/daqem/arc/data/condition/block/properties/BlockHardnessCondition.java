package com.daqem.arc.data.condition.block.properties;

import com.daqem.arc.api.action.data.ActionData;
import com.daqem.arc.api.action.data.type.ActionDataType;
import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.serializer.ConditionSerializer;
import com.daqem.arc.api.condition.serializer.IConditionSerializer;
import com.daqem.arc.api.condition.type.ConditionType;
import com.daqem.arc.api.condition.type.IConditionType;
import com.google.gson.*;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.state.BlockState;

public class BlockHardnessCondition extends AbstractCondition {

    private final float min;
    private final float max;

    public BlockHardnessCondition(boolean inverted, float min, float max) {
        super(inverted);
        this.min = min;
        this.max = max;

        if (min > max) {
            throw new IllegalArgumentException("min cannot be greater than max for BlockHardnessCondition.");
        }
    }

    @Override
    public boolean isMet(ActionData actionData) {
        BlockState blockState = actionData.getData(ActionDataType.BLOCK_STATE);
        BlockPos blockPos = actionData.getData(ActionDataType.BLOCK_POSITION);
        if (blockState == null || blockPos == null) return false;
        float hardness = blockState.getDestroySpeed(actionData.getPlayer().getLevel(), blockPos);
        return hardness >= min && hardness <= max;
    }

    @Override
    public IConditionType<?> getType() {
        return ConditionType.BLOCK_HARDNESS;
    }

    @Override
    public IConditionSerializer<?> getSerializer() {
        return ConditionSerializer.BLOCK_HARDNESS;
    }

    public static class Serializer implements ConditionSerializer<BlockHardnessCondition> {

        @Override
        public BlockHardnessCondition fromJson(ResourceLocation location, JsonObject jsonObject, boolean inverted) {
            return new BlockHardnessCondition(
                    inverted,
                    GsonHelper.getAsFloat(jsonObject, "min", Float.MIN_VALUE),
                    GsonHelper.getAsFloat(jsonObject, "max", Float.MAX_VALUE));
        }

        @Override
        public BlockHardnessCondition fromNetwork(ResourceLocation location, FriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new BlockHardnessCondition(
                    inverted,
                    friendlyByteBuf.readFloat(),
                    friendlyByteBuf.readFloat());
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, BlockHardnessCondition type) {
            ConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeFloat(type.min);
            friendlyByteBuf.writeFloat(type.max);
        }
    }
}
