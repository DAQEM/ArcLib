package com.daqem.arc.event;

import com.daqem.arc.api.action.IActionType;
import com.daqem.arc.api.action.data.ActionDataBuilder;
import com.daqem.arc.api.action.data.IActionDataType;
import com.daqem.arc.api.player.ArcServerPlayer;
import com.daqem.knot.Knot;
import com.daqem.knot.events.EventPriority;

public class MovementEvents {

    public static void registerEvents() {
        Knot.Events.Movement.WALK.register((serverPlayer, distanceInCm) -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.WALK)
                        .withData(IActionDataType.DISTANCE_IN_CM, distanceInCm)
                        .withData(IActionDataType.WORLD, serverPlayer.level())
                        .withData(IActionDataType.BLOCK_POSITION, serverPlayer.blockPosition())
                        .withData(IActionDataType.BLOCK_STATE, serverPlayer.level().getBlockState(serverPlayer.blockPosition().below()))
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);

        Knot.Events.Movement.START_WALK.register(serverPlayer -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.WALK_START)
                        .withData(IActionDataType.WORLD, serverPlayer.level())
                        .withData(IActionDataType.BLOCK_POSITION, serverPlayer.blockPosition())
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);

        Knot.Events.Movement.STOP_WALK.register(serverPlayer -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.WALK_STOP)
                        .withData(IActionDataType.WORLD, serverPlayer.level())
                        .withData(IActionDataType.BLOCK_POSITION, serverPlayer.blockPosition())
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);

        Knot.Events.Movement.SPRINT.register((serverPlayer, distanceInCm) -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.SPRINT)
                        .withData(IActionDataType.DISTANCE_IN_CM, distanceInCm)
                        .withData(IActionDataType.WORLD, serverPlayer.level())
                        .withData(IActionDataType.BLOCK_POSITION, serverPlayer.blockPosition())
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);

        Knot.Events.Movement.START_SPRINT.register(serverPlayer -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.SPRINT_START)
                        .withData(IActionDataType.WORLD, serverPlayer.level())
                        .withData(IActionDataType.BLOCK_POSITION, serverPlayer.blockPosition())
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);

        Knot.Events.Movement.STOP_SPRINT.register(serverPlayer -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.SPRINT_STOP)
                        .withData(IActionDataType.WORLD, serverPlayer.level())
                        .withData(IActionDataType.BLOCK_POSITION, serverPlayer.blockPosition())
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);

        Knot.Events.Movement.SWIM.register((serverPlayer, distanceInCm) -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.SWIM)
                        .withData(IActionDataType.DISTANCE_IN_CM, distanceInCm)
                        .withData(IActionDataType.WORLD, serverPlayer.level())
                        .withData(IActionDataType.BLOCK_POSITION, serverPlayer.blockPosition())
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);

        Knot.Events.Movement.START_SWIM.register(serverPlayer -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.SWIM_START)
                        .withData(IActionDataType.WORLD, serverPlayer.level())
                        .withData(IActionDataType.BLOCK_POSITION, serverPlayer.blockPosition())
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);

        Knot.Events.Movement.STOP_SWIM.register(serverPlayer -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.SWIM_STOP)
                        .withData(IActionDataType.WORLD, serverPlayer.level())
                        .withData(IActionDataType.BLOCK_POSITION, serverPlayer.blockPosition())
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);

        Knot.Events.Movement.CROUCH.register((serverPlayer, distanceInCm) -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.CROUCH)
                        .withData(IActionDataType.DISTANCE_IN_CM, distanceInCm)
                        .withData(IActionDataType.WORLD, serverPlayer.level())
                        .withData(IActionDataType.BLOCK_POSITION, serverPlayer.blockPosition())
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);

        Knot.Events.Movement.START_CROUCH.register(serverPlayer -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.CROUCH_START)
                        .withData(IActionDataType.WORLD, serverPlayer.level())
                        .withData(IActionDataType.BLOCK_POSITION, serverPlayer.blockPosition())
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);

        Knot.Events.Movement.STOP_CROUCH.register(serverPlayer -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.CROUCH_STOP)
                        .withData(IActionDataType.WORLD, serverPlayer.level())
                        .withData(IActionDataType.BLOCK_POSITION, serverPlayer.blockPosition())
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);

        Knot.Events.Movement.ELYTRA_FLY.register((serverPlayer, distanceInCm) -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.ELYTRA_FLY)
                        .withData(IActionDataType.DISTANCE_IN_CM, distanceInCm)
                        .withData(IActionDataType.WORLD, serverPlayer.level())
                        .withData(IActionDataType.BLOCK_POSITION, serverPlayer.blockPosition())
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);

        Knot.Events.Movement.START_ELYTRA_FLY.register(serverPlayer -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.ELYTRA_FLY_START)
                        .withData(IActionDataType.WORLD, serverPlayer.level())
                        .withData(IActionDataType.BLOCK_POSITION, serverPlayer.blockPosition())
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);

        Knot.Events.Movement.STOP_ELYTRA_FLY.register(serverPlayer -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.ELYTRA_FLY_STOP)
                        .withData(IActionDataType.WORLD, serverPlayer.level())
                        .withData(IActionDataType.BLOCK_POSITION, serverPlayer.blockPosition())
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);

        Knot.Events.Movement.HORSE_RIDE.register((serverPlayer, distanceInCm) -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.HORSE_RIDE)
                        .withData(IActionDataType.DISTANCE_IN_CM, distanceInCm)
                        .withData(IActionDataType.WORLD, serverPlayer.level())
                        .withData(IActionDataType.BLOCK_POSITION, serverPlayer.blockPosition())
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);

        Knot.Events.Movement.START_HORSE_RIDE.register(serverPlayer -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.HORSE_RIDE_START)
                        .withData(IActionDataType.WORLD, serverPlayer.level())
                        .withData(IActionDataType.BLOCK_POSITION, serverPlayer.blockPosition())
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);

        Knot.Events.Movement.STOP_HORSE_RIDE.register(serverPlayer -> {
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
