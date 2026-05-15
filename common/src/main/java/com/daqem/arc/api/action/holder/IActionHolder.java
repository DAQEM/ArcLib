package com.daqem.arc.api.action.holder;

import com.daqem.arc.api.action.IAction;
import com.daqem.arc.data.ActionData;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public interface IActionHolder {

    ResourceLocation getResourceLocation();

    List<IAction> getActions();

    void addAction(IAction action);

    void removeAction(IAction action);

    void clearActions();

    IActionHolderType<?> getType();

    IActionHolderSerializer<?> getSerializer();

    default boolean passedHolderCondition(ActionData actionData) {
        return true;
    }

    void addActions(List<IAction> actionHolderActions);
}
