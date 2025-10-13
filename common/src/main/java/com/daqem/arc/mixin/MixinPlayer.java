package com.daqem.arc.mixin;

import com.daqem.arc.api.event.ArcPlayerEvent;
import com.daqem.arc.api.event.EventResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(Player.class)
public abstract class MixinPlayer extends LivingEntity {

    protected MixinPlayer(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(at = @At("RETURN"), method = "getCurrentItemAttackStrengthDelay()F", cancellable = true)
    private void getCurrentItemAttackStrengthDelay(CallbackInfoReturnable<Float> cir) {
        Player player = (Player) (Object) this;
        MutableFloat speed = new MutableFloat(cir.getReturnValue());
        EventResult eventResult = ArcPlayerEvent.GET_ATTACK_SPEED.invoker().onGetAttackSpeed(
                player,
                player.getWeaponItem(),
                speed
        );
        if (eventResult.cancelsEvent()) {
            cir.setReturnValue(0F);
        } else if (!Objects.equals(speed.getValue(), cir.getReturnValue())) {
            cir.setReturnValue(speed.getValue());
        }
    }
}