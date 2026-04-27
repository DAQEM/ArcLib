package com.daqem.arc.model;

import com.daqem.knot.api.codec.KnotStreamCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.effect.MobEffectInstance;
import org.jetbrains.annotations.NotNull;

public enum EffectDataProperty implements StringRepresentable {
    AMPLIFIER("amplifier") {
        @Override
        public double getValue(MobEffectInstance instance) {
            return instance.getAmplifier() + 1.0;
        }
    },
    DURATION("duration") {
        @Override
        public double getValue(MobEffectInstance instance) {
            return instance.getDuration();
        }
    };

    public static final StringRepresentable.EnumCodec<@NotNull EffectDataProperty> CODEC = StringRepresentable.fromEnum(EffectDataProperty::values);
    public static final StreamCodec<RegistryFriendlyByteBuf, EffectDataProperty> STREAM_CODEC = KnotStreamCodecs.enumCodec(EffectDataProperty.class);

    private final String name;

    EffectDataProperty(String name) {
        this.name = name;
    }

    public abstract double getValue(MobEffectInstance instance);

    @Override
    public @NotNull String getSerializedName() {
        return name;
    }
}