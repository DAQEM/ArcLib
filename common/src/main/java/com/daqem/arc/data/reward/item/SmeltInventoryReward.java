package com.daqem.arc.data.reward.item;

import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.reward.AbstractReward;
import com.daqem.arc.api.reward.IRewardSerializer;
import com.daqem.arc.api.reward.IRewardType;
import com.daqem.arc.data.ActionData;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SmeltInventoryReward extends AbstractReward {

    private final List<ResourceLocation> recipes;

    public SmeltInventoryReward(double chance, int priority, List<ResourceLocation> recipes) {
        super(chance, priority);
        this.recipes = recipes;
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        if (actionData.getPlayer().arc$getPlayer() instanceof ServerPlayer player) {
            ServerLevel level = player.serverLevel();
            RecipeManager recipeManager = level.getRecipeManager();

            List<ItemStack> itemsToAdd = new ArrayList<>();
            List<Integer> slotsToClear = new ArrayList<>();

            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack stack = player.getInventory().getItem(i);
                if (!stack.isEmpty()) {
                    SingleRecipeInput singleRecipeInput = new SingleRecipeInput(stack);
                    Optional<RecipeHolder<@NotNull SmeltingRecipe>> recipeHolderOpt = recipeManager.getRecipeFor(RecipeType.SMELTING, singleRecipeInput, level);

                    if (recipeHolderOpt.isPresent()) {
                        if (!recipes.isEmpty() && !recipes.contains(recipeHolderOpt.get().id())) {
                            continue;
                        }

                        Recipe<@NotNull SingleRecipeInput> recipe = recipeHolderOpt.get().value();
                        ItemStack result = recipe.assemble(singleRecipeInput, player.registryAccess());

                        if (!result.isEmpty()) {
                            ItemStack smeltedStack = result.copy();
                            smeltedStack.setCount(stack.getCount());
                            itemsToAdd.add(smeltedStack);
                            slotsToClear.add(i);
                        }
                    }
                }
            }

            for (int slot : slotsToClear) {
                player.getInventory().setItem(slot, ItemStack.EMPTY);
            }

            for (ItemStack stackToAdd : itemsToAdd) {
                player.getInventory().add(stackToAdd);
                if (!stackToAdd.isEmpty()) {
                    player.drop(stackToAdd, false);
                }
            }
        }
        return new ActionResult();
    }

    @Override
    public IRewardType<?> getType() {
        return IRewardType.SMELT_INVENTORY;
    }

    public static class Serializer implements IRewardSerializer<SmeltInventoryReward> {

        @Override
        public SmeltInventoryReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new SmeltInventoryReward(
                    chance,
                    priority,
                    getOptionalResourceLocations(jsonObject, "recipes")
            );
        }

        @Override
        public SmeltInventoryReward fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf, double chance, int priority) {
            return new SmeltInventoryReward(
                    chance,
                    priority,
                    friendlyByteBuf.readList(FriendlyByteBuf::readResourceLocation)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, SmeltInventoryReward type) {
            IRewardSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeCollection(type.recipes, FriendlyByteBuf::writeResourceLocation);
        }
    }
}