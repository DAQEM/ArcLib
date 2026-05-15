package com.daqem.arc.networking;

import com.daqem.arc.client.networking.*;
import com.daqem.knot.Knot;

public interface ArcNetworking {

    static void init() {
        Knot.NETWORKING.registerClientbound(ClientboundActionHoldersScreenPacket.TYPE, ClientboundActionHoldersScreenPacket.STREAM_CODEC, ClientboundActionHoldersScreenPacketHandler::handle);
        Knot.NETWORKING.registerClientbound(ClientboundActionScreenPacket.TYPE, ClientboundActionScreenPacket.STREAM_CODEC, ClientboundActionScreenPacketHandler::handle);
        Knot.NETWORKING.registerClientbound(ClientboundSyncPlayerActionHoldersPacket.TYPE, ClientboundSyncPlayerActionHoldersPacket.STREAM_CODEC, ClientboundSyncPlayerActionHoldersPacketHandler::handle);
        Knot.NETWORKING.registerClientbound(ClientboundUpdateActionHoldersPacket.TYPE, ClientboundUpdateActionHoldersPacket.STREAM_CODEC, ClientboundUpdateActionHoldersPacketHandler::handle);
        Knot.NETWORKING.registerClientbound(ClientboundUpdateActionsPacket.TYPE, ClientboundUpdateActionsPacket.STREAM_CODEC, ClientboundUpdateActionsPacketHandler::handle);
    }
}
