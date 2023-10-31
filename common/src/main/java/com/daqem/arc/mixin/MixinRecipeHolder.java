package com.daqem.arc.mixin;

import com.daqem.arc.event.triggers.PlayerEvents;
import com.daqem.arc.api.player.ArcServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Collections;
import java.util.List;

@Mixin(RecipeHolder.class)
public interface MixinRecipeHolder {

    @Unique
    default void awardUsedRecipes(Player player, List<ItemStack> list) {
        Recipe<?> recipe = ((RecipeHolder) this).getRecipeUsed();
        if (player instanceof ArcServerPlayer arcServerPlayer) {
            if (recipe != null) {
                PlayerEvents.onCraftItem(arcServerPlayer, recipe, recipe.getResultItem(arcServerPlayer.arc$getServerPlayer().getServer().registryAccess()), player.level());
            }
        }
        if (recipe != null) {
            player.triggerRecipeCrafted(recipe, list);
            if (!recipe.isSpecial()) {
                player.awardRecipes(Collections.singleton(recipe));
                ((RecipeHolder) this).setRecipeUsed(null);
            }
        }
    }
}
