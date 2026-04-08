package com.daqem.arc.client.networking;

import com.daqem.arc.data.ActionHolderManager;
import com.daqem.arc.networking.ClientboundUpdateActionsPacket;
import com.daqem.knot.networking.ClientboundContext;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

public class ClientboundUpdateActionsPacketHandler {

    public static void handle(@NotNull ClientboundUpdateActionsPacket packet, ClientboundContext clientboundContext) {
        if (!Minecraft.getInstance().isLocalServer()) {
            ActionHolderManager.getInstance().registerActions(packet.actions());
        }
    }
}
