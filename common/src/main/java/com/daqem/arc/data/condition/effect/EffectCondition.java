package com.daqem.arc.data.condition.effect;

import com.daqem.arc.api.ComparisonType;
import com.daqem.arc.api.action.data.IActionDataType;
import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.data.ActionData;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.effect.MobEffectInstance;

public class EffectCondition extends AbstractCondition {

    private final MobEffectInstance effectInstance;
    private final boolean checkAmplifier;
    private final boolean checkDuration;
    private final ComparisonType amplifierComparisonType;
    private final ComparisonType durationComparisonType;

    public EffectCondition(boolean inverted, MobEffectInstance effect, boolean checkAmplifier, ComparisonType amplifierComparisonType, boolean checkDuration, ComparisonType durationComparisonType) {
        super(inverted);
        this.effectInstance = effect;
        this.checkAmplifier = checkAmplifier;
        this.amplifierComparisonType = amplifierComparisonType;
        this.checkDuration = checkDuration;
        this.durationComparisonType = durationComparisonType;
    }

    @Override
    public Component getDescription() {
        return getDescription(effectInstance.getEffect().value().getDisplayName());
    }

    @Override
    public boolean isMet(ActionData actionData) {
        MobEffectInstance effectInstance = actionData.getData(IActionDataType.MOB_EFFECT_INSTANCE);
        if (effectInstance != null) {
            if (effectInstance.getEffect().value() == this.effectInstance.getEffect().value()) {
                if (checkAmplifier) {
                    if (!amplifierComparisonType.compare(effectInstance.getAmplifier(), this.effectInstance.getAmplifier())) {
                        return false;
                    }
                }
                if (checkDuration) {
                    if (!durationComparisonType.compare(effectInstance.getDuration(), this.effectInstance.getDuration())) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.EFFECT;
    }

    public MobEffectInstance getEffectInstance() {
        return effectInstance;
    }

    public boolean isCheckAmplifier() {
        return checkAmplifier;
    }

    public boolean isCheckDuration() {
        return checkDuration;
    }

    public ComparisonType getAmplifierComparisonType() {
        return amplifierComparisonType;
    }

    public ComparisonType getDurationComparisonType() {
        return durationComparisonType;
    }

    public static class Serializer implements IConditionSerializer<EffectCondition> {

        @Override
        public EffectCondition fromJson(ResourceLocation location, JsonObject jsonObject, boolean inverted) {
            return new EffectCondition(
                    inverted,
                    getMobEffectInstance(jsonObject, "effect"),
                    GsonHelper.getAsBoolean(jsonObject, "check_amplifier", false),
                    getComparisonType(jsonObject, "amplifier_comparison", ComparisonType.EQUAL),
                    GsonHelper.getAsBoolean(jsonObject, "check_duration", false),
                    getComparisonType(jsonObject, "duration_comparison", ComparisonType.EQUAL)
            );
        }

        @Override
        public EffectCondition fromNetwork(ResourceLocation location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new EffectCondition(
                    inverted,
                    MobEffectInstance.STREAM_CODEC.decode(friendlyByteBuf),
                    friendlyByteBuf.readBoolean(),
                    friendlyByteBuf.readEnum(ComparisonType.class),
                    friendlyByteBuf.readBoolean(),
                    friendlyByteBuf.readEnum(ComparisonType.class)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, EffectCondition type) {
            IConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            MobEffectInstance.STREAM_CODEC.encode(friendlyByteBuf, type.effectInstance);
            friendlyByteBuf.writeBoolean(type.checkAmplifier);
            friendlyByteBuf.writeEnum(type.amplifierComparisonType);
            friendlyByteBuf.writeBoolean(type.checkDuration);
            friendlyByteBuf.writeEnum(type.durationComparisonType);
        }
    }
}
