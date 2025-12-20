package com.daqem.arc.data;

import com.daqem.arc.api.IArcRegistryAccessor;
import com.daqem.arc.api.action.IAction;
import com.daqem.arc.api.action.holder.IActionHolder;
import com.daqem.arc.api.action.holder.IActionHolderType;
import net.minecraft.resources.Identifier;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ActionHolderManager implements IArcRegistryAccessor {

    private final Map<IActionHolderType<?>, Map<Identifier, IAction>> actions = new ConcurrentHashMap<>();
    private final Map<IActionHolderType<?>, Map<Identifier, IActionHolder>> actionHolders = new ConcurrentHashMap<>();

    private static ActionHolderManager instance;

    private ActionHolderManager() {
    }

    public static ActionHolderManager getInstance() {
        if (instance == null) {
            instance = new ActionHolderManager();
        }
        return instance;
    }

    public void registerActionHolders(List<IActionHolder> actionHolders) {
        for (IActionHolder actionHolder : actionHolders) {
            this.actionHolders.computeIfAbsent(actionHolder.getType(), mapFunc -> new ConcurrentHashMap<>())
                    .put(actionHolder.getIdentifier(), actionHolder);

            List<IAction> actionsForHolder = getActionsForHolder(actionHolder);
            actionHolder.clearActions();
            actionHolder.addActions(actionsForHolder);
        }
    }

    public void registerActions(List<IAction> actions) {
        for (IAction action : actions) {
            this.actions.computeIfAbsent(action.getActionHolderType(), mapFunc -> new ConcurrentHashMap<>())
                    .put(action.getIdentifier(), action);
        }

        mapActionsByTheirHolders(actions).forEach((location, actionHolderActions) ->
                getActionHolder(location).ifPresent(holder -> {
                    holder.clearActions();
                    holder.addActions(actionHolderActions);
                })
        );
    }

    public void clearAllActionHoldersForType(IActionHolderType<?> type) {
        actionHolders.remove(type);
    }

    public void clearAllActions() {
        actions.clear();
        actionHolders.values().forEach(holderMap -> holderMap.values().forEach(IActionHolder::clearActions));
    }

    public List<IActionHolder> getActionHolders(List<Identifier> actionHolderLocations) {
        return actionHolderLocations.stream()
                .map(this::getActionHolder)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private Map<Identifier, List<IAction>> mapActionsByTheirHolders(List<IAction> actions) {
        return actions.stream().collect(Collectors.groupingBy(IAction::getActionHolderLocation));
    }

    public Optional<IActionHolder> getActionHolder(Identifier location) {
        for (Map<Identifier, IActionHolder> actionHolderMap : actionHolders.values()) {
            if (actionHolderMap.containsKey(location)) {
                return Optional.of(actionHolderMap.get(location));
            }
        }
        return Optional.empty();
    }

    private List<IAction> getActionsForHolder(IActionHolder actionHolder) {
        return actions.getOrDefault(actionHolder.getType(), Collections.emptyMap())
                .values().stream()
                .filter(action -> action.getActionHolderLocation().equals(actionHolder.getIdentifier()))
                .collect(Collectors.toList());
    }

    public Optional<IAction> getAction(Identifier actionLocation) {
        for (Map<Identifier, IAction> actionMap : actions.values()) {
            if (actionMap.containsKey(actionLocation)) {
                return Optional.of(actionMap.get(actionLocation));
            }
        }
        return Optional.empty();
    }

    public List<String> getActionLocationStrings() {
        return actions.values().stream()
                .flatMap(map -> map.keySet().stream())
                .map(Identifier::toString)
                .collect(Collectors.toList());
    }

    public List<IAction> getActions() {
        return actions.values().stream()
                .flatMap(map -> map.values().stream())
                .collect(Collectors.toList());
    }

    public List<IActionHolder> getActionHolders() {
        return actionHolders.values().stream()
                .flatMap(map -> map.values().stream())
                .collect(Collectors.toList());
    }
}
