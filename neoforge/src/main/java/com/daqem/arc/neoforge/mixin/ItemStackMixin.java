package com.daqem.arc.neoforge.mixin;

import com.daqem.arc.api.event.ArcItemEvent;
import com.daqem.arc.api.event.EventResult;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableInt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Unique
    private MutableInt arc$damage = null;

    @Inject(
            method = "hurtAndBreak(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;)V",
            at = @At("HEAD"),
            order = 900,
            cancellable = true)
    private void onHurtAndBreak(int damage, ServerLevel serverLevel, LivingEntity livingEntity, Consumer<Item> consumer, CallbackInfo ci) {
        if (livingEntity instanceof ServerPlayer serverPlayer) {
            MutableInt mutableDamage = new MutableInt(damage);
            EventResult eventResult = ArcItemEvent.HURT_ITEM.invoker().onHurtItem(serverPlayer, (ItemStack) (Object) this, mutableDamage);
            if (eventResult.cancelsEvent()) {
                ci.cancel();
                return;
            }
            if (mutableDamage.getValue() != damage) {
                this.arc$damage = mutableDamage;
            }
        }
    }

    @ModifyVariable(
            method = "hurtAndBreak(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;)V",
            at = @At("HEAD"),
            argsOnly = true,
            order = 1100
    )
    private int modifyDamage(int i) {
        try {
            if (this.arc$damage != null) {
                return this.arc$damage.getValue();
            }
            return i;
        }
        finally {
            this.arc$damage = null;
        }
    }
}
