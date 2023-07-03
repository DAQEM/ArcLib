package com.daqem.arc.api.action.serializer;

import com.daqem.arc.api.action.IAction;
import com.daqem.arc.api.action.holder.type.IActionHolderType;
import com.daqem.arc.api.condition.ICondition;
import com.daqem.arc.api.reward.IReward;
import com.daqem.arc.data.serializer.ArcSerializer;
import com.daqem.arc.registry.ArcRegistry;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public interface IActionSerializer<T extends IAction> extends ArcSerializer {

    T fromJson(ResourceLocation location, JsonObject jsonObject);

    T fromNetwork(ResourceLocation location, FriendlyByteBuf friendlyByteBuf);

    void toNetwork(FriendlyByteBuf friendlyByteBuf, T type);

    T fromJson(ResourceLocation location, JsonObject jsonObject, ResourceLocation actionHolderLocation, IActionHolderType<?> actionHolderType, boolean performOnClient, List<IReward> rewards, List<ICondition> conditions);

    T fromNetwork(ResourceLocation location, FriendlyByteBuf friendlyByteBuf, ResourceLocation actionHolderLocation, IActionHolderType<?> actionHolderType, boolean performOnClient, List<IReward> rewards, List<ICondition> conditions);

    static IAction fromNetwork(FriendlyByteBuf friendlyByteBuf) {
        ResourceLocation resourceLocation = friendlyByteBuf.readResourceLocation();
        ResourceLocation resourceLocation2 = friendlyByteBuf.readResourceLocation();
        return ArcRegistry.ACTION_SERIALIZER.getOptional(resourceLocation).orElseThrow(
                () -> new IllegalArgumentException("Unknown action serializer " + resourceLocation)
        ).fromNetwork(resourceLocation2, friendlyByteBuf);
    }

    static <T extends IAction> void toNetwork(T action, FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeResourceLocation(ArcRegistry.ACTION_SERIALIZER.getKey(action.getSerializer()));
        friendlyByteBuf.writeResourceLocation(action.getLocation());
        ((IActionSerializer<T>)action.getSerializer()).toNetwork(friendlyByteBuf, action);

    }
}
