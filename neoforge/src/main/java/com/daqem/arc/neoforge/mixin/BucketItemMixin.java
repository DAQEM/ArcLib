package com.daqem.arc.neoforge.mixin;

import com.daqem.arc.api.event.ArcItemEvent;
import com.daqem.arc.api.event.EventResult;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BucketItem.class)
public abstract class BucketItemMixin {

    @Shadow @Final
    public Fluid content;

    @Inject(
            method = "use",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/BucketItem;emptyContents(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/BlockHitResult;Lnet/minecraft/world/item/ItemStack;)Z"
            ),
            cancellable = true
    )
    private void arc$onEmptyBucket(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir, @Local(ordinal = 2) BlockPos blockPos3, @Local(ordinal = 0) ItemStack itemStack) {
        EventResult result = ArcItemEvent.EMPTY_BUCKET.invoker().onEmptyBucket(player, itemStack, level, blockPos3, this.content.defaultFluidState().createLegacyBlock());
        if (result.cancelsEvent()) {
            cir.setReturnValue(InteractionResult.FAIL);
        }
    }
}
