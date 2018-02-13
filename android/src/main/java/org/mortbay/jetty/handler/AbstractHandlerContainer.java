package org.mortbay.jetty.handler;

import org.mortbay.jetty.Handler;
import org.mortbay.jetty.HandlerContainer;
import org.mortbay.util.LazyList;

public abstract class AbstractHandlerContainer extends AbstractHandler implements HandlerContainer {
    static Class class$org$mortbay$jetty$Handler;

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (Throwable e) {
            throw new NoClassDefFoundError().initCause(e);
        }
    }

    protected Object expandChildren(Object obj, Class cls) {
        return obj;
    }

    protected Object expandHandler(Handler handler, Object obj, Class cls) {
        if (handler == null) {
            return obj;
        }
        if (handler != null && (cls == null || cls.isAssignableFrom(handler.getClass()))) {
            obj = LazyList.add(obj, handler);
        }
        if (handler instanceof AbstractHandlerContainer) {
            return ((AbstractHandlerContainer) handler).expandChildren(obj, cls);
        }
        if (!(handler instanceof HandlerContainer)) {
            return obj;
        }
        HandlerContainer handlerContainer = (HandlerContainer) handler;
        return LazyList.addArray(obj, cls == null ? handlerContainer.getChildHandlers() : handlerContainer.getChildHandlersByClass(cls));
    }

    public Handler getChildHandlerByClass(Class cls) {
        Object expandChildren = expandChildren(null, cls);
        return expandChildren == null ? null : (Handler) LazyList.get(expandChildren, 0);
    }

    public Handler[] getChildHandlers() {
        Class class$;
        Object expandChildren = expandChildren(null, null);
        if (class$org$mortbay$jetty$Handler == null) {
            class$ = class$("org.mortbay.jetty.Handler");
            class$org$mortbay$jetty$Handler = class$;
        } else {
            class$ = class$org$mortbay$jetty$Handler;
        }
        return (Handler[]) LazyList.toArray(expandChildren, class$);
    }

    public Handler[] getChildHandlersByClass(Class cls) {
        Class class$;
        Object expandChildren = expandChildren(null, cls);
        if (class$org$mortbay$jetty$Handler == null) {
            class$ = class$("org.mortbay.jetty.Handler");
            class$org$mortbay$jetty$Handler = class$;
        } else {
            class$ = class$org$mortbay$jetty$Handler;
        }
        return (Handler[]) LazyList.toArray(expandChildren, class$);
    }
}
