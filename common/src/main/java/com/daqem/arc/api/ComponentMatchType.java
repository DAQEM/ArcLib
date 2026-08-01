package com.daqem.arc.api;

import com.daqem.knot.api.codec.KnotStreamCodecs;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum ComponentMatchType implements StringRepresentable {
    NONE("none"),
    EXACT("exact"),
    CONTAINS("contains");

    public static final StringRepresentable.EnumCodec<@NotNull ComponentMatchType> CODEC = StringRepresentable.fromEnum(ComponentMatchType::values);
    public static final StreamCodec<RegistryFriendlyByteBuf, ComponentMatchType> STREAM_CODEC = KnotStreamCodecs.enumCodec(ComponentMatchType.class);

    private final String name;

    ComponentMatchType(String name) {
        this.name = name;
    }

    public boolean matches(ItemStack targetStack, ItemStack currentStack) {
        return switch (this) {
            case NONE -> true;
            case EXACT -> ItemStack.isSameItemSameComponents(targetStack, currentStack);
            case CONTAINS -> {
                for (var component : targetStack.getComponents()) {
                    if (!java.util.Objects.equals(currentStack.get(component.type()), component.value())) {
                        yield false;
                    }
                }
                yield true;
            }
        };
    }

    public static ComponentMatchType fromJson(JsonObject jsonObject, String key, ComponentMatchType defaultValue) {
        if (jsonObject.has(key)) {
            JsonElement element = jsonObject.get(key);
            if (element.isJsonPrimitive()) {
                if (element.getAsJsonPrimitive().isBoolean()) {
                    return element.getAsBoolean() ? EXACT : NONE;
                } else if (element.getAsJsonPrimitive().isString()) {
                    String str = element.getAsString().toLowerCase(Locale.ROOT);
                    return switch (str) {
                        case "exact", "true" -> EXACT;
                        case "contains" -> CONTAINS;
                        case "none", "false" -> NONE;
                        default -> defaultValue;
                    };
                }
            }
        }
        return defaultValue;
    }

    @Override
    public @NotNull String getSerializedName() {
        return name;
    }
}