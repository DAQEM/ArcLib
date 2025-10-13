package com.daqem.arc.api.event;

import com.daqem.arc.api.IArcAbstractArrow;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.mutable.MutableFloat;

public interface ArcPlayerEvent {

    Event<EntityHurtPlayer> ENTITY_HURT_PLAYER = EventFactory.createEventResult(EntityHurtPlayer.class);
    Event<PlayerHurtPlayer> PLAYER_HURT_PLAYER = EventFactory.createEventResult(PlayerHurtPlayer.class);

    Event<ShootProjectile> SHOOT_PROJECTILE = EventFactory.createLoop(ShootProjectile.class);
    Event<RodReelIn> ROD_REEL_IN = EventFactory.createLoop(RodReelIn.class);

    Event<BrewPotion> BREW_POTION = EventFactory.createLoop(BrewPotion.class);
    Event<AddEffect> ADD_EFFECT = EventFactory.createEventResult(AddEffect.class);
    Event<EnchantItem> ENCHANT_ITEM = EventFactory.createLoop(EnchantItem.class);
    Event<FishUpItem> FISH_UP_ITEM = EventFactory.createLoop(FishUpItem.class);
    Event<GrindItem> GRIND_ITEM = EventFactory.createLoop(GrindItem.class);
    Event<SmeltItem> SMELT_ITEM = EventFactory.createLoop(SmeltItem.class);
    Event<StripLog> STRIP_LOG = EventFactory.createEventResult(StripLog.class);
    Event<UseAnvil> USE_ANVIL = EventFactory.createLoop(UseAnvil.class);

    Event<Drink> DRINK = EventFactory.createEventResult(Drink.class);
    Event<Eat> EAT = EventFactory.createEventResult(Eat.class);

    Event<GetAttackSpeed> GET_ATTACK_SPEED = EventFactory.createEventResult(GetAttackSpeed.class);

    Event<Jump> JUMP = EventFactory.createLoop(Jump.class);
    Event<LandOnGround> LAND_ON_GROUND = EventFactory.createEventResult(LandOnGround.class);
    Event<BlockWithShield> BLOCK_WITH_SHIELD = EventFactory.createLoop(BlockWithShield.class);
    Event<ChangeDimension> CHANGE_DIMENSION = EventFactory.createLoop(ChangeDimension.class);

    interface EntityHurtPlayer {
        EventResult onEntityHurtPlayer(ServerPlayer serverPlayer, DamageSource damageSource, MutableFloat damage);
    }

    interface PlayerHurtPlayer {
        EventResult onPlayerHurtPlayer(ServerPlayer attacker, ServerPlayer defender, DamageSource damageSource, MutableFloat damage);
    }

    interface ShootProjectile {
        void onShootProjectile(ServerPlayer serverPlayer, IArcAbstractArrow arrow);
    }

    interface RodReelIn {
        void onRodReelIn(Player player, FishingHook fishingHook);
    }

    interface BrewPotion {
        void onBrewPotion(Player player, ItemStack potion, BrewingStandBlockEntity brewingStandBlockEntity);
    }

    interface AddEffect {
        EventResult onAddEffect(ServerPlayer serverPlayer, MobEffectInstance effect, Entity source);
    }

    interface EnchantItem {
        void onEnchantItem(ServerPlayer serverPlayer, ItemStack stack, int level);
    }

    interface FishUpItem {
        void onFishUpItem(ServerPlayer serverPlayer, ItemStack stack);
    }

    interface GrindItem {
        void onGrindItem(Player player, ItemStack stack, int experience);
    }

    interface SmeltItem {
        void onSmeltItem(ServerPlayer serverPlayer, Recipe<?> recipe, ItemStack stack, BlockPos furnacePos, Level level);
    }

    interface StripLog {
        EventResult onStripLog(Player player, InteractionHand hand, ItemStack stack, BlockPos pos, BlockState blockState, Level level);
    }

    interface UseAnvil {
        void onUseAnvil(Player player, ItemStack stack, int cost);
    }

    interface Drink {
        EventResult onDrink(Player player, ItemStack itemStack);
    }

    interface Eat {
        EventResult onEat(Player player, ItemStack itemStack);
    }

    interface GetAttackSpeed {
        EventResult onGetAttackSpeed(Player player, ItemStack itemStack, MutableFloat attackSpeed);
    }

    interface Jump {
        void onJump(Player player);
    }

    interface LandOnGround {
        EventResult onLandOnGround(Player player, double fallDistance);
    }

    interface BlockWithShield {
        void onBlockWithShield(Player player, DamageSource source, float amount);
    }

    interface ChangeDimension {
        void onChangeDimension(ServerPlayer player, ResourceKey<Level> from, ResourceKey<Level> to);
    }
}