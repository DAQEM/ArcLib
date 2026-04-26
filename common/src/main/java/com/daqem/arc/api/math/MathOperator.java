package com.daqem.arc.api.math;

import com.daqem.knot.api.codec.KnotStreamCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum MathOperator implements StringRepresentable {
    ADD("add"),
    SUBTRACT("subtract"),
    MULTIPLY("multiply"),
    DIVIDE("divide"),
    MODULO("modulo"),
    POWER("power"),
    MIN("min"),
    MAX("max");

    public static final StringRepresentable.EnumCodec<@NotNull MathOperator> CODEC = StringRepresentable.fromEnum(MathOperator::values);
    public static final StreamCodec<RegistryFriendlyByteBuf, MathOperator> STREAM_CODEC = KnotStreamCodecs.enumCodec(MathOperator.class);

    private final String name;

    MathOperator(String name) {
        this.name = name;
    }

    public double apply(double left, double right) {
        return switch (this) {
            case ADD -> left + right;
            case SUBTRACT -> left - right;
            case MULTIPLY -> left * right;
            case DIVIDE -> right == 0 ? 0 : left / right;
            case MODULO -> right == 0 ? 0 : left % right;
            case POWER -> Math.pow(left, right);
            case MIN -> Math.min(left, right);
            case MAX -> Math.max(left, right);
        };
    }

    @Override
    public @NotNull String getSerializedName() {
        return name;
    }
}