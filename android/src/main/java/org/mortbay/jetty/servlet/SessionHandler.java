package org.mortbay.jetty.servlet;

import java.io.IOException;
import java.util.EventListener;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.mortbay.jetty.HttpConnection;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.RetryRequest;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.SessionManager;
import org.mortbay.jetty.handler.HandlerWrapper;
import org.mortbay.log.Log;

public class SessionHandler extends HandlerWrapper {
    private SessionManager _sessionManager;

    public SessionHandler() {
        this(new HashSessionManager());
    }

    public SessionHandler(SessionManager sessionManager) {
        setSessionManager(sessionManager);
    }

    public void addEventListener(EventListener eventListener) {
        if (this._sessionManager != null) {
            this._sessionManager.addEventListener(eventListener);
        }
    }

    public void clearEventListeners() {
        if (this._sessionManager != null) {
            this._sessionManager.clearEventListeners();
        }
    }

    protected void doStart() throws Exception {
        this._sessionManager.start();
        super.doStart();
    }

    protected void doStop() throws Exception {
        super.doStop();
        this._sessionManager.stop();
    }

    public SessionManager getSessionManager() {
        return this._sessionManager;
    }

    public void handle(String str, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, int i) throws IOException, ServletException {
        SessionManager sessionManager;
        RetryRequest e;
        HttpSession httpSession;
        SessionManager sessionManager2;
        HttpSession session;
        Throwable th;
        Throwable th2;
        Object obj = null;
        setRequestedId(httpServletRequest, i);
        Request request = httpServletRequest instanceof Request ? (Request) httpServletRequest : HttpConnection.getCurrentConnection().getRequest();
        HttpSession session2;
        try {
            sessionManager = request.getSessionManager();
            try {
                session2 = request.getSession(false);
                try {
                    if (sessionManager != this._sessionManager) {
                        request.setSessionManager(this._sessionManager);
                        request.setSession(null);
                    }
                    if (this._sessionManager != null) {
                        obj = request.getSession(false);
                        if (obj == null) {
                            obj = request.recoverNewSession(this._sessionManager);
                            if (obj != null) {
                                request.setSession(obj);
                            }
                        } else if (obj != session2) {
                            Cookie access = this._sessionManager.access(obj, httpServletRequest.isSecure());
                            if (access != null) {
                                httpServletResponse.addCookie(access);
                            }
                        }
                    }
                    if (Log.isDebugEnabled()) {
                        Log.debug(new StringBuffer().append("sessionManager=").append(this._sessionManager).toString());
                        Log.debug(new StringBuffer().append("session=").append(obj).toString());
                    }
                    getHandler().handle(str, httpServletRequest, httpServletResponse, i);
                    HttpSession session3 = httpServletRequest.getSession(false);
                    if (sessionManager != this._sessionManager) {
                        if (session3 != null) {
                            this._sessionManager.complete(session3);
                        }
                        if (sessionManager != null) {
                            request.setSessionManager(sessionManager);
                            request.setSession(session2);
                        }
                    }
                } catch (RetryRequest e2) {
                    e = e2;
                    SessionManager sessionManager3 = sessionManager;
                    httpSession = session2;
                    sessionManager2 = sessionManager3;
                    try {
                        session = request.getSession(false);
                        if (session != null && session.isNew()) {
                            request.saveNewSession(this._sessionManager, session);
                        }
                        throw e;
                    } catch (Throwable th3) {
                        th = th3;
                        sessionManager3 = sessionManager2;
                        session2 = httpSession;
                        sessionManager = sessionManager3;
                        session = httpServletRequest.getSession(false);
                        if (sessionManager != this._sessionManager) {
                            if (session != null) {
                                this._sessionManager.complete(session);
                            }
                            if (sessionManager != null) {
                                request.setSessionManager(sessionManager);
                                request.setSession(session2);
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    session = httpServletRequest.getSession(false);
                    if (sessionManager != this._sessionManager) {
                        if (session != null) {
                            this._sessionManager.complete(session);
                        }
                        if (sessionManager != null) {
                            request.setSessionManager(sessionManager);
                            request.setSession(session2);
                        }
                    }
                    throw th;
                }
            } catch (RetryRequest e3) {
                RetryRequest retryRequest = e3;
                sessionManager2 = sessionManager;
                httpSession = null;
                e = retryRequest;
                session = request.getSession(false);
                request.saveNewSession(this._sessionManager, session);
                throw e;
            } catch (Throwable th5) {
                th2 = th5;
                session2 = null;
                th = th2;
                session = httpServletRequest.getSession(false);
                if (sessionManager != this._sessionManager) {
                    if (session != null) {
                        this._sessionManager.complete(session);
                    }
                    if (sessionManager != null) {
                        request.setSessionManager(sessionManager);
                        request.setSession(session2);
                    }
                }
                throw th;
            }
        } catch (RetryRequest e32) {
            httpSession = null;
            e = e32;
            sessionManager2 = null;
            session = request.getSession(false);
            request.saveNewSession(this._sessionManager, session);
            throw e;
        } catch (Throwable th52) {
            sessionManager = null;
            th2 = th52;
            session2 = null;
            th = th2;
            session = httpServletRequest.getSession(false);
            if (sessionManager != this._sessionManager) {
                if (session != null) {
                    this._sessionManager.complete(session);
                }
                if (sessionManager != null) {
                    request.setSessionManager(sessionManager);
                    request.setSession(session2);
                }
            }
            throw th;
        }
    }

    protected void setRequestedId(HttpServletRequest httpServletRequest, int i) {
        HttpSession httpSession = null;
        boolean z = false;
        Request request = httpServletRequest instanceof Request ? (Request) httpServletRequest : HttpConnection.getCurrentConnection().getRequest();
        String requestedSessionId = httpServletRequest.getRequestedSessionId();
        if (i == 1 && requestedSessionId == null) {
            HttpSession httpSession2;
            boolean z2;
            String requestURI;
            String sessionURLPrefix;
            int indexOf;
            int length;
            int i2;
            char charAt;
            SessionManager sessionManager = getSessionManager();
            if (this._sessionManager.isUsingCookies()) {
                Cookie[] cookies = httpServletRequest.getCookies();
                if (cookies != null && cookies.length > 0) {
                    boolean z3 = false;
                    for (int i3 = 0; i3 < cookies.length; i3++) {
                        if (sessionManager.getSessionCookie().equalsIgnoreCase(cookies[i3].getName())) {
                            if (requestedSessionId != null && sessionManager.getHttpSession(requestedSessionId) != null) {
                                httpSession2 = httpSession;
                                z2 = z3;
                                break;
                            }
                            requestedSessionId = cookies[i3].getValue();
                            if (Log.isDebugEnabled()) {
                                Log.debug(new StringBuffer().append("Got Session ID ").append(requestedSessionId).append(" from cookie").toString());
                            }
                            httpSession = sessionManager.getHttpSession(requestedSessionId);
                            if (httpSession != null) {
                                request.setSession(httpSession);
                                z3 = true;
                            } else {
                                z3 = true;
                            }
                        }
                    }
                    httpSession2 = httpSession;
                    z2 = z3;
                    if (requestedSessionId == null || r3 == null) {
                        requestURI = httpServletRequest.getRequestURI();
                        sessionURLPrefix = sessionManager.getSessionURLPrefix();
                        if (sessionURLPrefix != null) {
                            indexOf = requestURI.indexOf(sessionURLPrefix);
                            if (indexOf >= 0) {
                                length = indexOf + sessionURLPrefix.length();
                                i2 = length;
                                while (i2 < requestURI.length()) {
                                    charAt = requestURI.charAt(i2);
                                    if (charAt == ';' || charAt == '#' || charAt == '?' || charAt == '/') {
                                        break;
                                    }
                                    i2++;
                                }
                                requestedSessionId = requestURI.substring(length, i2);
                                if (Log.isDebugEnabled()) {
                                    z2 = false;
                                } else {
                                    Log.debug(new StringBuffer().append("Got Session ID ").append(requestedSessionId).append(" from URL").toString());
                                    z2 = false;
                                }
                            }
                        }
                    }
                    request.setRequestedSessionId(requestedSessionId);
                    if (requestedSessionId != null && r2) {
                        z = true;
                    }
                    request.setRequestedSessionIdFromCookie(z);
                }
            }
            httpSession2 = null;
            z2 = false;
            requestURI = httpServletRequest.getRequestURI();
            sessionURLPrefix = sessionManager.getSessionURLPrefix();
            if (sessionURLPrefix != null) {
                indexOf = requestURI.indexOf(sessionURLPrefix);
                if (indexOf >= 0) {
                    length = indexOf + sessionURLPrefix.length();
                    i2 = length;
                    while (i2 < requestURI.length()) {
                        charAt = requestURI.charAt(i2);
                        i2++;
                    }
                    requestedSessionId = requestURI.substring(length, i2);
                    if (Log.isDebugEnabled()) {
                        z2 = false;
                    } else {
                        Log.debug(new StringBuffer().append("Got Session ID ").append(requestedSessionId).append(" from URL").toString());
                        z2 = false;
                    }
                }
            }
            request.setRequestedSessionId(requestedSessionId);
            z = true;
            request.setRequestedSessionIdFromCookie(z);
        }
    }

    public void setServer(Server server) {
        Server server2 = getServer();
        if (!(server2 == null || server2 == server)) {
            server2.getContainer().update((Object) this, this._sessionManager, null, "sessionManager", true);
        }
        super.setServer(server);
        if (server != null && server != server2) {
            server.getContainer().update((Object) this, null, this._sessionManager, "sessionManager", true);
        }
    }

    public void setSessionManager(SessionManager sessionManager) {
        if (isStarted()) {
            throw new IllegalStateException();
        }
        Object obj = this._sessionManager;
        if (getServer() != null) {
            getServer().getContainer().update((Object) this, obj, (Object) sessionManager, "sessionManager", true);
        }
        if (sessionManager != null) {
            sessionManager.setSessionHandler(this);
        }
        this._sessionManager = sessionManager;
        if (obj != null) {
            obj.setSessionHandler(null);
        }
    }
}
