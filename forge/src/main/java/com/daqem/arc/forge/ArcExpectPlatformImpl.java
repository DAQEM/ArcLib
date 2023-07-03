package com.daqem.arc.forge;

import com.daqem.arc.ArcExpectPlatform;
import com.daqem.arc.forge.data.ActionManagerForge;
import com.daqem.arc.data.ActionManager;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class ArcExpectPlatformImpl {
    /**
     * This is our actual method to {@link ArcExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }

    public static ActionManager getActionManager() {
        return new ActionManagerForge();
    }
}