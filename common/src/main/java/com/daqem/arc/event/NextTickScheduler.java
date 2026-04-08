package com.daqem.arc.event;

import com.daqem.knot.Knot;

import java.util.concurrent.ConcurrentLinkedQueue;

public class NextTickScheduler {

    private static final ConcurrentLinkedQueue<Runnable> tasks = new ConcurrentLinkedQueue<>();

    public static void schedule(Runnable task) {
        tasks.add(task);
    }

    public static void registerEvent() {
        Knot.Events.Tick.SERVER_PRE.register(_ -> {
            while (!tasks.isEmpty()) {
                tasks.poll().run();
            }
        });
    }
}
