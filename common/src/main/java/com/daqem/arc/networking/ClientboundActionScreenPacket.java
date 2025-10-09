package com.daqem.arc.networking;

import com.daqem.arc.api.action.IAction;
import com.daqem.arc.data.ActionHolderManager;
import com.daqem.arc.api.action.IActionSerializer;
import com.daqem.arc.client.gui.action.ActionScreen;
import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public class ClientboundActionScreenPacket implements CustomPacketPayload {

    IAction action;

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundActionScreenPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull ClientboundActionScreenPacket decode(RegistryFriendlyByteBuf buf) {
            return new ClientboundActionScreenPacket(buf);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, ClientboundActionScreenPacket packet) {
            IActionSerializer.toNetwork(packet.action, buf);
        }
    };

    public ClientboundActionScreenPacket(IAction action) {
        this.action = action;
    }

    public ClientboundActionScreenPacket(RegistryFriendlyByteBuf friendlyByteBuf) {
        this.action = IActionSerializer.fromNetwork(friendlyByteBuf);
    }

    @Environment(EnvType.CLIENT)
    public static void handleClientSide(ClientboundActionScreenPacket packet, NetworkManager.PacketContext context) {
        Minecraft.getInstance().setScreen(new ActionScreen(
                ActionHolderManager.getInstance().getActions(),
                ActionHolderManager.getInstance().getActions().stream()
                        .filter(a -> a.getLocation().equals(packet.action.getLocation()))
                        .findFirst()
                        .orElse(null)));
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ArcNetworking.CLIENTBOUND_ACTION_SCREEN;
    }
}
