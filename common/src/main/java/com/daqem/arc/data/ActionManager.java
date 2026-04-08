package com.daqem.arc.data;

import com.daqem.arc.Arc;
import com.daqem.arc.api.action.IAction;
import com.daqem.arc.config.ArcCommonConfig;
import com.daqem.arc.data.condition.recipe.RecipeCache;
import com.daqem.arc.registry.ArcRegistry;
import com.daqem.knot.api.platform.Platform;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ActionManager extends SimplePreparableReloadListener<List<IAction>> {

    @Override
    protected @NotNull List<IAction> prepare(ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
        Map<Identifier, Resource> resourceMap = resourceManager.listResources("arc", (resourceLocation) ->
                        resourceLocation.getPath().endsWith(".json")).entrySet().stream()
                .collect(Collectors.toMap(entry ->
                                Identifier.fromNamespaceAndPath(
                                        entry.getKey().getNamespace(),
                                        entry.getKey().getPath()
                                                .substring(0, entry.getKey().getPath().length() - ".json".length())
                                                .substring("arc/".length())),
                        Map.Entry::getValue));

        Map<Identifier, JsonElement> map = new HashMap<>();
        for (Map.Entry<Identifier, Resource> entry : resourceMap.entrySet()) {
            Identifier location = entry.getKey();
            try {
                JsonElement jsonElement = GsonHelper.parse(entry.getValue().openAsReader());
                map.put(location, jsonElement);
            }
            catch (Exception runtimeException) {
                Arc.API.LOGGER.error("Parsing error loading action {}", location, runtimeException);
            }
        }

        try {
            Path configDir = Platform.INFO.getConfigFolder().resolve(Arc.MOD_ID).resolve("actions");
            if (!Files.exists(configDir)) {
                Files.createDirectories(configDir);
            }
            try (Stream<Path> paths = Files.walk(configDir)) {
                paths.filter(path -> path.toString().endsWith(".json"))
                        .forEach(path -> {
                            try (BufferedReader reader = Files.newBufferedReader(path)) {
                                JsonObject jsonElement = GsonHelper.parse(reader);
                                String relativePath = configDir.relativize(path).toString();
                                relativePath = relativePath.replace("\\", "/");
                                relativePath = relativePath.substring(0, relativePath.length() - ".json".length());
                                String namespace;
                                String resourcePath;
                                int firstSlashIndex = relativePath.indexOf('/');
                                if (firstSlashIndex > 0) {
                                    namespace = relativePath.substring(0, firstSlashIndex);
                                    resourcePath = relativePath.substring(firstSlashIndex + 1);
                                } else {
                                    namespace = Arc.MOD_ID;
                                    resourcePath = relativePath;
                                }
                                Identifier location = Identifier.fromNamespaceAndPath(namespace, resourcePath);
                                map.put(location, jsonElement);
                            } catch (Exception e) {
                                Arc.API.LOGGER.error("Parsing error loading action from config {}", path, e);
                            }
                        });
            }
        } catch (Exception e) {
            Arc.API.LOGGER.error("Error loading actions from config", e);
        }
        List<IAction> actions = new ArrayList<>();
        List<String> excludedActions = ArcCommonConfig.excludedActions.get();

        for (Map.Entry<Identifier, JsonElement> entry : map.entrySet()) {
            Identifier location = entry.getKey();
            if (excludedActions.contains(location.toString())) {
                continue;
            }
            try {
                IAction action = fromJson(location, GsonHelper.convertToJsonObject(entry.getValue(), "top element"));
                actions.add(action);
            }
            catch (JsonParseException | IllegalArgumentException runtimeException) {
                Arc.API.LOGGER.error("Parsing error loading action {}", location, runtimeException);
            }
        }

        return actions;
    }

    @Override
    protected void apply(@NotNull List<IAction> actions, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
        ActionHolderManager actionHolderManager = ActionHolderManager.getInstance();
        RecipeCache.invalidate();
        actionHolderManager.clearAllActions();
        actionHolderManager.registerActions(actions);
        Arc.API.LOGGER.info("Loaded {} actions", actions.size());
    }

    /**
     * Parses a JSON object and returns the corresponding IAction instance.
     *
     * @param location the resource location of the IAction
     * @param jsonObject the JSON object representing the IAction
     * @return the parsed IAction instance
     * @throws JsonSyntaxException if the JSON object is invalid or if the action type is unsupported
     */
    public static IAction fromJson(Identifier location, JsonObject jsonObject) {
        String type = GsonHelper.getAsString(jsonObject, "type");
        return ArcRegistry.ACTION.getOptional(Identifier.parse(type))
                .orElseThrow(() -> new JsonSyntaxException("Invalid or unsupported action type '" + type + "'"))
                .getSerializer().fromJson(location, jsonObject);
    }
}
