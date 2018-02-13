package org.mortbay.jetty;

import org.mortbay.component.LifeCycle;

public interface RequestLog extends LifeCycle {
    void log(Request request, Response response);
}
