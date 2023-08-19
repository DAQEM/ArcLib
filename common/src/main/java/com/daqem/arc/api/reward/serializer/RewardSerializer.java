package com.daqem.arc.api.reward.serializer;

import com.daqem.arc.Arc;
import com.daqem.arc.api.reward.IReward;
import com.daqem.arc.data.reward.CancelActionReward;
import com.daqem.arc.data.reward.block.DestroySpeedMultiplierReward;
import com.daqem.arc.data.reward.effect.EffectAmplifierAdditionReward;
import com.daqem.arc.data.reward.effect.EffectDurationMultiplierReward;
import com.daqem.arc.data.reward.effect.EffectReward;
import com.daqem.arc.data.reward.effect.RemoveEffectReward;
import com.daqem.arc.data.reward.experience.ExpReward;
import com.daqem.arc.data.reward.item.ItemReward;
import com.daqem.arc.data.reward.player.AttackSpeedMultiplierReward;
import com.daqem.arc.event.events.RegistryEvent;
import com.daqem.arc.registry.ArcRegistry;
import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

public interface RewardSerializer<T extends IReward> extends IRewardSerializer<T> {

    IRewardSerializer<DestroySpeedMultiplierReward> DESTROY_SPEED_MULTIPLIER = register(Arc.getId("destroy_speed_multiplier"), new DestroySpeedMultiplierReward.Serializer());
    IRewardSerializer<EffectAmplifierAdditionReward> EFFECT_AMPLIFIER_ADDITION = register(Arc.getId("effect_amplifier_addition"), new EffectAmplifierAdditionReward.Serializer());
    IRewardSerializer<EffectDurationMultiplierReward> EFFECT_DURATION_MULTIPLIER = register(Arc.getId("effect_duration_multiplier"), new EffectDurationMultiplierReward.Serializer());
    IRewardSerializer<EffectReward> EFFECT = register(Arc.getId("effect"), new EffectReward.Serializer());
    IRewardSerializer<RemoveEffectReward> REMOVE_EFFECT = register(Arc.getId("remove_effect"), new RemoveEffectReward.Serializer());
    IRewardSerializer<ExpReward> EXP = register(Arc.getId("exp"), new ExpReward.Serializer());
    IRewardSerializer<ItemReward> ITEM = register(Arc.getId("item"), new ItemReward.Serializer());
    IRewardSerializer<CancelActionReward> CANCEL_ACTION = register(Arc.getId("cancel_action"), new CancelActionReward.Serializer());
    IRewardSerializer<AttackSpeedMultiplierReward> ATTACK_SPEED_MULTIPLIER = register(Arc.getId("attack_speed_multiplier"), new AttackSpeedMultiplierReward.Serializer());

    @Override
    default T fromJson(ResourceLocation location, JsonObject jsonObject) {
        return fromJson(jsonObject, GsonHelper.getAsDouble(jsonObject, "chance", 100D));
    }

    @Override
    default T fromNetwork(ResourceLocation location, FriendlyByteBuf friendlyByteBuf) {
        return fromNetwork(friendlyByteBuf, friendlyByteBuf.readDouble());
    }

    @Override
    default void toNetwork(FriendlyByteBuf friendlyByteBuf, T type) {
        friendlyByteBuf.writeDouble(type.getChance());
    }

    static <T extends IReward> IRewardSerializer<T> register(final ResourceLocation location, final RewardSerializer<T> serializer) {
        return Registry.register(ArcRegistry.REWARD_SERIALIZER, location, serializer);
    }

    static void init() {
        RegistryEvent.REGISTER_REWARD_SERIALIZER.invoker().registerRewardSerializer();
    }
}
