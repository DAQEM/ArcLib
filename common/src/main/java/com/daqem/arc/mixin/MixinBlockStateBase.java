package com.daqem.arc.mixin;

import com.daqem.arc.api.event.ArcBlockEvent;
import com.daqem.arc.api.event.EventResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class MixinBlockStateBase {

    @Inject(at = @At("RETURN"), method = "useItemOn", cancellable = true)
    public void use(ItemStack itemStack, Level level, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
        BlockState state = level.getBlockState(blockHitResult.getBlockPos());
        EventResult eventResult = ArcBlockEvent.RIGHT_CLICK_BLOCK.invoker().onRightClickBlock(itemStack, level, player, interactionHand, state, blockHitResult.getBlockPos());
        if (eventResult.cancelsEvent()) {
            cir.setReturnValue(InteractionResult.FAIL);
        }
    }

    @Inject(at = @At("RETURN"), method = "getDestroyProgress(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)F", cancellable = true)
    public void getDestroyProgress(Player player, BlockGetter blockGetter, BlockPos blockPos, CallbackInfoReturnable<Float> cir) {
        MutableFloat speed = new MutableFloat(cir.getReturnValue());
        EventResult eventResult = ArcBlockEvent.GET_DESTROY_SPEED.invoker().onGetDestroySpeed(
                player,
                blockGetter.getBlockState(blockPos),
                blockPos,
                player.getMainHandItem(),
                speed
        );
        if (eventResult.cancelsEvent()) {
            cir.setReturnValue(0F);
        } else if (!Objects.equals(speed.getValue(), cir.getReturnValue())) {
            cir.setReturnValue(speed.getValue());
        }
    }
}
