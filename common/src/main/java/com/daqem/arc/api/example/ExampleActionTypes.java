package com.daqem.arc.api.example;

import com.daqem.arc.api.action.holder.type.ActionHolderType;
import net.minecraft.resources.ResourceLocation;

public class ExampleActionTypes {

    public static final ActionHolderType<ExampleActionHolder> EXAMPLE_ACTION_TYPE =
            ActionHolderType.register(new ResourceLocation("example:example_action_type"));

    public static void init() {
    }
}
