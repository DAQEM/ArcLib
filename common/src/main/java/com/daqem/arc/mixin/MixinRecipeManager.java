package com.daqem.arc.mixin;

import com.daqem.arc.data.RegistryOpsContext;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RecipeManager.class)
public class MixinRecipeManager {

    @Inject(method = "<init>", at = @At("RETURN"))
    private void arc$captureRegistryAccess(HolderLookup.Provider registries, CallbackInfo ci) {
        // Capture the active Registry Access for our ActionManager to use during prepare()
        RegistryOpsContext.set(registries);
    }
}