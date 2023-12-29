package com.daqem.arc.api.player.holder;

import com.daqem.arc.Arc;
import com.daqem.arc.api.action.holder.type.ActionHolderType;

public class PlayerActionTypes {

    public static final ActionHolderType<PlayerActionHolder> PLAYER_ACTION_TYPE =
            ActionHolderType.register(Arc.getId("player"));

    public static void init() {
    }
}
