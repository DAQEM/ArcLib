package com.daqem.arc.data;

import com.daqem.arc.Arc;
import com.daqem.arc.ArcExpectPlatform;
import com.daqem.arc.api.action.IAction;
import com.daqem.arc.api.action.type.IActionType;
import com.daqem.arc.registry.ArcRegistry;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public abstract class ActionManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    protected Map<IActionType<?>, Map<ResourceLocation, IAction>> actions = ImmutableMap.of();
    private Map<ResourceLocation, IAction> byName = ImmutableMap.of();
    private static ActionManager instance;

    public ActionManager() {
        super(GSON, "arc");
        instance = this;
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> map, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
        HashMap<IActionType<?>, ImmutableMap.Builder<ResourceLocation, IAction>> tempActionTypes = Maps.newHashMap();
        ImmutableMap.Builder<ResourceLocation, IAction> tempActions = ImmutableMap.builder();
        for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
            ResourceLocation location = entry.getKey();
            try {
                IAction action = fromJson(location, GsonHelper.convertToJsonObject(entry.getValue(), "top element"));
                tempActionTypes.computeIfAbsent(action.getType(), recipeType -> ImmutableMap.builder()).put(location, action);
                tempActions.put(location, action);
            }
            catch (JsonParseException | IllegalArgumentException runtimeException) {
                Arc.LOGGER.error("Parsing error loading action {}", location, runtimeException);
            }
        }
        this.actions = tempActionTypes.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, entry -> entry.getValue().build()));
        this.byName = tempActions.build();
        Arc.LOGGER.info("Loaded {} actions", this.byName.size());
    }

    /**
     * Returns a list of all actions of the specified type.
     *
     * @param actionType the type of action to filter the actions
     * @param <T>        the generic type of IAction
     * @return a list of actions filtered by the specified action type
     */
    public <T extends IAction> List<T> getAllActionsFor(IActionType<T> actionType) {
        return List.copyOf(this.byType(actionType).values());
    }

    /**
     * Returns a map of actions by type.
     *
     * @param actionType the type of action to filter the actions
     * @param <T>        the generic type of IAction
     * @return a map of actions filtered by the specified action type
     */
    @SuppressWarnings("unchecked")
    private <T extends IAction> Map<ResourceLocation, T> byType(IActionType<T> actionType) {
        return (Map<ResourceLocation, T>) this.actions.getOrDefault(actionType, Collections.emptyMap());
    }

    /**
     * Returns an optional IAction corresponding to the given ResourceLocation key.
     *
     * @param resourceLocation the key to identify the desired IAction
     * @return an Optional of the corresponding IAction, or an empty Optional if no matching IAction is found
     */
    public Optional<? extends IAction> byKey(ResourceLocation resourceLocation) {
        return Optional.ofNullable(this.byName.get(resourceLocation));
    }

    /**
     * Returns the instance of the ActionManager.
     *
     * @return the instance of the ActionManager
     */
    public static ActionManager getInstance() {
        return instance != null ? instance : ArcExpectPlatform.getActionManager();
    }

    /**
     * Returns the list of actions.
     *
     * @return the list of actions
     */
    public List<IAction> getActions() {
        return this.actions.values().stream().flatMap(map -> map.values().stream()).collect(Collectors.toSet()).stream().toList();
    }

    /**
     * Returns a list of ResourceLocations corresponding to the IAction resource locations.
     *
     * @return a list of ResourceLocations corresponding to the IAction resource locations
     */
    public List<ResourceLocation> getIActionIds() {
        return this.actions.values().stream().flatMap(map -> map.keySet().stream()).toList();
    }

    /**
     * Parses a JSON object and returns the corresponding IAction instance.
     *
     * @param location the resource location of the IAction
     * @param jsonObject the JSON object representing the IAction
     * @return the parsed IAction instance
     * @throws JsonSyntaxException if the JSON object is invalid or if the action type is unsupported
     */
    public static IAction fromJson(ResourceLocation location, JsonObject jsonObject) {
        String type = GsonHelper.getAsString(jsonObject, "type");
        return ArcRegistry.ACTION_SERIALIZER.getOptional(new ResourceLocation(type))
                .orElseThrow(() -> new JsonSyntaxException("Invalid or unsupported action type '" + type + "'"))
                .fromJson(location, jsonObject);
    }

    /**
     * Replaces the existing actions with the given list of actions.
     * Used to update the actions when a client joins a dedicated server.
     *
     * @param actions the list of actions to replace the current actions with
     */
    public void replaceActions(List<IAction> actions) {
        HashMap<IActionType<?>, Map<ResourceLocation, IAction>> map = Maps.newHashMap();
        ImmutableMap.Builder<ResourceLocation, IAction> builder = ImmutableMap.builder();

        actions.forEach(action -> {
            Map<ResourceLocation, IAction> map2 = map.computeIfAbsent(action.getType(), actionType -> Maps.newHashMap());
            ResourceLocation resourceLocation = action.getLocation();
            IAction action2 = map2.put(resourceLocation, action);
            builder.put(resourceLocation, action);
            if (action2 != null) {
                throw new IllegalStateException("Duplicate action ignored with ID " + resourceLocation);
            }
        });

        this.actions = ImmutableMap.copyOf(map);
        this.byName = builder.build();
        Arc.LOGGER.info("Updated {} actions", map.size());
    }
}
