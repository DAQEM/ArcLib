package com.daqem.arc.registry;

import com.daqem.arc.Arc;
import com.daqem.arc.api.action.holder.type.ActionHolderType;
import com.daqem.arc.api.action.holder.type.IActionHolderType;
import com.daqem.arc.api.action.serializer.ActionSerializer;
import com.daqem.arc.api.action.serializer.IActionSerializer;
import com.daqem.arc.api.action.type.ActionType;
import com.daqem.arc.api.action.type.IActionType;
import com.daqem.arc.api.condition.serializer.ConditionSerializer;
import com.daqem.arc.api.condition.serializer.IConditionSerializer;
import com.daqem.arc.api.condition.type.ConditionType;
import com.daqem.arc.api.condition.type.IConditionType;
import com.daqem.arc.api.reward.serializer.IRewardSerializer;
import com.daqem.arc.api.reward.serializer.RewardSerializer;
import com.daqem.arc.api.reward.type.IRewardType;
import com.daqem.arc.api.reward.type.RewardType;
import com.daqem.arc.event.events.RegistryEvent;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class ArcRegistry {

    public static final ResourceKey<Registry<IActionType<?>>> ACTION_KEY = ResourceKey.createRegistryKey(Arc.getId("action"));
    public static final ResourceKey<Registry<IRewardType<?>>> REWARD_KEY = ResourceKey.createRegistryKey(Arc.getId("reward"));
    public static final ResourceKey<Registry<IConditionType<?>>> CONDITION_KEY = ResourceKey.createRegistryKey(Arc.getId("condition"));
    public static final ResourceKey<Registry<IActionHolderType<?>>> ACTION_HOLDER_KEY = ResourceKey.createRegistryKey(Arc.getId("action_holder"));

    public static final ResourceKey<Registry<IActionSerializer<?>>> ACTION_SERIALIZER_KEY = ResourceKey.createRegistryKey(Arc.getId("action_serializer"));
    public static final ResourceKey<Registry<IRewardSerializer<?>>> REWARD_SERIALIZER_KEY = ResourceKey.createRegistryKey(Arc.getId("reward_serializer"));
    public static final ResourceKey<Registry<IConditionSerializer<?>>> CONDITION_SERIALIZER_KEY = ResourceKey.createRegistryKey(Arc.getId("condition_serializer"));


    public static Registry<IActionType<?>> ACTION;
    public static Registry<IRewardType<?>> REWARD;
    public static Registry<IConditionType<?>> CONDITION;
    public static Registry<IActionHolderType<?>> ACTION_HOLDER;

    public static Registry<IActionSerializer<?>> ACTION_SERIALIZER;
    public static Registry<IRewardSerializer<?>> REWARD_SERIALIZER;
    public static Registry<IConditionSerializer<?>> CONDITION_SERIALIZER;

    public static void init() {
        ACTION = new MappedRegistry<>(ArcRegistry.ACTION_KEY, Lifecycle.experimental(), null);
        REWARD = new MappedRegistry<>(ArcRegistry.REWARD_KEY, Lifecycle.experimental(), null);
        CONDITION = new MappedRegistry<>(ArcRegistry.CONDITION_KEY, Lifecycle.experimental(), null);
        ACTION_HOLDER = new MappedRegistry<>(ArcRegistry.ACTION_HOLDER_KEY, Lifecycle.experimental(), null);

        ACTION_SERIALIZER = new MappedRegistry<>(ArcRegistry.ACTION_SERIALIZER_KEY, Lifecycle.experimental(), null);
        REWARD_SERIALIZER = new MappedRegistry<>(ArcRegistry.REWARD_SERIALIZER_KEY, Lifecycle.experimental(), null);
        CONDITION_SERIALIZER = new MappedRegistry<>(ArcRegistry.CONDITION_SERIALIZER_KEY, Lifecycle.experimental(), null);

        ActionType.init();
        RewardType.init();
        ConditionType.init();
        ActionHolderType.init();

        ActionSerializer.init();
        RewardSerializer.init();
        ConditionSerializer.init();
    }
}
