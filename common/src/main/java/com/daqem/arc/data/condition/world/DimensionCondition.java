package com.daqem.arc.data.condition.world;

import com.daqem.arc.data.ActionData;
import com.daqem.arc.api.action.data.IActionDataType;
import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class DimensionCondition extends AbstractCondition {

    ResourceKey<Level> dimension;

    public DimensionCondition(boolean inverted, ResourceKey<Level> dimension) {
        super(inverted);
        this.dimension = dimension;
    }

    @Override
    public Component getDescription() {
        return getDescription(dimension.location());
    }

    @Override
    public boolean isMet(ActionData actionData) {
        Level world = actionData.getData(IActionDataType.WORLD);
        if (world == null)
            world = actionData.getPlayer().arc$getLevel();
        return world.dimension().location().equals(dimension.location());
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.DIMENSION;
    }

    public ResourceKey<Level> getDimension() {
        return dimension;
    }

    public static class Serializer implements IConditionSerializer<DimensionCondition> {

        @Override
        public DimensionCondition fromJson(ResourceLocation location, JsonObject jsonObject, boolean inverted) {
            return new DimensionCondition(
                    inverted,
                    getDimension(jsonObject, "dimension"));
        }

        @Override
        public DimensionCondition fromNetwork(ResourceLocation location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new DimensionCondition(
                    inverted,
                    friendlyByteBuf.readResourceKey(Registries.DIMENSION));
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, DimensionCondition type) {
            IConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeResourceKey(type.dimension);
        }
    }
}
