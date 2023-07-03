package com.daqem.arc.api.player;

import com.daqem.arc.player.stat.StatData;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;

public interface ArcServerPlayer extends ArcPlayer {

    ServerPlayer getServerPlayer();

    NonNullList<StatData> getStatData();

    void addStatData(StatData statData);

    void setSwimmingDistanceInCm(int swimmingDistanceInCm);

    void setElytraFlyingDistanceInCm(float flyingDistanceInCm);
}
