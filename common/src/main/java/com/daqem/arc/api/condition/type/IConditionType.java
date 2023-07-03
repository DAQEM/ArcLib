package com.daqem.arc.api.condition.type;

import com.daqem.arc.api.condition.ICondition;
import net.minecraft.resources.ResourceLocation;

public interface IConditionType<T extends ICondition> {

    ResourceLocation getLocation();
}
