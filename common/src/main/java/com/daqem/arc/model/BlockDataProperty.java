package com.daqem.arc.model;

import com.daqem.arc.api.action.data.IActionDataType;
import com.daqem.arc.data.ActionData;
import com.daqem.knot.api.codec.KnotStreamCodecs;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public enum BlockDataProperty implements StringRepresentable {
    HARDNESS("hardness") {
        @Override
        public double getValue(ActionData actionData) {
            BlockState state = actionData.getData(IActionDataType.BLOCK_STATE);
            Level level = actionData.getData(IActionDataType.WORLD);
            if (level == null) level = actionData.getPlayer().arc$getLevel();
            BlockPos pos = actionData.getData(IActionDataType.BLOCK_POSITION);

            if (state != null && pos != null) {
                return state.getDestroySpeed(level, pos);
            } else if (state != null) {
                return state.getBlock().defaultDestroyTime();
            }
            return 0.0;
        }
    },
    EXPLOSION_RESISTANCE("explosion_resistance") {
        @Override
        public double getValue(ActionData actionData) {
            BlockState state = actionData.getData(IActionDataType.BLOCK_STATE);
            if (state != null) {
                return state.getBlock().getExplosionResistance();
            }
            return 0.0;
        }
    },
    FRICTION("friction") {
        @Override
        public double getValue(ActionData actionData) {
            BlockState state = actionData.getData(IActionDataType.BLOCK_STATE);
            if (state != null) {
                return state.getBlock().getFriction();
            }
            return 0.0;
        }
    },
    LIGHT_EMISSION("light_emission") {
        @Override
        public double getValue(ActionData actionData) {
            BlockState state = actionData.getData(IActionDataType.BLOCK_STATE);
            if (state != null) {
                return state.getLightEmission();
            }
            return 0.0;
        }
    };

    public static final StringRepresentable.EnumCodec<@NotNull BlockDataProperty> CODEC = StringRepresentable.fromEnum(BlockDataProperty::values);
    public static final StreamCodec<RegistryFriendlyByteBuf, BlockDataProperty> STREAM_CODEC = KnotStreamCodecs.enumCodec(BlockDataProperty.class);

    private final String name;

    BlockDataProperty(String name) {
        this.name = name;
    }

    public abstract double getValue(ActionData actionData);

    @Override
    public @NotNull String getSerializedName() {
        return name;
    }
}