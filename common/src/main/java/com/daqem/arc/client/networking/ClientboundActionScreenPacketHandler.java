package com.daqem.arc.client.networking;

import com.daqem.arc.client.gui.action.ActionScreen;
import com.daqem.arc.data.ActionHolderManager;
import com.daqem.arc.networking.ClientboundActionScreenPacket;
import com.daqem.knot.networking.ClientboundContext;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

public class ClientboundActionScreenPacketHandler {

    public static void handle(@NotNull ClientboundActionScreenPacket packet, ClientboundContext clientboundContext) {
        Minecraft.getInstance().setScreen(new ActionScreen(
                ActionHolderManager.getInstance().getActions(),
                ActionHolderManager.getInstance().getActions().stream()
                        .filter(a -> a.getIdentifier().equals(packet.action().getIdentifier()))
                        .findFirst()
                        .orElse(null)));
    }
}
