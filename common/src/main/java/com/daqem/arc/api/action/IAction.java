package com.daqem.arc.api.action;

import com.daqem.arc.api.action.data.ActionData;
import com.daqem.arc.api.action.holder.IActionHolderType;
import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.condition.ICondition;
import com.daqem.arc.api.reward.IReward;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public interface IAction {

    IActionType<?> getType();

    IActionSerializer<?> getSerializer();

    ActionResult perform(ActionData actionData);

    boolean metConditions(ActionData actionData);

    ActionResult applyRewards(ActionData actionData);

    IActionHolderType<?> getActionHolderType();

    ResourceLocation getActionHolderLocation();

    boolean shouldPerformOnClient();

    ResourceLocation getLocation();

    Component getName();

    Component getDescription();

    Component getShortDescription();

    List<IReward> getRewards();

    List<ICondition> getConditions();
}
