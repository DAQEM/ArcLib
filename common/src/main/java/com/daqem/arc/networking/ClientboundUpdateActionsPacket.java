package com.daqem.arc.networking;

import com.daqem.arc.Arc;
import com.daqem.arc.api.action.IAction;
import com.daqem.arc.api.action.IActionSerializer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record ClientboundUpdateActionsPacket(List<IAction> actions) implements CustomPacketPayload {

    public static final Type<@NotNull ClientboundUpdateActionsPacket> TYPE = new Type<>(Arc.API.getId("clientbound_update_actions_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundUpdateActionsPacket> STREAM_CODEC = StreamCodec.composite(
            IActionSerializer.STREAM_CODEC.apply(ByteBufCodecs.list()),
            ClientboundUpdateActionsPacket::actions,
            ClientboundUpdateActionsPacket::new
    );

    @Override
    public @NotNull Type<? extends @NotNull CustomPacketPayload> type() {
        return TYPE;
    }
}
