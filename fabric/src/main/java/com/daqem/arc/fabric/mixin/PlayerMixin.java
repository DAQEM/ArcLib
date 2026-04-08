package com.daqem.arc.fabric.mixin;

import com.daqem.arc.api.event.ArcItemEvent;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerMixin {

    @Inject(method = "drop(Lnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/entity/item/ItemEntity;", at = @At("RETURN"), cancellable = true)
    private void drop(ItemStack itemStack, boolean bl, CallbackInfoReturnable<ItemEntity> cir) {
        if (cir.getReturnValue() != null && ArcItemEvent.DROP_ITEM.invoker().onDropItem((Player) (Object) this, cir.getReturnValue()).cancelsEvent()) {
            cir.setReturnValue(null);
        }
    }
}
