package com.daqem.arc.event;

import com.daqem.arc.api.action.IActionType;
import com.daqem.arc.api.action.data.ActionDataBuilder;
import com.daqem.arc.api.action.data.IActionDataType;
import com.daqem.arc.api.player.ArcServerPlayer;
import com.daqem.knot.Knot;
import com.daqem.knot.events.EventPriority;

public class AdvancementEvents {

    public static void registerEvents() {
        Knot.Events.Advancement.ADVANCEMENT.register((serverPlayer, advancementHolder) -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.ADVANCEMENT)
                        .withData(IActionDataType.ADVANCEMENT, advancementHolder)
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);
    }
}
