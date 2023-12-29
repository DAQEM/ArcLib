package com.daqem.arc.api.player.holder;

import com.daqem.arc.event.events.RegistryEvent;

public class PlayerRegistryEvent {

    public static void registerActionHolderType() {
        RegistryEvent.REGISTER_ACTION_HOLDER_TYPE.register(PlayerActionTypes::init);
    }
}
