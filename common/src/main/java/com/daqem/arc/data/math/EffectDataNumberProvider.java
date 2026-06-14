package com.daqem.arc.data.math;

import com.daqem.arc.Arc;
import com.daqem.arc.api.math.INumberProvider;
import com.daqem.arc.api.math.INumberProviderSerializer;
import com.daqem.arc.api.math.INumberProviderType;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.model.EffectDataProperty;
import com.daqem.arc.model.target.ArcEntityTarget;
import com.google.gson.JsonObject;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.Arrays;
import java.util.Locale;

public class EffectDataNumberProvider implements INumberProvider {

    private final MobEffect effect;
    private final EffectDataProperty property;
    private final ArcEntityTarget target;

    public EffectDataNumberProvider(MobEffect effect, EffectDataProperty property, ArcEntityTarget target) {
        this.effect = effect;
        this.property = property;
        this.target = target;
    }

    @Override
    public double resolve(ActionData actionData) {
        Entity entity = target.getEntity(actionData);
        if (entity instanceof LivingEntity livingEntity) {
            var effectInstance = livingEntity.getEffect(Holder.direct(effect));
            if (effectInstance != null) {
                return property.getValue(effectInstance);
            }
        }
        return 0.0;
    }

    @Override
    public Component getDescription() {
        return Arc.API.translatable("number_provider.effect_data",
                Arc.API.translatable("effect_data_property." + property.name().toLowerCase()),
                effect.getDisplayName(),
                Arc.API.translatable("entity_target." + target.name().toLowerCase())
        );
    }

    @Override
    public INumberProviderType<?> getType() {
        return INumberProviderType.EFFECT_DATA;
    }

    @Override
    public INumberProviderSerializer<? extends INumberProvider> getSerializer() {
        return getType().getSerializer();
    }

    public static class Serializer implements INumberProviderSerializer<EffectDataNumberProvider> {
        @Override
        public EffectDataNumberProvider fromJson(JsonObject jsonObject) {
            String propStr = GsonHelper.getAsString(jsonObject, "property", "AMPLIFIER").toUpperCase(Locale.ROOT);
            EffectDataProperty property;
            try {
                property = EffectDataProperty.valueOf(propStr);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Unknown EffectDataProperty: " + propStr + ". Options: " + Arrays.toString(EffectDataProperty.values()));
            }

            return new EffectDataNumberProvider(
                    getMobEffect(jsonObject, "effect"),
                    property,
                    getEntityTarget(jsonObject, "target", ArcEntityTarget.PLAYER)
            );
        }

        @Override
        public EffectDataNumberProvider fromNetwork(RegistryFriendlyByteBuf buf) {
            return new EffectDataNumberProvider(
                    MobEffect.STREAM_CODEC.decode(buf).value(),
                    buf.readEnum(EffectDataProperty.class),
                    buf.readEnum(ArcEntityTarget.class)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, EffectDataNumberProvider type) {
            INumberProviderSerializer.super.toNetwork(buf, type);
            MobEffect.STREAM_CODEC.encode(buf, Holder.direct(type.effect));
            buf.writeEnum(type.property);
            buf.writeEnum(type.target);
        }
    }
}