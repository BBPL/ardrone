package org.mortbay.jetty;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.mortbay.component.LifeCycle;

public interface SessionIdManager extends LifeCycle {
    void addSession(HttpSession httpSession);

    String getClusterId(String str);

    String getNodeId(String str, HttpServletRequest httpServletRequest);

    String getWorkerName();

    boolean idInUse(String str);

    void invalidateAll(String str);

    String newSessionId(HttpServletRequest httpServletRequest, long j);

    void removeSession(HttpSession httpSession);
}
