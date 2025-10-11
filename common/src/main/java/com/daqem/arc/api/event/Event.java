package com.daqem.arc.api.event;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

public final class Event<T> {

    private final Class<T> type;
    private final Function<T[], T> invokerFactory;
    private final List<Listener<T>> listeners = new CopyOnWriteArrayList<>();
    private T invoker;

    Event(Class<T> type, Function<T[], T> invokerFactory) {
        this.type = type;
        this.invokerFactory = invokerFactory;
        updateInvoker();
    }

    public void register(T listener) {
        register(listener, EventPriority.NORMAL);
    }

    public void register(T listener, EventPriority priority) {
        listeners.add(new Listener<>(listener, priority));
        updateInvoker();
    }

    @SuppressWarnings("unchecked")
    private void updateInvoker() {
        Listener<T>[] sortedListeners = listeners.toArray(new Listener[0]);
        Arrays.sort(sortedListeners, Comparator.comparing(Listener::priority));
        T[] listenerArray = (T[]) Array.newInstance(type, sortedListeners.length);
        for (int i = 0; i < sortedListeners.length; i++) {
            listenerArray[i] = sortedListeners[i].listener();
        }
        this.invoker = invokerFactory.apply(listenerArray);
    }

    public T invoker() {
        return invoker;
    }

    public void unregister(T listener) {
        if (listeners.removeIf(l -> l.listener().equals(listener))) {
            updateInvoker();
        }
    }

    public void clear() {
        listeners.clear();
        updateInvoker();
    }

    private record Listener<T>(T listener, EventPriority priority) {
    }
}
