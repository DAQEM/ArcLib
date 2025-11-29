package com.daqem.arc.mixin;

import com.daqem.arc.api.event.ArcBlockEvent;
import com.daqem.arc.api.event.EventResult;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.player.Player;
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
                    target = "Lnet/minecraft/world/entity/player/Player;getDestroySpeed(Lnet/minecraft/world/level/block/state/BlockState;)F"
            )
    )
    private float onGetDestroyProgress(float original, BlockState blockState, Player player) {
        // 1. Wrap the original speed (e.g., 1.0 for hand, 6.0 for iron tool)
        MutableFloat speed = new MutableFloat(original);

        // 2. Invoke your event
        // Note: 'view' is likely your BlockGetter/Level
        EventResult eventResult = ArcBlockEvent.GET_DESTROY_SPEED.invoker().onGetDestroySpeed(
                player,
                blockState,
                this.arc$getBlockHitResult(player).getBlockPos(),
                player.getMainHandItem(),
                speed
        );

        // 3. Handle cancellation
        if (eventResult.cancelsEvent()) {
            return 0.0F; // Returning 0 speed generally prevents mining progress
        }

        // 4. Return the modified value
        return speed.getValue();
    }

    private BlockHitResult arc$getBlockHitResult(Player player) {
        Vec3 eyePos = player.getEyePosition(1.0F);
        Vec3 viewVec = player.getViewVector(1.0F);
        Vec3 target = eyePos.add(viewVec.x * 5, viewVec.y * 5, viewVec.z * 5);
        return player.level().clip(new ClipContext(eyePos, target, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
    }
}
