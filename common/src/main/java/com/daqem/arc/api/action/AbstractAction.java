package com.daqem.arc.api.action;

import com.daqem.arc.Arc;
import com.daqem.arc.api.action.holder.IActionHolderType;
import com.daqem.arc.api.condition.ICondition;
import com.daqem.arc.api.reward.IReward;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public abstract class AbstractAction implements IAction {

    private final ResourceLocation location;
    private final ResourceLocation actionHolderLocation;
    private final IActionHolderType<?> actionHolderType;
    private final List<IReward> rewards;
    private final List<ICondition> conditions;

    public AbstractAction(ResourceLocation location, ResourceLocation actionHolderLocation, IActionHolderType<?> actionHolderType, List<IReward> rewards, List<ICondition> conditions) {
        this.location = location;
        this.actionHolderLocation = actionHolderLocation;
        this.actionHolderType = actionHolderType;
        this.rewards = rewards;
        this.conditions = conditions;
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return location;
    }

    @Override
    public IActionHolderType<?> getActionHolderType() {
        return actionHolderType;
    }

    @Override
    public ResourceLocation getActionHolderLocation() {
        return actionHolderLocation;
    }

    @Override
    public boolean shouldPerformOnClient() {
        return false;
    }

    @Override
    public IActionSerializer<?> getSerializer() {
        return getType().getSerializer();
    }

    @Override
    public Component getName() {
        return Arc.API.translatable("action." + this.getType().getResourceLocation().getPath());
    }

    @Override
    public Component getDescription() {
        return Arc.API.translatable("action.description." + this.getType().getResourceLocation().getPath());
    }

    @Override
    public List<IReward> getRewards() {
        return rewards;
    }

    @Override
    public List<ICondition> getConditions() {
        return conditions;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof IAction action && action.getResourceLocation().equals(this.getResourceLocation());
    }
}
