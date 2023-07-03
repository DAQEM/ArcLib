package com.daqem.arc.fabric;

import com.daqem.arc.ArcExpectPlatform;
import com.daqem.arc.fabric.data.ActionManagerFabric;
import com.daqem.arc.data.ActionManager;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class ArcExpectPlatformImpl {
    /**
     * This is our actual method to {@link ArcExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }

    public static ActionManager getActionManager() {
        return new ActionManagerFabric();
    }
}
