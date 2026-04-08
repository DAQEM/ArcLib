package com.daqem.arc.api.event;

import net.minecraft.server.level.ServerPlayer;

public interface ArcMovementEvent {

    Event<Walk> WALK = EventFactory.createLoop(Walk.class);
    Event<StartWalk> START_WALK = EventFactory.createLoop(StartWalk.class);
    Event<StopWalk> STOP_WALK = EventFactory.createLoop(StopWalk.class);

    Event<Sprint> SPRINT = EventFactory.createLoop(Sprint.class);
    Event<StartSprint> START_SPRINT = EventFactory.createLoop(StartSprint.class);
    Event<StopSprint> STOP_SPRINT = EventFactory.createLoop(StopSprint.class);

    Event<Swim> SWIM = EventFactory.createLoop(Swim.class);
    Event<StartSwim> START_SWIM = EventFactory.createLoop(StartSwim.class);
    Event<StopSwim> STOP_SWIM = EventFactory.createLoop(StopSwim.class);

    Event<Crouch> CROUCH = EventFactory.createLoop(Crouch.class);
    Event<StartCrouch> START_CROUCH = EventFactory.createLoop(StartCrouch.class);
    Event<StopCrouch> STOP_CROUCH = EventFactory.createLoop(StopCrouch.class);

    Event<ElytraFly> ELYTRA_FLY = EventFactory.createLoop(ElytraFly.class);
    Event<StartElytraFly> START_ELYTRA_FLY = EventFactory.createLoop(StartElytraFly.class);
    Event<StopElytraFly> STOP_ELYTRA_FLY = EventFactory.createLoop(StopElytraFly.class);

    Event<HorseRide> HORSE_RIDE = EventFactory.createLoop(HorseRide.class);
    Event<StartHorseRide> START_HORSE_RIDE = EventFactory.createLoop(StartHorseRide.class);
    Event<StopHorseRide> STOP_HORSE_RIDE = EventFactory.createLoop(StopHorseRide.class);

    interface Walk {
        void onWalk(ServerPlayer serverPlayer, double distanceInCm);
    }

    interface StartWalk {
        void onStartWalk(ServerPlayer serverPlayer);
    }

    interface StopWalk {
        void onStopWalk(ServerPlayer serverPlayer);
    }

    interface Sprint {
        void onSprint(ServerPlayer serverPlayer, double distanceInCm);
    }

    interface StartSprint {
        void onStartSprint(ServerPlayer serverPlayer);
    }

    interface StopSprint {
        void onStopSprint(ServerPlayer serverPlayer);
    }

    interface Swim {
        void onSwim(ServerPlayer serverPlayer, double distanceInCm);
    }

    interface StartSwim {
        void onStartSwim(ServerPlayer serverPlayer);
    }

    interface StopSwim {
        void onStopSwim(ServerPlayer serverPlayer);
    }

    interface Crouch {
        void onCrouch(ServerPlayer serverPlayer, double distanceInCm);
    }

    interface StartCrouch {
        void onStartCrouch(ServerPlayer serverPlayer);
    }

    interface StopCrouch {
        void onStopCrouch(ServerPlayer serverPlayer);
    }

    interface ElytraFly {
        void onElytraFly(ServerPlayer serverPlayer, double distanceInCm);
    }

    interface StartElytraFly {
        void onStartElytraFly(ServerPlayer serverPlayer);
    }

    interface StopElytraFly {
        void onStopElytraFly(ServerPlayer serverPlayer);
    }

    interface HorseRide {
        void onHorseRide(ServerPlayer serverPlayer, double distanceInCm);
    }

    interface StartHorseRide {
        void onStartHorseRide(ServerPlayer serverPlayer);
    }

    interface StopHorseRide {
        void onStopHorseRide(ServerPlayer serverPlayer);
    }
}
