package com.daqem.arc.model;

import com.daqem.arc.api.ComparisonType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ArcProperty(String name, String value, ComparisonType comparisonType) {

    public static final Codec<ArcProperty> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("name").forGetter(ArcProperty::name),
                    Codec.STRING.fieldOf("value").forGetter(ArcProperty::value),
                    ComparisonType.CODEC.fieldOf("comparison_type").forGetter(ArcProperty::comparisonType)
            ).apply(instance, ArcProperty::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, ArcProperty> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            ArcProperty::name,
            ByteBufCodecs.STRING_UTF8,
            ArcProperty::value,
            ComparisonType.STREAM_CODEC,
            ArcProperty::comparisonType,
            ArcProperty::new
    );

    public boolean matches(Comparable<?> value) {
        if (value == null) {
            return comparisonType == ComparisonType.NOT_EQUAL;
        }

        final String actualValueString = value.toString();

        switch (comparisonType) {
            case EQUAL:
                return this.value.equalsIgnoreCase(actualValueString);

            case NOT_EQUAL:
                return !this.value.equalsIgnoreCase(actualValueString);

            case GREATER_THAN, LESS_THAN, GREATER_THAN_OR_EQUAL, LESS_THAN_OR_EQUAL:
                try {
                    double thisNumericValue = Double.parseDouble(this.value);
                    double actualNumericValue = Double.parseDouble(actualValueString);

                    return comparisonType.compare(actualNumericValue, thisNumericValue);

                } catch (NumberFormatException e) {
                    return false;
                }

            default:
                return false;
        }
    }
}
