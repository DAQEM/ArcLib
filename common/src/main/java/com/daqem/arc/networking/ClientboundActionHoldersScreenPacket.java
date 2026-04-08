package com.daqem.arc.networking;

import com.daqem.arc.Arc;
import com.daqem.arc.api.action.holder.IActionHolder;
import com.daqem.arc.api.action.holder.IActionHolderSerializer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record ClientboundActionHoldersScreenPacket(List<IActionHolder> actionHolders) implements CustomPacketPayload {

    public static final Type<@NotNull ClientboundActionHoldersScreenPacket> TYPE = new Type<>(Arc.API.getId("clientbound_action_holders_screen_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundActionHoldersScreenPacket> STREAM_CODEC = StreamCodec.composite(
            IActionHolderSerializer.STREAM_CODEC.apply(ByteBufCodecs.list()),
            ClientboundActionHoldersScreenPacket::actionHolders,
            ClientboundActionHoldersScreenPacket::new
    );

    @Override
    public @NotNull Type<? extends @NotNull CustomPacketPayload> type() {
        return TYPE;
    }
}
