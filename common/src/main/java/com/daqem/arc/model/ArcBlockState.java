package com.daqem.arc.model;

import com.daqem.arc.api.ComparisonType;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public record ArcBlockState(Block block, List<ArcProperty> properties) {

    private static final Codec<List<ArcProperty>> SIMPLE_PROPERTIES_CODEC =
            Codec.unboundedMap(Codec.STRING, Codec.STRING).xmap(
                    map -> map.entrySet().stream()
                            .map(entry -> new ArcProperty(entry.getKey(), entry.getValue(), ComparisonType.EQUAL))
                            .collect(Collectors.toList()),
                    list -> list.stream()
                            .filter(prop -> prop.comparisonType() == ComparisonType.EQUAL)
                            .collect(Collectors.toMap(ArcProperty::name, ArcProperty::value, (a, b) -> b))
            );

    private static final Codec<List<ArcProperty>> FULL_PROPERTIES_CODEC = ArcProperty.CODEC.listOf();

    private static final Codec<ArcBlockState> OBJECT_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    BuiltInRegistries.BLOCK.byNameCodec().fieldOf("id").forGetter(ArcBlockState::block), // Also changed here for consistency
                    Codec.either(FULL_PROPERTIES_CODEC, SIMPLE_PROPERTIES_CODEC)
                            .fieldOf("properties")
                            .xmap(either -> either.map(list -> list, list -> list),
                                    list -> {
                                        boolean isSimple = list.stream().allMatch(p -> p.comparisonType() == ComparisonType.EQUAL);
                                        return isSimple ? Either.right(list) : Either.left(list);
                                    }
                            ).forGetter(ArcBlockState::properties)
            ).apply(instance, ArcBlockState::new)
    );

    private static final Codec<ArcBlockState> STRING_ONLY_CODEC = BuiltInRegistries.BLOCK.byNameCodec().xmap(
            block -> new ArcBlockState(block, Collections.emptyList()),
            ArcBlockState::block
    );

    public static final Codec<ArcBlockState> CODEC = Codec.either(OBJECT_CODEC, STRING_ONLY_CODEC)
            .xmap(
                    either -> either.map(state -> state, state -> state),
                    state -> {
                        if (state.properties().isEmpty()) {
                            return Either.right(state);
                        } else {
                            return Either.left(state);
                        }
                    }
            );

    public static final StreamCodec<RegistryFriendlyByteBuf, ArcBlockState> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.registry(BuiltInRegistries.BLOCK.key()),
            ArcBlockState::block,
            ArcProperty.STREAM_CODEC.apply(ByteBufCodecs.list()),
            ArcBlockState::properties,
            ArcBlockState::new
    );

    public boolean matches(BlockState blockState) {
        if (this.block != blockState.getBlock()) {
            return false;
        }

        if (this.properties.isEmpty()) {
            return true;
        }

        Map<String, List<ArcProperty>> propertyMap = this.properties.stream()
                .collect(Collectors.groupingBy(ArcProperty::name));

        return propertyMap.entrySet().stream().allMatch(entry -> {
            String propertyName = entry.getKey();
            List<ArcProperty> requiredProperties = entry.getValue();

            var property = blockState.getBlock().getStateDefinition().getProperty(propertyName);

            if (property == null) {
                return false;
            }

            Comparable<?> actualValue = blockState.getValue(property);

            return requiredProperties.stream()
                    .anyMatch(arcProperty -> arcProperty.matches(actualValue));
        });
    }

    public BlockState create() {
        BlockState blockState = this.block.defaultBlockState();

        for (ArcProperty arcProperty : this.properties) {
            if (arcProperty.comparisonType() != ComparisonType.EQUAL) {
                throw new IllegalStateException("Cannot create a BlockState with a non-EQUAL comparison type for property: " + arcProperty.name());
            }

            Property<?> property = blockState.getBlock().getStateDefinition().getProperty(arcProperty.name());
            if (property != null) {
                Optional<?> valueOptional = property.getValue(arcProperty.value());
                if (valueOptional.isPresent()) {
                    blockState = setValue(blockState, property, valueOptional.get());
                } else {
                    throw new IllegalArgumentException("Invalid value '" + arcProperty.value() + "' for property '" + arcProperty.name() + "' in block " + this.block);
                }
            } else {
                throw new IllegalArgumentException("Unknown property '" + arcProperty.name() + "' for block " + this.block);
            }
        }
        return blockState;
    }

    @SuppressWarnings("unchecked")
    private <T extends Comparable<T>> BlockState setValue(BlockState blockState, Property<T> property, Object value) {
        return blockState.setValue(property, (T) value);
    }
}