package com.daqem.arc.mixin;

import com.daqem.arc.api.action.holder.IActionHolder;
import com.daqem.arc.api.player.ArcPlayer;
import com.daqem.arc.api.player.ArcServerPlayer;
import com.daqem.arc.data.PlayerActionHolderManager;
import com.daqem.arc.networking.ClientboundSyncPlayerActionHoldersPacket;
import com.daqem.arc.player.BlockPosCache;
import com.daqem.knot.Knot;
import com.mojang.authlib.GameProfile;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(ServerPlayer.class)
public abstract class MixinServerPlayer extends Player implements ArcServerPlayer {

    @Shadow
    public ServerGamePacketListenerImpl connection;
    @Unique
    private final Map<Identifier, IActionHolder> arc$actionHolders = new HashMap<>();
    @Unique
    private boolean arc$actionHoldersDirty = false;
    @Unique
    private final Map<Object, Double> arc$actionLastMetDistances = new HashMap<>();
    @Unique
    public BlockPosCache arc$blockPosCache = new BlockPosCache();

    public MixinServerPlayer(Level level, GameProfile gameProfile) {
        super(level, gameProfile);
    }

    @Override
    public List<IActionHolder> arc$getActionHolders() {
        return new ArrayList<>(arc$actionHolders.values());
    }

    @Override
    public void arc$addActionHolder(IActionHolder actionHolder) {
        if (actionHolder == null) return;
        this.arc$actionHolders.put(actionHolder.getIdentifier(), actionHolder);
        this.arc$actionHoldersDirty = true;
    }

    @Override
    public void arc$addActionHolders(List<IActionHolder> actionHolders) {
        if (actionHolders == null) return;
        boolean changed = false;
        for (IActionHolder actionHolder : actionHolders) {
            if (actionHolder != null) {
                this.arc$actionHolders.put(actionHolder.getIdentifier(), actionHolder);
                changed = true;
            }
        }
        if (changed) {
            this.arc$actionHoldersDirty = true;
        }
    }

    @Override
    public void arc$removeActionHolder(IActionHolder actionHolder) {
        if (this.arc$actionHolders.remove(actionHolder.getIdentifier()) != null) {
            this.arc$actionHoldersDirty = true;
        }
    }

    @Override
    public void arc$clearActionHolders() {
        if (!this.arc$actionHolders.isEmpty()) {
            this.arc$actionHolders.clear();
            this.arc$actionHoldersDirty = true;
        }
    }

    @Override
    public ServerPlayer arc$getServerPlayer() {
        return (ServerPlayer) (Object) this;
    }

    @Override
    public Map<Object, Double> arc$getActionLastMetDistances() {
        return this.arc$actionLastMetDistances;
    }

    @Override
    public void arc$setActionLastMetDistance(Object action, double distance) {
        this.arc$actionLastMetDistances.put(action, distance);
    }

    @Override
    public void arc$syncActionHoldersWithClient() {
        if (this.connection == null) return;
        Knot.NETWORKING.sendToPlayer(arc$getServerPlayer(), new ClientboundSyncPlayerActionHoldersPacket(arc$getActionHolders()));
        this.arc$actionHoldersDirty = false;
    }

    @Override
    public BlockPosCache arc$getBlockPosCache() {
        return this.arc$blockPosCache;
    }

    @Override
    public double arc$nextRandomDouble() {
        return this.arc$getServerPlayer().getRandom().nextDouble();
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
        return arc$getServerPlayer();
    }

    @Inject(at = @At("TAIL"), method = "restoreFrom(Lnet/minecraft/server/level/ServerPlayer;Z)V")
    public void restoreFrom(ServerPlayer oldPlayer, boolean alive, CallbackInfo ci) {
        if (oldPlayer instanceof ArcServerPlayer arcServerPlayer) {
            this.arc$actionLastMetDistances.putAll(arcServerPlayer.arc$getActionLastMetDistances());
            this.arc$blockPosCache = arcServerPlayer.arc$getBlockPosCache();
            this.arc$actionHoldersDirty = true;
        }
    }

    @Inject(at = @At("TAIL"), method = "<init>")
    public void readAdditionalSaveData(MinecraftServer minecraftServer, ServerLevel serverLevel, GameProfile gameProfile, ClientInformation clientInformation, CallbackInfo ci) {
        if (((ServerPlayer) (Object) this) instanceof ArcPlayer arcPlayer) {
            arcPlayer.arc$addActionHolders(PlayerActionHolderManager.getInstance().getPlayerActionHoldersList());
        }
    }

    @Inject(at = @At("TAIL"), method = "tick")
    private void arc$tick(CallbackInfo ci) {
        if (this.arc$actionHoldersDirty) {
            this.arc$syncActionHoldersWithClient();
        }
    }
}
