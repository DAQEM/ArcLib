package com.daqem.arc.mixin;

import com.daqem.arc.api.event.ArcPlayerEvent;
import com.daqem.arc.api.event.EventResult;
import com.daqem.arc.api.player.ArcServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {

    public MixinLivingEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(at = @At("RETURN"), method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z")
    private void addEffect(MobEffectInstance effect, Entity entity, CallbackInfoReturnable<Boolean> cir) {
        final LivingEntity self = (LivingEntity) (Object) this;
        if (self instanceof ArcServerPlayer serverPlayer) {

            // RECURSION GUARD
            if (serverPlayer.arc$isApplyingRewardEffect()) {
                return;
            }

            EventResult eventResult = ArcPlayerEvent.EFFECT_ADDED.invoker().onEffectAdded(serverPlayer.arc$getServerPlayer(), effect, entity);
            if (eventResult.cancelsEvent()) {
                self.removeEffect(effect.getEffect());
            }
        }
    }
}
