package com.daqem.arc.api.example;

import com.daqem.arc.event.events.RegistryEvent;

public class ExampleRegistryEvent {

    public static void registerActionHolderType() {
        RegistryEvent.REGISTER_ACTION_HOLDER_TYPE.register(ExampleActionTypes::init);
    }
}
