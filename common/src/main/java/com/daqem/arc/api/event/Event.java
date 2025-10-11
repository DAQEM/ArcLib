package com.daqem.arc.api.event;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public final class Event<T> {

    private final Class<T> type;
    private final Function<T[], T> invokerFactory;
    private final List<Listener<T>> listeners = new ArrayList<>();
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
        listeners.sort(Comparator.comparing(Listener::priority));
        updateInvoker();
    }

    @SuppressWarnings("unchecked")
    private void updateInvoker() {
        T[] listenerArray = (T[]) Array.newInstance(type, listeners.size());
        for (int i = 0; i < listeners.size(); i++) {
            listenerArray[i] = listeners.get(i).listener();
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
