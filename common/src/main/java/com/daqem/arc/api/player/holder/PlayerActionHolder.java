package com.daqem.arc.api.player.holder;

import com.daqem.arc.api.action.holder.AbstractActionHolder;
import com.daqem.arc.api.action.holder.type.IActionHolderType;
import net.minecraft.resources.ResourceLocation;

public class PlayerActionHolder extends AbstractActionHolder {

    public PlayerActionHolder(ResourceLocation location) {
        super(location);
    }

    @Override
    public IActionHolderType<?> getType() {
        return PlayerActionTypes.PLAYER_ACTION_TYPE;
    }
}
