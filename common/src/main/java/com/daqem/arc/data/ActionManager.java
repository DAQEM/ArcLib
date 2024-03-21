package com.daqem.arc.data;

import com.daqem.arc.Arc;
import com.daqem.arc.ArcExpectPlatform;
import com.daqem.arc.api.action.IAction;
import com.daqem.arc.api.action.holder.ActionHolderManager;
import com.daqem.arc.api.action.type.IActionType;
import com.daqem.arc.config.ArcCommonConfig;
import com.daqem.arc.registry.ArcRegistry;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
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

    public ActionManager() {
        super(GSON, "arc");
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> map, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
        ActionHolderManager actionHolderManager = ActionHolderManager.getInstance();
        actionHolderManager.clearAllActions();

        List<IAction> actions = new ArrayList<>();

        if (!ArcCommonConfig.isDebug.get()) {
            map.entrySet().removeIf(entry -> entry.getKey().getNamespace().equals("debug"));
        }

        for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
            ResourceLocation location = entry.getKey();
            try {
                IAction action = fromJson(location, GsonHelper.convertToJsonObject(entry.getValue(), "top element"));
                actions.add(action);
            }
            catch (JsonParseException | IllegalArgumentException runtimeException) {
                Arc.LOGGER.error("Parsing error loading action {}", location, runtimeException);
            }
        }
        actionHolderManager.registerActions(actions);
        Arc.LOGGER.info("Loaded {} actions", actions.size());
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
        return ArcRegistry.ACTION.getOptional(new ResourceLocation(type))
                .orElseThrow(() -> new JsonSyntaxException("Invalid or unsupported action type '" + type + "'"))
                .getSerializer().fromJson(location, jsonObject);
    }
}
