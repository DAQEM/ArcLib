package com.daqem.arc.api;

import com.daqem.arc.api.action.IAction;
import com.daqem.arc.api.action.holder.IActionHolder;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.Optional;

public interface IArcRegistryAccessor {

    Optional<IAction> getAction(Identifier actionLocation);

    Optional<IActionHolder> getActionHolder(Identifier holderLocation);

    List<IAction> getActions();

    List<IActionHolder> getActionHolders();
}
