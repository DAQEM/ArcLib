package com.daqem.arc.neoforge.mixin;

import com.daqem.arc.api.event.ArcBlockEvent;
import com.daqem.arc.api.event.EventResult;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockBehaviour.class)
public class MixinBlockBehaviour {

    @ModifyExpressionValue(
            method = "getDestroyProgress",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;getDestroySpeed(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)F"
            )
    )
    private float onGetDestroyProgress(float original, BlockState blockState, Player player, BlockGetter blockGetter, BlockPos blockPos) {
        // 1. Wrap the original speed (e.g., 1.0 for hand, 6.0 for iron tool)
        MutableFloat speed = new MutableFloat(original);

        // 2. Invoke your event
        // Note: 'view' is likely your BlockGetter/Level
        EventResult eventResult = ArcBlockEvent.GET_DESTROY_SPEED.invoker().onGetDestroySpeed(
                player,
                blockState,
                blockPos,
                player.getMainHandItem(),
                speed
        );

        // 3. Handle cancellation
        if (eventResult.cancelsEvent()) {
            return 0.0F; // Returning 0 speed generally prevents mining progress
        }

        // 4. Return the modified value
        return speed.floatValue();
    }
}
