package com.daqem.arc.mixin;

import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.event.triggers.PlayerEvents;
import com.daqem.arc.api.player.ArcServerPlayer;
import net.minecraft.server.level.ServerPlayer;
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

    private LivingEntity getLivingEntity() {
        return (LivingEntity) (Object) this;
    }

    @Inject(at = @At("RETURN"), method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z")
    private void addEffect(MobEffectInstance effect, Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (getLivingEntity() instanceof ArcServerPlayer serverPlayer) {
            MobEffectInstance mobEffectInstance2 = getLivingEntity().getActiveEffectsMap().get(effect.getEffect());
            if (mobEffectInstance2 != null) {
                if (entity != null) {
                    if (entity instanceof ServerPlayer source) {
                        if (source.getName().getString().equals("a")) {
                            return;
                        }
                    }
                }
            }
            ActionResult actionResult = PlayerEvents.onEffectAdded(serverPlayer, effect, entity);
            if (actionResult.shouldCancelAction()) {
                getLivingEntity().removeEffect(effect.getEffect());
            }
        }
    }
}
