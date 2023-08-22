package com.daqem.arc.forge;

import com.daqem.arc.Arc;
import com.daqem.arc.api.example.ExampleActionHolderManager;
import com.daqem.arc.client.ArcClient;
import com.daqem.arc.forge.data.ActionManagerForge;
import com.daqem.arc.registry.ArcRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.NewRegistryEvent;

public class SideProxyForge {

    SideProxyForge() {
        IEventBus eventBus = MinecraftForge.EVENT_BUS;
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        eventBus.addListener(this::onAddReloadListeners);
        modEventBus.addListener(this::onRegisterRegistries);
    }

    public void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new ActionManagerForge());

        if (Arc.isDebugEnvironment()) {
            ExampleActionHolderManager.getInstance();
        }
    }

    @SubscribeEvent
    public void onRegisterRegistries(NewRegistryEvent event) {
        ArcRegistry.init();
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
