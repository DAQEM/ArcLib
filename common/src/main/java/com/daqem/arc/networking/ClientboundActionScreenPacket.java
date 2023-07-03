package com.daqem.arc.networking;

import com.daqem.arc.api.action.IAction;
import com.daqem.arc.api.action.serializer.IActionSerializer;
import com.daqem.arc.client.screen.ActionScreen;
import com.daqem.arc.data.ActionManager;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;

import java.awt.*;

public class ClientboundActionScreenPacket extends BaseS2CMessage {

    IAction action;

    public ClientboundActionScreenPacket(IAction action) {
        this.action = action;
    }

    public ClientboundActionScreenPacket(FriendlyByteBuf friendlyByteBuf) {
        this.action = IActionSerializer.fromNetwork(friendlyByteBuf);
    }

    @Override
    public MessageType getType() {
        return ArcNetworking.CLIENTBOUND_ACTION_SCREEN;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        IActionSerializer.toNetwork(action, buf);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        Minecraft.getInstance().setScreen(new ActionScreen(null,
                ActionManager.getInstance().getActions(),
                action,
                new Color(125, 35, 72, 0)));
    }
}
