package com.daqem.arc.data.condition.entity;

import com.daqem.arc.api.action.data.ActionData;
import com.daqem.arc.api.action.data.type.ActionDataType;
import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.serializer.ConditionSerializer;
import com.daqem.arc.api.condition.serializer.IConditionSerializer;
import com.daqem.arc.api.condition.type.ConditionType;
import com.daqem.arc.api.condition.type.IConditionType;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;

public class DamageSourceCondition extends AbstractCondition {

    private final String source;

    public DamageSourceCondition(boolean inverted, String source) {
        super(inverted);
        this.source = source;
    }

    @Override
    public Component getDescription() {
        return getDescription(source);
    }

    @Override
    public boolean isMet(ActionData actionData) {
        DamageSource damageSource = actionData.getData(ActionDataType.DAMAGE_SOURCE);
        return damageSource != null && damageSource.getMsgId().equals(source);
    }

    @Override
    public IConditionType<?> getType() {
        return ConditionType.DAMAGE_SOURCE;
    }

    @Override
    public IConditionSerializer<?> getSerializer() {
        return ConditionSerializer.DAMAGE_SOURCE;
    }

    public static class Serializer implements ConditionSerializer<DamageSourceCondition> {

        @Override
        public DamageSourceCondition fromJson(ResourceLocation location, JsonObject jsonObject, boolean inverted) {
            return new DamageSourceCondition(
                    inverted,
                    getString(jsonObject, "source"));
        }

        @Override
        public DamageSourceCondition fromNetwork(ResourceLocation location, FriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new DamageSourceCondition(
                    inverted,
                    friendlyByteBuf.readUtf());
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, DamageSourceCondition type) {
            ConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeUtf(type.source);
        }
    }
}
