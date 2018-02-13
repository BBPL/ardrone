package org.mortbay.jetty.servlet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionContext;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.mortbay.component.AbstractLifeCycle;
import org.mortbay.jetty.HttpOnlyCookie;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.SessionIdManager;
import org.mortbay.jetty.SessionManager;
import org.mortbay.jetty.handler.ContextHandler;
import org.mortbay.jetty.handler.ContextHandler.SContext;
import org.mortbay.util.LazyList;
import org.mortbay.util.URIUtil;

public abstract class AbstractSessionManager extends AbstractLifeCycle implements SessionManager {
    public static final int __distantFuture = 628992000;
    private static final HttpSessionContext __nullSessionContext = new NullSessionContext(null);
    protected SContext _context;
    protected int _dftMaxIdleSecs = -1;
    protected boolean _httpOnly = false;
    protected ClassLoader _loader;
    protected int _maxCookieAge = -1;
    protected int _maxSessions = 0;
    protected int _minSessions = 0;
    protected boolean _nodeIdInSessionId;
    protected int _refreshCookieAge;
    protected boolean _secureCookies = false;
    protected Object _sessionAttributeListeners;
    protected String _sessionCookie = SessionManager.__DefaultSessionCookie;
    protected String _sessionDomain;
    protected SessionHandler _sessionHandler;
    protected SessionIdManager _sessionIdManager;
    protected Object _sessionListeners;
    protected String _sessionPath;
    protected String _sessionURL = SessionManager.__DefaultSessionURL;
    protected String _sessionURLPrefix = new StringBuffer().append(";").append(this._sessionURL).append("=").toString();
    private boolean _usingCookies = true;

    static class C13411 {
    }

    public static class NullSessionContext implements HttpSessionContext {
        private NullSessionContext() {
        }

        NullSessionContext(C13411 c13411) {
            this();
        }

        public Enumeration getIds() {
            return Collections.enumeration(Collections.EMPTY_LIST);
        }

        public HttpSession getSession(String str) {
            return null;
        }
    }

    public interface SessionIf extends HttpSession {
        Session getSession();
    }

    public abstract class Session implements SessionIf, Serializable {
        protected long _accessed;
        protected final String _clusterId;
        protected long _cookieSet;
        protected final long _created;
        protected boolean _doInvalidate;
        protected boolean _idChanged;
        protected boolean _invalid;
        protected long _lastAccessed;
        protected long _maxIdleMs;
        protected boolean _newSession;
        protected final String _nodeId;
        protected int _requests;
        protected Map _values;
        private final AbstractSessionManager this$0;

        protected Session(AbstractSessionManager abstractSessionManager, long j, String str) {
            this.this$0 = abstractSessionManager;
            this._maxIdleMs = (long) (this.this$0._dftMaxIdleSecs * 1000);
            this._created = j;
            this._clusterId = str;
            this._nodeId = abstractSessionManager._sessionIdManager.getNodeId(this._clusterId, null);
            this._accessed = this._created;
        }

        protected Session(AbstractSessionManager abstractSessionManager, HttpServletRequest httpServletRequest) {
            this.this$0 = abstractSessionManager;
            this._maxIdleMs = (long) (this.this$0._dftMaxIdleSecs * 1000);
            this._newSession = true;
            this._created = System.currentTimeMillis();
            this._clusterId = abstractSessionManager._sessionIdManager.newSessionId(httpServletRequest, this._created);
            this._nodeId = abstractSessionManager._sessionIdManager.getNodeId(this._clusterId, httpServletRequest);
            this._accessed = this._created;
            this._requests = 1;
        }

        protected void access(long j) {
            synchronized (this) {
                this._newSession = false;
                this._lastAccessed = this._accessed;
                this._accessed = j;
                this._requests++;
            }
        }

        protected void bindValue(String str, Object obj) {
            if (obj != null && (obj instanceof HttpSessionBindingListener)) {
                ((HttpSessionBindingListener) obj).valueBound(new HttpSessionBindingEvent(this, str));
            }
        }

        protected void complete() {
            synchronized (this) {
                this._requests--;
                if (this._doInvalidate && this._requests <= 0) {
                    doInvalidate();
                }
            }
        }

        protected void cookieSet() {
            this._cookieSet = this._accessed;
        }

        protected void didActivate() {
            synchronized (this) {
                HttpSessionEvent httpSessionEvent = new HttpSessionEvent(this);
                for (Object next : this._values.values()) {
                    if (next instanceof HttpSessionActivationListener) {
                        ((HttpSessionActivationListener) next).sessionDidActivate(httpSessionEvent);
                    }
                }
            }
        }

        protected void doInvalidate() throws java.lang.IllegalStateException {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxOverflowException: Regions stack size limit reached
	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:37)
	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:61)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
            /*
            r5 = this;
            r4 = 1;
            r0 = r5._invalid;	 Catch:{ all -> 0x000b }
            if (r0 == 0) goto L_0x000f;	 Catch:{ all -> 0x000b }
        L_0x0005:
            r0 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x000b }
            r0.<init>();	 Catch:{ all -> 0x000b }
            throw r0;	 Catch:{ all -> 0x000b }
        L_0x000b:
            r0 = move-exception;
            r5._invalid = r4;
            throw r0;
        L_0x000f:
            r0 = r5._values;	 Catch:{ all -> 0x000b }
            if (r0 == 0) goto L_0x0071;	 Catch:{ all -> 0x000b }
        L_0x0013:
            r0 = r5._values;	 Catch:{ all -> 0x000b }
            r0 = r0.size();	 Catch:{ all -> 0x000b }
            if (r0 <= 0) goto L_0x0071;	 Catch:{ all -> 0x000b }
        L_0x001b:
            monitor-enter(r5);	 Catch:{ all -> 0x000b }
            r0 = new java.util.ArrayList;	 Catch:{ all -> 0x006b }
            r1 = r5._values;	 Catch:{ all -> 0x006b }
            r1 = r1.keySet();	 Catch:{ all -> 0x006b }
            r0.<init>(r1);	 Catch:{ all -> 0x006b }
            monitor-exit(r5);	 Catch:{ all -> 0x006b }
            r2 = r0.iterator();	 Catch:{ all -> 0x000b }
        L_0x002c:
            r0 = r2.hasNext();	 Catch:{ all -> 0x000b }
            if (r0 == 0) goto L_0x000f;	 Catch:{ all -> 0x000b }
        L_0x0032:
            r0 = r2.next();	 Catch:{ all -> 0x000b }
            r0 = (java.lang.String) r0;	 Catch:{ all -> 0x000b }
            monitor-enter(r5);	 Catch:{ all -> 0x000b }
            r1 = r5._values;	 Catch:{ all -> 0x006e }
            r1 = r1.remove(r0);	 Catch:{ all -> 0x006e }
            monitor-exit(r5);	 Catch:{ all -> 0x006e }
            r5.unbindValue(r0, r1);	 Catch:{ all -> 0x000b }
            r3 = r5.this$0;	 Catch:{ all -> 0x000b }
            r3 = r3._sessionAttributeListeners;	 Catch:{ all -> 0x000b }
            if (r3 == 0) goto L_0x002c;	 Catch:{ all -> 0x000b }
        L_0x0049:
            r3 = new javax.servlet.http.HttpSessionBindingEvent;	 Catch:{ all -> 0x000b }
            r3.<init>(r5, r0, r1);	 Catch:{ all -> 0x000b }
            r0 = 0;	 Catch:{ all -> 0x000b }
            r1 = r0;	 Catch:{ all -> 0x000b }
        L_0x0050:
            r0 = r5.this$0;	 Catch:{ all -> 0x000b }
            r0 = r0._sessionAttributeListeners;	 Catch:{ all -> 0x000b }
            r0 = org.mortbay.util.LazyList.size(r0);	 Catch:{ all -> 0x000b }
            if (r1 >= r0) goto L_0x002c;	 Catch:{ all -> 0x000b }
        L_0x005a:
            r0 = r5.this$0;	 Catch:{ all -> 0x000b }
            r0 = r0._sessionAttributeListeners;	 Catch:{ all -> 0x000b }
            r0 = org.mortbay.util.LazyList.get(r0, r1);	 Catch:{ all -> 0x000b }
            r0 = (javax.servlet.http.HttpSessionAttributeListener) r0;	 Catch:{ all -> 0x000b }
            r0.attributeRemoved(r3);	 Catch:{ all -> 0x000b }
            r0 = r1 + 1;
            r1 = r0;
            goto L_0x0050;
        L_0x006b:
            r0 = move-exception;
            monitor-exit(r5);	 Catch:{ all -> 0x006b }
            throw r0;	 Catch:{ all -> 0x000b }
        L_0x006e:
            r0 = move-exception;
            monitor-exit(r5);	 Catch:{ all -> 0x006e }
            throw r0;	 Catch:{ all -> 0x000b }
        L_0x0071:
            r5._invalid = r4;
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.mortbay.jetty.servlet.AbstractSessionManager.Session.doInvalidate():void");
        }

        public Object getAttribute(String str) {
            Object obj;
            synchronized (this) {
                if (this._invalid) {
                    throw new IllegalStateException();
                }
                obj = this._values == null ? null : this._values.get(str);
            }
            return obj;
        }

        public Enumeration getAttributeNames() {
            Enumeration enumeration;
            synchronized (this) {
                if (this._invalid) {
                    throw new IllegalStateException();
                }
                enumeration = Collections.enumeration(this._values == null ? Collections.EMPTY_LIST : new ArrayList(this._values.keySet()));
            }
            return enumeration;
        }

        protected String getClusterId() {
            return this._clusterId;
        }

        public long getCookieSetTime() {
            return this._cookieSet;
        }

        public long getCreationTime() throws IllegalStateException {
            if (!this._invalid) {
                return this._created;
            }
            throw new IllegalStateException();
        }

        public String getId() throws IllegalStateException {
            return this.this$0._nodeIdInSessionId ? this._nodeId : this._clusterId;
        }

        public long getLastAccessedTime() throws IllegalStateException {
            if (!this._invalid) {
                return this._lastAccessed;
            }
            throw new IllegalStateException();
        }

        public int getMaxInactiveInterval() {
            if (!this._invalid) {
                return (int) (this._maxIdleMs / 1000);
            }
            throw new IllegalStateException();
        }

        protected String getNodeId() {
            return this._nodeId;
        }

        public ServletContext getServletContext() {
            return this.this$0._context;
        }

        public Session getSession() {
            return this;
        }

        public HttpSessionContext getSessionContext() throws IllegalStateException {
            if (!this._invalid) {
                return AbstractSessionManager.access$100();
            }
            throw new IllegalStateException();
        }

        public Object getValue(String str) throws IllegalStateException {
            return getAttribute(str);
        }

        public String[] getValueNames() throws IllegalStateException {
            String[] strArr;
            synchronized (this) {
                if (this._invalid) {
                    throw new IllegalStateException();
                }
                if (this._values == null) {
                    strArr = new String[0];
                } else {
                    strArr = (String[]) this._values.keySet().toArray(new String[this._values.size()]);
                }
            }
            return strArr;
        }

        protected void initValues() {
            this._values = newAttributeMap();
        }

        public void invalidate() throws IllegalStateException {
            this.this$0.removeSession(this, true);
            doInvalidate();
        }

        public boolean isIdChanged() {
            return this._idChanged;
        }

        public boolean isNew() throws IllegalStateException {
            if (!this._invalid) {
                return this._newSession;
            }
            throw new IllegalStateException();
        }

        protected boolean isValid() {
            return !this._invalid;
        }

        protected abstract Map newAttributeMap();

        public void putValue(String str, Object obj) throws IllegalStateException {
            setAttribute(str, obj);
        }

        public void removeAttribute(String str) {
            synchronized (this) {
                if (this._invalid) {
                    throw new IllegalStateException();
                }
                if (this._values != null) {
                    Object remove = this._values.remove(str);
                    if (remove != null) {
                        unbindValue(str, remove);
                        if (this.this$0._sessionAttributeListeners != null) {
                            HttpSessionBindingEvent httpSessionBindingEvent = new HttpSessionBindingEvent(this, str, remove);
                            for (int i = 0; i < LazyList.size(this.this$0._sessionAttributeListeners); i++) {
                                ((HttpSessionAttributeListener) LazyList.get(this.this$0._sessionAttributeListeners, i)).attributeRemoved(httpSessionBindingEvent);
                            }
                        }
                    }
                }
            }
        }

        public void removeValue(String str) throws IllegalStateException {
            removeAttribute(str);
        }

        public void setAttribute(String str, Object obj) {
            synchronized (this) {
                if (obj == null) {
                    removeAttribute(str);
                } else if (this._invalid) {
                    throw new IllegalStateException();
                } else {
                    if (this._values == null) {
                        this._values = newAttributeMap();
                    }
                    Object put = this._values.put(str, obj);
                    if (put == null || !obj.equals(put)) {
                        unbindValue(str, put);
                        bindValue(str, obj);
                        if (this.this$0._sessionAttributeListeners != null) {
                            HttpSessionBindingEvent httpSessionBindingEvent = new HttpSessionBindingEvent(this, str, put == null ? obj : put);
                            for (int i = 0; i < LazyList.size(this.this$0._sessionAttributeListeners); i++) {
                                HttpSessionAttributeListener httpSessionAttributeListener = (HttpSessionAttributeListener) LazyList.get(this.this$0._sessionAttributeListeners, i);
                                if (put == null) {
                                    httpSessionAttributeListener.attributeAdded(httpSessionBindingEvent);
                                } else if (obj == null) {
                                    httpSessionAttributeListener.attributeRemoved(httpSessionBindingEvent);
                                } else {
                                    httpSessionAttributeListener.attributeReplaced(httpSessionBindingEvent);
                                }
                            }
                        }
                    }
                }
            }
        }

        public void setIdChanged(boolean z) {
            this._idChanged = z;
        }

        public void setMaxInactiveInterval(int i) {
            this._maxIdleMs = ((long) i) * 1000;
        }

        protected void timeout() throws IllegalStateException {
            this.this$0.removeSession(this, true);
            synchronized (this) {
                if (!this._invalid) {
                    if (this._requests <= 0) {
                        doInvalidate();
                    } else {
                        this._doInvalidate = true;
                    }
                }
            }
        }

        public String toString() {
            return new StringBuffer().append(getClass().getName()).append(":").append(getId()).append("@").append(hashCode()).toString();
        }

        protected void unbindValue(String str, Object obj) {
            if (obj != null && (obj instanceof HttpSessionBindingListener)) {
                ((HttpSessionBindingListener) obj).valueUnbound(new HttpSessionBindingEvent(this, str));
            }
        }

        protected void willPassivate() {
            synchronized (this) {
                HttpSessionEvent httpSessionEvent = new HttpSessionEvent(this);
                for (Object next : this._values.values()) {
                    if (next instanceof HttpSessionActivationListener) {
                        ((HttpSessionActivationListener) next).sessionWillPassivate(httpSessionEvent);
                    }
                }
            }
        }
    }

    static HttpSessionContext access$100() {
        return __nullSessionContext;
    }

    public Cookie access(HttpSession httpSession, boolean z) {
        long currentTimeMillis = System.currentTimeMillis();
        Session session = ((SessionIf) httpSession).getSession();
        session.access(currentTimeMillis);
        if (!isUsingCookies() || (!session.isIdChanged() && (getMaxCookieAge() <= 0 || getRefreshCookieAge() <= 0 || (currentTimeMillis - session.getCookieSetTime()) / 1000 <= ((long) getRefreshCookieAge())))) {
            return null;
        }
        Cookie sessionCookie = getSessionCookie(httpSession, this._context.getContextPath(), z);
        session.cookieSet();
        session.setIdChanged(false);
        return sessionCookie;
    }

    public void addEventListener(EventListener eventListener) {
        if (eventListener instanceof HttpSessionAttributeListener) {
            this._sessionAttributeListeners = LazyList.add(this._sessionAttributeListeners, eventListener);
        }
        if (eventListener instanceof HttpSessionListener) {
            this._sessionListeners = LazyList.add(this._sessionListeners, eventListener);
        }
    }

    protected abstract void addSession(Session session);

    protected void addSession(Session session, boolean z) {
        synchronized (this._sessionIdManager) {
            this._sessionIdManager.addSession(session);
            synchronized (this) {
                addSession(session);
                if (getSessions() > this._maxSessions) {
                    this._maxSessions = getSessions();
                }
            }
        }
        if (!z) {
            session.didActivate();
        } else if (this._sessionListeners != null) {
            HttpSessionEvent httpSessionEvent = new HttpSessionEvent(session);
            for (int i = 0; i < LazyList.size(this._sessionListeners); i++) {
                ((HttpSessionListener) LazyList.get(this._sessionListeners, i)).sessionCreated(httpSessionEvent);
            }
        }
    }

    public void clearEventListeners() {
        this._sessionAttributeListeners = null;
        this._sessionListeners = null;
    }

    public void complete(HttpSession httpSession) {
        ((SessionIf) httpSession).getSession().complete();
    }

    public void doStart() throws Exception {
        String str = null;
        this._context = ContextHandler.getCurrentContext();
        this._loader = Thread.currentThread().getContextClassLoader();
        if (this._sessionIdManager == null) {
            Server server = getSessionHandler().getServer();
            synchronized (server) {
                this._sessionIdManager = server.getSessionIdManager();
                if (this._sessionIdManager == null) {
                    this._sessionIdManager = new HashSessionIdManager();
                    server.setSessionIdManager(this._sessionIdManager);
                }
            }
        }
        if (!this._sessionIdManager.isStarted()) {
            this._sessionIdManager.start();
        }
        if (this._context != null) {
            String initParameter = this._context.getInitParameter(SessionManager.__SessionCookieProperty);
            if (initParameter != null) {
                this._sessionCookie = initParameter;
            }
            String initParameter2 = this._context.getInitParameter(SessionManager.__SessionURLProperty);
            if (initParameter2 != null) {
                initParameter = (initParameter2 == null || "none".equals(initParameter2)) ? null : initParameter2;
                this._sessionURL = initParameter;
                if (!(initParameter2 == null || "none".equals(initParameter2))) {
                    str = new StringBuffer().append(";").append(this._sessionURL).append("=").toString();
                }
                this._sessionURLPrefix = str;
            }
            if (this._maxCookieAge == -1) {
                initParameter = this._context.getInitParameter(SessionManager.__MaxAgeProperty);
                if (initParameter != null) {
                    this._maxCookieAge = Integer.parseInt(initParameter.trim());
                }
            }
            if (this._sessionDomain == null) {
                this._sessionDomain = this._context.getInitParameter(SessionManager.__SessionDomainProperty);
            }
            if (this._sessionPath == null) {
                this._sessionPath = this._context.getInitParameter(SessionManager.__SessionPathProperty);
            }
        }
        super.doStart();
    }

    public void doStop() throws Exception {
        super.doStop();
        invalidateSessions();
        this._loader = null;
    }

    public String getClusterId(HttpSession httpSession) {
        return ((SessionIf) httpSession).getSession().getClusterId();
    }

    public boolean getHttpOnly() {
        return this._httpOnly;
    }

    public HttpSession getHttpSession(String str) {
        Session session;
        String clusterId = getIdManager().getClusterId(str);
        synchronized (this) {
            session = getSession(clusterId);
            if (!(session == null || session.getNodeId().equals(str))) {
                session.setIdChanged(true);
            }
        }
        return session;
    }

    public SessionIdManager getIdManager() {
        return this._sessionIdManager;
    }

    public int getMaxCookieAge() {
        return this._maxCookieAge;
    }

    public int getMaxInactiveInterval() {
        return this._dftMaxIdleSecs;
    }

    public int getMaxSessions() {
        return this._maxSessions;
    }

    public SessionIdManager getMetaManager() {
        return getIdManager();
    }

    public int getMinSessions() {
        return this._minSessions;
    }

    public String getNodeId(HttpSession httpSession) {
        return ((SessionIf) httpSession).getSession().getNodeId();
    }

    public int getRefreshCookieAge() {
        return this._refreshCookieAge;
    }

    public boolean getSecureCookies() {
        return this._secureCookies;
    }

    public abstract Session getSession(String str);

    public String getSessionCookie() {
        return this._sessionCookie;
    }

    public Cookie getSessionCookie(HttpSession httpSession, String str, boolean z) {
        if (!isUsingCookies()) {
            return null;
        }
        String nodeId = getNodeId(httpSession);
        Cookie httpOnlyCookie = getHttpOnly() ? new HttpOnlyCookie(this._sessionCookie, nodeId) : new Cookie(this._sessionCookie, nodeId);
        if (str == null || str.length() == 0) {
            str = URIUtil.SLASH;
        }
        httpOnlyCookie.setPath(str);
        httpOnlyCookie.setMaxAge(getMaxCookieAge());
        boolean z2 = z && getSecureCookies();
        httpOnlyCookie.setSecure(z2);
        if (this._sessionDomain != null) {
            httpOnlyCookie.setDomain(this._sessionDomain);
        }
        if (this._sessionPath != null) {
            httpOnlyCookie.setPath(this._sessionPath);
        }
        return httpOnlyCookie;
    }

    public String getSessionDomain() {
        return this._sessionDomain;
    }

    public SessionHandler getSessionHandler() {
        return this._sessionHandler;
    }

    public abstract Map getSessionMap();

    public String getSessionPath() {
        return this._sessionPath;
    }

    public String getSessionURL() {
        return this._sessionURL;
    }

    public String getSessionURLPrefix() {
        return this._sessionURLPrefix;
    }

    public abstract int getSessions();

    protected abstract void invalidateSessions();

    public boolean isNodeIdInSessionId() {
        return this._nodeIdInSessionId;
    }

    public boolean isUsingCookies() {
        return this._usingCookies;
    }

    public boolean isValid(HttpSession httpSession) {
        return ((SessionIf) httpSession).getSession().isValid();
    }

    public HttpSession newHttpSession(HttpServletRequest httpServletRequest) {
        Session newSession = newSession(httpServletRequest);
        newSession.setMaxInactiveInterval(this._dftMaxIdleSecs);
        addSession(newSession, true);
        return newSession;
    }

    protected abstract Session newSession(HttpServletRequest httpServletRequest);

    public void removeEventListener(EventListener eventListener) {
        if (eventListener instanceof HttpSessionAttributeListener) {
            this._sessionAttributeListeners = LazyList.remove(this._sessionAttributeListeners, (Object) eventListener);
        }
        if (eventListener instanceof HttpSessionListener) {
            this._sessionListeners = LazyList.remove(this._sessionListeners, (Object) eventListener);
        }
    }

    protected abstract void removeSession(String str);

    public void removeSession(HttpSession httpSession, boolean z) {
        removeSession(((SessionIf) httpSession).getSession(), z);
    }

    public void removeSession(Session session, boolean z) {
        Object obj = null;
        synchronized (this) {
            if (getSession(session.getClusterId()) != null) {
                obj = 1;
                removeSession(session.getClusterId());
            }
        }
        if (obj != null && z) {
            this._sessionIdManager.removeSession(session);
            this._sessionIdManager.invalidateAll(session.getClusterId());
        }
        if (z && this._sessionListeners != null) {
            HttpSessionEvent httpSessionEvent = new HttpSessionEvent(session);
            int size = LazyList.size(this._sessionListeners);
            while (true) {
                int i = size - 1;
                if (size <= 0) {
                    break;
                }
                ((HttpSessionListener) LazyList.get(this._sessionListeners, i)).sessionDestroyed(httpSessionEvent);
                size = i;
            }
        }
        if (!z) {
            session.willPassivate();
        }
    }

    public void resetStats() {
        this._minSessions = getSessions();
        this._maxSessions = getSessions();
    }

    public void setHttpOnly(boolean z) {
        this._httpOnly = z;
    }

    public void setIdManager(SessionIdManager sessionIdManager) {
        this._sessionIdManager = sessionIdManager;
    }

    public void setMaxCookieAge(int i) {
        this._maxCookieAge = i;
        if (this._maxCookieAge > 0 && this._refreshCookieAge == 0) {
            this._refreshCookieAge = this._maxCookieAge / 3;
        }
    }

    public void setMaxInactiveInterval(int i) {
        this._dftMaxIdleSecs = i;
    }

    public void setMetaManager(SessionIdManager sessionIdManager) {
        setIdManager(sessionIdManager);
    }

    public void setNodeIdInSessionId(boolean z) {
        this._nodeIdInSessionId = z;
    }

    public void setRefreshCookieAge(int i) {
        this._refreshCookieAge = i;
    }

    public void setSecureCookies(boolean z) {
        this._secureCookies = z;
    }

    public void setSessionCookie(String str) {
        this._sessionCookie = str;
    }

    public void setSessionDomain(String str) {
        this._sessionDomain = str;
    }

    public void setSessionHandler(SessionHandler sessionHandler) {
        this._sessionHandler = sessionHandler;
    }

    public void setSessionPath(String str) {
        this._sessionPath = str;
    }

    public void setSessionURL(String str) {
        String str2 = null;
        String str3 = (str == null || "none".equals(str)) ? null : str;
        this._sessionURL = str3;
        if (!(str == null || "none".equals(str))) {
            str2 = new StringBuffer().append(";").append(this._sessionURL).append("=").toString();
        }
        this._sessionURLPrefix = str2;
    }

    public void setUsingCookies(boolean z) {
        this._usingCookies = z;
    }
}
