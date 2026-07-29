package com.daqem.arc.mixin;

import com.daqem.arc.api.action.holder.IActionHolder;
import com.daqem.arc.api.player.ArcServerPlayer;
import com.daqem.arc.data.ActionHolderManager;
import com.daqem.arc.networking.ClientboundUpdateActionHoldersPacket;
import com.daqem.arc.networking.ClientboundUpdateActionsPacket;
import com.daqem.knot.Knot;
import net.minecraft.network.Connection;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
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
            if (player instanceof ArcServerPlayer arcServerPlayer) {
                List<Identifier> actionHolderLocations = arcServerPlayer.arc$getActionHolders().stream().map(IActionHolder::getIdentifier).toList();
                arcServerPlayer.arc$clearActionHolders();
                List<IActionHolder> actionHolders = ActionHolderManager.getInstance().getActionHolders(actionHolderLocations);
                arcServerPlayer.arc$getActionLastMetDistances().clear();
                arcServerPlayer.arc$addActionHolders(actionHolders);
            }
            Knot.NETWORKING.sendToPlayer(player, new ClientboundUpdateActionsPacket(ActionHolderManager.getInstance().getActions()));
            Knot.NETWORKING.sendToPlayer(player, new ClientboundUpdateActionHoldersPacket(ActionHolderManager.getInstance().getActionHolders()));
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;sendPlayerPermissionLevel(Lnet/minecraft/server/level/ServerPlayer;)V", shift = At.Shift.BEFORE), method = "placeNewPlayer")
    private void placeNewPlayer(Connection connection, ServerPlayer player, CommonListenerCookie cookie, CallbackInfo ci) {
        Knot.NETWORKING.sendToPlayer(player, new ClientboundUpdateActionsPacket(ActionHolderManager.getInstance().getActions()));
        Knot.NETWORKING.sendToPlayer(player, new ClientboundUpdateActionHoldersPacket(ActionHolderManager.getInstance().getActionHolders()));

        if (player instanceof ArcServerPlayer arcServerPlayer) {
            arcServerPlayer.arc$syncActionHoldersWithClient();
        }
    }
}
