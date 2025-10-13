package com.daqem.arc.mixin;

import com.daqem.arc.api.event.ArcItemEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ResultSlot.class)
public abstract class MixinResultSlot {

    @Shadow
    @Final
    private Player player;

    @Inject(method = "checkTakeAchievements", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/RecipeCraftingHolder;awardUsedRecipes(Lnet/minecraft/world/entity/player/Player;Ljava/util/List;)V"))
    public void arc$onRecipeCrafted(ItemStack itemStack, CallbackInfo ci) {
        if (this.player instanceof ServerPlayer serverPlayer) {
            if (((Slot) (Object) this).container instanceof RecipeCraftingHolder recipeCraftingHolder) {
                if (recipeCraftingHolder.getRecipeUsed() instanceof RecipeHolder<?> recipeHolder) {
                    ArcItemEvent.CRAFT_ITEM.invoker().onCraftItem(serverPlayer, recipeHolder.value(), itemStack);
                }
            }
        }
    }
}
