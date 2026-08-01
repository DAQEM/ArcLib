package com.daqem.arc.model;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;

public class ArcItemStack {

    ItemStackTemplate itemStackTemplate;
    private ItemStack itemStack;

    public static final Codec<ArcItemStack> CODEC = ItemStackTemplate.CODEC.xmap(ArcItemStack::new, ArcItemStack::getItemStackTemplate);

    public static final StreamCodec<RegistryFriendlyByteBuf, ArcItemStack> STREAM_CODEC = ItemStackTemplate.STREAM_CODEC.map(ArcItemStack::new, ArcItemStack::getItemStackTemplate);

    public ArcItemStack(ItemStackTemplate itemStackTemplate) {
        this.itemStackTemplate = itemStackTemplate;
        this.itemStack = null;
    }

    public ItemStackTemplate getItemStackTemplate() {
        return itemStackTemplate;
    }

    public ItemStack getItemStack() {
        if (itemStack == null) {
            itemStack = itemStackTemplate.create();
        }
        return itemStack;
    }

    public Item getItem() {
        return getItemStack().getItem();
    }

    public Component getDisplayName() {
        return getItemStack().getHoverName();
    }

    public boolean matches(ItemStack other, boolean checkComponents) {
        boolean sameItem = ItemStack.isSameItem(getItemStack(), other);
        boolean hasCount = getItemStack().getCount() > 1;
        boolean sameCount = other.getCount() == getItemStack().getCount();
        boolean passOnCount = !hasCount || sameCount;
        boolean hasComponents = !checkComponents || !getItemStack().getComponents().isEmpty();
        boolean sameComponents = !checkComponents || ItemStack.isSameItemSameComponents(getItemStack(), other);
        boolean passOnComponents = !checkComponents || !hasComponents || sameComponents;
        return sameItem && passOnCount && passOnComponents;
    }
}
