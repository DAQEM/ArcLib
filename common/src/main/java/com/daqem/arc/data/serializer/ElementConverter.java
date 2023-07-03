package com.daqem.arc.data.serializer;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ElementConverter<T> {

    private final Registry<T> registry;

    public ElementConverter(Registry<T> registry) {
        this.registry = registry;
    }

    public List<T> convertToElements(List<String> elements) {
        return elements.stream()
                .filter(elementLoc -> !elementLoc.startsWith("#"))
                .filter(elementLoc -> elementLoc.contains(":"))
                .map(convertToElement())
                .collect(Collectors.toList());
    }

    public List<TagKey<T>> convertToElementTags(List<String> elementTags) {
        return elementTags.stream()
                .filter(elementLoc -> elementLoc.startsWith("#"))
                .filter(elementLoc -> elementLoc.contains(":"))
                .map(replaceHashAndConvertToTag())
                .collect(Collectors.toList());
    }

    public T convertToElement(String element) {
        T type = registry.get(new ResourceLocation(element));

        // If not found in the registry, it will return the default value for the type.
        // This checks if the element is actually in the registry.
        if (type == registry.get(new ResourceLocation("x"))) {
            throw new IllegalArgumentException(element + " could not be found in registry " + registry.key().location());
        }

        return type;
    }

    private Function<String, T> convertToElement() {
        return elementLoc -> registry.get(
                new ResourceLocation(elementLoc));
    }

    private Function<String, TagKey<T>> replaceHashAndConvertToTag() {
        return elementLoc -> TagKey.create(
                registry.key(),
                new ResourceLocation(
                        elementLoc.replace("#", "")));
    }
}
