package com.daqem.arc.data.reward.player;

import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.reward.AbstractReward;
import com.daqem.arc.api.reward.IRewardSerializer;
import com.daqem.arc.api.reward.IRewardType;
import com.daqem.arc.data.ActionData;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.Collection;
import java.util.List;

public class GiveRecipesReward extends AbstractReward {

    private final List<ResourceLocation> recipes;

    public GiveRecipesReward(double chance, int priority, List<ResourceLocation> recipes) {
        super(chance, priority);
        this.recipes = recipes;
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        if (actionData.getPlayer().arc$getPlayer() instanceof ServerPlayer player) {
            Collection<RecipeHolder<?>> recipesToUnlock = player.level().getServer().getRecipeManager().getRecipes().stream()
                    .filter(recipe -> this.recipes.contains(recipe.id().location()))
                    .toList();
            player.awardRecipes(recipesToUnlock);
        }
        return new ActionResult();
    }

    @Override
    public IRewardType<?> getType() {
        return IRewardType.GIVE_KNOWLEDGE;
    }

    public static class Serializer implements IRewardSerializer<GiveRecipesReward> {

        @Override
        public GiveRecipesReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new GiveRecipesReward(
                    chance,
                    priority,
                    getResourceLocations(jsonObject, "recipes")
            );
        }

        @Override
        public GiveRecipesReward fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf, double chance, int priority) {
            return new GiveRecipesReward(chance, priority, friendlyByteBuf.readList(FriendlyByteBuf::readResourceLocation));
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, GiveRecipesReward type) {
            IRewardSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeCollection(type.recipes, FriendlyByteBuf::writeResourceLocation);
        }
    }
}