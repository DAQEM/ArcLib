package com.daqem.arc.data.condition.item;

import com.daqem.arc.api.ComponentMatchType;
import com.daqem.arc.api.action.data.IActionDataType;
import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.ICondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.data.ActionData;
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
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemsCondition extends AbstractCondition {

    private final ComponentMatchType componentMatchType;
    private final List<ArcItemStack> items;

    public ItemsCondition(boolean inverted, ComponentMatchType componentMatchType, List<ArcItemStack> items) {
        super(inverted);
        this.componentMatchType = componentMatchType;
        this.items = items;
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

        for (ArcItemStack arcStack : items) {
            if (arcStack.matches(actionStack, componentMatchType)) {
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
        List<String> allNames = items.stream()
                .map(arcStack -> BuiltInRegistries.ITEM.getKey(arcStack.getItem()).toString())
                .distinct()
                .collect(Collectors.toList());
        return super.getDescription(String.join(", ", allNames));
    }

    public List<ItemStack> getItemStacks() {
        return getItemStacks(null);
    }

    @Deprecated()
    public List<ItemStack> getItemStacks(RegistryAccess registryAccess) {
        return items.stream().map(ArcItemStack::getItemStack).collect(Collectors.toList());
    }

    public ComponentMatchType getComponentMatchType() {
        return componentMatchType;
    }

    public static class Serializer implements IConditionSerializer<ItemsCondition> {

        @Override
        public ItemsCondition fromJson(Identifier location, JsonObject jsonObject, boolean inverted) {
            ComponentMatchType componentMatchType = ComponentMatchType.fromJson(jsonObject, "check_components", ComponentMatchType.NONE);
            List<ArcItemStack> items = new ArrayList<>();

            JsonArray itemsArray = GsonHelper.getAsJsonArray(jsonObject, "items");
            for (JsonElement element : itemsArray) {
                if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
                    String str = element.getAsString();
                    if (str.startsWith("#")) {
                        TagKey<Item> tagKey = TagKey.create(Registries.ITEM, Identifier.parse(str.substring(1)));
                        BuiltInRegistries.ITEM.get(tagKey).ifPresent(named ->
                                named.forEach(holder ->
                                        items.add(new ArcItemStack(new ItemStackTemplate(holder.value())))));
                    } else {
                        BuiltInRegistries.ITEM.get(Identifier.parse(str)).ifPresent(item ->
                                items.add(new ArcItemStack(new ItemStackTemplate(item.value()))));
                    }
                } else if (element.isJsonObject()) {
                    ArcItemStack stack = ArcItemStack.CODEC.parse(JsonOps.INSTANCE, element).getOrThrow();
                    items.add(stack);
                }
            }

            return new ItemsCondition(inverted, componentMatchType, items);
        }

        @Override
        public ItemsCondition fromNetwork(Identifier location, RegistryFriendlyByteBuf buf, boolean inverted) {
            ComponentMatchType componentMatchType = buf.readEnum(ComponentMatchType.class);
            List<ArcItemStack> items = buf.readList(buf1 -> ArcItemStack.STREAM_CODEC.decode((RegistryFriendlyByteBuf) buf1));
            return new ItemsCondition(inverted, componentMatchType, items);
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, ItemsCondition type) {
            IConditionSerializer.super.toNetwork(buf, type);
            buf.writeEnum(type.componentMatchType);
            buf.writeCollection(type.items, (buf1, stack) -> ArcItemStack.STREAM_CODEC.encode((RegistryFriendlyByteBuf) buf1, stack));
        }
    }
}