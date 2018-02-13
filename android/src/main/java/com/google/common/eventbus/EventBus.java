package com.google.common.eventbus;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
import com.google.common.reflect.TypeToken;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mortbay.jetty.servlet.ServletHandler;

@Beta
public class EventBus {
    private final ThreadLocal<ConcurrentLinkedQueue<EventWithHandler>> eventsToDispatch;
    private final HandlerFindingStrategy finder;
    private final LoadingCache<Class<?>, Set<Class<?>>> flattenHierarchyCache;
    private final SetMultimap<Class<?>, EventHandler> handlersByType;
    private final ThreadLocal<Boolean> isDispatching;
    private final Logger logger;

    class C07901 implements Supplier<Set<EventHandler>> {
        C07901() {
        }

        public Set<EventHandler> get() {
            return EventBus.this.newHandlerSet();
        }
    }

    class C07912 extends ThreadLocal<ConcurrentLinkedQueue<EventWithHandler>> {
        C07912() {
        }

        protected ConcurrentLinkedQueue<EventWithHandler> initialValue() {
            return new ConcurrentLinkedQueue();
        }
    }

    class C07923 extends ThreadLocal<Boolean> {
        C07923() {
        }

        protected Boolean initialValue() {
            return Boolean.valueOf(false);
        }
    }

    class C07934 extends CacheLoader<Class<?>, Set<Class<?>>> {
        C07934() {
        }

        public Set<Class<?>> load(Class<?> cls) throws Exception {
            return TypeToken.of((Class) cls).getTypes().rawTypes();
        }
    }

    static class EventWithHandler {
        final Object event;
        final EventHandler handler;

        public EventWithHandler(Object obj, EventHandler eventHandler) {
            this.event = obj;
            this.handler = eventHandler;
        }
    }

    public EventBus() {
        this(ServletHandler.__DEFAULT_SERVLET);
    }

    public EventBus(String str) {
        this.handlersByType = Multimaps.newSetMultimap(new ConcurrentHashMap(), new C07901());
        this.finder = new AnnotatedHandlerFinder();
        this.eventsToDispatch = new C07912();
        this.isDispatching = new C07923();
        this.flattenHierarchyCache = CacheBuilder.newBuilder().weakKeys().build(new C07934());
        this.logger = Logger.getLogger(EventBus.class.getName() + "." + str);
    }

    void dispatch(Object obj, EventHandler eventHandler) {
        try {
            eventHandler.handleEvent(obj);
        } catch (Throwable e) {
            this.logger.log(Level.SEVERE, "Could not dispatch event: " + obj + " to handler " + eventHandler, e);
        }
    }

    @Deprecated
    protected void dispatchQueuedEvents() {
        if (!((Boolean) this.isDispatching.get()).booleanValue()) {
            this.isDispatching.set(Boolean.valueOf(true));
            while (true) {
                EventWithHandler eventWithHandler = (EventWithHandler) ((ConcurrentLinkedQueue) this.eventsToDispatch.get()).poll();
                if (eventWithHandler == null) {
                    break;
                }
                try {
                    dispatch(eventWithHandler.event, eventWithHandler.handler);
                } finally {
                    this.isDispatching.set(Boolean.valueOf(false));
                }
            }
        }
    }

    void enqueueEvent(Object obj, EventHandler eventHandler) {
        ((ConcurrentLinkedQueue) this.eventsToDispatch.get()).offer(new EventWithHandler(obj, eventHandler));
    }

    @VisibleForTesting
    Set<Class<?>> flattenHierarchy(Class<?> cls) {
        try {
            return (Set) this.flattenHierarchyCache.get(cls);
        } catch (ExecutionException e) {
            throw Throwables.propagate(e.getCause());
        }
    }

    Set<EventHandler> getHandlersForEventType(Class<?> cls) {
        return this.handlersByType.get(cls);
    }

    Set<EventHandler> newHandlerSet() {
        return new CopyOnWriteArraySet();
    }

    public void post(Object obj) {
        Object obj2 = null;
        for (Class handlersForEventType : flattenHierarchy(obj.getClass())) {
            Set<EventHandler> handlersForEventType2 = getHandlersForEventType(handlersForEventType);
            if (!(handlersForEventType2 == null || handlersForEventType2.isEmpty())) {
                obj2 = 1;
                for (EventHandler enqueueEvent : handlersForEventType2) {
                    enqueueEvent(obj, enqueueEvent);
                }
            }
        }
        if (obj2 == null && !(obj instanceof DeadEvent)) {
            post(new DeadEvent(this, obj));
        }
        dispatchQueuedEvents();
    }

    public void register(Object obj) {
        this.handlersByType.putAll(this.finder.findAllHandlers(obj));
    }

    public void unregister(Object obj) {
        for (Entry entry : this.finder.findAllHandlers(obj).asMap().entrySet()) {
            Set handlersForEventType = getHandlersForEventType((Class) entry.getKey());
            Collection collection = (Collection) entry.getValue();
            if (handlersForEventType == null || !handlersForEventType.containsAll((Collection) entry.getValue())) {
                throw new IllegalArgumentException("missing event handler for an annotated method. Is " + obj + " registered?");
            }
            handlersForEventType.removeAll(collection);
        }
    }
}
