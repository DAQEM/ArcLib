package com.daqem.arc.event;

import dev.architectury.event.events.common.TickEvent;

import java.util.concurrent.ConcurrentLinkedQueue;

public class NextTickScheduler {

    private static final ConcurrentLinkedQueue<Runnable> tasks = new ConcurrentLinkedQueue<>();

    public static void schedule(Runnable task) {
        tasks.add(task);
    }

    public static void registerEvent() {
        TickEvent.SERVER_PRE.register(server -> {
            while (!tasks.isEmpty()) {
                tasks.poll().run();
            }
        });
    }
}
