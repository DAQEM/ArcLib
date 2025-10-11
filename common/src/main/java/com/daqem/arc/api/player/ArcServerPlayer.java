package com.daqem.arc.api.player;

import com.daqem.arc.api.MovementType;
import com.daqem.arc.api.action.holder.IActionHolder;
import com.daqem.arc.api.condition.ICondition;
import com.daqem.arc.player.BlockPosCache;
import com.daqem.arc.player.stat.StatData;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;

public interface ArcServerPlayer extends ArcPlayer {

    ServerPlayer arc$getServerPlayer();

    NonNullList<StatData> arc$getStatData();

    void arc$addStatData(StatData statData);

    double arc$getTotalWalkedCm();
    double arc$getTotalSprintedCm();
    double arc$getTotalSwamCm();
    double arc$getTotalCrouchedCm();
    double arc$getTotalElytraFlyCm();
    double arc$getTotalHorseRideCm();

    Map<Object, Double> arc$getActionLastMetDistances();
    void arc$setActionLastMetDistance(Object action, double distance);

    void arc$syncActionHoldersWithClient();

    BlockPosCache arc$getBlockPosCache();

    boolean arc$isApplyingRewardEffect();
    void arc$setApplyingRewardEffect(boolean isApplying);
}
