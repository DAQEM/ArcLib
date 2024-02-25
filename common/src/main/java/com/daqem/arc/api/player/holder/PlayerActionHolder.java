package com.daqem.arc.api.player.holder;

import com.daqem.arc.api.action.IAction;
import com.daqem.arc.api.action.holder.IActionHolder;
import com.daqem.arc.api.action.holder.type.IActionHolderType;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerActionHolder implements IActionHolder {

    private final ResourceLocation location;
    // Made this a maps, so we don't get duplicate actions.
    private final Map<ResourceLocation, IAction> actions = new HashMap<>();

    public PlayerActionHolder(ResourceLocation location) {
        this.location = location;
    }

    @Override
    public ResourceLocation getLocation() {
        return location;
    }

    @Override
    public List<IAction> getActions() {
        return new ArrayList<>(actions.values());
    }

    @Override
    public void addAction(IAction action) {
        this.actions.put(action.getLocation(), action);
    }

    @Override
    public IActionHolderType<?> getType() {
        return PlayerActionTypes.PLAYER_ACTION_TYPE;
    }
}
