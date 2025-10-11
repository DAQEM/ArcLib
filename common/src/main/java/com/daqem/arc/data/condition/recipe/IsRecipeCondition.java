package com.daqem.arc.data.condition.recipe;

import com.daqem.arc.api.condition.AbstractCondition;

public abstract class IsRecipeCondition extends AbstractCondition {

    public IsRecipeCondition(boolean inverted) {
        super(inverted);
    }
}
