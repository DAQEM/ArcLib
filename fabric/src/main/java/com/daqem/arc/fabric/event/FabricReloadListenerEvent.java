package com.daqem.arc.fabric.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;

public interface FabricReloadListenerEvent {

    /**
     * @see FabricReloadListenerEvent.RegisterReloadListeners#registerReloadListener(ResourceManagerHelper)
     */
    Event<FabricReloadListenerEvent.RegisterReloadListeners> REGISTER_RELOAD_LISTENERS = EventFactory.createLoop();

    interface RegisterReloadListeners {
        /**
         * Invoked before the Arc reload listener is registered.
         */
        void registerReloadListener(ResourceManagerHelper helper);
    }
}
