package com.daqem.arc.data.condition.combat;

import com.daqem.arc.api.action.data.IActionDataType;
import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.data.ActionData;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class CriticalHitCondition extends AbstractCondition {

    public CriticalHitCondition(boolean inverted) {
        super(inverted);
    }

    @Override
    public boolean isMet(ActionData actionData) {
        Boolean isCriticalHit = actionData.getData(IActionDataType.IS_CRITICAL_HIT);
        return isCriticalHit != null && isCriticalHit;
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.CRITICAL_HIT;
    }

    public static class Serializer implements IConditionSerializer<CriticalHitCondition> {

        @Override
        public CriticalHitCondition fromJson(ResourceLocation location, JsonObject jsonObject, boolean inverted) {
            return new CriticalHitCondition(inverted);
        }

        @Override
        public CriticalHitCondition fromNetwork(ResourceLocation location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new CriticalHitCondition(inverted);
        }
    }
}