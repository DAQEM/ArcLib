package com.daqem.arc.networking;

import com.daqem.arc.api.action.IAction;
import com.daqem.arc.api.action.holder.ActionHolderManager;
import com.daqem.arc.api.action.serializer.IActionSerializer;
import com.daqem.arc.client.gui.action.ActionScreen;
import com.daqem.arc.data.ActionManager;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;

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
    @Environment(value= EnvType.CLIENT)
    public void handle(NetworkManager.PacketContext context) {
        Minecraft.getInstance().setScreen(new ActionScreen(
                ActionHolderManager.getInstance().getActions(),
                ActionHolderManager.getInstance().getActions().stream()
                        .filter(a -> a.getLocation().equals(action.getLocation()))
                        .findFirst()
                        .orElse(null)));
    }
}
