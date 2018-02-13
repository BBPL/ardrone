package org.mortbay.jetty.servlet;

import java.security.SecureRandom;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.mortbay.component.AbstractLifeCycle;
import org.mortbay.jetty.SessionIdManager;
import org.mortbay.log.Log;
import org.mortbay.util.MultiMap;

public class HashSessionIdManager extends AbstractLifeCycle implements SessionIdManager {
    private static final String __NEW_SESSION_ID = "org.mortbay.jetty.newSessionId";
    protected Random _random;
    MultiMap _sessions;
    private boolean _weakRandom;
    private String _workerName;

    public HashSessionIdManager(Random random) {
        this._random = random;
    }

    public void addSession(HttpSession httpSession) {
        synchronized (this) {
            this._sessions.add(getClusterId(httpSession.getId()), httpSession);
        }
    }

    protected void doStart() {
        if (this._random == null) {
            try {
                Log.debug("Init SecureRandom.");
                this._random = new SecureRandom();
            } catch (Throwable e) {
                Log.warn("Could not generate SecureRandom for session-id randomness", e);
                this._random = new Random();
                this._weakRandom = true;
            }
        }
        this._sessions = new MultiMap();
    }

    protected void doStop() {
        if (this._sessions != null) {
            this._sessions.clear();
        }
        this._sessions = null;
    }

    public String getClusterId(String str) {
        int lastIndexOf = str.lastIndexOf(46);
        return lastIndexOf > 0 ? str.substring(0, lastIndexOf) : str;
    }

    public String getNodeId(String str, HttpServletRequest httpServletRequest) {
        String str2 = httpServletRequest == null ? null : (String) httpServletRequest.getAttribute("org.mortbay.http.ajp.JVMRoute");
        return str2 != null ? new StringBuffer().append(str).append('.').append(str2).toString() : this._workerName != null ? new StringBuffer().append(str).append('.').append(this._workerName).toString() : str;
    }

    public Random getRandom() {
        return this._random;
    }

    public String getWorkerName() {
        return this._workerName;
    }

    public boolean idInUse(String str) {
        return this._sessions.containsKey(str);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void invalidateAll(java.lang.String r3) {
        /*
        r2 = this;
    L_0x0000:
        monitor-enter(r2);
        r0 = r2._sessions;	 Catch:{ all -> 0x0024 }
        r0 = r0.containsKey(r3);	 Catch:{ all -> 0x0024 }
        if (r0 == 0) goto L_0x0022;
    L_0x0009:
        r0 = r2._sessions;	 Catch:{ all -> 0x0024 }
        r1 = 0;
        r0 = r0.getValue(r3, r1);	 Catch:{ all -> 0x0024 }
        r0 = (org.mortbay.jetty.servlet.AbstractSessionManager.Session) r0;	 Catch:{ all -> 0x0024 }
        r1 = r2._sessions;	 Catch:{ all -> 0x0024 }
        r1.removeValue(r3, r0);	 Catch:{ all -> 0x0024 }
        monitor-exit(r2);	 Catch:{ all -> 0x0024 }
        r1 = r0.isValid();
        if (r1 == 0) goto L_0x0000;
    L_0x001e:
        r0.invalidate();
        goto L_0x0000;
    L_0x0022:
        monitor-exit(r2);	 Catch:{ all -> 0x0024 }
        return;
    L_0x0024:
        r0 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0024 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mortbay.jetty.servlet.HashSessionIdManager.invalidateAll(java.lang.String):void");
    }

    public String newSessionId(HttpServletRequest httpServletRequest, long j) {
        String requestedSessionId;
        synchronized (this) {
            requestedSessionId = httpServletRequest.getRequestedSessionId();
            if (requestedSessionId != null) {
                requestedSessionId = getClusterId(requestedSessionId);
                if (idInUse(requestedSessionId)) {
                }
            }
            requestedSessionId = (String) httpServletRequest.getAttribute(__NEW_SESSION_ID);
            if (requestedSessionId == null || !idInUse(requestedSessionId)) {
                requestedSessionId = null;
                while (true) {
                    long hashCode;
                    if (requestedSessionId != null) {
                        if (!(requestedSessionId.length() == 0 || idInUse(requestedSessionId))) {
                            break;
                        }
                    }
                    if (this._weakRandom) {
                        hashCode = (((long) httpServletRequest.hashCode()) << 32) ^ ((((long) hashCode()) ^ Runtime.getRuntime().freeMemory()) ^ ((long) this._random.nextInt()));
                    } else {
                        hashCode = this._random.nextLong();
                    }
                    long nextLong = this._random.nextLong();
                    if (hashCode < 0) {
                        hashCode = -hashCode;
                    }
                    if (nextLong < 0) {
                        nextLong = -nextLong;
                    }
                    requestedSessionId = new StringBuffer().append(Long.toString(hashCode, 36)).append(Long.toString(nextLong, 36)).toString();
                }
                if (this._workerName != null) {
                    requestedSessionId = new StringBuffer().append(this._workerName).append(requestedSessionId).toString();
                }
                httpServletRequest.setAttribute(__NEW_SESSION_ID, requestedSessionId);
            }
        }
        return requestedSessionId;
    }

    public void removeSession(HttpSession httpSession) {
        synchronized (this) {
            this._sessions.removeValue(getClusterId(httpSession.getId()), httpSession);
        }
    }

    public void setRandom(Random random) {
        this._random = random;
        this._weakRandom = false;
    }

    public void setWorkerName(String str) {
        this._workerName = str;
    }
}
