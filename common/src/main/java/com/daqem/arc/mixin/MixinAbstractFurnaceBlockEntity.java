package com.daqem.arc.mixin;

import com.daqem.arc.event.triggers.PlayerEvents;
import com.daqem.arc.api.player.ArcServerPlayer;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class MixinAbstractFurnaceBlockEntity {

    @Shadow
    @Final
    private Object2IntOpenHashMap<ResourceLocation> recipesUsed;

    @Inject(at = @At("HEAD"), method = "awardUsedRecipesAndPopExperience")
    private void awardUsedRecipesAndPopExperience(ServerPlayer serverPlayer, CallbackInfo ci) {
        if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
            ServerLevel serverLevel = serverPlayer.serverLevel();
            this.recipesUsed.forEach((recipeId, recipeCount) -> {
                serverLevel.getRecipeManager().byKey(recipeId).ifPresent((recipe) -> {
                    for (int i = 0; i < recipeCount; i++) {
                        PlayerEvents.onSmeltItem(arcServerPlayer, recipe, recipe.getResultItem(arcServerPlayer.arc$getServerPlayer().getServer().registryAccess()),
                                ((AbstractFurnaceBlockEntity) (Object) this).getBlockPos(), serverLevel);
                    }
                });
            });
        }
    }
}
