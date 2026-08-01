package com.daqem.arc.data.condition.item;

import com.daqem.arc.api.ComponentMatchType;
import com.daqem.arc.api.action.data.IActionDataType;
import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.ICondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.data.RegistryOpsContext;
import com.daqem.arc.model.ArcItemStack;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;

import java.util.ArrayList;
import java.util.List;

public class ItemsCondition extends AbstractCondition {

    private final ComponentMatchType componentMatchType;
    private final List<ArcItemStack> items;
    private final List<TagKey<Item>> itemTags;

    public ItemsCondition(boolean inverted, ComponentMatchType componentMatchType, List<ArcItemStack> items, List<TagKey<Item>> itemTags) {
        super(inverted);
        this.componentMatchType = componentMatchType;
        this.items = items;
        this.itemTags = itemTags;
    }

    @Override
    public boolean isMet(ActionData actionData) {
        ItemStack actionStack = actionData.getData(IActionDataType.ITEM_STACK);
        if (actionStack == null) {
            Item actionItem = actionData.getData(IActionDataType.ITEM);
            if (actionItem != null) {
                actionStack = actionItem.getDefaultInstance();
            } else {
                return false;
            }
        }

        // 1. Check explicit items (with components)
        for (ArcItemStack arcStack : items) {
            if (arcStack.matches(actionStack, componentMatchType)) {
                return true;
            }
        }

        // 2. Check tags dynamically (ItemStack is aware of datapack tags)
        for (TagKey<Item> tag : itemTags) {
            if (actionStack.is(tag)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public IConditionType<? extends ICondition> getType() {
        return IConditionType.ITEMS;
    }

    @Override
    public Component getDescription() {
        return getDescription(items.stream().map(arcItemStack -> arcItemStack.getItemStack().getDisplayName()).reduce(
                (a, b) -> ((MutableComponent) a).append(", ").append(b)
        ).orElse(Component.literal("No Items")
        ), itemTags.stream().map(TagKey::location).map(Identifier::toString).reduce(
                (a, b) -> a + ", " + b
        ).orElse("No Item Tags"));
    }

    public List<ItemStack> getItemStacks(RegistryAccess registryAccess) {
        List<ItemStack> itemStacks = new ArrayList<>();
        for (ArcItemStack item : items) {
            itemStacks.add(item.getItemStack());
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

    public ComponentMatchType getComponentMatchType() {
        return componentMatchType;
    }

    public List<ArcItemStack> getItems() {
        return items;
    }

    public List<TagKey<Item>> getItemTags() {
        return itemTags;
    }

    public static class Serializer implements IConditionSerializer<ItemsCondition> {

        @Override
        public ItemsCondition fromJson(Identifier location, JsonObject jsonObject, boolean inverted) {
            ComponentMatchType componentMatchType = ComponentMatchType.fromJson(jsonObject, "check_components", ComponentMatchType.NONE);
            List<ArcItemStack> items = new ArrayList<>();
            List<TagKey<Item>> itemTags = new ArrayList<>();

            JsonArray itemsArray = GsonHelper.getAsJsonArray(jsonObject, "items");
            for (JsonElement element : itemsArray) {
                if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
                    String str = element.getAsString();
                    if (str.startsWith("#")) {
                        // Store the tag to be evaluated at runtime
                        TagKey<Item> tagKey = TagKey.create(Registries.ITEM, Identifier.parse(str.substring(1)));
                        itemTags.add(tagKey);
                    } else {
                        BuiltInRegistries.ITEM.get(Identifier.parse(str)).ifPresent(item ->
                                items.add(new ArcItemStack(new ItemStackTemplate(item.value()))));
                    }
                } else if (element.isJsonObject()) {
                    ArcItemStack stack = ArcItemStack.CODEC.parse(RegistryOpsContext.getOps(JsonOps.INSTANCE), element).getOrThrow();
                    items.add(stack);
                }
            }

            return new ItemsCondition(inverted, componentMatchType, items, itemTags);
        }

        @Override
        public ItemsCondition fromNetwork(Identifier location, RegistryFriendlyByteBuf buf, boolean inverted) {
            ComponentMatchType componentMatchType = buf.readEnum(ComponentMatchType.class);
            List<ArcItemStack> items = buf.readList(buf1 -> ArcItemStack.STREAM_CODEC.decode((RegistryFriendlyByteBuf) buf1));

            int tagCount = buf.readVarInt();
            List<TagKey<Item>> itemTags = new ArrayList<>(tagCount);
            for (int i = 0; i < tagCount; i++) {
                itemTags.add(TagKey.create(Registries.ITEM, buf.readIdentifier()));
            }

            return new ItemsCondition(inverted, componentMatchType, items, itemTags);
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, ItemsCondition type) {
            IConditionSerializer.super.toNetwork(buf, type);
            buf.writeEnum(type.componentMatchType);
            buf.writeCollection(type.items, (buf1, stack) -> ArcItemStack.STREAM_CODEC.encode((RegistryFriendlyByteBuf) buf1, stack));

            buf.writeVarInt(type.itemTags.size());
            for (TagKey<Item> tag : type.itemTags) {
                buf.writeIdentifier(tag.location());
            }
        }
    }
}