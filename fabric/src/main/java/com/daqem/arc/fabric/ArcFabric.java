package com.daqem.arc.fabric;

import com.daqem.arc.Arc;
import com.daqem.arc.fabric.data.ActionManagerFabric;
import com.daqem.arc.registry.ArcRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;

public class ArcFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Arc.initCommon();
        ArcRegistry.init();
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new ActionManagerFabric());
    }
}
