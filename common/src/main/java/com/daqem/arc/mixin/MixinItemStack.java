package com.daqem.arc.mixin;

import com.daqem.arc.event.triggers.PlayerEvents;
import com.daqem.arc.api.player.ArcServerPlayer;
import com.daqem.arc.api.action.result.ActionResult;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class MixinItemStack {

    @Shadow
    public abstract UseAnim getUseAnimation();

    @Shadow
    public abstract Item getItem();

    @Inject(at = @At("HEAD"), method = "finishUsingItem(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/item/ItemStack;")
    private void finishUsingItem(Level level, LivingEntity entity, CallbackInfoReturnable<ItemStack> cir) {
        if (entity instanceof ArcServerPlayer player) {
            if (this.getUseAnimation() == UseAnim.DRINK) {
                PlayerEvents.onPlayerDrink(player, getItemStack());
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "hurt(ILnet/minecraft/util/RandomSource;Lnet/minecraft/server/level/ServerPlayer;)Z", cancellable = true)
    private void hurt(int i, RandomSource randomSource, ServerPlayer serverPlayer, CallbackInfoReturnable<Boolean> cir) {
        if (serverPlayer instanceof ArcServerPlayer player) {
            if (getItemStack().isDamageableItem()) {
                ActionResult actionResult = PlayerEvents.onPlayerHurtItem(player, getItemStack());
                if (actionResult.shouldCancelAction()) {
                    cir.setReturnValue(false);
                    cir.cancel();
                }
            }
        }
    }

    private ItemStack getItemStack() {
        return (ItemStack) (Object) this;
    }
}
