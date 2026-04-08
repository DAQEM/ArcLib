package com.daqem.arc.event;

import com.daqem.arc.api.action.IActionType;
import com.daqem.arc.api.action.data.ActionDataBuilder;
import com.daqem.arc.api.action.data.IActionDataType;
import com.daqem.arc.api.event.ArcAdvancementEvent;
import com.daqem.arc.api.event.EventPriority;
import com.daqem.arc.api.player.ArcServerPlayer;
import dev.architectury.event.events.common.PlayerEvent;

public class AdvancementEvents {

    public static void registerEvents() {
        PlayerEvent.PLAYER_ADVANCEMENT.register((serverPlayer, advancementHolder) ->
                ArcAdvancementEvent.ADVANCEMENT.invoker().onAdvancement(serverPlayer, advancementHolder));

        ArcAdvancementEvent.ADVANCEMENT.register((serverPlayer, advancementHolder) -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.ADVANCEMENT)
                        .withData(IActionDataType.ADVANCEMENT, advancementHolder)
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);
    }
}
