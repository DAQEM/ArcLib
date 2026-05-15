package com.daqem.arc.data.condition.entity;

import com.daqem.arc.api.action.data.IActionDataType;
import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.data.ActionData;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Shearable;

public class ReadyForShearingCondition extends AbstractCondition {

    public ReadyForShearingCondition(boolean inverted) {
        super(inverted);
    }

    @Override
    public boolean isMet(ActionData actionData) {
        Entity entity = actionData.getData(IActionDataType.ENTITY);
        return entity instanceof Shearable shearable && shearable.readyForShearing();
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.READY_FOR_SHEARING;
    }

    public static class Serializer implements IConditionSerializer<ReadyForShearingCondition> {

        @Override
        public ReadyForShearingCondition fromJson(ResourceLocation location, JsonObject jsonObject, boolean inverted) {
            return new ReadyForShearingCondition(inverted);
        }

        @Override
        public ReadyForShearingCondition fromNetwork(ResourceLocation location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new ReadyForShearingCondition(inverted);
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, ReadyForShearingCondition type) {
            IConditionSerializer.super.toNetwork(friendlyByteBuf, type);
        }
    }
}
