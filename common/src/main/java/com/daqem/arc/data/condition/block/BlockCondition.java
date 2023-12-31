package com.daqem.arc.data.condition.block;

import com.daqem.arc.api.action.data.ActionData;
import com.daqem.arc.api.action.data.type.ActionDataType;
import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.serializer.ConditionSerializer;
import com.daqem.arc.api.condition.serializer.IConditionSerializer;
import com.daqem.arc.api.condition.type.ConditionType;
import com.daqem.arc.api.condition.type.IConditionType;
import com.google.gson.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BlockCondition extends AbstractCondition {

    private final Block block;

    public BlockCondition(boolean inverted, Block block) {
        super(inverted);
        this.block = block;
    }

    @Override
    public Component getDescription() {
        return getDescription(block.getName());
    }

    @Override
    public boolean isMet(ActionData actionData) {
        BlockState blockState = actionData.getData(ActionDataType.BLOCK_STATE);
        return blockState != null && blockState.getBlock() == this.block;
    }

    @Override
    public IConditionType<?> getType() {
        return ConditionType.BLOCK;
    }

    @Override
    public IConditionSerializer<?> getSerializer() {
        return ConditionSerializer.BLOCK;
    }

    public static class Serializer implements ConditionSerializer<BlockCondition> {

        @Override
        public BlockCondition fromJson(ResourceLocation location, JsonObject jsonObject, boolean inverted) {
            return new BlockCondition(
                    inverted,
                    getBlock(jsonObject, "block"));
        }

        @Override
        public BlockCondition fromNetwork(ResourceLocation location, FriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new BlockCondition(
                    inverted,
                    friendlyByteBuf.readById(BuiltInRegistries.BLOCK));
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, BlockCondition type) {
            ConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeId(BuiltInRegistries.BLOCK, type.block);
        }
    }
}
