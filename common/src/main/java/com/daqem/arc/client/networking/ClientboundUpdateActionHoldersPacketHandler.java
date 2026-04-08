package com.daqem.arc.client.networking;

import com.daqem.arc.data.ActionHolderManager;
import com.daqem.arc.networking.ClientboundUpdateActionHoldersPacket;
import com.daqem.knot.networking.ClientboundContext;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

public class ClientboundUpdateActionHoldersPacketHandler {

    public static void handle(@NotNull ClientboundUpdateActionHoldersPacket packet, ClientboundContext clientboundContext) {
        if (!Minecraft.getInstance().isLocalServer()) {
            ActionHolderManager.getInstance().registerActionHolders(packet.actionHolders());
        }
    }
}
