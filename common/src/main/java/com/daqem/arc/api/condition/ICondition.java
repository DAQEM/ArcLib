package com.daqem.arc.api.condition;

import com.daqem.arc.api.action.data.ActionData;
import net.minecraft.network.chat.Component;

public interface ICondition {

    IConditionType<? extends ICondition> getType();

    IConditionSerializer<? extends ICondition> getSerializer();

    boolean isMet(ActionData actionData);

    boolean isInverted();

    Component getName();

    Component getDescription(Object... args);

    Component getDescription();
}
