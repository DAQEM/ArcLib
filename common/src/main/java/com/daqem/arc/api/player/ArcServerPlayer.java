package com.daqem.arc.api.player;

import com.daqem.arc.player.stat.StatData;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;

public interface ArcServerPlayer extends ArcPlayer {

    ServerPlayer arc$getServerPlayer();

    NonNullList<StatData> arc$getStatData();

    void arc$addStatData(StatData statData);

    void arc$setSwimmingDistanceInCm(int swimmingDistanceInCm);

    void arc$setElytraFlyingDistanceInCm(float flyingDistanceInCm);
}
