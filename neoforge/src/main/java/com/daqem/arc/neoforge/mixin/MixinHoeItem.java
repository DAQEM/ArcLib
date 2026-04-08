package com.daqem.arc.neoforge.mixin;

import com.daqem.arc.api.event.ArcBlockEvent;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.function.Consumer;
import java.util.function.Predicate;

@Mixin(HoeItem.class)
public class MixinHoeItem {

    @Inject(
            method = "useOn",
            at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V"),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void arc$onTillSoil(UseOnContext useOnContext, CallbackInfoReturnable<InteractionResult> cir, Level level, BlockPos blockPos, BlockState toolModifiedState, Pair<Predicate<UseOnContext>, Consumer<UseOnContext>> pair, Predicate<UseOnContext> predicate, Consumer<UseOnContext> consumer, Player player) {
        ArcBlockEvent.TILL_SOIL.invoker().onTillSoil(level, blockPos, level.getBlockState(blockPos), player, useOnContext.getItemInHand());
    }
}