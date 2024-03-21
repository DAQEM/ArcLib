package com.daqem.arc.api.action.holder.serializer;

import com.daqem.arc.api.action.holder.IActionHolder;
import com.daqem.arc.data.serializer.ArcSerializer;
import com.daqem.arc.registry.ArcRegistry;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public interface IActionHolderSerializer<T extends IActionHolder> extends ArcSerializer {

    T fromJson(ResourceLocation location, JsonObject jsonObject, int i);

    T fromNetwork(ResourceLocation location, FriendlyByteBuf friendlyByteBuf, int i);

    static IActionHolder fromNetwork(FriendlyByteBuf friendlyByteBuf) {
        ResourceLocation resourceLocation = friendlyByteBuf.readResourceLocation();
        ResourceLocation resourceLocation2 = friendlyByteBuf.readResourceLocation();
        return ArcRegistry.ACTION_HOLDER.getOptional(resourceLocation).orElseThrow(
                () -> new IllegalArgumentException("Unknown action holder serializer " + resourceLocation)
        ).getSerializer().fromNetwork(resourceLocation2, friendlyByteBuf);
    }

    static <T extends IActionHolder> void toNetwork(T actionHolder, FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeResourceLocation(ArcRegistry.ACTION_HOLDER.getKey(actionHolder.getType()));
        friendlyByteBuf.writeResourceLocation(actionHolder.getLocation());
        ((IActionHolderSerializer<T>)actionHolder.getSerializer()).toNetwork(friendlyByteBuf, actionHolder);

    }

    default T fromJson(ResourceLocation location, JsonObject jsonObject) {
        return fromJson(location, jsonObject, 0);
    }

    default T fromNetwork(ResourceLocation location, FriendlyByteBuf friendlyByteBuf) {
        return fromNetwork(location, friendlyByteBuf, 0);
    }

    default void toNetwork(FriendlyByteBuf friendlyByteBuf, T type) {
    }
}
