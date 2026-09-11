package com.daqem.arc.fabric.mixin.compat.fallingtree;

import com.daqem.arc.api.event.ArcBlockEvent;
import com.daqem.arc.api.event.EventResult;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(targets = "fr.rakambda.fallingtree.fabric.common.FallingTreeCommonsImpl")
public class FallingTreeCommonsImplMixin {

    @WrapOperation(
            method = "checkCanBreakBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/fabricmc/fabric/api/event/player/PlayerBlockBreakEvents$Before;beforeBlockBreak(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/entity/BlockEntity;)Z"
            )
    )
    private boolean knot$wrapFallingTreeBlockBreak(PlayerBlockBreakEvents.Before instance, Level level, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity, Operation<Boolean> original) {
        boolean canBreak = original.call(instance, level, player, pos, state, blockEntity);

        if (canBreak && level instanceof ServerLevel serverLevel && player instanceof ServerPlayer serverPlayer) {
            EventResult knotResult = ArcBlockEvent.BREAK_BLOCK.invoker().onBreakBlock(serverLevel, pos, state, serverPlayer, () -> 0);
            if (knotResult.cancelsEvent()) {
                return false;
            }
        }
        return canBreak;
    }
}