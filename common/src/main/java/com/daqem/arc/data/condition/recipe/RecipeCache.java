package com.daqem.arc.data.condition.recipe;

import com.daqem.arc.api.IArcAbstractCookingRecipe;
import com.daqem.arc.api.IArcIngredient;
import com.daqem.arc.data.ActionData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RecipeCache {

    private static final Map<Class<? extends Recipe<?>>, List<IArcAbstractCookingRecipe>> recipeCache = new ConcurrentHashMap<>();

    /**
     * Invalidates and clears the entire recipe cache. This should be called
     * whenever server resources are reloaded.
     */
    public static void invalidate() {
        recipeCache.clear();
    }

    /**
     * Retrieves a cached list of recipes for a given class, or builds the cache if it doesn't exist.
     * @param server The Minecraft server instance.
     * @param recipeClass The class of the recipes to retrieve (e.g., BlastingRecipe.class).
     * @return A list of recipes of the specified type.
     */
    private static List<IArcAbstractCookingRecipe> getRecipes(MinecraftServer server, Class<? extends Recipe<?>> recipeClass) {
        return recipeCache.computeIfAbsent(recipeClass, (clazz) ->
                server.getRecipeManager().getRecipes().stream()
                        .map(RecipeHolder::value)
                        .filter(clazz::isInstance)
                        .map(r -> (IArcAbstractCookingRecipe) r) // This cast is safe due to the filter
                        .toList());
    }

    /**
     * Checks if a given cooking recipe has an equivalent version of another recipe type.
     * For example, it can check if a SmeltingRecipe for diamond ore has an equivalent BlastingRecipe.
     *
     * @param actionData The current action data.
     * @param currentRecipe The recipe being processed (e.g., a SmeltingRecipe).
     * @param targetRecipeClass The recipe type to check for (e.g., BlastingRecipe.class).
     * @return true if an equivalent recipe of the target type exists, false otherwise.
     */
    public static boolean isRecipeType(ActionData actionData, IArcAbstractCookingRecipe currentRecipe, Class<? extends Recipe<?>> targetRecipeClass) {
        MinecraftServer server = actionData.getPlayer().arc$getPlayer().level().getServer();
        if (server == null) {
            return false;
        }

        List<IArcAbstractCookingRecipe> candidateRecipes = getRecipes(server, targetRecipeClass);
        IArcIngredient baseIngredient = currentRecipe.arc$getIngredient();

        // If the recipe being cooked has no ingredient, it cannot match anything.
        if (baseIngredient == null) return false;

        for (IArcAbstractCookingRecipe candidateRecipe : candidateRecipes) {
            // 1. Check if the results are the same item.
            boolean resultsMatch = candidateRecipe.arc$getResult().getItem().equals(currentRecipe.arc$getResult().getItem());
            if (!resultsMatch) {
                continue;
            }

            // 2. Check if the ingredients are equivalent.
            IArcIngredient candidateIngredient = candidateRecipe.arc$getIngredient();
            if (candidateIngredient == null) {
                continue;
            }

            // Use HashSets to compare ingredients regardless of order, which is more robust for tags.
            if (new HashSet<>(baseIngredient.arc$getItems()).equals(new HashSet<>(candidateIngredient.arc$getItems()))) {
                return true; // Found an equivalent recipe of the target type.
            }
        }
        return false;
    }
}