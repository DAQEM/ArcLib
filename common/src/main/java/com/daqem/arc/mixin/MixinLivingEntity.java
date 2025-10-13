package com.daqem.arc.mixin;

import com.daqem.arc.api.event.ArcPlayerEvent;
import com.daqem.arc.api.event.EventResult;
import com.daqem.arc.api.player.ArcServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {

    public MixinLivingEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(at = @At(
            value = "INVOKE",
            target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;",
            shift = At.Shift.BEFORE
    ), method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z", cancellable = true)
    private void addEffect(MobEffectInstance effect, Entity entity, CallbackInfoReturnable<Boolean> cir) {
        final LivingEntity self = (LivingEntity) (Object) this;
        if (self instanceof ArcServerPlayer serverPlayer) {
            EventResult eventResult = ArcPlayerEvent.ADD_EFFECT.invoker().onAddEffect(serverPlayer.arc$getServerPlayer(), effect, entity);
            if (eventResult.cancelsEvent()) {
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "jumpFromGround")
    private void arc$onJump(CallbackInfo ci) {
        if ((Object) this instanceof Player player) {
            ArcPlayerEvent.JUMP.invoker().onJump(player);
        }
    }

    @Inject(at = @At("HEAD"), method = "causeFallDamage", cancellable = true)
    private void arc$onLand(double d, float f, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof Player player) {
            EventResult eventResult = ArcPlayerEvent.LAND_ON_GROUND.invoker().onLandOnGround(player, this.fallDistance);
            if (eventResult.cancelsEvent()) {
                cir.setReturnValue(false);
            }
        }
    }
}