package com.daqem.arc.client.networking;

import com.daqem.arc.client.gui.holder.ActionHoldersScreen;
import com.daqem.arc.networking.ClientboundActionHoldersScreenPacket;
import com.daqem.knot.networking.ClientboundContext;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

public class ClientboundActionHoldersScreenPacketHandler {

    public static void handle(@NotNull ClientboundActionHoldersScreenPacket packet, ClientboundContext clientboundContext) {
        Minecraft.getInstance().gui.setScreen(new ActionHoldersScreen(packet.actionHolders()));
    }
}
