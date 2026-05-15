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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface IActionDataType<T> {

    Map<ResourceLocation, IActionDataType<?>> TYPES = new ConcurrentHashMap<>();

    IActionDataType<BlockState> BLOCK_STATE = register(Arc.API.getId("block_state"));
    IActionDataType<BlockPos> BLOCK_POSITION = register(Arc.API.getId("block_position"));
    IActionDataType<Integer> EXP_DROP = register(Arc.API.getId("exp_drop"));
    IActionDataType<Integer> EXP_COST = register(Arc.API.getId("exp_cost"));
    IActionDataType<Level> WORLD = register(Arc.API.getId("world"));
    IActionDataType<DamageSource> DAMAGE_SOURCE = register(Arc.API.getId("damage_source"));
    IActionDataType<Entity> ENTITY = register(Arc.API.getId("entity"));
    IActionDataType<Float> DAMAGE_AMOUNT = register(Arc.API.getId("damage_amount"));
    IActionDataType<Double> DISTANCE_IN_CM = register(Arc.API.getId("distance_in_cm"));
    IActionDataType<ItemStack> ITEM_STACK = register(Arc.API.getId("item_stack"));
    IActionDataType<Item> ITEM = register(Arc.API.getId("item"));
    IActionDataType<AdvancementHolder> ADVANCEMENT = register(Arc.API.getId("advancement"));
    IActionDataType<MobEffectInstance> MOB_EFFECT_INSTANCE = register(Arc.API.getId("mob_effect_instance"));
    IActionDataType<Recipe<?>> RECIPE = register(Arc.API.getId("recipe"));
    IActionDataType<InteractionHand> HAND = register(Arc.API.getId("hand"));
    IActionDataType<Double> FALL_DISTANCE = register(Arc.API.getId("fall_distance"));
    IActionDataType<ResourceKey<Level>> FROM_DIMENSION = register(Arc.API.getId("from_dimension"));
    IActionDataType<ResourceKey<Level>> TO_DIMENSION = register(Arc.API.getId("to_dimension"));
    IActionDataType<MerchantOffer> TRADE_OFFER = register(Arc.API.getId("trade_offer"));
    IActionDataType<Boolean> IS_CRITICAL_HIT = register(Arc.API.getId("is_critical_hit"));

    static <T> IActionDataType<T> register(ResourceLocation location) {
        IActionDataType<T> type = () -> location;
        TYPES.put(location, type);
        return type;
    }

    ResourceLocation getResourceLocation();
}