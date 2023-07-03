package com.daqem.arc.data.condition.block.properties;

import com.daqem.arc.api.action.data.ActionData;
import com.daqem.arc.api.action.data.type.ActionDataType;
import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.serializer.ConditionSerializer;
import com.daqem.arc.api.condition.serializer.IConditionSerializer;
import com.daqem.arc.api.condition.type.ConditionType;
import com.daqem.arc.api.condition.type.IConditionType;
import com.google.gson.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.state.BlockState;

import java.lang.reflect.Type;

public class BlockMaterialColorCondition extends AbstractCondition {

    private final int id;

    public BlockMaterialColorCondition(boolean inverted, int id) {
        super(inverted);
        this.id = id;
    }

    @Override
    public boolean isMet(ActionData actionData) {
        BlockState blockState = actionData.getData(ActionDataType.BLOCK_STATE);
        return blockState != null && blockState.getMaterial().getColor().id == id;
    }

    @Override
    public IConditionType<?> getType() {
        return ConditionType.BLOCK_MATERIAL_COLOR;
    }

    @Override
    public IConditionSerializer<?> getSerializer() {
        return ConditionSerializer.BLOCK_MATERIAL_COLOR;
    }

    public static class Serializer implements ConditionSerializer<BlockMaterialColorCondition> {

        @Override
        public BlockMaterialColorCondition fromJson(ResourceLocation location, JsonObject jsonObject, boolean inverted) {
            return new BlockMaterialColorCondition(
                    inverted,
                    GsonHelper.getAsInt(jsonObject, "id"));
        }

        @Override
        public BlockMaterialColorCondition fromNetwork(ResourceLocation location, FriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new BlockMaterialColorCondition(
                    inverted,
                    friendlyByteBuf.readInt());
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, BlockMaterialColorCondition type) {
            ConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeInt(type.id);
        }
    }
}
