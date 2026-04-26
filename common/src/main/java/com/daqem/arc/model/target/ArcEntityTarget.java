package com.daqem.arc.model.target;

import com.daqem.arc.api.action.data.IActionDataType;
import com.daqem.arc.data.ActionData;
import com.daqem.knot.api.codec.KnotStreamCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum ArcEntityTarget implements StringRepresentable {
    PLAYER("player"),
    ENTITY("entity");

    public static final StringRepresentable.EnumCodec<@NotNull ArcEntityTarget> CODEC = StringRepresentable.fromEnum(ArcEntityTarget::values);
    public static final StreamCodec<RegistryFriendlyByteBuf, ArcEntityTarget> STREAM_CODEC = KnotStreamCodecs.enumCodec(ArcEntityTarget.class);

    private final String name;

    ArcEntityTarget(String name) {
        this.name = name;
    }

    @Nullable
    public Entity getEntity(ActionData actionData) {
        return switch (this) {
            case PLAYER -> actionData.getPlayer().arc$getPlayer();
            case ENTITY -> actionData.getData(IActionDataType.ENTITY);
        };
    }

    @Override
    public @NotNull String getSerializedName() {
        return name;
    }
}