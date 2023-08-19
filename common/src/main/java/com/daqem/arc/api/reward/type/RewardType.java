package com.daqem.arc.api.reward.type;

import com.daqem.arc.Arc;
import com.daqem.arc.api.action.IAction;
import com.daqem.arc.api.action.type.ActionType;
import com.daqem.arc.api.reward.IReward;
import com.daqem.arc.api.reward.serializer.IRewardSerializer;
import com.daqem.arc.data.reward.CancelActionReward;
import com.daqem.arc.data.reward.block.DestroySpeedMultiplierReward;
import com.daqem.arc.data.reward.effect.EffectAmplifierAdditionReward;
import com.daqem.arc.data.reward.effect.EffectDurationMultiplierReward;
import com.daqem.arc.data.reward.effect.EffectReward;
import com.daqem.arc.data.reward.effect.RemoveEffectReward;
import com.daqem.arc.data.reward.experience.ExpReward;
import com.daqem.arc.data.reward.item.ItemReward;
import com.daqem.arc.data.reward.player.AttackSpeedMultiplierReward;
import com.daqem.arc.data.reward.world.DropItemReward;
import com.daqem.arc.event.events.RegistryEvent;
import com.daqem.arc.registry.ArcRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public interface RewardType<T extends IReward> extends IRewardType<T> {

    IRewardType<ExpReward> EXP = register(Arc.getId("exp"));
    IRewardType<ItemReward> ITEM = register(Arc.getId("item"));
    IRewardType<EffectReward> EFFECT = register(Arc.getId("effect"));
    IRewardType<EffectDurationMultiplierReward> EFFECT_DURATION_MULTIPLIER = register(Arc.getId("effect_duration_multiplier"));
    IRewardType<EffectAmplifierAdditionReward> EFFECT_AMPLIFIER_ADDITION = register(Arc.getId("effect_amplifier_addition"));
    IRewardType<RemoveEffectReward> REMOVE_EFFECT = register(Arc.getId("remove_effect"));
    IRewardType<CancelActionReward> CANCEL_ACTION = register(Arc.getId("cancel_action"));
    IRewardType<DestroySpeedMultiplierReward> DESTROY_SPEED_MULTIPLIER = register(Arc.getId("destroy_speed_multiplier"));
    IRewardType<AttackSpeedMultiplierReward> ATTACK_SPEED_MULTIPLIER = register(Arc.getId("attack_speed_multiplier"));
    IRewardType<DropItemReward> DROP_ITEM = register(Arc.getId("drop_item"));

    static <T extends IReward> IRewardType<T> register(final ResourceLocation location) {
        return Registry.register(ArcRegistry.REWARD, location, new RewardType<T>(){

            @Override
            public ResourceLocation getLocation() {
                return location;
            }

            @Override
            public String toString() {
                return location.toString();
            }
        });
    }

    static void init() {
        RegistryEvent.REGISTER_REWARD_TYPE.invoker().registerRewardType();
    }
}
