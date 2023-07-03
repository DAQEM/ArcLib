package com.daqem.arc.api.condition.serializer;

import com.daqem.arc.api.condition.ICondition;
import com.daqem.arc.data.serializer.ArcSerializer;
import com.daqem.arc.registry.ArcRegistry;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public interface IConditionSerializer<T extends ICondition> extends ArcSerializer {

    T fromJson(ResourceLocation location, JsonObject jsonObject);

    T fromNetwork(ResourceLocation location, FriendlyByteBuf friendlyByteBuf);

    void toNetwork(FriendlyByteBuf friendlyByteBuf, T type);

    T fromJson(ResourceLocation location, JsonObject jsonObject, boolean inverted);

    T fromNetwork(ResourceLocation location, FriendlyByteBuf friendlyByteBuf, boolean inverted);

    static ICondition fromNetwork(FriendlyByteBuf friendlyByteBuf) {
        ResourceLocation resourceLocation = friendlyByteBuf.readResourceLocation();
        ResourceLocation resourceLocation2 = friendlyByteBuf.readResourceLocation();
        return ArcRegistry.CONDITION_SERIALIZER.getOptional(resourceLocation).orElseThrow(
                () -> new IllegalArgumentException("Unknown condition serializer " + resourceLocation)
        ).fromNetwork(resourceLocation2, friendlyByteBuf);
    }

    static <T extends ICondition> void toNetwork(T condition, FriendlyByteBuf friendlyByteBuf, ResourceLocation location) {
        friendlyByteBuf.writeResourceLocation(ArcRegistry.CONDITION_SERIALIZER.getKey(condition.getSerializer()));
        friendlyByteBuf.writeResourceLocation(location);
        ((IConditionSerializer<T>)condition.getSerializer()).toNetwork(friendlyByteBuf, condition);

    }
}
