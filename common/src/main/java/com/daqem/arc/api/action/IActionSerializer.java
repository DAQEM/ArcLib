package com.daqem.arc.api.action;

import com.daqem.arc.api.action.holder.IActionHolderType;
import com.daqem.arc.api.condition.ICondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.reward.IReward;
import com.daqem.arc.api.reward.IRewardSerializer;
import com.daqem.arc.data.serializer.ArcSerializer;
import com.daqem.arc.registry.ArcRegistry;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.JsonOps;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public interface IActionSerializer<T extends IAction> extends ArcSerializer {

    T fromJson(ResourceLocation location, JsonObject jsonObject, ResourceLocation actionHolderLocation, IActionHolderType<?> actionHolderType, List<IReward> rewards, List<ICondition> conditions);

    T fromNetwork(ResourceLocation location, RegistryFriendlyByteBuf friendlyByteBuf, ResourceLocation actionHolderLocation, IActionHolderType<?> actionHolderType, List<IReward> rewards, List<ICondition> conditions);

    static IAction fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf) {
        ResourceLocation resourceLocation = friendlyByteBuf.readResourceLocation();
        ResourceLocation resourceLocation2 = friendlyByteBuf.readResourceLocation();
        return ArcRegistry.ACTION.getOptional(resourceLocation).orElseThrow(
                () -> new IllegalArgumentException("Unknown action serializer " + resourceLocation)
        ).getSerializer().fromNetwork(resourceLocation2, friendlyByteBuf);
    }

    @SuppressWarnings("unchecked")
    static <T extends IAction> void toNetwork(T action, RegistryFriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeResourceLocation(Objects.requireNonNull(ArcRegistry.ACTION.getKey(action.getType())));
        friendlyByteBuf.writeResourceLocation(action.getLocation());
        ((IActionSerializer<T>) action.getSerializer()).toNetwork(friendlyByteBuf, action);

    }

    default T fromJson(ResourceLocation location, JsonObject jsonObject) {
        List<IReward> rewards = new ArrayList<>();
        if (jsonObject.has("rewards")) {
            jsonObject.getAsJsonArray("rewards").forEach(jsonElement -> {
                ResourceLocation rewardTypeLocation = getResourceLocation(jsonElement.getAsJsonObject(), "type");
                rewards.add(ArcRegistry.REWARD.getOptional(rewardTypeLocation)
                        .orElseThrow(() -> new JsonParseException("Unknown reward type: " + rewardTypeLocation))
                        .getSerializer().fromJson(location, jsonElement.getAsJsonObject()));
            });
        }

        List<ICondition> conditions = new ArrayList<>();
        if (jsonObject.has("conditions")) {
            jsonObject.getAsJsonArray("conditions").forEach(jsonElement -> {
                ResourceLocation conditionTypeLocation = getResourceLocation(jsonElement.getAsJsonObject(), "type");
                conditions.add(ArcRegistry.CONDITION.getOptional(conditionTypeLocation)
                        .orElseThrow(() -> new JsonParseException("Unknown condition type: " + conditionTypeLocation))
                        .getSerializer().fromJson(location, jsonElement.getAsJsonObject()));
            });
        }

        JsonObject holderObject = GsonHelper.getAsJsonObject(jsonObject, "holder");

        return fromJson(location, jsonObject,
                getResourceLocation(holderObject, "id"),
                ArcRegistry.ACTION_HOLDER.byNameCodec().decode(JsonOps.INSTANCE, holderObject.get("type")).result()
                        .orElseThrow(() -> new JsonParseException("Invalid action holder type")).getFirst(),
                rewards, conditions);
    }

    default T fromNetwork(ResourceLocation location, RegistryFriendlyByteBuf friendlyByteBuf) {
        return fromNetwork(location, friendlyByteBuf,
                friendlyByteBuf.readResourceLocation(),
                ArcRegistry.ACTION_HOLDER.getOptional(friendlyByteBuf.readResourceLocation()).orElse(null),
                friendlyByteBuf.readList(object -> IRewardSerializer.fromNetwork((RegistryFriendlyByteBuf) object)),
                friendlyByteBuf.readList(object -> IConditionSerializer.fromNetwork((RegistryFriendlyByteBuf) object)));
    }

    default void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, T type) {
        friendlyByteBuf.writeResourceLocation(type.getActionHolderLocation());
        friendlyByteBuf.writeResourceLocation(type.getActionHolderType().getLocation());
        friendlyByteBuf.writeCollection(type.getRewards(),
                (friendlyByteBuf1, reward) -> IRewardSerializer.toNetwork(reward, (RegistryFriendlyByteBuf) friendlyByteBuf1, type.getLocation()));
        friendlyByteBuf.writeCollection(type.getConditions(),
                (friendlyByteBuf1, condition) -> IConditionSerializer.toNetwork(condition, (RegistryFriendlyByteBuf) friendlyByteBuf1, type.getLocation()));
    }
}
