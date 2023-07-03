package com.daqem.arc.forge.registry;

import com.daqem.arc.registry.ArcRegistry;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.MappedRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.NewRegistryEvent;

public class ArcRegistryForge {

    @SubscribeEvent
    public static void onRegisterRegistries(NewRegistryEvent event) {
        ArcRegistry.initRegistry();
    }
}
