package com.daqem.arc.api.action.data.type;

import com.daqem.arc.Arc;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface ActionDataType<T> extends IActionDataType<T> {

    IActionDataType<BlockState> BLOCK_STATE = create(Arc.getId("block_state"));
    IActionDataType<BlockPos> BLOCK_POSITION = create(Arc.getId("block_position"));
    IActionDataType<Integer> EXP_DROP = create(Arc.getId("exp_drop"));
    IActionDataType<Integer> EXP_LEVEL = create(Arc.getId("exp_level"));
    IActionDataType<Level> WORLD = create(Arc.getId("world"));
    IActionDataType<DamageSource> DAMAGE_SOURCE = create(Arc.getId("damage_source"));
    IActionDataType<Entity> ENTITY = create(Arc.getId("entity"));
    IActionDataType<Float> DAMAGE_AMOUNT = create(Arc.getId("damage_amount"));
    IActionDataType<Integer> DISTANCE_IN_CM = create(Arc.getId("swimming_distance_in_cm"));
    IActionDataType<ItemStack> ITEM_STACK = create(Arc.getId("item_stack"));
    IActionDataType<Item> ITEM = create(Arc.getId("item"));
    IActionDataType<Advancement> ADVANCEMENT = create(Arc.getId("advancement"));
    IActionDataType<MobEffectInstance> MOB_EFFECT_INSTANCE = create(Arc.getId("mob_effect_instance"));
    IActionDataType<Recipe<?>> RECIPE = create(Arc.getId("recipe"));

    private static <T> IActionDataType<T> create(ResourceLocation location) {
        return () -> location;
    }
}
