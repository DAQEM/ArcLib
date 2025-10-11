package com.daqem.arc.mixin;

import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MobEffectInstance.class)
public interface MobEffectInstanceAccessor {

    @Accessor("amplifier")
    void arc$setAmplifier(int amplifier);

    @Accessor("duration")
    void arc$setDuration(int duration);

    @Accessor("ambient")
    void arc$setAmbient(boolean ambient);

    @Accessor("showIcon")
    void arc$setShowIcon(boolean showIcon);

    @Accessor("visible")
    void arc$setVisible(boolean visible);

}
