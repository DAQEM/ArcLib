package com.daqem.arc.model;

import com.daqem.arc.api.ComparisonType;
import com.daqem.arc.api.entity.IEntityDataResolver;
import com.daqem.arc.registry.EntityDataRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.Optional;

public record EntityDataProperty(ResourceLocation id, String value, ComparisonType comparisonType) {

    public static final Codec<EntityDataProperty> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("id").forGetter(EntityDataProperty::id),
                    Codec.STRING.fieldOf("value").forGetter(EntityDataProperty::value),
                    ComparisonType.CODEC.optionalFieldOf("comparison", ComparisonType.EQUAL).forGetter(EntityDataProperty::comparisonType)
            ).apply(instance, EntityDataProperty::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, EntityDataProperty> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC,
            EntityDataProperty::id,
            ByteBufCodecs.STRING_UTF8,
            EntityDataProperty::value,
            ComparisonType.STREAM_CODEC,
            EntityDataProperty::comparisonType,
            EntityDataProperty::new
    );

    public boolean matches(Entity entity) {
        Optional<IEntityDataResolver<?>> entityDataResolver = EntityDataRegistry.get(id);
        return entityDataResolver
                .map(resolver -> {
                    Object actualValue = resolver.getDataFetcher().apply(entity);
                    if (actualValue instanceof Optional<?> optional) {
                        return optional.isPresent() && matchesValue(optional.get());
                    }
                    return matchesValue(actualValue);
                })
                .orElse(false);
    }

    private boolean matchesValue(Object actualValue) {
        if (actualValue == null) {
            return comparisonType == ComparisonType.NOT_EQUAL;
        }

        final String actualValueString = actualValue.toString();

        if (actualValue instanceof Boolean) {
            return this.value.equalsIgnoreCase(actualValueString);
        }

        switch (comparisonType) {
            case EQUAL:
                return this.value.equalsIgnoreCase(actualValueString);
            case NOT_EQUAL:
                return !this.value.equalsIgnoreCase(actualValueString);
            default:
                try {
                    double thisNumericValue = Double.parseDouble(this.value);
                    double actualNumericValue = Double.parseDouble(actualValueString);
                    return comparisonType.compare(actualNumericValue, thisNumericValue);
                } catch (NumberFormatException e) {
                    return false;
                }
        }
    }
}