package com.daqem.arc.api.reward.serializer;

import com.daqem.arc.Arc;
import com.daqem.arc.api.reward.IReward;
import com.daqem.arc.data.reward.CancelActionReward;
import com.daqem.arc.data.reward.block.BlockDropMultiplierReward;
import com.daqem.arc.data.reward.block.DestroySpeedMultiplierReward;
import com.daqem.arc.data.reward.effect.EffectAmplifierAdditionReward;
import com.daqem.arc.data.reward.effect.EffectDurationMultiplierReward;
import com.daqem.arc.data.reward.effect.EffectReward;
import com.daqem.arc.data.reward.effect.RemoveEffectReward;
import com.daqem.arc.data.reward.entity.EntityOnFireReward;
import com.daqem.arc.data.reward.entity.MultipleArrowsReward;
import com.daqem.arc.data.reward.experience.ExpMultiplierReward;
import com.daqem.arc.data.reward.experience.ExpReward;
import com.daqem.arc.data.reward.item.ItemReward;
import com.daqem.arc.data.reward.player.AttackSpeedMultiplierReward;
import com.daqem.arc.data.reward.player.MoveToEntityReward;
import com.daqem.arc.data.reward.world.DropItemReward;
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
    IRewardSerializer<DropItemReward> DROP_ITEM = register(Arc.getId("drop_item"), new DropItemReward.Serializer());
    IRewardSerializer<MultipleArrowsReward> MULTIPLE_ARROWS = register(Arc.getId("multiple_arrows"), new MultipleArrowsReward.Serializer());
    IRewardSerializer<EntityOnFireReward> ENTITY_ON_FIRE = register(Arc.getId("entity_on_fire"), new EntityOnFireReward.Serializer());
    IRewardSerializer<BlockDropMultiplierReward> BLOCK_DROP_MULTIPLIER = register(Arc.getId("block_drop_multiplier"), new BlockDropMultiplierReward.Serializer());
    IRewardSerializer<MoveToEntityReward> MOVE_TO_ENTITY = register(Arc.getId("move_to_entity"), new MoveToEntityReward.Serializer());
    IRewardSerializer<ExpMultiplierReward> EXP_MULTIPLIER = register(Arc.getId("exp_multiplier"), new ExpMultiplierReward.Serializer());

    @Override
    default T fromJson(ResourceLocation location, JsonObject jsonObject) {
        return fromJson(jsonObject, GsonHelper.getAsDouble(jsonObject, "chance", 100D), GsonHelper.getAsInt(jsonObject, "priority", 1));
    }

    @Override
    default T fromNetwork(ResourceLocation location, FriendlyByteBuf friendlyByteBuf) {
        return fromNetwork(friendlyByteBuf, friendlyByteBuf.readDouble(), friendlyByteBuf.readInt());
    }

    @Override
    default void toNetwork(FriendlyByteBuf friendlyByteBuf, T type) {
        friendlyByteBuf.writeDouble(type.getChance());
        friendlyByteBuf.writeInt(type.getPriority());
    }

    static <T extends IReward> IRewardSerializer<T> register(final ResourceLocation location, final RewardSerializer<T> serializer) {
        return Registry.register(ArcRegistry.REWARD_SERIALIZER, location, serializer);
    }

    static void init() {
        RegistryEvent.REGISTER_REWARD_SERIALIZER.invoker().registerRewardSerializer();
    }
}
