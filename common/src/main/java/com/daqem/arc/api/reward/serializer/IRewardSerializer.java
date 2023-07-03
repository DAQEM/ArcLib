package com.daqem.arc.api.reward.serializer;

import com.daqem.arc.api.reward.IReward;
import com.daqem.arc.data.serializer.ArcSerializer;
import com.daqem.arc.registry.ArcRegistry;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public interface IRewardSerializer<T extends IReward> extends ArcSerializer {

    T fromJson(ResourceLocation location, JsonObject jsonObject);

    T fromNetwork(ResourceLocation location, FriendlyByteBuf friendlyByteBuf);

    void toNetwork(FriendlyByteBuf friendlyByteBuf, T type);

    T fromJson(JsonObject jsonObject, double chance);

    T fromNetwork(FriendlyByteBuf friendlyByteBuf, double chance);

    static IReward fromNetwork(FriendlyByteBuf friendlyByteBuf) {
        ResourceLocation resourceLocation = friendlyByteBuf.readResourceLocation();
        ResourceLocation resourceLocation2 = friendlyByteBuf.readResourceLocation();
        return ArcRegistry.REWARD_SERIALIZER.getOptional(resourceLocation).orElseThrow(
                () -> new IllegalArgumentException("Unknown reward serializer " + resourceLocation)
        ).fromNetwork(resourceLocation2, friendlyByteBuf);
    }

    static <T extends IReward> void toNetwork(T reward, FriendlyByteBuf friendlyByteBuf, ResourceLocation location) {
        friendlyByteBuf.writeResourceLocation(ArcRegistry.REWARD_SERIALIZER.getKey(reward.getSerializer()));
        friendlyByteBuf.writeResourceLocation(location);
        ((IRewardSerializer<T>)reward.getSerializer()).toNetwork(friendlyByteBuf, reward);

    }
}
