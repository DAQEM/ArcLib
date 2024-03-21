package com.daqem.arc.networking;

import com.daqem.arc.Arc;
import dev.architectury.networking.simple.MessageType;
import dev.architectury.networking.simple.SimpleNetworkManager;

public interface ArcNetworking {

    SimpleNetworkManager NETWORK = SimpleNetworkManager.create(Arc.MOD_ID);

    MessageType CLIENTBOUND_UPDATE_ACTIONS = NETWORK.registerS2C("clientbound_update_actions", ClientboundUpdateActionsPacket::new);
    MessageType CLIENTBOUND_UPDATE_ACTION_HOLDERS = NETWORK.registerS2C("clientbound_update_action_holders", ClientboundUpdateActionHoldersPacket::new);
    MessageType CLIENTBOUND_ACTION_SCREEN = NETWORK.registerS2C("clientbound_action_screen", ClientboundActionScreenPacket::new);
    MessageType CLIENTBOUND_SYNC_PLAYER_ACTION_HOLDERS = NETWORK.registerS2C("clientbound_sync_player_action_holders", ClientboundSyncPlayerActionHoldersPacket::new);

    static void init() {
    }
}
