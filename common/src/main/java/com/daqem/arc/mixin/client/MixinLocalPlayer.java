package com.daqem.arc.mixin.client;

import com.daqem.arc.api.action.holder.IActionHolder;
import com.daqem.arc.api.player.ArcClientPlayer;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.ProfilePublicKey;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mixin(LocalPlayer.class)
public abstract class MixinLocalPlayer extends AbstractClientPlayer implements ArcClientPlayer {

    public MixinLocalPlayer(ClientLevel clientLevel, GameProfile gameProfile, @Nullable ProfilePublicKey profilePublicKey) {
        super(clientLevel, gameProfile, profilePublicKey);
    }

    @Override
    public List<IActionHolder> getActionHolders() {
        return new ArrayList<>();
    }

    @Override
    public String name() {
        return getLocalPlayer().getName().getString();
    }

    @Override
    public double nextRandomDouble() {
        return getLocalPlayer().getRandom().nextDouble();
    }

    @Override
    public @NotNull UUID getUUID() {
        return super.getUUID();
    }

    @Override
    public @NotNull Level getLevel() {
        return super.getLevel();
    }

    @Override
    public Player getPlayer() {
        return getLocalPlayer();
    }

    @Override
    public LocalPlayer getLocalPlayer() {
        return (LocalPlayer) (Object) this;
    }
}
