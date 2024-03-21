package com.daqem.arc.networking;

import com.daqem.arc.api.action.holder.ActionHolderManager;
import com.daqem.arc.api.action.holder.IActionHolder;
import com.daqem.arc.api.player.ArcClientPlayer;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class ClientboundSyncPlayerActionHoldersPacket extends BaseS2CMessage {

    private final List<IActionHolder> actionHolders;

    public ClientboundSyncPlayerActionHoldersPacket(List<IActionHolder> actionHolders) {
        this.actionHolders = actionHolders;
    }

    public ClientboundSyncPlayerActionHoldersPacket(FriendlyByteBuf friendlyByteBuf) {
        List<ResourceLocation> actionHolderLocations = new ArrayList<>();
        int size = friendlyByteBuf.readInt();
        for (int i = 0; i < size; i++) {
            actionHolderLocations.add(friendlyByteBuf.readResourceLocation());
        }
        this.actionHolders = ActionHolderManager.getInstance().getActionHolders(actionHolderLocations);
    }

    @Override
    public MessageType getType() {
        return ArcNetworking.CLIENTBOUND_SYNC_PLAYER_ACTION_HOLDERS;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(actionHolders.size());
        for (IActionHolder actionHolder : actionHolders) {
            buf.writeResourceLocation(actionHolder.getLocation());
        }
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        if (Minecraft.getInstance().player instanceof ArcClientPlayer arcClientPlayer) {
            arcClientPlayer.arc$clearActionHolders();
            arcClientPlayer.arc$addActionHolders(actionHolders);
        }
    }
}
