package com.daqem.arc.api.event;

import java.lang.reflect.Proxy;
import java.util.function.Function;

public final class EventFactory {

    private EventFactory() {
    }

    @SuppressWarnings("unchecked")
    private static <T> Event<T> createArrayBacked(Class<? super T> type, Function<T[], T> invokerFactory) {
        return new Event<>((Class<T>) type, invokerFactory);
    }

    @SuppressWarnings("unchecked")
    public static <T> T simpleInvoker(Class<? super T> listenerClass, T[] listeners) {
        return (T) Proxy.newProxyInstance(
                listenerClass.getClassLoader(),
                new Class[]{listenerClass},
                (proxy, method, args) -> {
                    for (T listener : listeners) {
                        method.invoke(listener, args);
                    }
                    Class<?> returnType = method.getReturnType();
                    if (returnType.isPrimitive()) {
                        if (returnType == boolean.class) return false;
                        return 0;
                    }
                    return null;
                });
    }

    @SuppressWarnings("unchecked")
    public static <T> T eventResultInvoker(Class<? super T> listenerClass, T[] listeners) {
        return (T) Proxy.newProxyInstance(
                listenerClass.getClassLoader(),
                new Class[]{listenerClass},
                (proxy, method, args) -> {
                    for (T listener : listeners) {
                        EventResult result = (EventResult) method.invoke(listener, args);
                        if (result.interrupts()) {
                            return result;
                        }
                    }
                    return EventResult.PASS;
                });
    }

    public static <T> Event<T> createLoop(Class<? super T> type) {
        return createArrayBacked(type, listeners -> simpleInvoker(type, listeners));
    }

    public static <T> Event<T> createEventResult(Class<? super T> type) {
        return createArrayBacked(type, listeners -> eventResultInvoker(type, listeners));
    }


}