package com.daqem.arc.data.condition.item;

import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.model.target.ArcItemTarget;
import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemsCondition extends AbstractCondition {

    private final List<Item> items;
    private final List<TagKey<Item>> itemTags;
    private final ArcItemTarget target;

    public ItemsCondition(boolean inverted, List<Item> items, List<TagKey<Item>> itemTags, ArcItemTarget target) {
        super(inverted);
        this.items = items;
        this.itemTags = itemTags;
        this.target = target;
    }

    @Override
    public Component getDescription() {
        return getDescription(items.stream().map(item -> item.getDefaultInstance().getDisplayName()).reduce(
                (a, b) -> ((MutableComponent) a).append(", ").append(b)
        ).orElse(Component.literal("No Items")
        ), itemTags.stream().map(TagKey::location).map(ResourceLocation::toString).reduce(
                (a, b) -> a + ", " + b
        ).orElse("No Item Tags"));
    }

    @Override
    public boolean isMet(ActionData actionData) {
        ItemStack itemStack = target.getItemStack(actionData, actionData.getPlayer().arc$getPlayer());
        return itemStack != null && !itemStack.isEmpty() && (isItem(itemStack) || isItemByTag(itemStack));
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.ITEMS;
    }

    private boolean isItem(ItemStack itemStack) {
        return this.items.contains(itemStack.getItem());
    }

    private boolean isItemByTag(ItemStack itemStack) {
        return this.itemTags.stream().anyMatch(itemStack::is);
    }

    public List<Item> getItems() {
        return items;
    }

    public List<TagKey<Item>> getItemTags() {
        return itemTags;
    }

    public ArcItemTarget getTarget() {
        return target;
    }

    public List<ItemStack> getItemStacks(RegistryAccess registryAccess) {
        List<ItemStack> itemStacks = new ArrayList<>();
        for (Item item : items) {
            itemStacks.add(new ItemStack(item));
        }
        for (TagKey<Item> itemTag : itemTags) {
            registryAccess.lookupOrThrow(Registries.ITEM).get(itemTag).ifPresent(holders -> {
                for (var holder : holders) {
                    itemStacks.add(new ItemStack(holder.value()));
                }
            });
        }
        return itemStacks;
    }

    public static class Serializer implements IConditionSerializer<ItemsCondition> {
        @Override
        public ItemsCondition fromJson(ResourceLocation location, JsonObject jsonObject, boolean inverted) {
            return new ItemsCondition(
                    inverted,
                    getItems(jsonObject, "items"),
                    getItemTags(jsonObject, "items"),
                    getItemTarget(jsonObject, "target", ArcItemTarget.ACTION)
            );
        }

        @Override
        public ItemsCondition fromNetwork(ResourceLocation location, RegistryFriendlyByteBuf buf, boolean inverted) {
            int itemCount = buf.readVarInt();
            int tagCount = buf.readVarInt();

            List<Item> items = new ArrayList<>();
            List<TagKey<Item>> itemTags = new ArrayList<>();

            for (int i = 0; i < itemCount; i++) {
                items.add(ByteBufCodecs.registry(Registries.ITEM).decode(buf));
            }
            for (int i = 0; i < tagCount; i++) {
                itemTags.add(TagKey.create(BuiltInRegistries.ITEM.key(), buf.readResourceLocation()));
            }

            return new ItemsCondition(inverted, items, itemTags, buf.readEnum(ArcItemTarget.class));
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, ItemsCondition type) {
            IConditionSerializer.super.toNetwork(buf, type);
            buf.writeVarInt(type.items.size());
            buf.writeVarInt(type.itemTags.size());
            type.items.forEach(item -> ByteBufCodecs.registry(Registries.ITEM).encode(buf, item));
            type.itemTags.forEach(tag -> buf.writeResourceLocation(tag.location()));
            buf.writeEnum(type.target);
        }
    }
}