package com.daqem.arc.data.condition.effect;

import com.daqem.arc.api.action.data.ActionData;
import com.daqem.arc.api.action.data.type.ActionDataType;
import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.serializer.IConditionSerializer;
import com.daqem.arc.api.condition.type.ConditionType;
import com.daqem.arc.api.condition.type.IConditionType;
import com.google.gson.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

public class EffectCondition extends AbstractCondition {

    private final MobEffect effect;

    public EffectCondition(boolean inverted, MobEffect effect) {
        super(inverted);
        this.effect = effect;
    }

    @Override
    public Component getDescription() {
        return getDescription(effect.getDisplayName());
    }

    @Override
    public boolean isMet(ActionData actionData) {
        MobEffectInstance effectInstance = actionData.getData(ActionDataType.MOB_EFFECT_INSTANCE);
        return effectInstance != null && effectInstance.getEffect() == this.effect;
    }

    @Override
    public IConditionType<?> getType() {
        return ConditionType.EFFECT;
    }

    public static class Serializer implements IConditionSerializer<EffectCondition> {

        @Override
        public EffectCondition fromJson(ResourceLocation location, JsonObject jsonObject, boolean inverted) {
            return new EffectCondition(
                    inverted,
                    getMobEffect(jsonObject, "effect"));
        }

        @Override
        public EffectCondition fromNetwork(ResourceLocation location, FriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new EffectCondition(
                    inverted,
                    friendlyByteBuf.readById(BuiltInRegistries.MOB_EFFECT));
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, EffectCondition type) {
            IConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeId(BuiltInRegistries.MOB_EFFECT, type.effect);
        }
    }
}
