package com.daqem.arc.data.condition.recipe;

import com.daqem.arc.api.action.data.ActionData;
import com.daqem.arc.api.condition.AbstractCondition;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.SmeltingRecipe;

import java.util.List;

public abstract class IsRecipeCondition<T extends Recipe<?>> extends AbstractCondition {

    public IsRecipeCondition(boolean inverted) {
        super(inverted);
    }

    protected boolean isSmeltingRecipeVersion(ActionData actionData, SmeltingRecipe smeltingRecipe, Class<? extends Recipe<?>> recipeClass) {
        MinecraftServer server = actionData.getPlayer().arc$getPlayer().getServer();
        if (server != null) {
            List<T> recipes = server.getRecipeManager().getRecipes().stream()
                    .filter(recipeClass::isInstance)
                    .map(r -> (T) r)
                    .toList();
            for (T r : recipes) {
                if (r.getResultItem(server.registryAccess()).getItem().equals(smeltingRecipe.getResultItem(server.registryAccess()).getItem())) {
                    for (int i = 0; i < r.getIngredients().size(); i++) {
                        Ingredient ingredient = r.getIngredients().get(i);
                        for (int i1 = 0; i1 < ingredient.getItems().length; i1++) {
                            Item item = ingredient.getItems()[i1].getItem();
                            Item item1 = smeltingRecipe.getIngredients().get(i).getItems()[i1].getItem();
                            if (item != item1) {
                                return false;
                            }
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
