package com.daqem.arc.api.reward;

import com.daqem.arc.data.ActionData;
import com.daqem.arc.api.action.result.ActionResult;
import net.minecraft.network.chat.Component;

public interface IReward {

    IRewardType<?> getType();

    IRewardSerializer<? extends IReward> getSerializer();

    double getChance();

    int getPriority();

    ActionResult apply(ActionData actionData);

    boolean passedChance(ActionData actionData);

    Component getName();

    Component getDescription(Object... args);

    Component getDescription();
}
