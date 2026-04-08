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

public class BlockEvents {

    public static void registerEvents() {
        Knot.Events.Block.BREAK_BLOCK.register((serverLevel, blockPos, blockState, serverPlayer) -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                ActionResult actionResult = new ActionDataBuilder(arcServerPlayer, IActionType.BREAK_BLOCK)
                        .withData(IActionDataType.BLOCK_STATE, blockState)
                        .withData(IActionDataType.BLOCK_POSITION, blockPos)
                        .withData(IActionDataType.WORLD, serverLevel)
                        .build()
                        .sendToAction();

                if (actionResult.shouldCancelAction()) {
                    return EventResult.INTERRUPT_FALSE;
                }
            }
            return EventResult.PASS;
        }, EventPriority.HIGH);

        Knot.Events.Block.PLACE_BLOCK.register((level, pos, state, placer) -> {
            if (placer instanceof ArcPlayer arcPlayer) {
                ActionResult actionResult = new ActionDataBuilder(arcPlayer, IActionType.PLACE_BLOCK)
                        .withData(IActionDataType.BLOCK_STATE, state)
                        .withData(IActionDataType.BLOCK_POSITION, pos)
                        .withData(IActionDataType.WORLD, level)
                        .build()
                        .sendToAction();

                if (actionResult.shouldCancelAction()) {
                    return EventResult.INTERRUPT_FALSE;
                }

                if (placer instanceof ArcServerPlayer arcServerPlayer) {
                    arcServerPlayer.arc$getBlockPosCache().add(pos);
                }
            }
            return EventResult.PASS;
        }, EventPriority.HIGH);

        Knot.Events.Block.RIGHT_CLICK_BLOCK.register((itemStack, level, player, hand, state, blockPos) -> {
            if (player instanceof ArcPlayer arcPlayer) {
                ActionResult actionResult = new ActionDataBuilder(arcPlayer, IActionType.INTERACT_BLOCK)
                        .withData(IActionDataType.BLOCK_STATE, state)
                        .withData(IActionDataType.BLOCK_POSITION, blockPos)
                        .withData(IActionDataType.WORLD, level)
                        .withData(IActionDataType.ITEM_STACK, itemStack)
                        .withData(IActionDataType.HAND, hand)
                        .build()
                        .sendToAction();
                if (actionResult.shouldCancelAction()) {
                    return EventResult.INTERRUPT_FALSE;
                }
            }
            return EventResult.PASS;
        }, EventPriority.HIGH);

        Knot.Events.Block.PLANT_CROP.register((level, blockPos, blockState, planter) -> {
            if (planter instanceof ArcPlayer arcPlayer) {
                ActionResult actionResult = new ActionDataBuilder(arcPlayer, IActionType.PLANT_CROP)
                        .withData(IActionDataType.BLOCK_STATE, blockState)
                        .withData(IActionDataType.BLOCK_POSITION, blockPos)
                        .withData(IActionDataType.WORLD, level)
                        .build()
                        .sendToAction();
                if (actionResult.shouldCancelAction()) {
                    return EventResult.INTERRUPT_TRUE;
                }
            }
            return EventResult.PASS;
        }, EventPriority.HIGH);

        Knot.Events.Block.HARVEST_CROP.register((level, blockPos, blockState, serverPlayer) -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                ActionResult actionResult = new ActionDataBuilder(arcServerPlayer, IActionType.HARVEST_CROP)
                        .withData(IActionDataType.BLOCK_STATE, blockState)
                        .withData(IActionDataType.BLOCK_POSITION, blockPos)
                        .withData(IActionDataType.WORLD, level)
                        .build()
                        .sendToAction();
                if (actionResult.shouldCancelAction()) {
                    return EventResult.INTERRUPT_FALSE;
                }
            }
            return EventResult.PASS;
        }, EventPriority.HIGH);

        Knot.Events.Block.GET_DESTROY_SPEED.register((player, blockState, blockPos, itemStack, speed) -> {
            if (player instanceof ArcPlayer arcPlayer) {
                ActionResult actionResult = new ActionDataBuilder(arcPlayer, IActionType.GET_DESTROY_SPEED)
                        .withData(IActionDataType.BLOCK_STATE, blockState)
                        .withData(IActionDataType.BLOCK_POSITION, blockPos)
                        .withData(IActionDataType.ITEM_STACK, itemStack)
                        .withData(IActionDataType.ITEM, itemStack.getItem())
                        .withData(IActionDataType.WORLD, player.level())
                        .build()
                        .sendToAction();
                if (actionResult.shouldCancelAction()) {
                    return EventResult.INTERRUPT_FALSE;
                }
                if (actionResult.getDestroySpeedModifier() != 1F) {
                    speed.setValue(speed.floatValue() * actionResult.getDestroySpeedModifier());
                }
            }
            return EventResult.PASS;
        }, EventPriority.HIGH);

        Knot.Events.Block.TILL_SOIL.register((level, blockPos, resultingState, player, hoe) -> {
            if (player instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.TILL_SOIL)
                        .withData(IActionDataType.BLOCK_STATE, resultingState)
                        .withData(IActionDataType.BLOCK_POSITION, blockPos)
                        .withData(IActionDataType.WORLD, level)
                        .withData(IActionDataType.ITEM_STACK, hoe)
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);
    }
}