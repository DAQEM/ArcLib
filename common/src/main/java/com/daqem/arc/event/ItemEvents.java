package com.daqem.arc.event;

import com.daqem.arc.api.action.IActionType;
import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.event.ArcItemEvent;
import com.daqem.arc.api.event.EventPriority;
import com.daqem.arc.api.event.EventResult;
import com.daqem.arc.api.player.ArcPlayer;
import com.daqem.arc.api.player.ArcServerPlayer;
import com.daqem.arc.api.action.data.ActionDataBuilder;
import com.daqem.arc.api.action.data.IActionDataType;

public class ItemEvents {

    public static void registerEvents() {
        ArcItemEvent.DROP_ITEM.register((player, itemEntity) -> {
            if (player instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.DROP_ITEM)
                        .withData(IActionDataType.ENTITY, itemEntity)
                        .withData(IActionDataType.ITEM, itemEntity.getItem().getItem())
                        .withData(IActionDataType.ITEM_STACK, itemEntity.getItem())
                        .withData(IActionDataType.WORLD, player.level())
                        .withData(IActionDataType.BLOCK_POSITION, itemEntity.blockPosition())
                        .build()
                        .sendToAction();
            }
            return EventResult.PASS;
        }, EventPriority.HIGH);

        ArcItemEvent.CRAFT_ITEM.register((serverPlayer, recipe, stack) -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.CRAFT_ITEM)
                        .withData(IActionDataType.RECIPE, recipe)
                        .withData(IActionDataType.ITEM, stack.getItem())
                        .withData(IActionDataType.ITEM_STACK, stack)
                        .withData(IActionDataType.WORLD, serverPlayer.level())
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);

        ArcItemEvent.HURT_ITEM.register((serverPlayer, itemStack, damage) -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                ActionResult actionResult = new ActionDataBuilder(arcServerPlayer, IActionType.HURT_ITEM)
                        .withData(IActionDataType.ITEM_STACK, itemStack)
                        .withData(IActionDataType.ITEM, itemStack.getItem())
                        .withData(IActionDataType.WORLD, serverPlayer.level())
                        .withData(IActionDataType.DAMAGE_AMOUNT, damage.floatValue())
                        .build()
                        .sendToAction();

                if (actionResult.shouldCancelAction()) {
                    return EventResult.INTERRUPT_FALSE;
                }
                if (actionResult.getDamageModifier() != 1F) {
                    damage.setValue((int) (damage.getValue() * actionResult.getDamageModifier()));
                }
            }
            return EventResult.PASS;
        }, EventPriority.HIGH);

        ArcItemEvent.THROW_ITEM.register((serverPlayer, projectile) -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.THROW_ITEM)
                        .withData(IActionDataType.ITEM_STACK, projectile.getItem())
                        .withData(IActionDataType.ENTITY, projectile)
                        .withData(IActionDataType.ITEM, projectile.getItem().getItem())
                        .withData(IActionDataType.WORLD, serverPlayer.level())
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);

        ArcItemEvent.USE_ITEM.register((level, player, hand, itemStack) -> {
            if (player instanceof ArcPlayer arcPlayer) {
                ActionResult actionResult = new ActionDataBuilder(arcPlayer, IActionType.USE_ITEM)
                        .withData(IActionDataType.ITEM_STACK, itemStack)
                        .withData(IActionDataType.ITEM, itemStack.getItem())
                        .withData(IActionDataType.WORLD, level)
                        .withData(IActionDataType.HAND, hand)
                        .build()
                        .sendToAction();

                if (actionResult.shouldCancelAction()) {
                    return EventResult.INTERRUPT_FALSE;
                }
            }
            return EventResult.PASS;
        }, EventPriority.HIGH);
    }
}
