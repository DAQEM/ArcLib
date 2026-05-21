package com.daqem.arc.mixin;

import com.daqem.arc.api.blockentity.IArcBrewingStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BaseContainerBlockEntity.class)
public class MixinBaseContainerBlockEntity {

    @Inject(at = @At("HEAD"), method = "stillValid(Lnet/minecraft/world/entity/player/Player;)Z")
    private void stillValid(Player player, CallbackInfoReturnable<Boolean> cir) {
        if ((BaseContainerBlockEntity) (Object) this instanceof BrewingStandBlockEntity brewingStand) {
            ((IArcBrewingStand) brewingStand).arc$setLastPlayerToInteract(player.getUUID());
        }
    }
}