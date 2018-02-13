package com.google.common.eventbus;

import com.google.common.annotations.Beta;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;

@Beta
public class AsyncEventBus extends EventBus {
    private final ConcurrentLinkedQueue<EventWithHandler> eventsToDispatch = new ConcurrentLinkedQueue();
    private final Executor executor;

    public AsyncEventBus(String str, Executor executor) {
        super(str);
        this.executor = executor;
    }

    public AsyncEventBus(Executor executor) {
        this.executor = executor;
    }

    void dispatch(final Object obj, final EventHandler eventHandler) {
        this.executor.execute(new Runnable() {
            public void run() {
                super.dispatch(obj, eventHandler);
            }
        });
    }

    protected void dispatchQueuedEvents() {
        while (true) {
            EventWithHandler eventWithHandler = (EventWithHandler) this.eventsToDispatch.poll();
            if (eventWithHandler != null) {
                dispatch(eventWithHandler.event, eventWithHandler.handler);
            } else {
                return;
            }
        }
    }

    void enqueueEvent(Object obj, EventHandler eventHandler) {
        this.eventsToDispatch.offer(new EventWithHandler(obj, eventHandler));
    }
}
