package com.daqem.arc.api.action.holder.type;

import com.daqem.arc.api.action.holder.IActionHolder;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public interface IActionHolderType<T extends IActionHolder> {

    ResourceLocation getLocation();

    List<IActionHolder> getActionHolders();
}
