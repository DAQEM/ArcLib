package com.daqem.arc.event;

import com.daqem.arc.api.IArcAbstractArrow;
import com.daqem.arc.api.action.IActionType;
import com.daqem.arc.api.player.ArcPlayer;
import com.daqem.arc.api.player.ArcServerPlayer;
import com.daqem.arc.api.action.data.ActionDataBuilder;
import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.action.data.IActionDataType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

public class PlayerEvents {

    public static void onPlayerEat(ArcServerPlayer player, ItemStack stack) {
        new ActionDataBuilder(player, IActionType.EAT)
                .withData(IActionDataType.ITEM_STACK, stack)
                .build()
                .sendToAction();
    }

    public static void onPlayerDrink(ArcServerPlayer player, ItemStack stack) {
        new ActionDataBuilder(player, IActionType.DRINK)
                .withData(IActionDataType.ITEM_STACK, stack)
                .build()
                .sendToAction();
    }

    public static void onShootProjectile(ArcServerPlayer player, IArcAbstractArrow shotArrowEntity) {
        new ActionDataBuilder(player, IActionType.SHOOT_PROJECTILE)
                .withData(IActionDataType.ITEM_STACK, shotArrowEntity.arc$getPickupItem())
                .withData(IActionDataType.ENTITY, (AbstractArrow) shotArrowEntity)
                .build()
                .sendToAction();
    }

    public static void onBrewPotion(ArcServerPlayer player, ItemStack stack, BlockPos pos, Level level) {
        new ActionDataBuilder(player, IActionType.BREW_POTION)
                .withData(IActionDataType.ITEM_STACK, stack)
                .withData(IActionDataType.BLOCK_POSITION, pos)
                .withData(IActionDataType.BLOCK_STATE, level.getBlockState(pos))
                .withData(IActionDataType.WORLD, level)
                .build()
                .sendToAction();
    }

    public static ActionResult onEffectAdded(ArcServerPlayer player, MobEffectInstance effect, Entity source) {
        return new ActionDataBuilder(player, IActionType.EFFECT_ADDED)
                .withData(IActionDataType.MOB_EFFECT_INSTANCE, effect)
                .withData(IActionDataType.ENTITY, source)
                .build()
                .sendToAction();

    }

    public static void onSmeltItem(ArcServerPlayer player, Recipe<?> recipe, ItemStack stack, BlockPos furnacePos, Level level) {
        new ActionDataBuilder(player, IActionType.SMELT_ITEM)
                .withData(IActionDataType.ITEM_STACK, stack)
                .withData(IActionDataType.BLOCK_POSITION, furnacePos)
                .withData(IActionDataType.BLOCK_STATE, level.getBlockState(furnacePos))
                .withData(IActionDataType.WORLD, level)
                .withData(IActionDataType.RECIPE, recipe)
                .build()
                .sendToAction();
    }

    public static void onCraftItem(ArcServerPlayer player, Recipe<?> recipe, ItemStack stack, Level level) {
        new ActionDataBuilder(player, IActionType.CRAFT_ITEM)
                .withData(IActionDataType.ITEM_STACK, stack)
                .withData(IActionDataType.WORLD, level)
                .withData(IActionDataType.RECIPE, recipe)
                .build()
                .sendToAction();
    }

    public static void onEnchantItem(ArcServerPlayer player, ItemStack stack, int level) {
        new ActionDataBuilder(player, IActionType.ENCHANT_ITEM)
                .withData(IActionDataType.ITEM_STACK, stack)
                .withData(IActionDataType.EXP_LEVEL, level)
                .build()
                .sendToAction();
    }

    public static void onFishedUpItem(ArcServerPlayer player, ItemStack stack) {
        new ActionDataBuilder(player, IActionType.FISHED_UP_ITEM)
                .withData(IActionDataType.ITEM_STACK, stack)
                .withData(IActionDataType.ITEM, stack.getItem())
                .build()
                .sendToAction();
    }

    public static void onStripLog(ArcServerPlayer player, BlockPos pos, Level level) {
        new ActionDataBuilder(player, IActionType.STRIP_LOG)
                .withData(IActionDataType.BLOCK_STATE, level.getBlockState(pos))
                .withData(IActionDataType.BLOCK_POSITION, pos)
                .withData(IActionDataType.WORLD, level)
                .build()
                .sendToAction();
    }

    public static void onGrindItem(ArcServerPlayer player) {
        new ActionDataBuilder(player, IActionType.GRIND_ITEM)
                .build()
                .sendToAction();
    }

    public static void onUseAnvil(ArcServerPlayer player, ItemStack stack) {
        new ActionDataBuilder(player, IActionType.USE_ANVIL)
                .withData(IActionDataType.ITEM_STACK, stack)
                .withData(IActionDataType.ITEM, stack.getItem())
                .build()
                .sendToAction();
    }

    public static ActionResult onPlayerHurtItem(ArcServerPlayer player, ItemStack itemStack) {
        return new ActionDataBuilder(player, IActionType.HURT_ITEM)
                .withData(IActionDataType.ITEM_STACK, itemStack)
                .withData(IActionDataType.ITEM, itemStack.getItem())
                .build()
                .sendToAction();
    }

    public static void onRodReelIn(ArcPlayer serverPlayer, FishingHook fishingHook) {
        new ActionDataBuilder(serverPlayer, IActionType.ROD_REEL_IN)
                .withData(IActionDataType.ENTITY, fishingHook)
                .withData(IActionDataType.BLOCK_POSITION, fishingHook.blockPosition())
                .withData(IActionDataType.WORLD, fishingHook.level())
                .build()
                .sendToAction();
    }
}
