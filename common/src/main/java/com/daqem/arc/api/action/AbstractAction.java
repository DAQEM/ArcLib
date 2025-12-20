package com.daqem.arc.api.action;

import com.daqem.arc.Arc;
import com.daqem.arc.api.action.holder.IActionHolderType;
import com.daqem.arc.api.condition.ICondition;
import com.daqem.arc.api.reward.IReward;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.List;

public abstract class AbstractAction implements IAction {

    private final Identifier location;
    private final Identifier actionHolderLocation;
    private final IActionHolderType<?> actionHolderType;
    private final List<IReward> rewards;
    private final List<ICondition> conditions;

    public AbstractAction(Identifier location, Identifier actionHolderLocation, IActionHolderType<?> actionHolderType, List<IReward> rewards, List<ICondition> conditions) {
        this.location = location;
        this.actionHolderLocation = actionHolderLocation;
        this.actionHolderType = actionHolderType;
        this.rewards = rewards;
        this.conditions = conditions;
    }

    @Override
    public Identifier getIdentifier() {
        return location;
    }

    @Override
    public IActionHolderType<?> getActionHolderType() {
        return actionHolderType;
    }

    @Override
    public Identifier getActionHolderLocation() {
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
        return Arc.translatable("action." + this.getType().getIdentifier().getPath());
    }

    @Override
    public Component getDescription() {
        return Arc.translatable("action.description." + this.getType().getIdentifier().getPath());
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
        return obj instanceof IAction action && action.getIdentifier().equals(this.getIdentifier());
    }
}
