package com.daqem.arc.networking;

import com.daqem.arc.api.action.holder.ActionHolderManager;
import com.daqem.arc.api.action.holder.IActionHolder;
import com.daqem.arc.api.action.holder.serializer.IActionHolderSerializer;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;

import java.util.List;

public class ClientboundUpdateActionHoldersPacket extends BaseS2CMessage {

    private final List<IActionHolder> actionsHolders;

    public ClientboundUpdateActionHoldersPacket(List<IActionHolder> actionsHolders) {
        this.actionsHolders = actionsHolders;
    }

    public ClientboundUpdateActionHoldersPacket(FriendlyByteBuf friendlyByteBuf) {
        this.actionsHolders = friendlyByteBuf.readList(IActionHolderSerializer::fromNetwork);
    }

    @Override
    public MessageType getType() {
        return ArcNetworking.CLIENTBOUND_UPDATE_ACTION_HOLDERS;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeCollection(actionsHolders,
                (friendlyByteBuf, action) -> IActionHolderSerializer.toNetwork(action, friendlyByteBuf));
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        if (!Minecraft.getInstance().isLocalServer()) {
            ActionHolderManager.getInstance().registerActionHolders(actionsHolders);
        }
    }
}
