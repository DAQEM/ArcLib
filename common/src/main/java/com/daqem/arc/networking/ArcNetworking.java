package com.daqem.arc.networking;

import com.daqem.arc.Arc;
import dev.architectury.networking.simple.MessageType;
import dev.architectury.networking.simple.SimpleNetworkManager;

public interface ArcNetworking {

    SimpleNetworkManager NETWORK = SimpleNetworkManager.create(Arc.MOD_ID);

    MessageType CLIENTBOUND_UPDATE_ACTIONS = NETWORK.registerS2C("clientbound_update_actions", ClientboundUpdateActionsPacket::new);
    MessageType CLIENTBOUND_ACTION_SCREEN = NETWORK.registerS2C("clientbound_action_screen", ClientboundActionScreenPacket::new);

    static void init() {
    }
}
