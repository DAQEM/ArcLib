package com.daqem.arc.networking;

import com.daqem.arc.api.action.IAction;
import com.daqem.arc.api.action.holder.ActionHolderManager;
import com.daqem.arc.api.action.serializer.IActionSerializer;
import com.daqem.arc.data.ActionManager;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;

import java.util.List;

public class ClientboundUpdateActionsPacket extends BaseS2CMessage {

    private final List<IAction> actions;

    public ClientboundUpdateActionsPacket(List<IAction> actions) {
        this.actions = actions;
    }

    public ClientboundUpdateActionsPacket(FriendlyByteBuf friendlyByteBuf) {
        this.actions = friendlyByteBuf.readList(IActionSerializer::fromNetwork);
    }

    @Override
    public MessageType getType() {
        return ArcNetworking.CLIENTBOUND_UPDATE_ACTIONS;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeCollection(actions,
                (friendlyByteBuf, action) -> IActionSerializer.toNetwork(action, friendlyByteBuf));
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        if (!Minecraft.getInstance().isLocalServer()) {
            ActionHolderManager.getInstance().registerActions(actions);
        }
    }
}
