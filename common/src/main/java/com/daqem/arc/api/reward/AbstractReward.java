package com.daqem.arc.api.reward;

import com.daqem.arc.api.action.data.ActionData;
import com.daqem.arc.api.action.result.ActionResult;

public abstract class AbstractReward implements IReward {

    private final double chance;
    private final int priority;

    public AbstractReward(double chance, int priority) {
        this.chance = chance;
        this.priority = priority;
    }

    @Override
    public double getChance() {
        return chance;
    }

    public int getPriority() {
        return priority;
    }

    public abstract ActionResult apply(ActionData actionData);

    public boolean passedChance(ActionData actionData) {
        return chance == 100 || actionData.getPlayer().arc$nextRandomDouble() * 100 <= chance;
    }
}
