package com.daqem.arc.mixin;

import com.daqem.arc.api.action.holder.IActionHolder;
import com.daqem.arc.api.player.ArcClientPlayer;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mixin(LocalPlayer.class)
public abstract class MixinLocalPlayer extends AbstractClientPlayer implements ArcClientPlayer {

    @Unique
    private final List<IActionHolder> arc$actionHolders = new ArrayList<>();

    public MixinLocalPlayer(ClientLevel clientLevel, GameProfile gameProfile) {
        super(clientLevel, gameProfile);
    }

    @Override
    public List<IActionHolder> arc$getActionHolders() {
        return arc$actionHolders;
    }

    @Override
    public void arc$addActionHolder(IActionHolder actionHolder) {
        if (actionHolder == null) return;
        arc$actionHolders.add(actionHolder);
    }

    @Override
    public void arc$addActionHolders(List<IActionHolder> actionHolders) {
        if (actionHolders == null) return;
        actionHolders.removeIf(Objects::isNull);
        this.arc$actionHolders.addAll(actionHolders);
    }

    @Override
    public void arc$removeActionHolder(IActionHolder actionHolder) {
        arc$actionHolders.remove(actionHolder);
    }

    @Override
    public void arc$clearActionHolders() {
        arc$actionHolders.clear();
    }

    @Override
    public double arc$nextRandomDouble() {
        return arc$getLocalPlayer().getRandom().nextDouble();
    }

    @Override
    public @NotNull Level arc$getLevel() {
        return super.level();
    }

    @Override
    public String arc$getName() {
        return super.getName().getString();
    }

    @Override
    public Player arc$getPlayer() {
        return arc$getLocalPlayer();
    }

    @Override
    public LocalPlayer arc$getLocalPlayer() {
        return (LocalPlayer) (Object) this;
    }
}
