package com.daqem.arc.api.event;

public enum EventResult {
    PASS(false, null),
    INTERRUPT(true, null),
    INTERRUPT_TRUE(true, true),
    INTERRUPT_FALSE(true, false);

    private final boolean interrupts;
    private final Boolean value;

    EventResult(boolean interrupts, Boolean value) {
        this.interrupts = interrupts;
        this.value = value;
    }

    public boolean interrupts() {
        return interrupts;
    }

    public boolean cancelsEvent() {
        return Boolean.FALSE.equals(value);
    }

    public dev.architectury.event.EventResult toArchEventResult() {
        if (this == PASS) {
            return dev.architectury.event.EventResult.pass();
        } else if (this == INTERRUPT) {
            return dev.architectury.event.EventResult.interruptDefault();
        } else if (this == INTERRUPT_TRUE) {
            return dev.architectury.event.EventResult.interrupt(true);
        } else if (this == INTERRUPT_FALSE) {
            return dev.architectury.event.EventResult.interrupt(false);
        }
        return interrupts ? dev.architectury.event.EventResult.interrupt(value) : dev.architectury.event.EventResult.pass();
    }
}
