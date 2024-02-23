package com.daqem.arc.data;

import com.daqem.arc.Arc;
import com.daqem.arc.api.action.holder.ActionHolderManager;
import com.daqem.arc.api.action.holder.IActionHolder;
import com.daqem.arc.api.player.holder.PlayerActionHolder;
import com.daqem.arc.api.player.holder.PlayerActionTypes;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class PlayerActionHolderManager extends SimpleJsonResourceReloadListener {

    private static PlayerActionHolderManager instance;
    private ImmutableMap<ResourceLocation, IActionHolder> playerActionHolders = ImmutableMap.of();

    public PlayerActionHolderManager() {
        super(new Gson(), "please_do_not_use_this");
        instance = this;
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> object, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
        ActionHolderManager.getInstance().clearActionHolders(PlayerActionTypes.PLAYER_ACTION_TYPE);

        ResourceLocation location = Arc.getId("player");
        IActionHolder playerActionHolder = new PlayerActionHolder(location);
        this.playerActionHolders = ImmutableMap.of(location, playerActionHolder);

        this.playerActionHolders.values().forEach(ActionHolderManager.getInstance()::registerActionHolder);
    }

    public static PlayerActionHolderManager getInstance() {
        return instance;
    }

    public ImmutableMap<ResourceLocation, IActionHolder> getPlayerActionHolders() {
        return this.playerActionHolders;
    }

    public List<IActionHolder> getPlayerActionHoldersList() {
        return new ArrayList<>(this.playerActionHolders.values());
    }
}
