package com.daqem.arc.api.reward.serializer;

import com.daqem.arc.api.reward.IReward;
import com.daqem.arc.data.serializer.ArcSerializer;
import com.daqem.arc.registry.ArcRegistry;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

public interface IRewardSerializer<T extends IReward> extends ArcSerializer {

    T fromJson(JsonObject jsonObject, double chance, int priority);

    T fromNetwork(FriendlyByteBuf friendlyByteBuf, double chance, int priority);

    static IReward fromNetwork(FriendlyByteBuf friendlyByteBuf) {
        ResourceLocation resourceLocation = friendlyByteBuf.readResourceLocation();
        ResourceLocation resourceLocation2 = friendlyByteBuf.readResourceLocation();
        return ArcRegistry.REWARD.getOptional(resourceLocation).orElseThrow(
                () -> new IllegalArgumentException("Unknown reward serializer " + resourceLocation)
        ).getSerializer().fromNetwork(resourceLocation2, friendlyByteBuf);
    }

    static <T extends IReward> void toNetwork(T reward, FriendlyByteBuf friendlyByteBuf, ResourceLocation location) {
        friendlyByteBuf.writeResourceLocation(ArcRegistry.REWARD.getKey(reward.getType()));
        friendlyByteBuf.writeResourceLocation(location);
        ((IRewardSerializer<T>)reward.getSerializer()).toNetwork(friendlyByteBuf, reward);

    }

    default T fromJson(ResourceLocation location, JsonObject jsonObject) {
        return fromJson(jsonObject, GsonHelper.getAsDouble(jsonObject, "chance", 100D), GsonHelper.getAsInt(jsonObject, "priority", 1));
    }

    default T fromNetwork(ResourceLocation location, FriendlyByteBuf friendlyByteBuf) {
        return fromNetwork(friendlyByteBuf, friendlyByteBuf.readDouble(), friendlyByteBuf.readInt());
    }

    default void toNetwork(FriendlyByteBuf friendlyByteBuf, T type) {
        friendlyByteBuf.writeDouble(type.getChance());
        friendlyByteBuf.writeInt(type.getPriority());
    }
}
