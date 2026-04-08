package com.daqem.arc.client.networking;

import com.daqem.arc.api.player.ArcClientPlayer;
import com.daqem.arc.networking.ClientboundSyncPlayerActionHoldersPacket;
import com.daqem.knot.networking.ClientboundContext;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

public class ClientboundSyncPlayerActionHoldersPacketHandler {

    public static void handle(@NotNull ClientboundSyncPlayerActionHoldersPacket packet, ClientboundContext clientboundContext) {
        if (Minecraft.getInstance().player instanceof ArcClientPlayer arcClientPlayer) {
            arcClientPlayer.arc$clearActionHolders();
            arcClientPlayer.arc$addActionHolders(packet.actionHolders());
        }
    }
}
