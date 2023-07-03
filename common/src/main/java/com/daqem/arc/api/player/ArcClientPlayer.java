package com.daqem.arc.api.player;

import com.daqem.arc.api.player.ArcPlayer;
import net.minecraft.client.player.LocalPlayer;

public interface ArcClientPlayer extends ArcPlayer {

    LocalPlayer getLocalPlayer();
}
