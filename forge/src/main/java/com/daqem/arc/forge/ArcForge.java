package com.daqem.arc.forge;

import com.daqem.arc.Arc;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Arc.MOD_ID)
public class ArcForge {
    public ArcForge() {
        EventBuses.registerModEventBus(Arc.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        Arc.initCommon();

        DistExecutor.safeRunForDist(
                () -> SideProxyForge.Client::new,
                () -> SideProxyForge.Server::new
        );
    }
}
