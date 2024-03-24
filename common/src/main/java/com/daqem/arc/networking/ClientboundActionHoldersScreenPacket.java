package com.daqem.arc.networking;

import com.daqem.arc.api.action.holder.IActionHolder;
import com.daqem.arc.api.action.holder.serializer.IActionHolderSerializer;
import com.daqem.arc.api.action.serializer.IActionSerializer;
import com.daqem.arc.client.gui.holder.ActionHoldersScreen;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;

import java.util.List;

public class ClientboundActionHoldersScreenPacket extends BaseS2CMessage {

    private final List<IActionHolder> actionHolders;

    public ClientboundActionHoldersScreenPacket(List<IActionHolder> actionHolders) {
        this.actionHolders = actionHolders;
    }

    public ClientboundActionHoldersScreenPacket(FriendlyByteBuf buf) {
        this.actionHolders = buf.readList(IActionHolderSerializer::fromNetwork);
    }

    @Override
    public MessageType getType() {
        return ArcNetworking.CLIENTBOUND_ACTION_HOLDERS_SCREEN_PACKET;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeCollection(actionHolders,
                (friendlyByteBuf, action) -> IActionHolderSerializer.toNetwork(action, friendlyByteBuf));
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        Minecraft.getInstance().setScreen(new ActionHoldersScreen(actionHolders));
    }
}
