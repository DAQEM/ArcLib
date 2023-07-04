package com.daqem.arc;

import com.daqem.arc.event.EventPlayerJoin;
import com.daqem.arc.event.command.EventRegisterCommands;
import com.daqem.arc.event.triggers.AdvancementEvents;
import com.daqem.arc.event.triggers.BlockEvents;
import com.daqem.arc.event.triggers.EntityEvents;
import com.daqem.arc.event.triggers.ItemEvents;
import com.daqem.arc.networking.ArcNetworking;
import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public class Arc {
    public static final String MOD_ID = "arc";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final boolean DEV_ENVIRONMENT = false;

    public static void initCommon() {
        registerEvents();
        ArcNetworking.init();
    }

    private static void registerEvents() {
        EventRegisterCommands.registerEvent();

        BlockEvents.registerEvents();
        ItemEvents.registerEvents();
        EntityEvents.registerEvents();
        AdvancementEvents.registerEvents();


        EventPlayerJoin.registerEvent();
    }

    public static ResourceLocation getId(String id) {
        return new ResourceLocation(MOD_ID, id);
    }

    public static MutableComponent translatable(String str) {
        return Component.translatable(MOD_ID + "." + str);
    }

    public static MutableComponent translatable(String str, Object... objects) {
        return Component.translatable(MOD_ID + "." + str, objects);
    }

    public static MutableComponent literal(String str) {
        return Component.literal(str);
    }

    public static boolean isDevEnvironment() {
        return DEV_ENVIRONMENT;
    }
}
