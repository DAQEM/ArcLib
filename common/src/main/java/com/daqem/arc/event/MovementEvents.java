package com.daqem.arc.event;

import com.daqem.arc.api.action.IActionType;
import com.daqem.arc.api.player.ArcServerPlayer;
import com.daqem.arc.api.action.data.ActionDataBuilder;
import com.daqem.arc.api.action.data.IActionDataType;

public class MovementEvents {

    public static void onSwim(ArcServerPlayer player, int swimmingDistanceInCm) {
        new ActionDataBuilder(player, IActionType.SWIM)
                .withData(IActionDataType.DISTANCE_IN_CM, swimmingDistanceInCm)
                .build()
                .sendToAction();
    }

    public static void onStartSwimming(ArcServerPlayer player) {
        new ActionDataBuilder(player, IActionType.SWIM_START)
                .build()
                .sendToAction();
    }

    public static void onStopSwimming(ArcServerPlayer player) {
        new ActionDataBuilder(player, IActionType.SWIM_STOP)
                .build()
                .sendToAction();
    }

    public static void onWalk(ArcServerPlayer player, int walkingDistanceInCm) {
        new ActionDataBuilder(player, IActionType.WALK)
                .withData(IActionDataType.DISTANCE_IN_CM, walkingDistanceInCm)
                .build()
                .sendToAction();
    }

    public static void onStopWalking(ArcServerPlayer player) {
        new ActionDataBuilder(player, IActionType.WALK_STOP)
                .build()
                .sendToAction();
    }

    public static void onStartWalking(ArcServerPlayer player) {
        new ActionDataBuilder(player, IActionType.WALK_START)
                .build()
                .sendToAction();
    }

    public static void onSprint(ArcServerPlayer player, int sprintingDistanceInCm) {
        new ActionDataBuilder(player, IActionType.SPRINT)
                .withData(IActionDataType.DISTANCE_IN_CM, sprintingDistanceInCm)
                .build()
                .sendToAction();
    }

    public static void onStopSprinting(ArcServerPlayer player) {
        new ActionDataBuilder(player, IActionType.SPRINT_STOP)
                .build()
                .sendToAction();
    }

    public static void onStartSprinting(ArcServerPlayer player) {
        new ActionDataBuilder(player, IActionType.SPRINT_START)
                .build()
                .sendToAction();
    }

    public static void onCrouch(ArcServerPlayer player, int crouchDistanceInCm) {
        new ActionDataBuilder(player, IActionType.CROUCH)
                .withData(IActionDataType.DISTANCE_IN_CM, crouchDistanceInCm)
                .build()
                .sendToAction();
    }

    public static void onStopCrouching(ArcServerPlayer player) {
        new ActionDataBuilder(player, IActionType.CROUCH_STOP)
                .build()
                .sendToAction();
    }

    public static void onStartCrouching(ArcServerPlayer player) {
        new ActionDataBuilder(player, IActionType.CROUCH_START)
                .build()
                .sendToAction();
    }

    public static void onElytraFly(ArcServerPlayer player, int flyingDistanceInCm) {
        new ActionDataBuilder(player, IActionType.ELYTRA_FLY)
                .withData(IActionDataType.DISTANCE_IN_CM, flyingDistanceInCm)
                .build()
                .sendToAction();
    }

    public static void onStopElytraFlying(ArcServerPlayer player) {
        new ActionDataBuilder(player, IActionType.ELYTRA_FLY_STOP)
                .build()
                .sendToAction();
    }

    public static void onStartElytraFlying(ArcServerPlayer player) {
        new ActionDataBuilder(player, IActionType.ELYTRA_FLY_START)
                .build()
                .sendToAction();
    }

    public static void onHorseRide(ArcServerPlayer player, int ridingDistanceInCm) {
        new ActionDataBuilder(player, IActionType.HORSE_RIDE)
                .withData(IActionDataType.DISTANCE_IN_CM, ridingDistanceInCm)
                .build()
                .sendToAction();
    }

    public static void onStopHorseRiding(ArcServerPlayer player) {
        new ActionDataBuilder(player, IActionType.HORSE_RIDE_STOP)
                .build()
                .sendToAction();
    }

    public static void onStartHorseRiding(ArcServerPlayer player) {
        new ActionDataBuilder(player, IActionType.HORSE_RIDE_START)
                .build()
                .sendToAction();
    }
}
