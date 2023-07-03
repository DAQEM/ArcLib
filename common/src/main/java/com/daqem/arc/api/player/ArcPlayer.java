package com.daqem.arc.api.player;

import com.daqem.arc.api.action.holder.IActionHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.UUID;

public interface ArcPlayer {

    List<IActionHolder> getActionHolders();

    UUID getUUID();

    String name();

    double nextRandomDouble();

    Level getLevel();

    Player getPlayer();
}
