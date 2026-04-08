package com.daqem.arc;

import com.daqem.arc.api.IArcRegistryAccessor;
import com.daqem.arc.command.ArcCommand;
import com.daqem.arc.config.ArcCommonConfig;
import com.daqem.arc.data.ActionHolderManager;
import com.daqem.arc.data.ActionManager;
import com.daqem.arc.data.PlayerActionHolderManager;
import com.daqem.arc.event.*;
import com.daqem.arc.networking.ArcNetworking;
import com.daqem.arc.registry.ArcRegistry;
import com.daqem.arc.registry.EntityDataRegistry;
import com.daqem.knot.Knot;

public class Arc {
    public static final String MOD_ID = "arc";
    public static final Knot API = new Knot(MOD_ID);
    public static boolean DEBUG = false;

    public static void init() {
        ArcCommonConfig.init();
        ArcNetworking.init();
        registerEvents();
        ArcRegistry.init();
        EntityDataRegistry.init();
        Knot.RELOAD_REGISTRY.registerData(Arc.API.getId("please_do_not_use_this"), new PlayerActionHolderManager());
        Knot.RELOAD_REGISTRY.registerData(Arc.API.getId(MOD_ID), new ActionManager());
    }

    private static void registerEvents() {
        Knot.Events.Server.COMMAND_REGISTER.register(ArcCommand::registerCommand);

        AdvancementEvents.registerEvents();
        BlockEvents.registerEvents();
        EntityEvents.registerEvents();
        ItemEvents.registerEvents();
        MovementEvents.registerEvents();
        PlayerEvents.registerEvents();
        NextTickScheduler.registerEvent();
    }

    @SuppressWarnings("unused")
    public static IArcRegistryAccessor getRegistryAccessor() {
        return ActionHolderManager.getInstance();
    }
}
