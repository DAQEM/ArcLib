package com.daqem.arc.event;

import com.daqem.arc.api.action.IActionType;
import com.daqem.arc.api.action.data.ActionDataBuilder;
import com.daqem.arc.api.action.data.IActionDataType;
import com.daqem.arc.api.event.ArcMovementEvent;
import com.daqem.arc.api.event.EventPriority;
import com.daqem.arc.api.player.ArcServerPlayer;

public class MovementEvents {

    public static void registerEvents() {
        ArcMovementEvent.WALK.register((serverPlayer, distanceInCm) -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.WALK)
                        .withData(IActionDataType.DISTANCE_IN_CM, distanceInCm)
                        .withData(IActionDataType.WORLD, serverPlayer.level())
                        .withData(IActionDataType.BLOCK_POSITION, serverPlayer.blockPosition())
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);

        ArcMovementEvent.START_WALK.register(serverPlayer -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.WALK_START)
                        .withData(IActionDataType.WORLD, serverPlayer.level())
                        .withData(IActionDataType.BLOCK_POSITION, serverPlayer.blockPosition())
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);

        ArcMovementEvent.STOP_WALK.register(serverPlayer -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.WALK_STOP)
                        .withData(IActionDataType.WORLD, serverPlayer.level())
                        .withData(IActionDataType.BLOCK_POSITION, serverPlayer.blockPosition())
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);

        ArcMovementEvent.SPRINT.register((serverPlayer, distanceInCm) -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.SPRINT)
                        .withData(IActionDataType.DISTANCE_IN_CM, distanceInCm)
                        .withData(IActionDataType.WORLD, serverPlayer.level())
                        .withData(IActionDataType.BLOCK_POSITION, serverPlayer.blockPosition())
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);

        ArcMovementEvent.START_SPRINT.register(serverPlayer -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.SPRINT_START)
                        .withData(IActionDataType.WORLD, serverPlayer.level())
                        .withData(IActionDataType.BLOCK_POSITION, serverPlayer.blockPosition())
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);

        ArcMovementEvent.STOP_SPRINT.register(serverPlayer -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.SPRINT_STOP)
                        .withData(IActionDataType.WORLD, serverPlayer.level())
                        .withData(IActionDataType.BLOCK_POSITION, serverPlayer.blockPosition())
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);

        ArcMovementEvent.SWIM.register((serverPlayer, distanceInCm) -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.SWIM)
                        .withData(IActionDataType.DISTANCE_IN_CM, distanceInCm)
                        .withData(IActionDataType.WORLD, serverPlayer.level())
                        .withData(IActionDataType.BLOCK_POSITION, serverPlayer.blockPosition())
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);

        ArcMovementEvent.START_SWIM.register(serverPlayer -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.SWIM_START)
                        .withData(IActionDataType.WORLD, serverPlayer.level())
                        .withData(IActionDataType.BLOCK_POSITION, serverPlayer.blockPosition())
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);

        ArcMovementEvent.STOP_SWIM.register(serverPlayer -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.SWIM_STOP)
                        .withData(IActionDataType.WORLD, serverPlayer.level())
                        .withData(IActionDataType.BLOCK_POSITION, serverPlayer.blockPosition())
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);

        ArcMovementEvent.CROUCH.register((serverPlayer, distanceInCm) -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.CROUCH)
                        .withData(IActionDataType.DISTANCE_IN_CM, distanceInCm)
                        .withData(IActionDataType.WORLD, serverPlayer.level())
                        .withData(IActionDataType.BLOCK_POSITION, serverPlayer.blockPosition())
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);

        ArcMovementEvent.START_CROUCH.register(serverPlayer -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.CROUCH_START)
                        .withData(IActionDataType.WORLD, serverPlayer.level())
                        .withData(IActionDataType.BLOCK_POSITION, serverPlayer.blockPosition())
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);

        ArcMovementEvent.STOP_CROUCH.register(serverPlayer -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.CROUCH_STOP)
                        .withData(IActionDataType.WORLD, serverPlayer.level())
                        .withData(IActionDataType.BLOCK_POSITION, serverPlayer.blockPosition())
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);

        ArcMovementEvent.ELYTRA_FLY.register((serverPlayer, distanceInCm) -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.ELYTRA_FLY)
                        .withData(IActionDataType.DISTANCE_IN_CM, distanceInCm)
                        .withData(IActionDataType.WORLD, serverPlayer.level())
                        .withData(IActionDataType.BLOCK_POSITION, serverPlayer.blockPosition())
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);

        ArcMovementEvent.START_ELYTRA_FLY.register(serverPlayer -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.ELYTRA_FLY_START)
                        .withData(IActionDataType.WORLD, serverPlayer.level())
                        .withData(IActionDataType.BLOCK_POSITION, serverPlayer.blockPosition())
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);

        ArcMovementEvent.STOP_ELYTRA_FLY.register(serverPlayer -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.ELYTRA_FLY_STOP)
                        .withData(IActionDataType.WORLD, serverPlayer.level())
                        .withData(IActionDataType.BLOCK_POSITION, serverPlayer.blockPosition())
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);

        ArcMovementEvent.HORSE_RIDE.register((serverPlayer, distanceInCm) -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.HORSE_RIDE)
                        .withData(IActionDataType.DISTANCE_IN_CM, distanceInCm)
                        .withData(IActionDataType.WORLD, serverPlayer.level())
                        .withData(IActionDataType.BLOCK_POSITION, serverPlayer.blockPosition())
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);

        ArcMovementEvent.START_HORSE_RIDE.register(serverPlayer -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.HORSE_RIDE_START)
                        .withData(IActionDataType.WORLD, serverPlayer.level())
                        .withData(IActionDataType.BLOCK_POSITION, serverPlayer.blockPosition())
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);

        ArcMovementEvent.STOP_HORSE_RIDE.register(serverPlayer -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.HORSE_RIDE_STOP)
                        .withData(IActionDataType.WORLD, serverPlayer.level())
                        .withData(IActionDataType.BLOCK_POSITION, serverPlayer.blockPosition())
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);
    }
}
