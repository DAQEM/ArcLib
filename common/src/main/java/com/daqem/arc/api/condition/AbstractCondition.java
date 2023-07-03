package com.daqem.arc.api.condition;

public abstract class AbstractCondition implements ICondition {

    private final boolean inverted;

    public AbstractCondition(boolean inverted) {
        this.inverted = inverted;
    }

    @Override
    public boolean isInverted() {
        return inverted;
    }
}
