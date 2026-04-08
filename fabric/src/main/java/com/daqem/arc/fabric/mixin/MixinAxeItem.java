package com.daqem.arc.fabric.mixin;

import com.daqem.arc.api.event.ArcPlayerEvent;
import com.daqem.arc.api.event.EventResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(AxeItem.class)
public abstract class MixinAxeItem extends Item {

    public MixinAxeItem(Properties properties) {
        super(properties);
    }

    @Inject(
            method = "evaluateNewBlockState",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V",
                    ordinal = 0
            ),
            cancellable = true
    )
    private void onEvaluateLogStrip(Level level, BlockPos blockPos, Player player, BlockState blockState, CallbackInfoReturnable<Optional<BlockState>> cir) {
        if (player != null) {
            ItemStack stack = player.getMainHandItem();
            InteractionHand hand = InteractionHand.MAIN_HAND;

            if (stack.getItem() != this) {
                stack = player.getOffhandItem();
                hand = InteractionHand.OFF_HAND;
            }

            EventResult eventResult = ArcPlayerEvent.STRIP_LOG.invoker().onStripLog(
                    player,
                    hand,
                    stack,
                    blockPos,
                    blockState,
                    level
            );

            if (eventResult.cancelsEvent()) {
                cir.setReturnValue(Optional.empty());
            }
        }
    }
}