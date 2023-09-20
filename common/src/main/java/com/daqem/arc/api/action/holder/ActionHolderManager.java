package com.daqem.arc.api.action.holder;

import com.daqem.arc.api.action.IAction;
import com.daqem.arc.api.action.holder.type.IActionHolderType;
import com.daqem.arc.data.ActionManager;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ActionHolderManager {

    private final Map<IActionHolderType<?>, Map<ResourceLocation, IActionHolder>> actionHolders = new HashMap<>();

    private static ActionHolderManager instance;

    private ActionHolderManager() {
    }

    public static ActionHolderManager getInstance() {
        if (instance == null) {
            instance = new ActionHolderManager();
        }
        return instance;
    }

    /**
     * Registers an action holder.
     *
     * @param actionHolder The action holder to be registered.
     */
    public void registerActionHolder(IActionHolder actionHolder) {
        if (this.actionHolders.containsKey(actionHolder.getType())) {
            this.actionHolders.get(actionHolder.getType()).put(actionHolder.getLocation(), actionHolder);
        } else {
            Map<ResourceLocation, IActionHolder> actionHolders = new HashMap<>();
            actionHolders.put(actionHolder.getLocation(), actionHolder);
            this.actionHolders.put(actionHolder.getType(), actionHolders);
        }
        ActionManager.getInstance().assignActionsToActionHolders();
    }

    /**
     * Registers an action with the specified action holder.
     *
     * @param action The action to be registered.
     */
    public void registerAction(IAction action) {
        IActionHolder actionHolder = getActionHolder(action.getActionHolderType(), action.getActionHolderLocation());
        if (actionHolder != null) {
            if (!actionHolder.getActions().contains(action)) {
                actionHolder.addAction(action);
            }
        }
    }

    /**
     * Retrieves a list of action holders for the specified action holder type.
     *
     * @param actionHolderType The type of action holder to retrieve.
     * @return The list of action holders for the specified type, or {@code null} if no action holders were found.
     */
    @SuppressWarnings("unused")
    public @Nullable List<IActionHolder> getActionHolders(IActionHolderType<?> actionHolderType) {
        if (!actionHolders.containsKey(actionHolderType)) return null;
        return new ArrayList<>(actionHolders.get(actionHolderType).values());
    }

    /**
     * Retrieves an action holder for the specified action holder type and location.
     *
     * @param actionHolderType The type of action holder to retrieve.
     * @param actionHolderLocation The location of the action holder to retrieve.
     * @return The action holder with the specified type and location, or {@code null} if no action holder was found.
     */
    public @Nullable IActionHolder getActionHolder(IActionHolderType<?> actionHolderType, ResourceLocation actionHolderLocation) {
        if (!actionHolders.containsKey(actionHolderType)) return null;
        return actionHolders.get(actionHolderType).values().stream()
                .filter(actionHolder -> actionHolder.getLocation().equals(actionHolderLocation))
                .findFirst()
                .orElse(null);
    }

    /**
     * Removes all action holders of the specified type.
     *
     * @param actionHolderType The type of action holder to remove.
     */
    @SuppressWarnings("unused")
    public void clearActionHolders(IActionHolderType<?> actionHolderType) {
        actionHolders.remove(actionHolderType);
    }
}
