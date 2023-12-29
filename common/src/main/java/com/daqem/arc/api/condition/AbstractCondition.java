package com.daqem.arc.api.condition;

import com.daqem.arc.Arc;
import net.minecraft.network.chat.Component;

public abstract class AbstractCondition implements ICondition {

    private final boolean inverted;

    public AbstractCondition(boolean inverted) {
        this.inverted = inverted;
    }

    @Override
    public boolean isInverted() {
        return inverted;
    }

    @Override
    public Component getName() {
        return Arc.translatable("condition." + this.getType().getLocation().getPath());
    }

    @Override
    public Component getDescription() {
        return Arc.translatable("condition.description." + this.getType().getLocation().getPath());
    }
}
