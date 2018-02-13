package org.mortbay.jetty;

import java.util.EventListener;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.mortbay.component.LifeCycle;
import org.mortbay.jetty.servlet.SessionHandler;

public interface SessionManager extends LifeCycle {
    public static final String __DefaultSessionCookie = "JSESSIONID";
    public static final String __DefaultSessionDomain = null;
    public static final String __DefaultSessionURL = "jsessionid";
    public static final String __MaxAgeProperty = "org.mortbay.jetty.servlet.MaxAge";
    public static final String __SessionCookieProperty = "org.mortbay.jetty.servlet.SessionCookie";
    public static final String __SessionDomainProperty = "org.mortbay.jetty.servlet.SessionDomain";
    public static final String __SessionPathProperty = "org.mortbay.jetty.servlet.SessionPath";
    public static final String __SessionURLProperty = "org.mortbay.jetty.servlet.SessionURL";

    Cookie access(HttpSession httpSession, boolean z);

    void addEventListener(EventListener eventListener);

    void clearEventListeners();

    void complete(HttpSession httpSession);

    String getClusterId(HttpSession httpSession);

    boolean getHttpOnly();

    HttpSession getHttpSession(String str);

    SessionIdManager getIdManager();

    int getMaxCookieAge();

    int getMaxInactiveInterval();

    SessionIdManager getMetaManager();

    String getNodeId(HttpSession httpSession);

    boolean getSecureCookies();

    String getSessionCookie();

    Cookie getSessionCookie(HttpSession httpSession, String str, boolean z);

    String getSessionDomain();

    String getSessionPath();

    String getSessionURL();

    String getSessionURLPrefix();

    boolean isUsingCookies();

    boolean isValid(HttpSession httpSession);

    HttpSession newHttpSession(HttpServletRequest httpServletRequest);

    void removeEventListener(EventListener eventListener);

    void setIdManager(SessionIdManager sessionIdManager);

    void setMaxCookieAge(int i);

    void setMaxInactiveInterval(int i);

    void setSessionCookie(String str);

    void setSessionDomain(String str);

    void setSessionHandler(SessionHandler sessionHandler);

    void setSessionPath(String str);

    void setSessionURL(String str);
}
