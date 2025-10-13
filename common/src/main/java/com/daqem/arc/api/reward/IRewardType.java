package com.daqem.arc.api.reward;

import com.daqem.arc.Arc;
import com.daqem.arc.data.reward.CancelActionReward;
import com.daqem.arc.data.reward.block.BlockDropMultiplierReward;
import com.daqem.arc.data.reward.block.DestroySpeedMultiplierReward;
import com.daqem.arc.data.reward.effect.EffectAmplifierAdditionReward;
import com.daqem.arc.data.reward.effect.EffectDurationMultiplierReward;
import com.daqem.arc.data.reward.effect.EffectReward;
import com.daqem.arc.data.reward.effect.RemoveEffectReward;
import com.daqem.arc.data.reward.entity.*;
import com.daqem.arc.data.reward.experience.ExpMultiplierReward;
import com.daqem.arc.data.reward.experience.ExpReward;
import com.daqem.arc.data.reward.item.*;
import com.daqem.arc.data.reward.player.*;
import com.daqem.arc.data.reward.server.CommandReward;
import com.daqem.arc.data.reward.world.*;
import com.daqem.arc.registry.ArcRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public interface IRewardType<T extends IReward> {

    IRewardType<ExpReward> EXP = register(Arc.getId("exp"), new ExpReward.Serializer());
    IRewardType<ItemReward> ITEM = register(Arc.getId("item"), new ItemReward.Serializer());
    IRewardType<EffectReward> EFFECT = register(Arc.getId("effect"), new EffectReward.Serializer());
    IRewardType<EffectDurationMultiplierReward> EFFECT_DURATION_MULTIPLIER = register(Arc.getId("effect_duration_multiplier"), new EffectDurationMultiplierReward.Serializer());
    IRewardType<EffectAmplifierAdditionReward> EFFECT_AMPLIFIER_ADDITION = register(Arc.getId("effect_amplifier_addition"), new EffectAmplifierAdditionReward.Serializer());
    IRewardType<RemoveEffectReward> REMOVE_EFFECT = register(Arc.getId("remove_effect"), new RemoveEffectReward.Serializer());
    IRewardType<CancelActionReward> CANCEL_ACTION = register(Arc.getId("cancel_action"), new CancelActionReward.Serializer());
    IRewardType<DestroySpeedMultiplierReward> DESTROY_SPEED_MULTIPLIER = register(Arc.getId("destroy_speed_multiplier"), new DestroySpeedMultiplierReward.Serializer());
    IRewardType<AttackSpeedMultiplierReward> ATTACK_SPEED_MULTIPLIER = register(Arc.getId("attack_speed_multiplier"), new AttackSpeedMultiplierReward.Serializer());
    IRewardType<DropItemReward> DROP_ITEM = register(Arc.getId("drop_item"), new DropItemReward.Serializer());
    IRewardType<MultipleArrowsReward> MULTIPLE_ARROWS = register(Arc.getId("multiple_arrows"), new MultipleArrowsReward.Serializer());
    IRewardType<EntityOnFireReward> ENTITY_ON_FIRE = register(Arc.getId("entity_on_fire"), new EntityOnFireReward.Serializer());
    IRewardType<BlockDropMultiplierReward> BLOCK_DROP_MULTIPLIER = register(Arc.getId("block_drop_multiplier"), new BlockDropMultiplierReward.Serializer());
    IRewardType<MoveToEntityReward> MOVE_TO_ENTITY = register(Arc.getId("move_to_entity"), new MoveToEntityReward.Serializer());
    IRewardType<ExpMultiplierReward> EXP_MULTIPLIER = register(Arc.getId("exp_multiplier"), new ExpMultiplierReward.Serializer());
    IRewardType<DamageMultiplierReward> DAMAGE_MULTIPLIER = register(Arc.getId("damage_multiplier"), new DamageMultiplierReward.Serializer());
    IRewardType<CommandReward> COMMAND = register(Arc.getId("command"), new CommandReward.Serializer());
    IRewardType<HealReward> HEAL = register(Arc.getId("heal"), new HealReward.Serializer());
    IRewardType<FeedReward> FEED = register(Arc.getId("feed"), new FeedReward.Serializer());
    IRewardType<CleanseEffectsReward> CLEANSE_EFFECTS = register(Arc.getId("cleanse_effects"), new CleanseEffectsReward.Serializer());
    IRewardType<LaunchPlayerReward> LAUNCH_PLAYER = register(Arc.getId("launch_player"), new LaunchPlayerReward.Serializer());
    IRewardType<TeleportPlayerReward> TELEPORT_PLAYER = register(Arc.getId("teleport_player"), new TeleportPlayerReward.Serializer());
    IRewardType<GiveRecipesReward> GIVE_KNOWLEDGE = register(Arc.getId("give_knowledge"), new GiveRecipesReward.Serializer());
    IRewardType<RepairHeldItemReward> REPAIR_HELD_ITEM = register(Arc.getId("repair_held_item"), new RepairHeldItemReward.Serializer());
    IRewardType<RepairAllArmorReward> REPAIR_ALL_ARMOR = register(Arc.getId("repair_all_armor"), new RepairAllArmorReward.Serializer());
    IRewardType<SmeltInventoryReward> SMELT_INVENTORY = register(Arc.getId("smelt_inventory"), new SmeltInventoryReward.Serializer());
    IRewardType<EnchantItemReward> ENCHANT_HELD_ITEM = register(Arc.getId("enchant_held_item"), new EnchantItemReward.Serializer());
    IRewardType<SpawnEntityReward> SPAWN_ENTITY = register(Arc.getId("spawn_entity"), new SpawnEntityReward.Serializer());
    IRewardType<CreateExplosionReward> CREATE_EXPLOSION = register(Arc.getId("create_explosion"), new CreateExplosionReward.Serializer());
    IRewardType<SetBlockReward> SET_BLOCK = register(Arc.getId("set_block"), new SetBlockReward.Serializer());
    IRewardType<StrikeLightningReward> STRIKE_LIGHTNING = register(Arc.getId("strike_lightning"), new StrikeLightningReward.Serializer());
    IRewardType<ChangeWeatherReward> CHANGE_WEATHER = register(Arc.getId("change_weather"), new ChangeWeatherReward.Serializer());
    IRewardType<ChangeTimeReward> CHANGE_TIME = register(Arc.getId("change_time"), new ChangeTimeReward.Serializer());
    IRewardType<DisarmEntityReward> DISARM_ENTITY = register(Arc.getId("disarm_entity"), new DisarmEntityReward.Serializer());
    IRewardType<FreezeEntityReward> FREEZE_ENTITY = register(Arc.getId("freeze_entity"), new FreezeEntityReward.Serializer());
    IRewardType<PullEntityReward> PULL_ENTITY = register(Arc.getId("pull_entity"), new PullEntityReward.Serializer());
    IRewardType<PushEntityReward> PUSH_ENTITY = register(Arc.getId("push_entity"), new PushEntityReward.Serializer());
    IRewardType<PlaySoundReward> PLAY_SOUND = register(Arc.getId("play_sound"), new PlaySoundReward.Serializer());


    static <T extends IReward> IRewardType<T> register(final ResourceLocation location, final IRewardSerializer<T> serializer) {
        return Registry.register(ArcRegistry.REWARD, location, new IRewardType<T>(){

            @Override
            public ResourceLocation getLocation() {
                return location;
            }

            @Override
            public IRewardSerializer<T> getSerializer() {
                return serializer;
            }

            @Override
            public String toString() {
                return location.toString();
            }
        });
    }

    static void init() {
    }

    ResourceLocation getLocation();

    IRewardSerializer<T> getSerializer();
}