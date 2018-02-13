package org.mortbay.jetty.servlet;

import com.google.api.client.http.ExponentialBackOffPolicy;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import javax.servlet.http.HttpServletRequest;
import org.mortbay.log.Log;
import org.mortbay.util.LazyList;

public class HashSessionManager extends AbstractSessionManager {
    private boolean _lazyLoad = false;
    private int _savePeriodMs = 0;
    private TimerTask _saveTask;
    private int _scavengePeriodMs = 30000;
    protected Map _sessions;
    private boolean _sessionsLoaded = false;
    private File _storeDir;
    private TimerTask _task;
    private Timer _timer;

    class C13421 extends TimerTask {
        private final HashSessionManager this$0;

        C13421(HashSessionManager hashSessionManager) {
            this.this$0 = hashSessionManager;
        }

        public void run() {
            try {
                this.this$0.saveSessions();
            } catch (Throwable e) {
                Log.warn(e);
            }
        }
    }

    class C13432 extends TimerTask {
        private final HashSessionManager this$0;

        C13432(HashSessionManager hashSessionManager) {
            this.this$0 = hashSessionManager;
        }

        public void run() {
            HashSessionManager.access$000(this.this$0);
        }
    }

    protected class ClassLoadingObjectInputStream extends ObjectInputStream {
        private final HashSessionManager this$0;

        public ClassLoadingObjectInputStream(HashSessionManager hashSessionManager) throws IOException {
            this.this$0 = hashSessionManager;
        }

        public ClassLoadingObjectInputStream(HashSessionManager hashSessionManager, InputStream inputStream) throws IOException {
            this.this$0 = hashSessionManager;
            super(inputStream);
        }

        public Class resolveClass(ObjectStreamClass objectStreamClass) throws IOException, ClassNotFoundException {
            try {
                return Class.forName(objectStreamClass.getName(), false, Thread.currentThread().getContextClassLoader());
            } catch (ClassNotFoundException e) {
                return super.resolveClass(objectStreamClass);
            }
        }
    }

    protected class Session extends org.mortbay.jetty.servlet.AbstractSessionManager.Session {
        private static final long serialVersionUID = -2134521374206116367L;
        private final HashSessionManager this$0;

        protected Session(HashSessionManager hashSessionManager, long j, String str) {
            this.this$0 = hashSessionManager;
            super(hashSessionManager, j, str);
        }

        protected Session(HashSessionManager hashSessionManager, HttpServletRequest httpServletRequest) {
            this.this$0 = hashSessionManager;
            super(hashSessionManager, httpServletRequest);
        }

        public void invalidate() throws IllegalStateException {
            super.invalidate();
            remove(getId());
        }

        protected Map newAttributeMap() {
            return new HashMap(3);
        }

        public void remove(String str) {
            if (str != null && !this.this$0.isStopping() && !this.this$0.isStopped() && HashSessionManager.access$200(this.this$0) != null && HashSessionManager.access$200(this.this$0).exists()) {
                new File(HashSessionManager.access$200(this.this$0), str).delete();
            }
        }

        public void save(FileOutputStream fileOutputStream) throws IOException {
            OutputStream dataOutputStream = new DataOutputStream(fileOutputStream);
            dataOutputStream.writeUTF(this._clusterId);
            dataOutputStream.writeUTF(this._nodeId);
            dataOutputStream.writeBoolean(this._idChanged);
            dataOutputStream.writeLong(this._created);
            dataOutputStream.writeLong(this._cookieSet);
            dataOutputStream.writeLong(this._accessed);
            dataOutputStream.writeLong(this._lastAccessed);
            dataOutputStream.writeInt(this._requests);
            if (this._values != null) {
                dataOutputStream.writeInt(this._values.size());
                for (String writeUTF : this._values.keySet()) {
                    dataOutputStream.writeUTF(writeUTF);
                }
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(dataOutputStream);
                for (Object writeObject : this._values.values()) {
                    objectOutputStream.writeObject(writeObject);
                }
                objectOutputStream.close();
            } else {
                dataOutputStream.writeInt(0);
            }
            dataOutputStream.close();
        }

        public void setMaxInactiveInterval(int i) {
            super.setMaxInactiveInterval(i);
            if (this._maxIdleMs > 0 && this._maxIdleMs / 10 < ((long) HashSessionManager.access$100(this.this$0))) {
                this.this$0.setScavengePeriod((i + 9) / 10);
            }
        }
    }

    static void access$000(HashSessionManager hashSessionManager) {
        hashSessionManager.scavenge();
    }

    static int access$100(HashSessionManager hashSessionManager) {
        return hashSessionManager._scavengePeriodMs;
    }

    static File access$200(HashSessionManager hashSessionManager) {
        return hashSessionManager._storeDir;
    }

    private void scavenge() {
        if (!isStopping() && !isStopped()) {
            long currentTimeMillis;
            Object obj;
            Session session;
            Thread currentThread = Thread.currentThread();
            ClassLoader contextClassLoader = currentThread.getContextClassLoader();
            try {
                if (this._loader != null) {
                    currentThread.setContextClassLoader(this._loader);
                }
                currentTimeMillis = System.currentTimeMillis();
                if (!this._sessionsLoaded && this._lazyLoad) {
                    restoreSessions();
                }
            } catch (Throwable e) {
                Log.debug(e);
            } catch (Throwable e2) {
                try {
                    if (e2 instanceof ThreadDeath) {
                        ThreadDeath threadDeath = (ThreadDeath) e2;
                    } else {
                        Log.warn("Problem scavenging sessions", e2);
                        return;
                    }
                } finally {
                    currentThread.setContextClassLoader(contextClassLoader);
                }
            }
            synchronized (this) {
                obj = null;
                for (Session session2 : this._sessions.values()) {
                    long j = session2._maxIdleMs;
                    if (j > 0 && j + session2._accessed < currentTimeMillis) {
                        obj = LazyList.add(obj, session2);
                    }
                }
            }
            int size = LazyList.size(obj);
            while (true) {
                int i = size - 1;
                if (size > 0) {
                    session2 = (Session) LazyList.get(obj, i);
                    currentTimeMillis = session2._maxIdleMs;
                    if (currentTimeMillis > 0 && currentTimeMillis + session2._accessed < System.currentTimeMillis()) {
                        session2.timeout();
                        size = this._sessions.size();
                        if (size < this._minSessions) {
                            this._minSessions = size;
                        }
                    }
                    size = i;
                } else {
                    currentThread.setContextClassLoader(contextClassLoader);
                    return;
                }
            }
        }
    }

    protected void addSession(org.mortbay.jetty.servlet.AbstractSessionManager.Session session) {
        this._sessions.put(session.getClusterId(), session);
    }

    public void doStart() throws Exception {
        this._sessions = new HashMap();
        super.doStart();
        this._timer = new Timer(true);
        setScavengePeriod(getScavengePeriod());
        if (this._storeDir != null) {
            if (!this._storeDir.exists()) {
                this._storeDir.mkdir();
            }
            if (!this._lazyLoad) {
                restoreSessions();
            }
        }
        setSavePeriod(getSavePeriod());
    }

    public void doStop() throws Exception {
        if (this._storeDir != null) {
            saveSessions();
        }
        super.doStop();
        this._sessions.clear();
        this._sessions = null;
        synchronized (this) {
            if (this._saveTask != null) {
                this._saveTask.cancel();
            }
            if (this._task != null) {
                this._task.cancel();
            }
            if (this._timer != null) {
                this._timer.cancel();
            }
            this._timer = null;
        }
    }

    public int getSavePeriod() {
        return this._savePeriodMs <= 0 ? 0 : this._savePeriodMs / 1000;
    }

    public int getScavengePeriod() {
        return this._scavengePeriodMs / 1000;
    }

    public org.mortbay.jetty.servlet.AbstractSessionManager.Session getSession(String str) {
        try {
            if (!this._sessionsLoaded && this._lazyLoad) {
                restoreSessions();
            }
        } catch (Throwable e) {
            Log.warn(e);
        }
        return this._sessions == null ? null : (Session) this._sessions.get(str);
    }

    public Map getSessionMap() {
        return Collections.unmodifiableMap(this._sessions);
    }

    public int getSessions() {
        return this._sessions.size();
    }

    public File getStoreDirectory() {
        return this._storeDir;
    }

    protected void invalidateSessions() {
        Iterator it = new ArrayList(this._sessions.values()).iterator();
        while (it.hasNext()) {
            ((Session) it.next()).invalidate();
        }
        this._sessions.clear();
    }

    public boolean isLazyLoad() {
        return this._lazyLoad;
    }

    protected org.mortbay.jetty.servlet.AbstractSessionManager.Session newSession(HttpServletRequest httpServletRequest) {
        return new Session(this, httpServletRequest);
    }

    protected void removeSession(String str) {
        this._sessions.remove(str);
    }

    public Session restoreSession(FileInputStream fileInputStream) throws Exception {
        InputStream dataInputStream = new DataInputStream(fileInputStream);
        String readUTF = dataInputStream.readUTF();
        dataInputStream.readUTF();
        dataInputStream.readBoolean();
        long readLong = dataInputStream.readLong();
        long readLong2 = dataInputStream.readLong();
        dataInputStream.readLong();
        long readLong3 = dataInputStream.readLong();
        dataInputStream.readInt();
        Session session = new Session(this, readLong, readUTF);
        session._cookieSet = readLong2;
        session._lastAccessed = readLong3;
        int readInt = dataInputStream.readInt();
        if (readInt > 0) {
            int i;
            ArrayList arrayList = new ArrayList();
            for (i = 0; i < readInt; i++) {
                arrayList.add(dataInputStream.readUTF());
            }
            ClassLoadingObjectInputStream classLoadingObjectInputStream = new ClassLoadingObjectInputStream(this, dataInputStream);
            for (i = 0; i < readInt; i++) {
                session.setAttribute((String) arrayList.get(i), classLoadingObjectInputStream.readObject());
            }
            classLoadingObjectInputStream.close();
        } else {
            session.initValues();
        }
        dataInputStream.close();
        return session;
    }

    public void restoreSessions() throws Exception {
        if (this._storeDir != null && this._storeDir.exists()) {
            if (this._storeDir.canRead()) {
                File[] listFiles = this._storeDir.listFiles();
                int i = 0;
                while (listFiles != null && i < listFiles.length) {
                    try {
                        FileInputStream fileInputStream = new FileInputStream(listFiles[i]);
                        org.mortbay.jetty.servlet.AbstractSessionManager.Session restoreSession = restoreSession(fileInputStream);
                        fileInputStream.close();
                        addSession(restoreSession, false);
                        listFiles[i].delete();
                    } catch (Throwable e) {
                        Log.warn(new StringBuffer().append("Problem restoring session ").append(listFiles[i].getName()).toString(), e);
                    }
                    i++;
                }
                this._sessionsLoaded = true;
                return;
            }
            Log.warn(new StringBuffer().append("Unable to restore Sessions: Cannot read from Session storage directory ").append(this._storeDir.getAbsolutePath()).toString());
        }
    }

    public void saveSessions() throws Exception {
        if (this._storeDir != null && this._storeDir.exists()) {
            if (this._storeDir.canWrite()) {
                synchronized (this) {
                    for (Entry entry : this._sessions.entrySet()) {
                        String str = (String) entry.getKey();
                        Session session = (Session) entry.getValue();
                        try {
                            File file = new File(this._storeDir, str);
                            if (file.exists()) {
                                file.delete();
                            }
                            file.createNewFile();
                            FileOutputStream fileOutputStream = new FileOutputStream(file);
                            session.save(fileOutputStream);
                            fileOutputStream.close();
                        } catch (Throwable e) {
                            Log.warn(new StringBuffer().append("Problem persisting session ").append(str).toString(), e);
                        }
                    }
                }
                return;
            }
            Log.warn(new StringBuffer().append("Unable to save Sessions: Session persistence storage directory ").append(this._storeDir.getAbsolutePath()).append(" is not writeable").toString());
        }
    }

    public void setLazyLoad(boolean z) {
        this._lazyLoad = z;
    }

    public void setMaxInactiveInterval(int i) {
        super.setMaxInactiveInterval(i);
        if (this._dftMaxIdleSecs > 0 && this._scavengePeriodMs > this._dftMaxIdleSecs * 1000) {
            setScavengePeriod((this._dftMaxIdleSecs + 9) / 10);
        }
    }

    public void setSavePeriod(int i) {
        int i2 = this._savePeriodMs;
        i2 = i * 1000;
        if (i2 < 0) {
            i2 = 0;
        }
        this._savePeriodMs = i2;
        if (this._timer != null) {
            synchronized (this) {
                if (this._saveTask != null) {
                    this._saveTask.cancel();
                }
                if (this._savePeriodMs > 0 && this._storeDir != null) {
                    this._saveTask = new C13421(this);
                    this._timer.schedule(this._saveTask, (long) this._savePeriodMs, (long) this._savePeriodMs);
                }
            }
        }
    }

    public void setScavengePeriod(int i) {
        int i2 = ExponentialBackOffPolicy.DEFAULT_MAX_INTERVAL_MILLIS;
        int i3 = 1000;
        if (i == 0) {
            i = 60;
        }
        int i4 = this._scavengePeriodMs;
        int i5 = i * 1000;
        if (i5 <= ExponentialBackOffPolicy.DEFAULT_MAX_INTERVAL_MILLIS) {
            i2 = i5;
        }
        if (i2 >= 1000) {
            i3 = i2;
        }
        this._scavengePeriodMs = i3;
        if (this._timer == null) {
            return;
        }
        if (i3 != i4 || this._task == null) {
            synchronized (this) {
                if (this._task != null) {
                    this._task.cancel();
                }
                this._task = new C13432(this);
                this._timer.schedule(this._task, (long) this._scavengePeriodMs, (long) this._scavengePeriodMs);
            }
        }
    }

    public void setStoreDirectory(File file) {
        this._storeDir = file;
    }
}
