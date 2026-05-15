package com.daqem.arc.data.condition.entity;

import com.daqem.arc.api.action.data.IActionDataType;
import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.data.ActionData;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
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
    public Component getDescription() {
        return getDescription(block.getName());
    }

    @Override
    public boolean isMet(ActionData actionData) {
        Entity entity = actionData.getData(IActionDataType.ENTITY);
        return entity != null && entity.level().getBlockState(entity.blockPosition()).is(block);
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.ENTITY_IN_BLOCK;
    }

    public Block getBlock() {
        return block;
    }

    public static class Serializer implements IConditionSerializer<EntityInBlockCondition> {

        @Override
        public EntityInBlockCondition fromJson(ResourceLocation location, JsonObject jsonObject, boolean inverted) {
            return new EntityInBlockCondition(
                    inverted,
                    getBlock(jsonObject, "block"));
        }

        @Override
        public EntityInBlockCondition fromNetwork(ResourceLocation location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new EntityInBlockCondition(
                    inverted,
                    BuiltInRegistries.BLOCK.byId(friendlyByteBuf.readVarInt()));
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, EntityInBlockCondition type) {
            IConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeVarInt(BuiltInRegistries.BLOCK.getId(type.block));
        }
    }
}
