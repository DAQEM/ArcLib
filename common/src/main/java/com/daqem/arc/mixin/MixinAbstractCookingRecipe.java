package com.daqem.arc.mixin;

import com.daqem.arc.api.IArcAbstractCookingRecipe;
import com.daqem.arc.api.IArcIngredient;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractCookingRecipe.class)
public abstract class MixinAbstractCookingRecipe implements Recipe<SingleRecipeInput>, IArcAbstractCookingRecipe {

    @Shadow
    @Final
    protected ItemStack result;

    @Shadow
    @Final
    protected Ingredient ingredient;

    @Override
    public ItemStack arc$getResult() {
        return this.result;
    }

    @Override
    public IArcIngredient arc$getIngredient() {
        Ingredient ingredient = this.ingredient;
        if ((Object) ingredient instanceof IArcIngredient arcIngredient) {
            return arcIngredient;
        }
        return null;
    }
}
