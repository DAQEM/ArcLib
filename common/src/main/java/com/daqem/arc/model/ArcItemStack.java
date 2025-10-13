package com.daqem.arc.model;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

public record ArcItemStack(ItemStack itemStack) {

    public static final Codec<ArcItemStack> CODEC = ItemStack.CODEC.xmap(ArcItemStack::new, ArcItemStack::itemStack);

    public static final StreamCodec<RegistryFriendlyByteBuf, ArcItemStack> STREAM_CODEC = ItemStack.STREAM_CODEC.map(ArcItemStack::new, ArcItemStack::itemStack);

    public Component getDisplayName() {
        return itemStack.getHoverName();
    }

    public boolean matches(ItemStack other, boolean checkComponents) {
        boolean sameItem = ItemStack.isSameItem(this.itemStack, other);
        boolean hasCount = this.itemStack.getCount() > 1;
        boolean sameCount = other.getCount() == this.itemStack.getCount();
        boolean passOnCount = !hasCount || sameCount;
        boolean hasComponents = !checkComponents || !this.itemStack.getComponents().isEmpty();
        boolean sameComponents = !checkComponents || ItemStack.isSameItemSameComponents(this.itemStack, other);
        boolean passOnComponents = !checkComponents || !hasComponents || sameComponents;
        return sameItem && passOnCount && passOnComponents;
    }
}
