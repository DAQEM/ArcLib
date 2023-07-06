package com.daqem.arc.forge.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraftforge.event.AddReloadListenerEvent;

public interface ForgeReloadListenerEvent {

    /**
     * @see ForgeReloadListenerEvent.RegisterReloadListeners#registerReloadListener(AddReloadListenerEvent)
     */
    Event<RegisterReloadListeners> REGISTER_RELOAD_LISTENERS = EventFactory.createLoop();

    interface RegisterReloadListeners {
        /**
         * Invoked before the Arc reload listener is registered.
         */
        void registerReloadListener(AddReloadListenerEvent event);
    }
}
