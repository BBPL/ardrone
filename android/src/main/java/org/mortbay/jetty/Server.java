package org.mortbay.jetty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javax.servlet.ServletException;
import org.mortbay.component.Container;
import org.mortbay.component.LifeCycle;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.handler.HandlerCollection;
import org.mortbay.jetty.handler.HandlerWrapper;
import org.mortbay.jetty.security.UserRealm;
import org.mortbay.log.Log;
import org.mortbay.thread.QueuedThreadPool;
import org.mortbay.thread.ThreadPool;
import org.mortbay.util.Attributes;
import org.mortbay.util.AttributesMap;
import org.mortbay.util.LazyList;
import org.mortbay.util.MultiException;

public class Server extends HandlerWrapper implements Attributes {
    public static final String SNAPSHOT_VERSION = "6.1-SNAPSHOT";
    public static final String UNKNOWN_VERSION = "6.1.x";
    private static String _version;
    static Class class$java$lang$Runtime;
    static Class class$java$lang$Thread;
    static Class class$org$mortbay$jetty$Connector;
    static Class class$org$mortbay$jetty$Server;
    static Class class$org$mortbay$jetty$Server$Graceful;
    static Class class$org$mortbay$jetty$security$UserRealm;
    private static ShutdownHookThread hookThread = new ShutdownHookThread(null);
    private AttributesMap _attributes = new AttributesMap();
    private Connector[] _connectors;
    private Container _container = new Container();
    private List _dependentLifeCycles = new ArrayList();
    private int _graceful = 0;
    private UserRealm[] _realms;
    private boolean _sendDateHeader = false;
    private boolean _sendServerVersion = true;
    private SessionIdManager _sessionIdManager;
    private ThreadPool _threadPool;

    static class C13311 {
    }

    public interface Graceful {
        void setShutdown(boolean z);
    }

    private static class ShutdownHookThread extends Thread {
        private boolean hooked;
        private ArrayList servers;

        private ShutdownHookThread() {
            this.hooked = false;
            this.servers = new ArrayList();
        }

        ShutdownHookThread(C13311 c13311) {
            this();
        }

        private void createShutdownHook() {
            if (!Boolean.getBoolean("JETTY_NO_SHUTDOWN_HOOK") && !this.hooked) {
                try {
                    Class class$;
                    Class cls;
                    if (Server.class$java$lang$Runtime == null) {
                        class$ = Server.class$("java.lang.Runtime");
                        Server.class$java$lang$Runtime = class$;
                        cls = class$;
                    } else {
                        cls = Server.class$java$lang$Runtime;
                    }
                    if (Server.class$java$lang$Thread == null) {
                        class$ = Server.class$("java.lang.Thread");
                        Server.class$java$lang$Thread = class$;
                    } else {
                        class$ = Server.class$java$lang$Thread;
                    }
                    cls.getMethod("addShutdownHook", new Class[]{class$}).invoke(Runtime.getRuntime(), new Object[]{this});
                    this.hooked = true;
                } catch (Exception e) {
                    if (Log.isDebugEnabled()) {
                        Log.debug("No shutdown hook in JVM ", e);
                    }
                }
            }
        }

        public boolean add(Server server) {
            createShutdownHook();
            return this.servers.add(server);
        }

        public boolean addAll(Collection collection) {
            createShutdownHook();
            return this.servers.addAll(collection);
        }

        public void clear() {
            createShutdownHook();
            this.servers.clear();
        }

        public boolean contains(Server server) {
            return this.servers.contains(server);
        }

        public boolean remove(Server server) {
            createShutdownHook();
            return this.servers.remove(server);
        }

        public boolean removeAll(Collection collection) {
            createShutdownHook();
            return this.servers.removeAll(collection);
        }

        public void run() {
            setName("Shutdown");
            Log.info("Shutdown hook executing");
            Iterator it = this.servers.iterator();
            while (it.hasNext()) {
                Server server = (Server) it.next();
                if (server != null) {
                    try {
                        server.stop();
                    } catch (Throwable e) {
                        Log.warn(e);
                    }
                    Log.info("Shutdown hook complete");
                    try {
                        Thread.sleep(1000);
                    } catch (Throwable e2) {
                        Log.warn(e2);
                    }
                }
            }
        }
    }

    static {
        Class class$;
        String implementationVersion;
        if (class$org$mortbay$jetty$Server == null) {
            class$ = class$("org.mortbay.jetty.Server");
            class$org$mortbay$jetty$Server = class$;
        } else {
            class$ = class$org$mortbay$jetty$Server;
        }
        if (class$.getPackage() != null) {
            if (class$org$mortbay$jetty$Server == null) {
                class$ = class$("org.mortbay.jetty.Server");
                class$org$mortbay$jetty$Server = class$;
            } else {
                class$ = class$org$mortbay$jetty$Server;
            }
            if (class$.getPackage().getImplementationVersion() != null) {
                if (class$org$mortbay$jetty$Server == null) {
                    class$ = class$("org.mortbay.jetty.Server");
                    class$org$mortbay$jetty$Server = class$;
                } else {
                    class$ = class$org$mortbay$jetty$Server;
                }
                implementationVersion = class$.getPackage().getImplementationVersion();
                _version = implementationVersion;
            }
        }
        implementationVersion = UNKNOWN_VERSION;
        _version = implementationVersion;
    }

    public Server() {
        setServer(this);
    }

    public Server(int i) {
        setServer(this);
        new SocketConnector().setPort(i);
        setConnectors(new Connector[]{r0});
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (Throwable e) {
            throw new NoClassDefFoundError().initCause(e);
        }
    }

    public static String getVersion() {
        return _version;
    }

    public void addConnector(Connector connector) {
        Class class$;
        Connector[] connectors = getConnectors();
        if (class$org$mortbay$jetty$Connector == null) {
            class$ = class$("org.mortbay.jetty.Connector");
            class$org$mortbay$jetty$Connector = class$;
        } else {
            class$ = class$org$mortbay$jetty$Connector;
        }
        setConnectors((Connector[]) LazyList.addToArray(connectors, connector, class$));
    }

    public void addHandler(Handler handler) {
        if (getHandler() == null) {
            setHandler(handler);
        } else if (getHandler() instanceof HandlerCollection) {
            ((HandlerCollection) getHandler()).addHandler(handler);
        } else {
            Handler handlerCollection = new HandlerCollection();
            handlerCollection.setHandlers(new Handler[]{getHandler(), handler});
            setHandler(handlerCollection);
        }
    }

    public void addLifeCycle(LifeCycle lifeCycle) {
        if (lifeCycle != null) {
            if (!this._dependentLifeCycles.contains(lifeCycle)) {
                this._dependentLifeCycles.add(lifeCycle);
                this._container.addBean(lifeCycle);
            }
            try {
                if (isStarted()) {
                    lifeCycle.start();
                }
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void addUserRealm(UserRealm userRealm) {
        Class class$;
        UserRealm[] userRealms = getUserRealms();
        if (class$org$mortbay$jetty$security$UserRealm == null) {
            class$ = class$("org.mortbay.jetty.security.UserRealm");
            class$org$mortbay$jetty$security$UserRealm = class$;
        } else {
            class$ = class$org$mortbay$jetty$security$UserRealm;
        }
        setUserRealms((UserRealm[]) LazyList.addToArray(userRealms, userRealm, class$));
    }

    public void clearAttributes() {
        this._attributes.clearAttributes();
    }

    protected void doStart() throws Exception {
        int i = 0;
        Log.info(new StringBuffer().append("jetty-").append(_version).toString());
        HttpGenerator.setServerVersion(_version);
        MultiException multiException = new MultiException();
        int i2 = 0;
        while (this._realms != null && i2 < this._realms.length) {
            if (this._realms[i2] instanceof LifeCycle) {
                ((LifeCycle) this._realms[i2]).start();
            }
            i2++;
        }
        for (LifeCycle start : this._dependentLifeCycles) {
            try {
                start.start();
            } catch (Throwable th) {
                multiException.add(th);
            }
        }
        if (this._threadPool == null) {
            setThreadPool(new QueuedThreadPool());
        }
        if (this._sessionIdManager != null) {
            this._sessionIdManager.start();
        }
        try {
            if (this._threadPool instanceof LifeCycle) {
                ((LifeCycle) this._threadPool).start();
            }
        } catch (Throwable th2) {
            multiException.add(th2);
        }
        try {
            super.doStart();
        } catch (Throwable th22) {
            Log.warn("Error starting handlers", th22);
        }
        if (this._connectors != null) {
            while (i < this._connectors.length) {
                try {
                    this._connectors[i].start();
                } catch (Throwable th222) {
                    multiException.add(th222);
                }
                i++;
            }
        }
        multiException.ifExceptionThrow();
    }

    protected void doStop() throws Exception {
        int length;
        int i = 0;
        MultiException multiException = new MultiException();
        int i2 = 0;
        while (this._realms != null && i2 < this._realms.length) {
            if (this._realms[i2] instanceof LifeCycle) {
                ((LifeCycle) this._realms[i2]).stop();
            }
            i2++;
        }
        if (this._graceful > 0) {
            Class class$;
            if (this._connectors != null) {
                length = this._connectors.length;
                while (true) {
                    i2 = length - 1;
                    if (length <= 0) {
                        break;
                    }
                    Log.info("Graceful shutdown {}", this._connectors[i2]);
                    try {
                        this._connectors[i2].close();
                        length = i2;
                    } catch (Throwable th) {
                        multiException.add(th);
                        length = i2;
                    }
                }
            }
            if (class$org$mortbay$jetty$Server$Graceful == null) {
                class$ = class$("org.mortbay.jetty.Server$Graceful");
                class$org$mortbay$jetty$Server$Graceful = class$;
            } else {
                class$ = class$org$mortbay$jetty$Server$Graceful;
            }
            Handler[] childHandlersByClass = getChildHandlersByClass(class$);
            while (i < childHandlersByClass.length) {
                Graceful graceful = (Graceful) childHandlersByClass[i];
                Log.info("Graceful shutdown {}", graceful);
                graceful.setShutdown(true);
                i++;
            }
            Thread.sleep((long) this._graceful);
        }
        ListIterator listIterator;
        if (this._connectors != null) {
            length = this._connectors.length;
            while (true) {
                i2 = length - 1;
                if (length > 0) {
                    try {
                        this._connectors[i2].stop();
                        length = i2;
                    } catch (Throwable th2) {
                        multiException.add(th2);
                        length = i2;
                    }
                }
            }
            super.doStop();
            if (this._sessionIdManager != null) {
                this._sessionIdManager.stop();
            }
            if (this._threadPool instanceof LifeCycle) {
                ((LifeCycle) this._threadPool).stop();
            }
            if (!this._dependentLifeCycles.isEmpty()) {
                listIterator = this._dependentLifeCycles.listIterator(this._dependentLifeCycles.size());
                while (listIterator.hasPrevious()) {
                    try {
                        ((LifeCycle) listIterator.previous()).stop();
                    } catch (Throwable th22) {
                        multiException.add(th22);
                    }
                }
            }
            multiException.ifExceptionThrow();
        }
        try {
            super.doStop();
        } catch (Throwable th222) {
            multiException.add(th222);
        }
        if (this._sessionIdManager != null) {
            this._sessionIdManager.stop();
        }
        try {
            if (this._threadPool instanceof LifeCycle) {
                ((LifeCycle) this._threadPool).stop();
            }
        } catch (Throwable th2222) {
            multiException.add(th2222);
        }
        if (this._dependentLifeCycles.isEmpty()) {
            listIterator = this._dependentLifeCycles.listIterator(this._dependentLifeCycles.size());
            while (listIterator.hasPrevious()) {
                ((LifeCycle) listIterator.previous()).stop();
            }
        }
        multiException.ifExceptionThrow();
    }

    public Object getAttribute(String str) {
        return this._attributes.getAttribute(str);
    }

    public Enumeration getAttributeNames() {
        return AttributesMap.getAttributeNamesCopy(this._attributes);
    }

    public Connector[] getConnectors() {
        return this._connectors;
    }

    public Container getContainer() {
        return this._container;
    }

    public int getGracefulShutdown() {
        return this._graceful;
    }

    public Handler[] getHandlers() {
        return getHandler() instanceof HandlerCollection ? ((HandlerCollection) getHandler()).getHandlers() : null;
    }

    public boolean getSendDateHeader() {
        return this._sendDateHeader;
    }

    public boolean getSendServerVersion() {
        return this._sendServerVersion;
    }

    public SessionIdManager getSessionIdManager() {
        return this._sessionIdManager;
    }

    public boolean getStopAtShutdown() {
        return hookThread.contains(this);
    }

    public ThreadPool getThreadPool() {
        return this._threadPool;
    }

    public UserRealm[] getUserRealms() {
        return this._realms;
    }

    public void handle(HttpConnection httpConnection) throws IOException, ServletException {
        String pathInfo = httpConnection.getRequest().getPathInfo();
        if (Log.isDebugEnabled()) {
            Log.debug(new StringBuffer().append("REQUEST ").append(pathInfo).append(" on ").append(httpConnection).toString());
            handle(pathInfo, httpConnection.getRequest(), httpConnection.getResponse(), 1);
            Log.debug(new StringBuffer().append("RESPONSE ").append(pathInfo).append("  ").append(httpConnection.getResponse().getStatus()).toString());
            return;
        }
        handle(pathInfo, httpConnection.getRequest(), httpConnection.getResponse(), 1);
    }

    public void join() throws InterruptedException {
        getThreadPool().join();
    }

    public void removeAttribute(String str) {
        this._attributes.removeAttribute(str);
    }

    public void removeConnector(Connector connector) {
        setConnectors((Connector[]) LazyList.removeFromArray(getConnectors(), connector));
    }

    public void removeHandler(Handler handler) {
        if (getHandler() instanceof HandlerCollection) {
            ((HandlerCollection) getHandler()).removeHandler(handler);
        }
    }

    public void removeLifeCycle(LifeCycle lifeCycle) {
        if (lifeCycle != null) {
            this._dependentLifeCycles.remove(lifeCycle);
            this._container.removeBean(lifeCycle);
        }
    }

    public void removeUserRealm(UserRealm userRealm) {
        setUserRealms((UserRealm[]) LazyList.removeFromArray(getUserRealms(), userRealm));
    }

    public void setAttribute(String str, Object obj) {
        this._attributes.setAttribute(str, obj);
    }

    public void setConnectors(Connector[] connectorArr) {
        if (connectorArr != null) {
            for (Connector server : connectorArr) {
                server.setServer(this);
            }
        }
        this._container.update((Object) this, this._connectors, (Object[]) connectorArr, "connector");
        this._connectors = connectorArr;
    }

    public void setGracefulShutdown(int i) {
        this._graceful = i;
    }

    public void setHandlers(Handler[] handlerArr) {
        HandlerCollection handlerCollection;
        if (getHandler() instanceof HandlerCollection) {
            handlerCollection = (HandlerCollection) getHandler();
        } else {
            handlerCollection = new HandlerCollection();
            setHandler(handlerCollection);
        }
        handlerCollection.setHandlers(handlerArr);
    }

    public void setSendDateHeader(boolean z) {
        this._sendDateHeader = z;
    }

    public void setSendServerVersion(boolean z) {
        this._sendServerVersion = z;
    }

    public void setSessionIdManager(SessionIdManager sessionIdManager) {
        this._container.update((Object) this, this._sessionIdManager, (Object) sessionIdManager, "sessionIdManager", true);
        this._sessionIdManager = sessionIdManager;
    }

    public void setStopAtShutdown(boolean z) {
        if (z) {
            hookThread.add(this);
        } else {
            hookThread.remove(this);
        }
    }

    public void setThreadPool(ThreadPool threadPool) {
        this._container.update((Object) this, this._threadPool, (Object) threadPool, "threadpool", true);
        this._threadPool = threadPool;
    }

    public void setUserRealms(UserRealm[] userRealmArr) {
        this._container.update((Object) this, this._realms, (Object[]) userRealmArr, "realm", true);
        this._realms = userRealmArr;
    }
}
