package com.daqem.arc.api.action.data;

import com.daqem.arc.api.action.IActionType;
import com.daqem.arc.api.player.ArcPlayer;
import com.daqem.arc.data.ActionData;

import java.util.HashMap;
import java.util.Map;

public final class ActionDataBuilder {

    private final ArcPlayer player;
    private final IActionType<?> actionType;
    private final Map<IActionDataType<?>, Object> actionData;

    public ActionDataBuilder(ArcPlayer player, IActionType<?> actionType) {
        this.player = player;
        this.actionType = actionType;
        this.actionData = new HashMap<>();
    }

    public <T> ActionDataBuilder withData(IActionDataType<T> actionDataType, T value) {
        this.actionData.put(actionDataType, value);
        return this;
    }

    public ActionData build() {
        return new ActionData(this.player, this.actionType, this.actionData);
    }
}
