package com.daqem.arc.api.action.data;

import com.daqem.arc.Arc;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface IActionDataType<T> {

    IActionDataType<BlockState> BLOCK_STATE = register(Arc.getId("block_state"));
    IActionDataType<BlockPos> BLOCK_POSITION = register(Arc.getId("block_position"));
    IActionDataType<Integer> EXP_DROP = register(Arc.getId("exp_drop"));
    IActionDataType<Integer> EXP_LEVEL = register(Arc.getId("exp_level"));
    IActionDataType<Level> WORLD = register(Arc.getId("world"));
    IActionDataType<DamageSource> DAMAGE_SOURCE = register(Arc.getId("damage_source"));
    IActionDataType<Entity> ENTITY = register(Arc.getId("entity"));
    IActionDataType<Float> DAMAGE_AMOUNT = register(Arc.getId("damage_amount"));
    IActionDataType<Double> DISTANCE_IN_CM = register(Arc.getId("distance_in_cm"));
    IActionDataType<ItemStack> ITEM_STACK = register(Arc.getId("item_stack"));
    IActionDataType<Item> ITEM = register(Arc.getId("item"));
    IActionDataType<AdvancementHolder> ADVANCEMENT = register(Arc.getId("advancement"));
    IActionDataType<MobEffectInstance> MOB_EFFECT_INSTANCE = register(Arc.getId("mob_effect_instance"));
    IActionDataType<Recipe<?>> RECIPE = register(Arc.getId("recipe"));
    IActionDataType<InteractionHand> HAND = register(Arc.getId("hand"));
    IActionDataType<Double> FALL_DISTANCE = register(Arc.getId("fall_distance"));
    IActionDataType<ResourceKey<Level>> FROM_DIMENSION = register(Arc.getId("from_dimension"));
    IActionDataType<ResourceKey<Level>> TO_DIMENSION = register(Arc.getId("to_dimension"));
    IActionDataType<MerchantOffer> TRADE_OFFER = register(Arc.getId("trade_offer"));
    IActionDataType<Boolean> IS_CRITICAL_HIT = register(Arc.getId("is_critical_hit"));

    static <T> IActionDataType<T> register(ResourceLocation location) {
        return () -> location;
    }

    ResourceLocation getLocation();
}