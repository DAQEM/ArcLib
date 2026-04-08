package com.daqem.arc.api.player;

import com.daqem.arc.player.BlockPosCache;
import com.daqem.arc.player.stat.StatData;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;

public interface ArcServerPlayer extends ArcPlayer {

    ServerPlayer arc$getServerPlayer();

    Map<Object, Double> arc$getActionLastMetDistances();
    void arc$setActionLastMetDistance(Object action, double distance);

    void arc$syncActionHoldersWithClient();

    BlockPosCache arc$getBlockPosCache();
}
