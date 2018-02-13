package com.google.common.eventbus;

import com.google.common.annotations.Beta;

@Beta
public class DeadEvent {
    private final Object event;
    private final Object source;

    public DeadEvent(Object obj, Object obj2) {
        this.source = obj;
        this.event = obj2;
    }

    public Object getEvent() {
        return this.event;
    }

    public Object getSource() {
        return this.source;
    }
}
