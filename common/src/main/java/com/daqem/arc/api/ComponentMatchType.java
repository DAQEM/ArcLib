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
                // We only check the components that were explicitly defined in the patch (from the JSON).
                // This ignores default components inherited from the base item (e.g. max_damage on a diamond helmet).
                for (var entry : targetStack.getComponentsPatch().entrySet()) {
                    if (entry.getValue().isPresent()) {
                        if (!java.util.Objects.equals(currentStack.get(entry.getKey()), entry.getValue().get())) {
                            yield false;
                        }
                    } else {
                        // If it's empty, it means the component was explicitly removed in the patch.
                        // So the current stack should NOT have it.
                        if (currentStack.has(entry.getKey())) {
                            yield false;
                        }
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