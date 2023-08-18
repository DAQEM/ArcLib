package com.daqem.arc.api.action.holder;

import com.daqem.arc.api.action.IAction;
import com.daqem.arc.api.action.data.ActionData;
import com.daqem.arc.api.action.holder.type.IActionHolderType;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public interface IActionHolder {

    ResourceLocation getLocation();

    List<IAction> getActions();

    void addAction(IAction action);

    IActionHolderType<?> getType();

    default boolean passedHolderCondition(ActionData actionData) {
        return true;
    }
}
