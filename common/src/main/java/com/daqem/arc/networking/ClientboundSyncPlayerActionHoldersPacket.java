package com.daqem.arc.networking;

import com.daqem.arc.Arc;
import com.daqem.arc.api.action.holder.IActionHolder;
import com.daqem.arc.api.action.holder.IActionHolderSerializer;
import com.daqem.arc.data.ActionHolderManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public record ClientboundSyncPlayerActionHoldersPacket(List<IActionHolder> actionHolders) implements CustomPacketPayload {

    public static final Type<@NotNull ClientboundSyncPlayerActionHoldersPacket> TYPE = new Type<>(Arc.API.getId("clientbound_sync_player_action_holders_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSyncPlayerActionHoldersPacket> STREAM_CODEC = StreamCodec.composite(
            IActionHolderSerializer.STREAM_CODEC.apply(ByteBufCodecs.list()),
            ClientboundSyncPlayerActionHoldersPacket::actionHolders,
            ClientboundSyncPlayerActionHoldersPacket::new
    );

    @Override
    public @NotNull Type<? extends @NotNull CustomPacketPayload> type() {
        return TYPE;
    }
}
