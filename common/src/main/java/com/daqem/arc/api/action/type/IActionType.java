package com.daqem.arc.api.action.type;

import com.daqem.arc.api.action.IAction;
import net.minecraft.resources.ResourceLocation;

public interface IActionType<T extends IAction> {

    ResourceLocation getLocation();
}
