package org.mortbay.jetty.servlet;

import java.security.SecureRandom;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import org.mortbay.component.AbstractLifeCycle;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.SessionIdManager;
import org.mortbay.log.Log;

public abstract class AbstractSessionIdManager extends AbstractLifeCycle implements SessionIdManager {
    private static final String __NEW_SESSION_ID = "org.mortbay.jetty.newSessionId";
    protected Random _random;
    protected Server _server;
    protected boolean _weakRandom;
    protected String _workerName;

    public AbstractSessionIdManager(Server server) {
        this._server = server;
    }

    public AbstractSessionIdManager(Server server, Random random) {
        this._random = random;
        this._server = server;
    }

    public void doStart() {
        initRandom();
    }

    public Random getRandom() {
        return this._random;
    }

    public String getWorkerName() {
        return this._workerName;
    }

    public void initRandom() {
        if (this._random == null) {
            try {
                this._random = new SecureRandom();
                this._weakRandom = false;
            } catch (Throwable e) {
                Log.warn("Could not generate SecureRandom for session-id randomness", e);
                this._random = new Random();
                this._weakRandom = true;
            }
        }
        this._random.setSeed(((this._random.nextLong() ^ System.currentTimeMillis()) ^ ((long) hashCode())) ^ Runtime.getRuntime().freeMemory());
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
                    if (requestedSessionId != null) {
                        if (!(requestedSessionId.length() == 0 || idInUse(requestedSessionId))) {
                            break;
                        }
                    }
                    long hashCode = this._weakRandom ? ((((long) hashCode()) ^ Runtime.getRuntime().freeMemory()) ^ ((long) this._random.nextInt())) ^ (((long) httpServletRequest.hashCode()) << 32) : this._random.nextLong();
                    long j2 = hashCode < 0 ? -hashCode : hashCode;
                    hashCode = this._weakRandom ? ((((long) hashCode()) ^ Runtime.getRuntime().freeMemory()) ^ ((long) this._random.nextInt())) ^ (((long) httpServletRequest.hashCode()) << 32) : this._random.nextLong();
                    if (hashCode < 0) {
                        hashCode = -hashCode;
                    }
                    requestedSessionId = new StringBuffer().append(Long.toString(j2, 36)).append(Long.toString(hashCode, 36)).toString();
                    if (this._workerName != null) {
                        requestedSessionId = new StringBuffer().append(this._workerName).append(requestedSessionId).toString();
                    }
                }
                httpServletRequest.setAttribute(__NEW_SESSION_ID, requestedSessionId);
            }
        }
        return requestedSessionId;
    }

    public void setRandom(Random random) {
        this._random = random;
        this._weakRandom = false;
    }

    public void setWorkerName(String str) {
        this._workerName = str;
    }
}
