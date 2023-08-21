package com.daqem.arc.data.condition.entity;

import com.daqem.arc.api.action.data.ActionData;
import com.daqem.arc.api.action.data.type.ActionDataType;
import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.serializer.ConditionSerializer;
import com.daqem.arc.api.condition.serializer.IConditionSerializer;
import com.daqem.arc.api.condition.type.ConditionType;
import com.daqem.arc.api.condition.type.IConditionType;
import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;

public class EntityInBlockCondition extends AbstractCondition {

    private final Block block;

    public EntityInBlockCondition(boolean inverted, Block block) {
        super(inverted);
        this.block = block;
    }

    @Override
    public boolean isMet(ActionData actionData) {
        Entity entity = actionData.getData(ActionDataType.ENTITY);
        return entity != null && entity.level.getBlockState(entity.blockPosition()).is(block);
    }

    @Override
    public IConditionType<?> getType() {
        return ConditionType.ENTITY_IN_BLOCK;
    }

    @Override
    public IConditionSerializer<?> getSerializer() {
        return ConditionSerializer.ENTITY_IN_BLOCK;
    }

    public static class Serializer implements ConditionSerializer<EntityInBlockCondition> {

        @Override
        public EntityInBlockCondition fromJson(ResourceLocation location, JsonObject jsonObject, boolean inverted) {
            return new EntityInBlockCondition(
                    inverted,
                    getBlock(jsonObject, "block"));
        }

        @Override
        public EntityInBlockCondition fromNetwork(ResourceLocation location, FriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new EntityInBlockCondition(
                    inverted,
                    friendlyByteBuf.readById(Registry.BLOCK));
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, EntityInBlockCondition type) {
            ConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeId(Registry.BLOCK, type.block);
        }
    }
}
