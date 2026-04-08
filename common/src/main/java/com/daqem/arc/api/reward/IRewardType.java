package com.daqem.arc.api.reward;

import com.daqem.arc.Arc;
import com.daqem.arc.data.reward.CancelActionReward;
import com.daqem.arc.data.reward.block.BlockDropMultiplierReward;
import com.daqem.arc.data.reward.block.DestroySpeedMultiplierReward;
import com.daqem.arc.data.reward.combat.DamageMultiplierReward;
import com.daqem.arc.data.reward.combat.MultipleArrowsReward;
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
import net.minecraft.resources.Identifier;

public interface IRewardType<T extends IReward> {

    IRewardType<ExpReward> EXP = register(Arc.API.getId("exp"), new ExpReward.Serializer());
    IRewardType<ItemReward> ITEM = register(Arc.API.getId("item"), new ItemReward.Serializer());
    IRewardType<EffectReward> EFFECT = register(Arc.API.getId("effect"), new EffectReward.Serializer());
    IRewardType<EffectDurationMultiplierReward> EFFECT_DURATION_MULTIPLIER = register(Arc.API.getId("effect_duration_multiplier"), new EffectDurationMultiplierReward.Serializer());
    IRewardType<EffectAmplifierAdditionReward> EFFECT_AMPLIFIER_ADDITION = register(Arc.API.getId("effect_amplifier_addition"), new EffectAmplifierAdditionReward.Serializer());
    IRewardType<RemoveEffectReward> REMOVE_EFFECT = register(Arc.API.getId("remove_effect"), new RemoveEffectReward.Serializer());
    IRewardType<CancelActionReward> CANCEL_ACTION = register(Arc.API.getId("cancel_action"), new CancelActionReward.Serializer());
    IRewardType<DestroySpeedMultiplierReward> DESTROY_SPEED_MULTIPLIER = register(Arc.API.getId("destroy_speed_multiplier"), new DestroySpeedMultiplierReward.Serializer());
    IRewardType<AttackSpeedMultiplierReward> ATTACK_SPEED_MULTIPLIER = register(Arc.API.getId("attack_speed_multiplier"), new AttackSpeedMultiplierReward.Serializer());
    IRewardType<DropItemReward> DROP_ITEM = register(Arc.API.getId("drop_item"), new DropItemReward.Serializer());
    IRewardType<MultipleArrowsReward> MULTIPLE_ARROWS = register(Arc.API.getId("multiple_arrows"), new MultipleArrowsReward.Serializer());
    IRewardType<EntityOnFireReward> ENTITY_ON_FIRE = register(Arc.API.getId("entity_on_fire"), new EntityOnFireReward.Serializer());
    IRewardType<BlockDropMultiplierReward> BLOCK_DROP_MULTIPLIER = register(Arc.API.getId("block_drop_multiplier"), new BlockDropMultiplierReward.Serializer());
    IRewardType<MoveToEntityReward> MOVE_TO_ENTITY = register(Arc.API.getId("move_to_entity"), new MoveToEntityReward.Serializer());
    IRewardType<ExpMultiplierReward> EXP_MULTIPLIER = register(Arc.API.getId("exp_multiplier"), new ExpMultiplierReward.Serializer());
    IRewardType<DamageMultiplierReward> DAMAGE_MULTIPLIER = register(Arc.API.getId("damage_multiplier"), new DamageMultiplierReward.Serializer());
    IRewardType<CommandReward> COMMAND = register(Arc.API.getId("command"), new CommandReward.Serializer());
    IRewardType<HealReward> HEAL = register(Arc.API.getId("heal"), new HealReward.Serializer());
    IRewardType<FeedReward> FEED = register(Arc.API.getId("feed"), new FeedReward.Serializer());
    IRewardType<CleanseEffectsReward> CLEANSE_EFFECTS = register(Arc.API.getId("cleanse_effects"), new CleanseEffectsReward.Serializer());
    IRewardType<LaunchPlayerReward> LAUNCH_PLAYER = register(Arc.API.getId("launch_player"), new LaunchPlayerReward.Serializer());
    IRewardType<TeleportPlayerReward> TELEPORT_PLAYER = register(Arc.API.getId("teleport_player"), new TeleportPlayerReward.Serializer());
    IRewardType<GiveRecipesReward> GIVE_RECIPES = register(Arc.API.getId("give_recipes"), new GiveRecipesReward.Serializer());
    IRewardType<RepairHeldItemReward> REPAIR_HELD_ITEM = register(Arc.API.getId("repair_held_item"), new RepairHeldItemReward.Serializer());
    IRewardType<RepairAllArmorReward> REPAIR_ALL_ARMOR = register(Arc.API.getId("repair_all_armor"), new RepairAllArmorReward.Serializer());
    IRewardType<SmeltInventoryReward> SMELT_INVENTORY = register(Arc.API.getId("smelt_inventory"), new SmeltInventoryReward.Serializer());
    IRewardType<EnchantItemReward> ENCHANT_ITEM = register(Arc.API.getId("enchant_item"), new EnchantItemReward.Serializer());
    IRewardType<SpawnEntityReward> SPAWN_ENTITY = register(Arc.API.getId("spawn_entity"), new SpawnEntityReward.Serializer());
    IRewardType<CreateExplosionReward> CREATE_EXPLOSION = register(Arc.API.getId("create_explosion"), new CreateExplosionReward.Serializer());
    IRewardType<SetBlockReward> SET_BLOCK = register(Arc.API.getId("set_block"), new SetBlockReward.Serializer());
    IRewardType<StrikeLightningReward> STRIKE_LIGHTNING = register(Arc.API.getId("strike_lightning"), new StrikeLightningReward.Serializer());
    IRewardType<ChangeWeatherReward> CHANGE_WEATHER = register(Arc.API.getId("change_weather"), new ChangeWeatherReward.Serializer());
    IRewardType<ChangeTimeReward> CHANGE_TIME = register(Arc.API.getId("change_time"), new ChangeTimeReward.Serializer());
    IRewardType<DisarmEntityReward> DISARM_ENTITY = register(Arc.API.getId("disarm_entity"), new DisarmEntityReward.Serializer());
    IRewardType<FreezeEntityReward> FREEZE_ENTITY = register(Arc.API.getId("freeze_entity"), new FreezeEntityReward.Serializer());
    IRewardType<PullEntityReward> PULL_ENTITY = register(Arc.API.getId("pull_entity"), new PullEntityReward.Serializer());
    IRewardType<PushEntityReward> PUSH_ENTITY = register(Arc.API.getId("push_entity"), new PushEntityReward.Serializer());
    IRewardType<PlaySoundReward> PLAY_SOUND = register(Arc.API.getId("play_sound"), new PlaySoundReward.Serializer());


    static <T extends IReward> IRewardType<T> register(final Identifier location, final IRewardSerializer<T> serializer) {
        return Registry.register(ArcRegistry.REWARD, location, new IRewardType<T>(){

            @Override
            public Identifier getIdentifier() {
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

    Identifier getIdentifier();

    IRewardSerializer<T> getSerializer();
}