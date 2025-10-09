package com.daqem.arc.api;

import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum ComparisonType implements StringRepresentable {
    EQUAL("=="),
    NOT_EQUAL("!="),
    GREATER_THAN(">"),
    LESS_THAN("<"),
    GREATER_THAN_OR_EQUAL(">="),
    LESS_THAN_OR_EQUAL("<=");

    public static final StringRepresentable.EnumCodec<ComparisonType> CODEC = StringRepresentable.fromEnum(ComparisonType::values);

    private final String symbol;

    ComparisonType(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public boolean compare(double a, double b) {
        return switch (this) {
            case EQUAL -> a == b;
            case NOT_EQUAL -> a != b;
            case GREATER_THAN -> a > b;
            case LESS_THAN -> a < b;
            case GREATER_THAN_OR_EQUAL -> a >= b;
            case LESS_THAN_OR_EQUAL -> a <= b;
        };
    }

    @Override
    public String toString() {
        return symbol;
    }

    @Override
    public @NotNull String getSerializedName() {
        return symbol;
    }
}
