package com.daqem.arc.forge;

import com.daqem.arc.client.ArcClient;
import com.daqem.arc.forge.registry.ArcRegistryForge;
import com.daqem.arc.forge.data.ActionManagerForge;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class SideProxyForge {

    SideProxyForge() {
        IEventBus eventBus = MinecraftForge.EVENT_BUS;
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::onAddReloadListeners);

        modEventBus.addListener(ArcRegistryForge::onRegisterRegistries);
    }

    public void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new ActionManagerForge());
    }

    public static class Server extends SideProxyForge {
        Server() {

        }

    }

    public static class Client extends SideProxyForge {

        Client() {
            ArcClient.init();
        }
    }
}
