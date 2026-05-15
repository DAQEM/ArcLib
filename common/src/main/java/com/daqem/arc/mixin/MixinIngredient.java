package com.daqem.arc.mixin;

import com.daqem.arc.api.IArcIngredient;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Mixin(Ingredient.class)
public abstract class MixinIngredient implements Predicate<ItemStack>, IArcIngredient {

    @Shadow
    @Nullable
    private ItemStack[] itemStacks;

    @Override
    public List<Item> arc$getItems() {
        if (this.itemStacks == null) return new ArrayList<>();
        return Stream.of(this.itemStacks)
                .filter(stack -> !stack.isEmpty())
                .map(ItemStack::getItem)
                .toList();
    }
}
