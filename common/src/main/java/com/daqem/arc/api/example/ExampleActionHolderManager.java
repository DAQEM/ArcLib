package com.daqem.arc.api.example;

import com.daqem.arc.api.action.holder.IActionHolder;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class ExampleActionHolderManager {

    private static final List<IActionHolder> actionHolders = new ArrayList<>();
    private static ExampleActionHolderManager instance;

    public ExampleActionHolderManager() {
        instance = this;
        actionHolders.add(new ExampleActionHolder(new ResourceLocation("example:example_action_holder_1")));
        actionHolders.add(new ExampleActionHolder(new ResourceLocation("example:example_action_holder_2")));
        actionHolders.add(new ExampleActionHolder(new ResourceLocation("example:example_action_holder_3")));
        actionHolders.add(new ExampleActionHolder(new ResourceLocation("example:example_action_holder_4")));
    }

    public List<IActionHolder> getActionHolders() {
        return actionHolders;
    }

    public static ExampleActionHolderManager getInstance() {
        return instance == null ? new ExampleActionHolderManager() : instance;
    }
}
