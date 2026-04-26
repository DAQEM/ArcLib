package com.daqem.arc.registry;

import com.daqem.arc.Arc;
import com.daqem.arc.api.action.IActionType;
import com.daqem.arc.api.action.holder.IActionHolderType;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.api.math.INumberProviderType;
import com.daqem.arc.api.reward.IRewardType;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class ArcRegistry {

    public static final ResourceKey<Registry<IActionType<?>>> ACTION_KEY = ResourceKey.createRegistryKey(Arc.API.getId("action"));
    public static final ResourceKey<Registry<IRewardType<?>>> REWARD_KEY = ResourceKey.createRegistryKey(Arc.API.getId("reward"));
    public static final ResourceKey<Registry<IConditionType<?>>> CONDITION_KEY = ResourceKey.createRegistryKey(Arc.API.getId("condition"));
    public static final ResourceKey<Registry<IActionHolderType<?>>> ACTION_HOLDER_KEY = ResourceKey.createRegistryKey(Arc.API.getId("action_holder"));
    public static final ResourceKey<Registry<INumberProviderType<?>>> NUMBER_PROVIDER_KEY = ResourceKey.createRegistryKey(Arc.API.getId("number_provider"));

    public static final Registry<IActionType<?>> ACTION = new MappedRegistry<>(ArcRegistry.ACTION_KEY, Lifecycle.experimental(), false);
    public static final Registry<IRewardType<?>> REWARD = new MappedRegistry<>(ArcRegistry.REWARD_KEY, Lifecycle.experimental(), false);
    public static final Registry<IConditionType<?>> CONDITION = new MappedRegistry<>(ArcRegistry.CONDITION_KEY, Lifecycle.experimental(), false);
    public static final Registry<IActionHolderType<?>> ACTION_HOLDER = new MappedRegistry<>(ArcRegistry.ACTION_HOLDER_KEY, Lifecycle.experimental(), false);
    public static final Registry<INumberProviderType<?>> NUMBER_PROVIDER = new MappedRegistry<>(ArcRegistry.NUMBER_PROVIDER_KEY, Lifecycle.experimental(), false);

    public static void init() {
        IActionType.init();
        IRewardType.init();
        IConditionType.init();
        IActionHolderType.init();
        INumberProviderType.init();
    }
}
