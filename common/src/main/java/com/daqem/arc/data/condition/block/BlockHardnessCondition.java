package com.daqem.arc.data.condition.block;

import com.daqem.arc.api.action.data.IActionDataType;
import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.api.math.INumberProvider;
import com.daqem.arc.api.math.INumberProviderSerializer;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.data.math.ConstantNumberProvider;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;

public class BlockHardnessCondition extends AbstractCondition {

    private final INumberProvider min;
    private final INumberProvider max;

    public BlockHardnessCondition(boolean inverted, INumberProvider min, INumberProvider max) {
        super(inverted);
        this.min = min;
        this.max = max;
    }

    @Override
    public Component getDescription() {
        return getDescription(min.toString(), max.toString());
    }

    @Override
    public boolean isMet(ActionData actionData) {
        BlockState blockState = actionData.getData(IActionDataType.BLOCK_STATE);
        BlockPos blockPos = actionData.getData(IActionDataType.BLOCK_POSITION);
        if (blockState == null || blockPos == null) return false;

        float hardness = blockState.getDestroySpeed(actionData.getPlayer().arc$getLevel(), blockPos);

        double minVal = min.resolve(actionData);
        double maxVal = max.resolve(actionData);

        if (minVal > maxVal) {
            double temp = minVal;
            minVal = maxVal;
            maxVal = temp;
        }

        return hardness >= minVal && hardness <= maxVal;
    }

    public INumberProvider getMax() {
        return max;
    }

    public INumberProvider getMin() {
        return min;
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.BLOCK_HARDNESS;
    }

    public static class Serializer implements IConditionSerializer<BlockHardnessCondition> {

        @Override
        public BlockHardnessCondition fromJson(Identifier location, JsonObject jsonObject, boolean inverted) {
            return new BlockHardnessCondition(
                    inverted,
                    getNumberProvider(jsonObject, "min", new ConstantNumberProvider(Float.MIN_VALUE)),
                    getNumberProvider(jsonObject, "max", new ConstantNumberProvider(Float.MAX_VALUE))
            );
        }

        @Override
        public BlockHardnessCondition fromNetwork(Identifier location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new BlockHardnessCondition(
                    inverted,
                    INumberProviderSerializer.fromNetworkStatic(friendlyByteBuf),
                    INumberProviderSerializer.fromNetworkStatic(friendlyByteBuf)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, BlockHardnessCondition type) {
            IConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            INumberProviderSerializer.toNetwork(type.min, friendlyByteBuf);
            INumberProviderSerializer.toNetwork(type.max, friendlyByteBuf);
        }
    }
}