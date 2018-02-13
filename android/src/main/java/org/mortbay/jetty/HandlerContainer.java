package org.mortbay.jetty;

import org.mortbay.component.LifeCycle;

public interface HandlerContainer extends LifeCycle {
    void addHandler(Handler handler);

    Handler getChildHandlerByClass(Class cls);

    Handler[] getChildHandlers();

    Handler[] getChildHandlersByClass(Class cls);

    void removeHandler(Handler handler);
}
