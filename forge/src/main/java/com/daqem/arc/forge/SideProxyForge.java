package com.daqem.arc.forge;

import com.daqem.arc.Arc;
import com.daqem.arc.api.example.ExampleActionHolderManager;
import com.daqem.arc.client.ArcClient;
import com.daqem.arc.forge.data.ActionManagerForge;
import com.daqem.arc.forge.event.ForgeReloadListenerEvent;
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

        if (Arc.isDevEnvironment()) {
            ForgeReloadListenerEvent.REGISTER_RELOAD_LISTENERS.register((manager) -> {
                ExampleActionHolderManager.getInstance();
            });
        }
    }

    public void onAddReloadListeners(AddReloadListenerEvent event) {
        ForgeReloadListenerEvent.REGISTER_RELOAD_LISTENERS.invoker().registerReloadListener(event);

        event.addListener(new ActionManagerForge());
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
