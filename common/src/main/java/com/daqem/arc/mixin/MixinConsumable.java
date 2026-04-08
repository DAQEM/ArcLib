package com.daqem.arc.mixin;

import com.daqem.arc.api.event.ArcPlayerEvent;
import com.daqem.arc.api.event.EventResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Consumable.class)
public abstract class MixinConsumable {

    @Shadow public abstract ItemUseAnimation animation();

    @Inject(at = @At("HEAD"), method = "onConsume", cancellable = true)
    private void onConsume(Level level, LivingEntity livingEntity, ItemStack itemStack, CallbackInfoReturnable<ItemStack> cir) {
        if (livingEntity instanceof Player player) {
            if (this.animation() == ItemUseAnimation.EAT) {
                EventResult eventResult = ArcPlayerEvent.EAT.invoker().onEat(player, itemStack);
                if (eventResult.cancelsEvent()) {
                    cir.setReturnValue(itemStack);
                }
            }
            if (this.animation() == ItemUseAnimation.DRINK) {
                EventResult eventResult = ArcPlayerEvent.DRINK.invoker().onDrink(player, itemStack);
                if (eventResult.cancelsEvent()) {
                    cir.setReturnValue(itemStack);
                }
            }
        }
    }
}
