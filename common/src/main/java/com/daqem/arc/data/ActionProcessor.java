package com.daqem.arc.data;

import com.daqem.arc.Arc;
import com.daqem.arc.api.action.IAction;
import com.daqem.arc.api.action.holder.IActionHolder;
import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.reward.IReward;

public class ActionProcessor {

    private static final ActionProcessor INSTANCE = new ActionProcessor();

    private ActionProcessor() {}

    public static ActionProcessor getInstance() {
        return INSTANCE;
    }

    public ActionResult process(IAction action, ActionData actionData) {
        IActionHolder sourceActionHolder = actionData.getPlayer().arc$getActionHolders().stream()
                .filter(holder -> holder.getType() == action.getActionHolderType()
                        && holder.getLocation().equals(action.getActionHolderLocation()))
                .findFirst()
                .orElse(null);

        if (sourceActionHolder == null) {
            // This can happen if an action is defined for a holder that the player doesn't have.
            // It's not an error, just a case where the action doesn't apply.
            return new ActionResult();
        }

        actionData.setSourceActionHolder(sourceActionHolder);

        ActionResult result = new ActionResult();

        //noinspection resource
        if (actionData.getPlayer().arc$getLevel().isClientSide() && !action.shouldPerformOnClient()) {
            return result;
        }

        if (!sourceActionHolder.passedHolderCondition(actionData)) {
            return result;
        }

        if (action.getConditions().stream().allMatch(condition -> condition.isMet(actionData) != condition.isInverted())) {
            if (Arc.isDebugEnvironment()) {
                Arc.LOGGER.info("Action {} passed conditions for action holder {}", action.getLocation(), action.getActionHolderLocation());
            }
            result = applyRewards(action, actionData);
        }

        return result;
    }

    private ActionResult applyRewards(IAction action, ActionData actionData) {
        return action.getRewards().stream()
                .sorted((r1, r2) -> Integer.compare(r2.getPriority(), r1.getPriority()))
                .map(reward -> applyReward(reward, actionData))
                .reduce(new ActionResult(), ActionResult::merge);
    }

    private ActionResult applyReward(IReward reward, ActionData actionData) {
        if (reward.passedChance(actionData)) {
            return reward.apply(actionData);
        }
        return new ActionResult();
    }
}
