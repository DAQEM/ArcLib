package com.daqem.arc.networking;

import com.daqem.arc.data.ActionHolderManager;
import com.daqem.arc.api.action.holder.IActionHolder;
import com.daqem.arc.api.action.holder.IActionHolderSerializer;
import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ClientboundUpdateActionHoldersPacket implements CustomPacketPayload {

    private final List<IActionHolder> actionHolders;

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundUpdateActionHoldersPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull ClientboundUpdateActionHoldersPacket decode(RegistryFriendlyByteBuf buf) {
            return new ClientboundUpdateActionHoldersPacket(buf);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, ClientboundUpdateActionHoldersPacket packet) {
            buf.writeCollection(packet.actionHolders,
                    (friendlyByteBuf, action) -> IActionHolderSerializer.toNetwork(action, (RegistryFriendlyByteBuf) friendlyByteBuf));
        }
    };

    public ClientboundUpdateActionHoldersPacket(List<IActionHolder> actionHolders) {
        this.actionHolders = actionHolders;
    }

    public ClientboundUpdateActionHoldersPacket(RegistryFriendlyByteBuf friendlyByteBuf) {
        this.actionHolders = friendlyByteBuf.readList(friendlyByteBuf1 -> IActionHolderSerializer.fromNetwork((RegistryFriendlyByteBuf) friendlyByteBuf1));
    }

    @Environment(EnvType.CLIENT)
    public static void handleClientSide(ClientboundUpdateActionHoldersPacket packet, NetworkManager.PacketContext context) {
        if (!Minecraft.getInstance().isLocalServer()) {
            ActionHolderManager.getInstance().registerActionHolders(packet.actionHolders);
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ArcNetworking.CLIENTBOUND_UPDATE_ACTION_HOLDERS;
    }
}
