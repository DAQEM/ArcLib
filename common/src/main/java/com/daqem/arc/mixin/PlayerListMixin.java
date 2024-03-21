package com.daqem.arc.mixin;

import com.daqem.arc.Arc;
import com.daqem.arc.api.action.IAction;
import com.daqem.arc.api.player.ArcServerPlayer;
import com.daqem.arc.config.ArcCommonConfig;
import com.daqem.arc.data.ActionManager;
import com.daqem.arc.networking.ClientboundUpdateActionsPacket;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {

    @Shadow @Final private List<ServerPlayer> players;

    @Inject(at = @At("TAIL"), method = "reloadResources")
    private void reloadResources(CallbackInfo ci) {
        for (ServerPlayer player : this.players) {
            if (ArcCommonConfig.isDebug.get()) {
                Arc.LOGGER.info("Sending actions to player {}", player.getName().getString());
            }
            if (player instanceof ArcServerPlayer arcServerPlayer) {
                arcServerPlayer.arc$getActionHolders().forEach(actionHolder -> {
                    List<IAction> newActions = ActionManager.getInstance().getActions();

                    List<IAction> actionsToRemove = actionHolder.getActions().stream().filter(action ->
                            newActions.stream().noneMatch(newAction -> newAction.getLocation().equals(action.getLocation()))
                    ).toList();
                    actionsToRemove.forEach(actionHolder::removeAction);

                    List<IAction> actionsToAdd = newActions.stream().filter(newAction ->
                            newAction.getActionHolderLocation().equals(actionHolder.getLocation())
                    ).toList();

                    actionHolder.clearActions();

                    actionsToAdd.forEach(actionHolder::addAction);
                });
            }
            new ClientboundUpdateActionsPacket(ActionManager.getInstance().getActions()).sendTo(player);
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;sendPlayerPermissionLevel(Lnet/minecraft/server/level/ServerPlayer;)V", shift = At.Shift.BEFORE), method = "placeNewPlayer")
    private void placeNewPlayer(Connection connection, ServerPlayer serverPlayer, CallbackInfo ci) {
        new ClientboundUpdateActionsPacket(ActionManager.getInstance().getActions()).sendTo(serverPlayer);
    }
}
