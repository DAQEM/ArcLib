package com.daqem.arc.fabric.mixin;

import com.daqem.arc.api.event.ArcEntityEvent;
import com.daqem.arc.api.event.ArcPlayerEvent;
import com.daqem.arc.api.event.EventResult;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityFabricMixin {

    @Unique
    private MutableFloat arc$damage = null;

    @Inject(
            method = "hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;isSleeping()Z",
                    shift = At.Shift.BEFORE
            ),
            order = 900,
            cancellable = true
    )
    private void onHurtServer(ServerLevel serverLevel, DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> cir) {
        this.arc$damage = new MutableFloat(f);
        final LivingEntity self = (LivingEntity) (Object) this;
        if (self instanceof ServerPlayer serverPlayer) {
            EventResult eventResult = ArcPlayerEvent.ENTITY_HURT_PLAYER.invoker().onEntityHurtPlayer(serverPlayer, damageSource, this.arc$damage);
            if (eventResult.cancelsEvent()) {
                cir.setReturnValue(false);
                return;
            }
        }
        if (damageSource.getEntity() instanceof ServerPlayer serverPlayer) {
            EventResult eventResult = ArcEntityEvent.PLAYER_HURT_ENTITY.invoker().onPlayerHurtEntity(serverPlayer, self, damageSource, this.arc$damage);
            if (eventResult.cancelsEvent()) {
                cir.setReturnValue(false);
                return;
            }
        }
        if (self instanceof ServerPlayer defender && damageSource.getEntity() instanceof ServerPlayer attacker) {
            EventResult eventResult = ArcPlayerEvent.PLAYER_HURT_PLAYER.invoker().onPlayerHurtPlayer(attacker, defender, damageSource, this.arc$damage);
            if (eventResult.cancelsEvent()) {
                cir.setReturnValue(false);
            }
        }
    }

    @ModifyVariable(
            method = "hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;isSleeping()Z",
                    shift = At.Shift.BEFORE
            ),
            order = 1100,
            name = "f"
    )
    private float modifyDamage(float f) {
        if (this.arc$damage != null) {
            return this.arc$damage.getValue();
        }
        return f;
    }
}
