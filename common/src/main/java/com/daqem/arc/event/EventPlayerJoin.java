package com.daqem.arc.event;

import com.daqem.arc.Arc;
import com.daqem.arc.data.ActionManager;
import com.daqem.arc.networking.ClientboundUpdateActionsPacket;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.PlayerEvent;
import net.minecraft.server.dedicated.DedicatedServer;

public class EventPlayerJoin {

    public static void registerEvent() {
        PlayerEvent.PLAYER_JOIN.register((player) -> {
            if (player.server instanceof DedicatedServer) {
                new ClientboundUpdateActionsPacket(ActionManager.getInstance().getActions()).sendTo(player);
            }
        });
    }
}


