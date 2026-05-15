package com.daqem.arc.api.action;

import com.daqem.arc.api.action.holder.IActionHolderType;
import com.daqem.arc.api.condition.ICondition;
import com.daqem.arc.api.reward.IReward;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public interface IAction {

    IActionType<?> getType();

    IActionSerializer<?> getSerializer();

    IActionHolderType<?> getActionHolderType();

    ResourceLocation getActionHolderLocation();

    boolean shouldPerformOnClient();

    ResourceLocation getResourceLocation();

    Component getName();

    Component getDescription();

    List<IReward> getRewards();

    List<ICondition> getConditions();
}
