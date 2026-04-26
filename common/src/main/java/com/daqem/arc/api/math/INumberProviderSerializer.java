package com.daqem.arc.api.math;

import com.daqem.arc.data.serializer.ArcSerializer;
import com.daqem.arc.registry.ArcRegistry;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.Identifier;

import java.util.Objects;

public interface INumberProviderSerializer<T extends INumberProvider> extends ArcSerializer {

    T fromJson(JsonObject jsonObject);

    T fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf);

    static INumberProvider fromNetworkStatic(RegistryFriendlyByteBuf friendlyByteBuf) {
        Identifier resourceLocation = friendlyByteBuf.readIdentifier();
        return ArcRegistry.NUMBER_PROVIDER.getOptional(resourceLocation).orElseThrow(
                () -> new IllegalArgumentException("Unknown number provider serializer " + resourceLocation)
        ).getSerializer().fromNetwork(friendlyByteBuf);
    }

    @SuppressWarnings("unchecked")
    static <T extends INumberProvider> void toNetwork(T provider, RegistryFriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeIdentifier(Objects.requireNonNull(ArcRegistry.NUMBER_PROVIDER.getKey(provider.getType())));
        ((INumberProviderSerializer<T>) provider.getSerializer()).toNetwork(friendlyByteBuf, provider);
    }

    default void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, T type) {
    }
}