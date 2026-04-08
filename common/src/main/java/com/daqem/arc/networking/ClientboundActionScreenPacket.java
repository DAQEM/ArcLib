package com.daqem.arc.networking;

import com.daqem.arc.Arc;
import com.daqem.arc.api.action.IAction;
import com.daqem.arc.api.action.IActionSerializer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record ClientboundActionScreenPacket(IAction action) implements CustomPacketPayload {

    public static final Type<@NotNull ClientboundActionScreenPacket> TYPE = new Type<>(Arc.API.getId("clientbound_action_screen_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundActionScreenPacket> STREAM_CODEC = StreamCodec.composite(
            IActionSerializer.STREAM_CODEC,
            ClientboundActionScreenPacket::action,
            ClientboundActionScreenPacket::new
    );

    @Override
    public @NotNull Type<? extends @NotNull CustomPacketPayload> type() {
        return TYPE;
    }
}
