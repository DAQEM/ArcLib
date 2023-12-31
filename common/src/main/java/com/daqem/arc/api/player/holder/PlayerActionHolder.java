package com.daqem.arc.api.player.holder;

import com.daqem.arc.api.action.IAction;
import com.daqem.arc.api.action.holder.IActionHolder;
import com.daqem.arc.api.action.holder.type.IActionHolderType;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class PlayerActionHolder implements IActionHolder {

    private final ResourceLocation location;
    private final List<IAction> actions = new ArrayList<>();

    public PlayerActionHolder(ResourceLocation location) {
        this.location = location;
    }

    @Override
    public ResourceLocation getLocation() {
        return location;
    }

    @Override
    public List<IAction> getActions() {
        return actions;
    }

    @Override
    public void addAction(IAction action) {
        this.actions.add(action);
    }

    @Override
    public IActionHolderType<?> getType() {
        return PlayerActionTypes.PLAYER_ACTION_TYPE;
    }
}
