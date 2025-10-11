package com.daqem.arc.data.serializer;

import com.daqem.arc.api.ComparisonType;
import com.daqem.arc.model.ArcBlockState;
import com.daqem.arc.model.EntityDataProperty;
import com.google.gson.*;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface ArcSerializer {

    //region Resource Location

    default ResourceLocation getResourceLocation(JsonObject jsonObject, String key, @Nullable ResourceLocation defaultLocation) {
        if (!jsonObject.has(key) || jsonObject.get(key).isJsonNull()) {
            if (defaultLocation != null) {
                return defaultLocation;
            }
            throw new JsonParseException("Expected '" + key + "' to be a resource location");
        }

        return ResourceLocation.CODEC.decode(JsonOps.INSTANCE, jsonObject.get(key)).result()
                .orElseGet(() -> {
                    if (defaultLocation != null) {
                        return new Pair<>(defaultLocation, null);
                    }
                    throw new JsonParseException("Expected '" + jsonObject.get(key) + "' to be a resource location, but it was invalid");
                }).getFirst();
    }

    default ResourceLocation getResourceLocation(JsonObject jsonObject, String key) {
        return getResourceLocation(jsonObject, key, null);
    }

    default @Nullable ResourceLocation getOptionalResourceLocation(JsonObject jsonObject, String key) {
        if (!jsonObject.has(key) || jsonObject.get(key).isJsonNull()) {
            return null;
        }
        return getResourceLocation(jsonObject, key, null);
    }

    //endregion

    //region Item

    default Item getItem(JsonObject jsonObject, String key, @Nullable Item defaultItem) {
        if (!jsonObject.has(key) || jsonObject.get(key).isJsonNull()) {
            if (defaultItem != null) {
                return defaultItem;
            }
            throw new JsonParseException("Expected '" + key + "' to be an item");
        }

        return BuiltInRegistries.ITEM.byNameCodec().decode(JsonOps.INSTANCE, jsonObject.get(key)).result()
                .orElseGet(() -> {
                    if (defaultItem != null) {
                        return new Pair<>(defaultItem, null);
                    }
                    throw new JsonParseException("Expected '" + jsonObject.get(key) + "' to be an item, but it was invalid");
                }).getFirst();
    }

    default Item getItem(JsonObject jsonObject, String key) {
        return getItem(jsonObject, key, null);
    }

    default @Nullable Item getOptionalItem(JsonObject jsonObject, String key) {
        if (!jsonObject.has(key) || jsonObject.get(key).isJsonNull()) {
            return null;
        }
        return getItem(jsonObject, key, null);
    }

    //endregion

    //region Items

    default List<Item> getItems(JsonObject jsonObject, String key, List<Item> defaultItems) {
        if (!jsonObject.has(key) || jsonObject.get(key).isJsonNull()) {
            if (defaultItems != null) {
                return defaultItems;
            }
            throw new JsonParseException("Expected '" + key + "' to be a list of items");
        }

        JsonElement element = jsonObject.get(key);

        if (element.isJsonArray()) {
            JsonArray jsonArray = element.getAsJsonArray();
            List<Item> items = new ArrayList<>();
            for (JsonElement itemElement : jsonArray) {
                String itemName = itemElement.getAsString();
                if (itemName != null && !itemName.startsWith("#")) {
                    Item item = BuiltInRegistries.ITEM.byNameCodec().decode(JsonOps.INSTANCE, itemElement).result()
                            .orElseThrow(() -> new JsonParseException("Expected '" + key + "' to be a list of items, but one of the items was invalid: " + itemName))
                            .getFirst();
                    items.add(item);
                }
            }
            return items;
        } else {
            throw new JsonParseException("Expected '" + key + "' to be a list of items, but it was not an array");
        }
    }

    default List<Item> getItems(JsonObject jsonObject, String key) {
        return getItems(jsonObject, key, new ArrayList<>());
    }

    //endregion

    //region Item Tags

    default List<TagKey<Item>> getItemTags(JsonObject jsonObject, String key, @Nullable List<TagKey<Item>> defaultItemTags) {
        if (!jsonObject.has(key) || jsonObject.get(key).isJsonNull()) {
            if (defaultItemTags != null) {
                return defaultItemTags;
            }
            throw new JsonParseException("Expected '" + key + "' to be a list of item tags");
        }

        JsonElement element = jsonObject.get(key);

        if (element.isJsonArray()) {
            JsonArray jsonArray = element.getAsJsonArray();
            List<TagKey<Item>> itemTags = new ArrayList<>();
            for (JsonElement itemTagElement : jsonArray) {
                if (itemTagElement.isJsonPrimitive()) {
                    String itemTagName = itemTagElement.getAsString();
                    if (itemTagName != null && itemTagName.startsWith("#")) {
                        itemTagName = itemTagName.substring(1);
                        String finalItemTagName = itemTagName;
                        ResourceLocation resourceLocation = ResourceLocation.CODEC.decode(JsonOps.INSTANCE, new JsonPrimitive(finalItemTagName)).result()
                                .orElseThrow(() -> new JsonParseException("Expected '" + key + "' to be a list of item tags, but one of the item tags was invalid: " + finalItemTagName))
                                .getFirst();
                        TagKey<Item> itemTag = TagKey.create(BuiltInRegistries.ITEM.key(), resourceLocation);
                        itemTags.add(itemTag);
                    }
                }
            }
            return itemTags;
        } else {
            throw new JsonParseException("Expected '" + key + "' to be a list of item tags, but it was not an array");
        }
    }

    default List<TagKey<Item>> getItemTags(JsonObject jsonObject, String key) {
        return getItemTags(jsonObject, key, new ArrayList<>());
    }

    //endregion

    //region ItemStack

    default ItemStack getItemStack(JsonObject jsonObject, String key, @Nullable ItemStack defaultItemStack) {
        if (!jsonObject.has(key) || jsonObject.get(key).isJsonNull()) {
            if (defaultItemStack != null) {
                return defaultItemStack;
            }
            throw new JsonParseException("Expected '" + key + "' to be an item stack");
        }

        JsonElement element = jsonObject.get(key);

        if (element.isJsonPrimitive()) {
            Item item = getItem(jsonObject, key);
            return new ItemStack(item);
        }

        return ItemStack.CODEC.decode(JsonOps.INSTANCE, element).result()
                .orElseGet(() -> {
                    if (defaultItemStack != null) {
                        return new Pair<>(defaultItemStack, null);
                    }
                    throw new JsonParseException("Expected '" + jsonObject.get(key) + "' to be an item stack, but it was invalid");
                }).getFirst();
    }

    default ItemStack getItemStack(JsonObject jsonObject, String key) {
        return getItemStack(jsonObject, key, null);
    }

    default @Nullable ItemStack getOptionalItemStack(JsonObject jsonObject, String key) {
        if (!jsonObject.has(key) || jsonObject.get(key).isJsonNull()) {
            return null;
        }
        return getItemStack(jsonObject, key, null);
    }

    //endregion

    //region ItemStacks

    default List<ItemStack> getItemStacks(JsonObject jsonObject, String key, List<ItemStack> defaultItemStacks) {
        if (!jsonObject.has(key) || jsonObject.get(key).isJsonNull()) {
            if (defaultItemStacks != null) {
                return defaultItemStacks;
            }
            throw new JsonParseException("Expected '" + key + "' to be a list of item stacks");
        }

        JsonElement element = jsonObject.get(key);

        if (element.isJsonArray()) {
            JsonArray jsonArray = element.getAsJsonArray();
            List<ItemStack> itemStacks = new ArrayList<>();
            for (JsonElement itemElement : jsonArray) {
                String itemName = itemElement.getAsString();
                if (itemName != null && !itemName.startsWith("#")) {
                    if (itemElement.isJsonPrimitive()) {
                        Item item = BuiltInRegistries.ITEM.byNameCodec().decode(JsonOps.INSTANCE, itemElement).result()
                                .orElseThrow(() -> new JsonParseException("Expected '" + key + "' to be a list of item stacks, but one of the items was invalid: " + itemElement.getAsString()))
                                .getFirst();
                        itemStacks.add(new ItemStack(item));
                    } else {
                        ItemStack itemStack = ItemStack.CODEC.decode(JsonOps.INSTANCE, itemElement).result()
                                .orElseThrow(() -> new JsonParseException("Expected '" + key + "' to be a list of item stacks, but one of the item stacks was invalid"))
                                .getFirst();
                        itemStacks.add(itemStack);
                    }
                }
            }
            return itemStacks;
        } else {
            throw new JsonParseException("Expected '" + key + "' to be a list of item stacks, but it was not an array");
        }
    }

    //endregion

    //region Mob Effect

    default MobEffect getMobEffect(JsonObject jsonObject, String key, @Nullable MobEffect defaultEffect) {
        if (!jsonObject.has(key) || jsonObject.get(key).isJsonNull()) {
            if (defaultEffect != null) {
                return defaultEffect;
            }
            throw new JsonParseException("Expected '" + key + "' to be a mob effect");
        }

        return BuiltInRegistries.MOB_EFFECT.byNameCodec().decode(JsonOps.INSTANCE, jsonObject.get(key)).result()
                .orElseGet(() -> {
                    if (defaultEffect != null) {
                        return new Pair<>(defaultEffect, null);
                    }
                    throw new JsonParseException("Expected '" + jsonObject.get(key) + "' to be a mob effect, but it was invalid");
                }).getFirst();
    }

    default MobEffect getMobEffect(JsonObject jsonObject, String key) {
        return getMobEffect(jsonObject, key, null);
    }

    default @Nullable MobEffect getOptionalMobEffect(JsonObject jsonObject, String key) {
        if (!jsonObject.has(key) || jsonObject.get(key).isJsonNull()) {
            return null;
        }
        return getMobEffect(jsonObject, key, null);
    }

    //endregion

    //region Mob Effect Instance

    default MobEffectInstance getMobEffectInstance(JsonObject jsonObject, String key, @Nullable MobEffectInstance defaultEffect) {
        if (!jsonObject.has(key) || jsonObject.get(key).isJsonNull()) {
            if (defaultEffect != null) {
                return defaultEffect;
            }
            throw new JsonParseException("Expected '" + key + "' to be a mob effect instance");
        }

        JsonElement element = jsonObject.get(key);

        if (element.isJsonPrimitive()) {
            MobEffect mobEffect = getMobEffect(jsonObject, key);
            return new MobEffectInstance(Holder.direct(mobEffect), 200, 0, false, true);
        }

        return MobEffectInstance.CODEC.decode(JsonOps.INSTANCE, element).result()
                .orElseGet(() -> {
                    if (defaultEffect != null) {
                        return new Pair<>(defaultEffect, null);
                    }
                    throw new JsonParseException("Expected '" + jsonObject.get(key) + "' to be a mob effect instance, but it was invalid");
                }).getFirst();
    }

    default MobEffectInstance getMobEffectInstance(JsonObject jsonObject, String key) {
        return getMobEffectInstance(jsonObject, key, null);
    }

    default @Nullable MobEffectInstance getOptionalMobEffectInstance(JsonObject jsonObject, String key) {
        if (!jsonObject.has(key) || jsonObject.get(key).isJsonNull()) {
            return null;
        }
        return getMobEffectInstance(jsonObject, key, null);
    }

    //endregion

    //region Mob Effect Category

    default MobEffectCategory getMobEffectCategory(JsonObject jsonObject, String key, @Nullable MobEffectCategory defaultCategory) {
        if (!jsonObject.has(key) || jsonObject.get(key).isJsonNull()) {
            if (defaultCategory != null) {
                return defaultCategory;
            }
            throw new JsonParseException("Expected '" + key + "' to be a mob effect category");
        }

        String categoryName = GsonHelper.getAsString(jsonObject, key).toUpperCase();
        MobEffectCategory category;
        try {
            category = MobEffectCategory.valueOf(categoryName);
        } catch (IllegalArgumentException e) {
            if (defaultCategory != null) {
                return defaultCategory;
            }
            throw new JsonParseException("Expected '" + categoryName + "' to be a mob effect category, but it was invalid. Options are:" + Arrays.stream(MobEffectCategory.values()).map(Enum::name).collect(Collectors.joining(", ")) + ".");
        }
        return category;
    }

    default MobEffectCategory getMobEffectCategory(JsonObject jsonObject, String key) {
        return getMobEffectCategory(jsonObject, key, null);
    }

    default @Nullable MobEffectCategory getOptionalMobEffectCategory(JsonObject jsonObject, String key) {
        if (!jsonObject.has(key) || jsonObject.get(key).isJsonNull()) {
            return null;
        }
        return getMobEffectCategory(jsonObject, key, null);
    }

    //endregion

    //region Comparison Type

    default ComparisonType getComparisonType(JsonObject jsonObject, String key, @Nullable ComparisonType defaultType) {
        if (!jsonObject.has(key) || jsonObject.get(key).isJsonNull()) {
            if (defaultType != null) {
                return defaultType;
            }
            throw new JsonParseException("Expected '" + key + "' to be a comparison type");
        }

        return ComparisonType.CODEC.decode(JsonOps.INSTANCE, jsonObject.get(key)).result()
                .orElseGet(() -> {
                    if (defaultType != null) {
                        return new Pair<>(defaultType, null);
                    }
                    throw new JsonParseException("Expected '" + jsonObject.get(key) + "' to be a comparison type, but it was invalid");
                }).getFirst();
    }

    default ComparisonType getComparisonType(JsonObject jsonObject, String key) {
        return getComparisonType(jsonObject, key, null);
    }

    default @Nullable ComparisonType getOptionalComparisonType(JsonObject jsonObject, String key) {
        if (!jsonObject.has(key) || jsonObject.get(key).isJsonNull()) {
            return null;
        }
        return getComparisonType(jsonObject, key, null);
    }

    //endregion

    //region Block

    default Block getBlock(JsonObject jsonObject, String key, @Nullable Block defaultBlock) {
        if (!jsonObject.has(key) || jsonObject.get(key).isJsonNull()) {
            if (defaultBlock != null) {
                return defaultBlock;
            }
            throw new JsonParseException("Expected '" + key + "' to be a block");
        }

        return BuiltInRegistries.BLOCK.byNameCodec().decode(JsonOps.INSTANCE, jsonObject.get(key)).result()
                .orElseGet(() -> {
                    if (defaultBlock != null) {
                        return new Pair<>(defaultBlock, null);
                    }
                    throw new JsonParseException("Expected '" + jsonObject.get(key) + "' to be a block, but it was invalid");
                }).getFirst();
    }

    default Block getBlock(JsonObject jsonObject, String key) {
        return getBlock(jsonObject, key, null);
    }

    default @Nullable Block getOptionalBlock(JsonObject jsonObject, String key) {
        if (!jsonObject.has(key) || jsonObject.get(key).isJsonNull()) {
            return null;
        }
        return getBlock(jsonObject, key, null);
    }

    //endregion

    //region Blocks

    default List<Block> getBlocks(JsonObject jsonObject, String key, @Nullable List<Block> defaultBlocks) {
        if (!jsonObject.has(key) || jsonObject.get(key).isJsonNull()) {
            if (defaultBlocks != null) {
                return defaultBlocks;
            }
            throw new JsonParseException("Expected '" + key + "' to be a list of blocks");
        }

        JsonElement element = jsonObject.get(key);

        if (element.isJsonArray()) {
            JsonArray jsonArray = element.getAsJsonArray();
            List<Block> blocks = new ArrayList<>();
            for (JsonElement blockElement : jsonArray) {
                String blockName = blockElement.getAsString();
                if (blockName != null && !blockName.startsWith("#")) {
                    Block block = BuiltInRegistries.BLOCK.byNameCodec().decode(JsonOps.INSTANCE, blockElement).result()
                            .orElseThrow(() -> new JsonParseException("Expected '" + key + "' to be a list of blocks, but one of the blocks was invalid: " + blockName))
                            .getFirst();
                    blocks.add(block);
                }
            }
            return blocks;
        } else {
            throw new JsonParseException("Expected '" + key + "' to be a list of blocks, but it was not an array");
        }
    }

    default List<Block> getBlocks(JsonObject jsonObject, String key) {
        return getBlocks(jsonObject, key, new ArrayList<>());
    }

    //endregion

    //region Block State

    default ArcBlockState getBlockState(JsonObject jsonObject, String key, @Nullable ArcBlockState defaultBlock) {
        if (!jsonObject.has(key) || jsonObject.get(key).isJsonNull()) {
            if (defaultBlock != null) {
                return defaultBlock;
            }
            throw new JsonParseException("Expected '" + key + "' to be a block");
        }

        return ArcBlockState.CODEC.decode(JsonOps.INSTANCE, jsonObject.get(key)).result()
                .orElseGet(() -> {
                    if (defaultBlock != null) {
                        return new Pair<>(defaultBlock, null);
                    }
                    throw new JsonParseException("Expected '" + jsonObject.get(key) + "' to be a block, but it was invalid");
                }).getFirst();
    }

    default ArcBlockState getBlockState(JsonObject jsonObject, String key) {
        return getBlockState(jsonObject, key, null);
    }

    default @Nullable ArcBlockState getOptionalBlockState(JsonObject jsonObject, String key) {
        if (!jsonObject.has(key) || jsonObject.get(key).isJsonNull()) {
            return null;
        }
        return getBlockState(jsonObject, key, null);
    }

    //endregion

    //region Block States

    default List<ArcBlockState> getBlockStates(JsonObject jsonObject, String key, @Nullable List<ArcBlockState> defaultBlocks) {
        if (!jsonObject.has(key) || jsonObject.get(key).isJsonNull()) {
            if (defaultBlocks != null) {
                return defaultBlocks;
            }
            throw new JsonParseException("Expected '" + key + "' to be a block");
        }

        return ArcBlockState.CODEC.listOf().decode(JsonOps.INSTANCE, jsonObject.get(key)).result()
                .orElseGet(() -> {
                    if (defaultBlocks != null) {
                        return new Pair<>(defaultBlocks, null);
                    }
                    throw new JsonParseException("Expected '" + jsonObject.get(key) + "' to be a block, but it was invalid");
                }).getFirst();
    }

    default List<ArcBlockState> getBlockStates(JsonObject jsonObject, String key) {
        return getBlockStates(jsonObject, key, null);
    }

    default List<ArcBlockState> getOptionalBlockStates(JsonObject jsonObject, String key) {
        if (!jsonObject.has(key) || jsonObject.get(key).isJsonNull()) {
            return new ArrayList<>();
        }
        return getBlockStates(jsonObject, key, new ArrayList<>());
    }

    //endregion

    //region Block Tags

    default List<TagKey<Block>> getBlockTags(JsonObject jsonObject, String key, @Nullable List<TagKey<Block>> defaultBlockTags) {
        if (!jsonObject.has(key) || jsonObject.get(key).isJsonNull()) {
            if (defaultBlockTags != null) {
                return defaultBlockTags;
            }
            throw new JsonParseException("Expected '" + key + "' to be a list of block tags");
        }

        JsonElement element = jsonObject.get(key);

        if (element.isJsonArray()) {
            JsonArray jsonArray = element.getAsJsonArray();
            List<TagKey<Block>> blockTags = new ArrayList<>();
            for (JsonElement blockTagElement : jsonArray) {
                if (blockTagElement.isJsonPrimitive()) {
                    String blockTagName = blockTagElement.getAsString();
                    if (blockTagName != null && blockTagName.startsWith("#")) {
                        ResourceLocation resourceLocation = ResourceLocation.CODEC.decode(JsonOps.INSTANCE, blockTagElement).result()
                                .orElseThrow(() -> new JsonParseException("Expected '" + key + "' to be a list of block tags, but one of the block tags was invalid: " + blockTagName))
                                .getFirst();
                        TagKey<Block> blockTag = TagKey.create(BuiltInRegistries.BLOCK.key(), resourceLocation);
                        blockTags.add(blockTag);
                    }
                }
            }
            return blockTags;
        } else {
            throw new JsonParseException("Expected '" + key + "' to be a list of block tags, but it was not an array");
        }
    }

    default List<TagKey<Block>> getBlockTags(JsonObject jsonObject, String key) {
        return getBlockTags(jsonObject, key, new ArrayList<>());
    }

    //endregion

    //region Entity Type

    default EntityType<?> getEntityType(JsonObject jsonObject, String key, @Nullable EntityType<?> defaultEntityType) {
        if (!jsonObject.has(key) || jsonObject.get(key).isJsonNull()) {
            if (defaultEntityType != null) {
                return defaultEntityType;
            }
            throw new JsonParseException("Expected '" + key + "' to be an entity type");
        }

        return BuiltInRegistries.ENTITY_TYPE.byNameCodec().decode(JsonOps.INSTANCE, jsonObject.get(key)).result()
                .orElseGet(() -> {
                    if (defaultEntityType != null) {
                        return new Pair<>(defaultEntityType, null);
                    }
                    throw new JsonParseException("Invalid entity type");
                }).getFirst();
    }

    default EntityType<?> getEntityType(JsonObject jsonObject, String key) {
        return getEntityType(jsonObject, key, null);
    }

    default @Nullable EntityType<?> getOptionalEntityType(JsonObject jsonObject, String key) {
        if (!jsonObject.has(key) || jsonObject.get(key).isJsonNull()) {
            return null;
        }
        return getEntityType(jsonObject, key, null);
    }

    //endregion

    //region Entity Types

    default List<EntityType<?>> getEntityTypes(JsonObject jsonObject, String key, @Nullable List<EntityType<?>> defaultEntityTypes) {
        if (!jsonObject.has(key) || jsonObject.get(key).isJsonNull()) {
            if (defaultEntityTypes != null) {
                return defaultEntityTypes;
            }
            throw new JsonParseException("Expected '" + key + "' to be a list of entity types");
        }

        JsonElement element = jsonObject.get(key);

        if (element.isJsonArray()) {
            JsonArray jsonArray = element.getAsJsonArray();
            List<EntityType<?>> entityTypes = new ArrayList<>();
            for (JsonElement entityTypeElement : jsonArray) {
                String entityTypeName = entityTypeElement.getAsString();
                if (entityTypeName != null && !entityTypeName.startsWith("#")) {
                    EntityType<?> entityType = BuiltInRegistries.ENTITY_TYPE.byNameCodec().decode(JsonOps.INSTANCE, entityTypeElement).result()
                            .orElseThrow(() -> new JsonParseException("Expected '" + key + "' to be a list of entity types, but one of the entity types was invalid: " + entityTypeName))
                            .getFirst();
                    entityTypes.add(entityType);
                }
            }
            return entityTypes;
        } else {
            throw new JsonParseException("Expected '" + key + "' to be a list of entity types, but it was not an array");
        }
    }

    default List<EntityType<?>> getEntityTypes(JsonObject jsonObject, String key) {
        return getEntityTypes(jsonObject, key, new ArrayList<>());
    }

    //endregion

    //region Entity Data Property

    default EntityDataProperty getEntityDataProperty(JsonObject jsonObject, String key, @Nullable EntityDataProperty defaultEntityDataProperty) {
        if (!jsonObject.has(key) || jsonObject.get(key).isJsonNull()) {
            if (defaultEntityDataProperty != null) {
                return defaultEntityDataProperty;
            }
            throw new JsonParseException("Expected '" + key + "' to be a entity data property");
        }

        return EntityDataProperty.CODEC.decode(JsonOps.INSTANCE, jsonObject.get(key)).result()
                .orElseGet(() -> {
                    if (defaultEntityDataProperty != null) {
                        return new Pair<>(defaultEntityDataProperty, null);
                    }
                    throw new JsonParseException("Expected '" + jsonObject.get(key) + "' to be a entity data property, but it was invalid");
                }).getFirst();
    }

    default EntityDataProperty getEntityDataProperty(JsonObject jsonObject, String key) {
        return getEntityDataProperty(jsonObject, key, null);
    }

    default @Nullable EntityDataProperty getOptionalEntityDataProperty(JsonObject jsonObject, String key) {
        if (!jsonObject.has(key) || jsonObject.get(key).isJsonNull()) {
            return null;
        }
        return getEntityDataProperty(jsonObject, key, null);
    }

    //endregion

    //region Entity Data Property

    default List<EntityDataProperty> getEntityDataProperties(JsonObject jsonObject, String key, @Nullable List<EntityDataProperty> defaultEntityDataProperties) {
        if (!jsonObject.has(key) || jsonObject.get(key).isJsonNull()) {
            if (defaultEntityDataProperties != null) {
                return defaultEntityDataProperties;
            }
            throw new JsonParseException("Expected '" + key + "' to be a list of entity data properties");
        }

        return EntityDataProperty.CODEC.listOf().decode(JsonOps.INSTANCE, jsonObject.get(key)).result()
                .orElseGet(() -> {
                    if (defaultEntityDataProperties != null) {
                        return new Pair<>(defaultEntityDataProperties, null);
                    }
                    throw new JsonParseException("Expected '" + jsonObject.get(key) + "' to be a entity data property, but it was invalid");
                }).getFirst();
    }

    default List<EntityDataProperty> getEntityDataProperties(JsonObject jsonObject, String key) {
        return getEntityDataProperties(jsonObject, key, null);
    }

    default List<EntityDataProperty> getOptionalEntityDataProperties(JsonObject jsonObject, String key) {
        if (!jsonObject.has(key) || jsonObject.get(key).isJsonNull()) {
            return new ArrayList<>();
        }
        return getEntityDataProperties(jsonObject, key, new ArrayList<>());
    }

    //endregion

    //region String

    default String getString(JsonObject jsonObject, String elementName, @Nullable String defaultString) {
        if (!jsonObject.has(elementName) || jsonObject.get(elementName).isJsonNull()) {
            if (defaultString != null) {
                return defaultString;
            }
            throw new JsonParseException("Expected '" + elementName + "' to be a string");
        }
        return GsonHelper.getAsString(jsonObject, elementName);
    }

    default String getString(JsonObject jsonObject, String elementName) {
        return getString(jsonObject, elementName, null);
    }

    default @Nullable String getOptionalString(JsonObject jsonObject, String elementName) {
        if (!jsonObject.has(elementName) || jsonObject.get(elementName).isJsonNull()) {
            return null;
        }
        return getString(jsonObject, elementName, null);
    }

    //endregion

    //region Hand

    default InteractionHand getHand(JsonObject jsonObject, String elementName, @Nullable InteractionHand defaultHand) {
        if (!jsonObject.has(elementName) || jsonObject.get(elementName).isJsonNull()) {
            if (defaultHand != null) {
                return defaultHand;
            }
            throw new JsonParseException("Expected '" + elementName + "' to be a hand");
        }
        String handName = GsonHelper.getAsString(jsonObject, elementName).toUpperCase();
        InteractionHand hand = handName.equals("MAIN") ? InteractionHand.MAIN_HAND : handName.equals("OFF") ? InteractionHand.OFF_HAND : null;
        if (hand == null) {
            try {
                hand = InteractionHand.valueOf(handName);
            } catch (IllegalArgumentException e) {
                throw new JsonParseException("Expected '" + handName + "' to be a hand, but it was invalid. Options are:" + Arrays.stream(InteractionHand.values()).map(Enum::name).collect(Collectors.joining(", ")) + ".");
            }
        }
        return hand;
    }

    default InteractionHand getHand(JsonObject jsonObject, String elementName) {
        return getHand(jsonObject, elementName, null);
    }

    default @Nullable InteractionHand getOptionalHand(JsonObject jsonObject, String elementName) {
        if (!jsonObject.has(elementName) || jsonObject.get(elementName).isJsonNull()) {
            return null;
        }
        return getHand(jsonObject, elementName, null);
    }

    //endregion

    //region Dimension

    default ResourceKey<Level> getDimension(JsonObject jsonObject, String elementName, @Nullable ResourceKey<Level> defaultDimension) {
        if (!jsonObject.has(elementName) || jsonObject.get(elementName).isJsonNull()) {
            if (defaultDimension != null) {
                return defaultDimension;
            }
            throw new JsonParseException("Expected '" + elementName + "' to be a dimension");
        }

        return Level.RESOURCE_KEY_CODEC.decode(JsonOps.INSTANCE, jsonObject.get(elementName)).result()
                .orElseGet(() -> {
                    if (defaultDimension != null) {
                        return new Pair<>(defaultDimension, null);
                    }
                    throw new JsonParseException("Expected '" + jsonObject.get(elementName) + "' to be a dimension, but it was invalid");
                }).getFirst();
    }

    default ResourceKey<Level> getDimension(JsonObject jsonObject, String elementName) {
        return getDimension(jsonObject, elementName, null);
    }

    default @Nullable ResourceKey<Level> getOptionalDimension(JsonObject jsonObject, String elementName) {
        if (!jsonObject.has(elementName) || jsonObject.get(elementName).isJsonNull()) {
            return null;
        }
        return getDimension(jsonObject, elementName, null);
    }

    //endregion
}