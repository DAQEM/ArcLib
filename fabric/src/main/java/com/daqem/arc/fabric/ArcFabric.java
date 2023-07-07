package com.daqem.arc.fabric;

import com.daqem.arc.Arc;
import com.daqem.arc.api.example.ExampleActionHolderManager;
import com.daqem.arc.fabric.event.FabricReloadListenerEvent;
import com.daqem.arc.fabric.data.ActionManagerFabric;
import com.daqem.arc.registry.ArcRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.DynamicRegistrySetupCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;

public class ArcFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Arc.initCommon();

        if (Arc.isDevEnvironment()) {
            FabricReloadListenerEvent.REGISTER_RELOAD_LISTENERS.register((manager) -> {
                ExampleActionHolderManager.getInstance();
            });
        }

        DynamicRegistrySetupCallback.EVENT.register(registryManager -> {
            ArcRegistry.init();

            ResourceManagerHelper helper = ResourceManagerHelper.get(PackType.SERVER_DATA);
            FabricReloadListenerEvent.REGISTER_RELOAD_LISTENERS.invoker().registerReloadListener(helper);

            helper.registerReloadListener(new ActionManagerFabric());
        });


    }
}
