package com.daqem.arc.model;

import com.daqem.knot.api.codec.KnotStreamCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public enum ItemDataProperty implements StringRepresentable {
    DURABILITY("durability") {
        @Override
        public double getValue(ItemStack stack) {
            return stack.isDamageableItem() ? stack.getMaxDamage() - stack.getDamageValue() : 0.0;
        }
    },
    MAX_DURABILITY("max_durability") {
        @Override
        public double getValue(ItemStack stack) {
            return stack.isDamageableItem() ? stack.getMaxDamage() : 0.0;
        }
    },
    COUNT("count") {
        @Override
        public double getValue(ItemStack stack) {
            return stack.getCount();
        }
    };

    public static final StringRepresentable.EnumCodec<@NotNull ItemDataProperty> CODEC = StringRepresentable.fromEnum(ItemDataProperty::values);
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemDataProperty> STREAM_CODEC = KnotStreamCodecs.enumCodec(ItemDataProperty.class);

    private final String name;

    ItemDataProperty(String name) {
        this.name = name;
    }

    public abstract double getValue(ItemStack stack);

    @Override
    public @NotNull String getSerializedName() {
        return name;
    }
}