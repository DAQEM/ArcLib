package com.daqem.arc.forge;

import com.daqem.arc.Arc;
import com.daqem.arc.api.player.holder.PlayerActionHolderManager;
import com.daqem.arc.client.ArcClient;
import com.daqem.arc.command.argument.ActionArgument;
import com.daqem.arc.forge.data.ActionManagerForge;
import com.daqem.arc.registry.ArcRegistry;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.NewRegistryEvent;

public class SideProxyForge {

    SideProxyForge() {
        IEventBus eventBus = MinecraftForge.EVENT_BUS;
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        eventBus.addListener(this::onAddReloadListeners);
        modEventBus.addListener(this::onRegisterRegistries);

        registerCommandArgumentTypes();
    }

    private void registerCommandArgumentTypes() {
        DeferredRegister<ArgumentTypeInfo<?, ?>> argTypeRegistry = DeferredRegister.create(Registries.COMMAND_ARGUMENT_TYPE, Arc.MOD_ID);
        argTypeRegistry.register("action", () -> ArgumentTypeInfos.registerByClass(ActionArgument.class, SingletonArgumentInfo.contextFree(ActionArgument::action)));
        argTypeRegistry.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new ActionManagerForge());

        if (Arc.isDebugEnvironment()) {
            PlayerActionHolderManager.getInstance();
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
