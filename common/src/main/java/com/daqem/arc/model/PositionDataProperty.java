package com.daqem.arc.model;

import com.daqem.arc.data.ActionData;
import com.daqem.knot.api.codec.KnotStreamCodecs;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public enum PositionDataProperty implements StringRepresentable {
    X("x") {
        @Override
        public double getValue(ActionData data, Vec3 pos) { return pos.x(); }
    },
    Y("y") {
        @Override
        public double getValue(ActionData data, Vec3 pos) { return pos.y(); }
    },
    Z("z") {
        @Override
        public double getValue(ActionData data, Vec3 pos) { return pos.z(); }
    },
    LIGHT_LEVEL("light_level") {
        @Override
        public double getValue(ActionData data, Vec3 pos) {
            return data.getPlayer().arc$getLevel().getLightEmission(BlockPos.containing(pos));
        }
    },
    TIME_OF_DAY("time_of_day") {
        @Override
        public double getValue(ActionData data, Vec3 pos) {
            return data.getPlayer().arc$getLevel().getOverworldClockTime() % 24000;
        }
    },
    MOON_PHASE("moon_phase") {
        @Override
        public double getValue(ActionData data, Vec3 pos) {
            Level level = data.getPlayer().arc$getLevel();
            return level.environmentAttributes().getValue(EnvironmentAttributes.MOON_PHASE, pos).index();
        }
    };

    public static final StringRepresentable.EnumCodec<@NotNull PositionDataProperty> CODEC = StringRepresentable.fromEnum(PositionDataProperty::values);
    public static final StreamCodec<RegistryFriendlyByteBuf, PositionDataProperty> STREAM_CODEC = KnotStreamCodecs.enumCodec(PositionDataProperty.class);

    private final String name;

    PositionDataProperty(String name) {
        this.name = name;
    }

    public abstract double getValue(ActionData data, Vec3 pos);

    @Override
    public @NotNull String getSerializedName() {
        return name;
    }
}