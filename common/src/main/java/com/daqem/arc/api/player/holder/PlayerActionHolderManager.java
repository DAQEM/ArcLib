package com.daqem.arc.api.player.holder;

import com.daqem.arc.Arc;
import com.daqem.arc.api.action.holder.ActionHolderManager;
import com.daqem.arc.api.action.holder.IActionHolder;

import java.util.ArrayList;
import java.util.List;

public class PlayerActionHolderManager {

    private static final List<IActionHolder> actionHolders = new ArrayList<>();
    private static PlayerActionHolderManager instance;

    public PlayerActionHolderManager() {
        instance = this;
        actionHolders.add(new PlayerActionHolder(Arc.getId("player")));

        actionHolders.forEach(ActionHolderManager.getInstance()::registerActionHolder);
    }

    public List<IActionHolder> getActionHolders() {
        return actionHolders;
    }

    public static PlayerActionHolderManager getInstance() {
        return instance == null ? new PlayerActionHolderManager() : instance;
    }
}
