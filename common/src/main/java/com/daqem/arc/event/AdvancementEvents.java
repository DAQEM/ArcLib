package com.daqem.arc.event;

import com.daqem.arc.api.action.IActionType;
import com.daqem.arc.api.player.ArcServerPlayer;
import com.daqem.arc.api.action.data.ActionDataBuilder;
import com.daqem.arc.api.action.data.IActionDataType;
import dev.architectury.event.events.common.PlayerEvent;

public class AdvancementEvents {

    public static void registerEvents() {
        PlayerEvent.PLAYER_ADVANCEMENT.register((player, advancement) -> {
            if (player instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.ADVANCEMENT)
                        .withData(IActionDataType.ADVANCEMENT, advancement)
                        .build()
                        .sendToAction();
            }
        });
    }
}
