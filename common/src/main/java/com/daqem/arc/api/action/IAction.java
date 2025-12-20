package com.daqem.arc.api.action;

import com.daqem.arc.api.action.holder.IActionHolderType;
import com.daqem.arc.api.condition.ICondition;
import com.daqem.arc.api.reward.IReward;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.List;

public interface IAction {

    IActionType<?> getType();

    IActionSerializer<?> getSerializer();

    IActionHolderType<?> getActionHolderType();

    Identifier getActionHolderLocation();

    boolean shouldPerformOnClient();

    Identifier getIdentifier();

    Component getName();

    Component getDescription();

    List<IReward> getRewards();

    List<ICondition> getConditions();
}
