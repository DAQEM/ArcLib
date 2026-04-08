package com.daqem.arc.event;

import com.daqem.arc.api.action.IActionType;
import com.daqem.arc.api.action.data.ActionDataBuilder;
import com.daqem.arc.api.action.data.IActionDataType;
import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.player.ArcPlayer;
import com.daqem.arc.api.player.ArcServerPlayer;
import com.daqem.knot.Knot;
import com.daqem.knot.events.EventPriority;
import com.daqem.knot.events.EventResult;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;

public class PlayerEvents {

    public static void registerEvents() {
        Knot.Events.Player.ENTITY_HURT_PLAYER.register((serverPlayer, damageSource, damage) -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                ActionResult actionResult = new ActionDataBuilder(arcServerPlayer, IActionType.GET_HURT)
                        .withData(IActionDataType.ENTITY, damageSource.getEntity())
                        .withData(IActionDataType.DAMAGE_SOURCE, damageSource)
                        .withData(IActionDataType.DAMAGE_AMOUNT, damage.floatValue())
                        .build()
                        .sendToAction();

                if (actionResult.shouldCancelAction()) {
                    return EventResult.INTERRUPT_FALSE;
                }

                if (actionResult.getDamageModifier() != 1F) {
                    damage.setValue(damage.floatValue() * actionResult.getDamageModifier());
                }
            }
            return EventResult.PASS;
        }, EventPriority.HIGH);

        Knot.Events.Player.PLAYER_HURT_PLAYER.register((attacker, defender, damageSource, damage) -> {
            if (attacker instanceof ArcServerPlayer arcServerPlayer) {
                ActionResult actionResult = new ActionDataBuilder(arcServerPlayer, IActionType.HURT_PLAYER)
                        .withData(IActionDataType.ENTITY, defender)
                        .withData(IActionDataType.DAMAGE_SOURCE, damageSource)
                        .withData(IActionDataType.DAMAGE_AMOUNT, damage.floatValue())
                        .build()
                        .sendToAction();

                if (actionResult.shouldCancelAction()) {
                    return EventResult.INTERRUPT_FALSE;
                }

                if (actionResult.getDamageModifier() != 1F) {
                    damage.setValue(damage.floatValue() * actionResult.getDamageModifier());
                }
            }
            return EventResult.PASS;
        }, EventPriority.HIGH);

        Knot.Events.Player.BREW_POTION.register((player, potion, brewingStandBlockEntity) -> {
            if (player instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.BREW_POTION)
                        .withData(IActionDataType.ITEM_STACK, potion)
                        .withData(IActionDataType.BLOCK_POSITION, brewingStandBlockEntity.getBlockPos())
                        .withData(IActionDataType.BLOCK_STATE, brewingStandBlockEntity.getBlockState())
                        .withData(IActionDataType.WORLD, brewingStandBlockEntity.getLevel())
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);

        Knot.Events.Player.EAT.register((player, itemStack) -> {
            if (player instanceof ArcPlayer arcPlayer) {
                ActionResult actionResult = new ActionDataBuilder(arcPlayer, IActionType.EAT)
                        .withData(IActionDataType.ITEM_STACK, itemStack)
                        .withData(IActionDataType.ITEM, itemStack.getItem())
                        .withData(IActionDataType.WORLD, player.level())
                        .withData(IActionDataType.BLOCK_POSITION, player.blockPosition())
                        .build()
                        .sendToAction();

                if (actionResult.shouldCancelAction()) {
                    return EventResult.INTERRUPT_FALSE;
                }
            }
            return EventResult.PASS;
        }, EventPriority.HIGH);

        Knot.Events.Player.DRINK.register((player, itemStack) -> {
            if (player instanceof ArcPlayer arcPlayer) {
                ActionResult actionResult = new ActionDataBuilder(arcPlayer, IActionType.DRINK)
                        .withData(IActionDataType.ITEM_STACK, itemStack)
                        .withData(IActionDataType.ITEM, itemStack.getItem())
                        .withData(IActionDataType.WORLD, player.level())
                        .withData(IActionDataType.BLOCK_POSITION, player.blockPosition())
                        .build()
                        .sendToAction();

                if (actionResult.shouldCancelAction()) {
                    return EventResult.INTERRUPT_FALSE;
                }
            }
            return EventResult.PASS;
        }, EventPriority.HIGH);

        Knot.Events.Player.ADD_EFFECT.register((serverPlayer, effect, source) -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                ActionResult actionResult = new ActionDataBuilder(arcServerPlayer, IActionType.ADD_EFFECT)
                        .withData(IActionDataType.MOB_EFFECT_INSTANCE, effect)
                        .withData(IActionDataType.ENTITY, source)
                        .withData(IActionDataType.WORLD, serverPlayer.level())
                        .withData(IActionDataType.BLOCK_POSITION, serverPlayer.blockPosition())
                        .build()
                        .sendToAction();

                if (effect != null) {
                    if (actionResult.getCancelEffects().stream().anyMatch(e -> e.is(effect.getEffect()))) {
                        return EventResult.INTERRUPT_FALSE;
                    }
                }

                if (actionResult.shouldCancelAction()) {
                    return EventResult.INTERRUPT_FALSE;
                }
            }
            return EventResult.PASS;
        }, EventPriority.HIGH);

        Knot.Events.Player.SMELT_ITEM.register((serverPlayer, recipe, stack, furnacePos, level) -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.SMELT_ITEM)
                        .withData(IActionDataType.ITEM_STACK, stack)
                        .withData(IActionDataType.BLOCK_POSITION, furnacePos)
                        .withData(IActionDataType.BLOCK_STATE, level.getBlockState(furnacePos))
                        .withData(IActionDataType.WORLD, level)
                        .withData(IActionDataType.RECIPE, recipe)
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);

        Knot.Events.Player.ENCHANT_ITEM.register((serverPlayer, stack, level) -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.ENCHANT_ITEM)
                        .withData(IActionDataType.ITEM_STACK, stack)
                        .withData(IActionDataType.EXP_LEVEL, level)
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);

        Knot.Events.Player.FISH_UP_ITEM.register((serverPlayer, stack) -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.FISHED_UP_ITEM)
                        .withData(IActionDataType.ITEM_STACK, stack)
                        .withData(IActionDataType.ITEM, stack.getItem())
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);

        Knot.Events.Player.STRIP_LOG.register((player, hand, stack, pos, blockState, level) -> {
            if (player instanceof ArcPlayer arcPlayer) {
                ActionResult actionResult = new ActionDataBuilder(arcPlayer, IActionType.STRIP_LOG)
                        .withData(IActionDataType.BLOCK_STATE, blockState)
                        .withData(IActionDataType.BLOCK_POSITION, pos)
                        .withData(IActionDataType.WORLD, level)
                        .withData(IActionDataType.ITEM_STACK, stack)
                        .withData(IActionDataType.ITEM, stack.getItem())
                        .withData(IActionDataType.HAND, hand)
                        .build()
                        .sendToAction();

                if (actionResult.shouldCancelAction()) {
                    return EventResult.INTERRUPT_FALSE;
                }
            }
            return EventResult.PASS;
        }, EventPriority.HIGH);

        Knot.Events.Player.GRIND_ITEM.register((player, stack, experience) -> {
            if (player instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.GRIND_ITEM)
                        .withData(IActionDataType.ITEM_STACK, stack)
                        .withData(IActionDataType.ITEM, stack.getItem())
                        .withData(IActionDataType.WORLD, player.level())
                        .withData(IActionDataType.BLOCK_POSITION, player.blockPosition())
                        .withData(IActionDataType.EXP_DROP, experience)
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);

        Knot.Events.Player.USE_ANVIL.register((player, stack, cost) -> {
            if (player instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.USE_ANVIL)
                        .withData(IActionDataType.ITEM_STACK, stack)
                        .withData(IActionDataType.ITEM, stack.getItem())
                        .withData(IActionDataType.WORLD, player.level())
                        .withData(IActionDataType.BLOCK_POSITION, player.blockPosition())
                        .withData(IActionDataType.EXP_LEVEL, cost)
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);

        Knot.Events.Player.ROD_REEL_IN.register((player, fishingHook) -> {
            if (player instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.ROD_REEL_IN)
                        .withData(IActionDataType.ENTITY, fishingHook)
                        .withData(IActionDataType.BLOCK_POSITION, fishingHook.blockPosition())
                        .withData(IActionDataType.BLOCK_STATE, fishingHook.level().getBlockState(fishingHook.blockPosition()))
                        .withData(IActionDataType.WORLD, fishingHook.level())
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);

        Knot.Events.Player.GET_ATTACK_SPEED.register((player, itemStack, attackSpeed) -> {
            if (player instanceof ArcPlayer arcPlayer) {
                ActionResult actionResult = new ActionDataBuilder(arcPlayer, IActionType.GET_ATTACK_SPEED)
                        .withData(IActionDataType.ITEM_STACK, itemStack)
                        .withData(IActionDataType.ITEM, itemStack.getItem())
                        .withData(IActionDataType.WORLD, player.level())
                        .withData(IActionDataType.BLOCK_POSITION, player.blockPosition())
                        .build()
                        .sendToAction();

                if (actionResult.shouldCancelAction()) {
                    return EventResult.INTERRUPT_FALSE;
                }

                if (actionResult.getAttackSpeedModifier() != 1F) {
                    attackSpeed.setValue(attackSpeed.floatValue() / actionResult.getAttackSpeedModifier());
                }
            }
            return EventResult.PASS;
        }, EventPriority.HIGH);

        Knot.Events.Player.JUMP.register(player -> {
            if (player instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.JUMP)
                        .withData(IActionDataType.BLOCK_POSITION, player.blockPosition())
                        .withData(IActionDataType.WORLD, player.level())
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);

        Knot.Events.Player.LAND_ON_GROUND.register((player, fallDistance) -> {
            if (player instanceof ArcServerPlayer arcServerPlayer) {
                ActionResult actionResult = new ActionDataBuilder(arcServerPlayer, IActionType.LAND_ON_GROUND)
                        .withData(IActionDataType.FALL_DISTANCE, fallDistance)
                        .withData(IActionDataType.BLOCK_POSITION, player.blockPosition())
                        .withData(IActionDataType.WORLD, player.level())
                        .build()
                        .sendToAction();

                if (actionResult.shouldCancelAction()) {
                    return EventResult.INTERRUPT_FALSE;
                }
            }
            return EventResult.PASS;
        }, EventPriority.HIGH);

        Knot.Events.Player.BLOCK_WITH_SHIELD.register((player, source, amount) -> {
            if (player instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.BLOCK_WITH_SHIELD)
                        .withData(IActionDataType.DAMAGE_SOURCE, source)
                        .withData(IActionDataType.DAMAGE_AMOUNT, amount)
                        .withData(IActionDataType.ENTITY, source.getEntity())
                        .withData(IActionDataType.BLOCK_POSITION, player.blockPosition())
                        .withData(IActionDataType.WORLD, player.level())
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);

        Knot.Events.Player.CHANGE_DIMENSION.register((player, from, to) -> {
            if (player instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.CHANGE_DIMENSION)
                        .withData(IActionDataType.FROM_DIMENSION, from)
                        .withData(IActionDataType.TO_DIMENSION, to)
                        .withData(IActionDataType.WORLD, player.level())
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);

        Knot.Events.Player.SHOOT_PROJECTILE.register((player, projectile) -> {
            if (player instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.SHOOT_PROJECTILE)
                        .withData(IActionDataType.ITEM_STACK, projectile.knot$getPickupItem())
                        .withData(IActionDataType.ENTITY, (AbstractArrow) projectile)
                        .withData(IActionDataType.WORLD, player.level())
                        .withData(IActionDataType.BLOCK_POSITION, ((AbstractArrow) projectile).blockPosition())
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);
    }
}