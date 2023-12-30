package com.daqem.arc.networking;

import com.daqem.arc.api.action.IAction;
import com.daqem.arc.api.action.serializer.IActionSerializer;
import com.daqem.arc.client.gui.ActionScreen;
import com.daqem.arc.data.ActionManager;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;

import java.awt.*;

public class ClientboundActionScreenPacket extends BaseS2CMessage {

    IAction action;
    boolean newScreen;

    public ClientboundActionScreenPacket(IAction action, boolean newScreen) {
        this.action = action;
        this.newScreen = newScreen;
    }

    public ClientboundActionScreenPacket(FriendlyByteBuf friendlyByteBuf) {
        this.action = IActionSerializer.fromNetwork(friendlyByteBuf);
        this.newScreen = friendlyByteBuf.readBoolean();
    }

    @Override
    public MessageType getType() {
        return ArcNetworking.CLIENTBOUND_ACTION_SCREEN;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        IActionSerializer.toNetwork(action, buf);
        buf.writeBoolean(newScreen);
    }

    @Override
    @Environment(value= EnvType.CLIENT)
    public void handle(NetworkManager.PacketContext context) {
        if (newScreen) {
            Minecraft.getInstance().setScreen(new com.daqem.arc.client.gui.action.ActionScreen(ActionManager.getInstance().getActions(),
                    ActionManager.getInstance().getActions().stream().filter(a -> a.getLocation().equals(action.getLocation())).findFirst().orElse(null)));
        } else {
            Minecraft.getInstance().setScreen(new ActionScreen(null,
                    ActionManager.getInstance().getActions(),
                    action,
                    new Color(125, 35, 72, 0)));
        }
    }
}
