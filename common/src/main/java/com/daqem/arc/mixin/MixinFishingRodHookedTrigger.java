package com.daqem.arc.mixin;

import com.daqem.arc.api.event.ArcPlayerEvent;
import net.minecraft.advancements.criterion.FishingRodHookedTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(FishingRodHookedTrigger.class)
public class MixinFishingRodHookedTrigger {

    @Inject(method = "trigger", at = @At("HEAD"))
    private void trigger(ServerPlayer serverPlayer, ItemStack itemStack, FishingHook fishingHook, Collection<ItemStack> collection, CallbackInfo ci) {
        for (ItemStack stack : collection) {
            ArcPlayerEvent.FISH_UP_ITEM.invoker().onFishUpItem(serverPlayer, stack);
        }
    }
}
