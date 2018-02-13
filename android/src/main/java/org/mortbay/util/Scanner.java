package org.mortbay.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import org.mortbay.log.Log;

public class Scanner {
    private Map _currentScan = new HashMap();
    private FilenameFilter _filter;
    private List _listeners = Collections.synchronizedList(new ArrayList());
    private Map _prevScan = new HashMap();
    private boolean _recursive = true;
    private boolean _reportExisting = true;
    private volatile boolean _running = false;
    private List _scanDirs;
    private int _scanInterval;
    private TimerTask _task;
    private Timer _timer;

    public interface Listener {
    }

    public interface DiscreteListener extends Listener {
        void fileAdded(String str) throws Exception;

        void fileChanged(String str) throws Exception;

        void fileRemoved(String str) throws Exception;
    }

    public interface BulkListener extends Listener {
        void filesChanged(List list) throws Exception;
    }

    class C13511 extends TimerTask {
        private final Scanner this$0;

        C13511(Scanner scanner) {
            this.this$0 = scanner;
        }

        public void run() {
            this.this$0.scan();
        }
    }

    private void reportAddition(String str) {
        for (Object next : this._listeners) {
            try {
                if (next instanceof DiscreteListener) {
                    ((DiscreteListener) next).fileAdded(str);
                }
            } catch (Throwable e) {
                warn(next, str, e);
            } catch (Throwable e2) {
                warn(next, str, e2);
            }
        }
    }

    private void reportBulkChanges(List list) {
        for (Object next : this._listeners) {
            try {
                if (next instanceof BulkListener) {
                    ((BulkListener) next).filesChanged(list);
                }
            } catch (Throwable e) {
                warn(next, list.toString(), e);
            } catch (Throwable e2) {
                warn(next, list.toString(), e2);
            }
        }
    }

    private void reportChange(String str) {
        for (Object next : this._listeners) {
            try {
                if (next instanceof DiscreteListener) {
                    ((DiscreteListener) next).fileChanged(str);
                }
            } catch (Throwable e) {
                warn(next, str, e);
            } catch (Throwable e2) {
                warn(next, str, e2);
            }
        }
    }

    private void reportRemoval(String str) {
        for (Object next : this._listeners) {
            try {
                if (next instanceof DiscreteListener) {
                    ((DiscreteListener) next).fileRemoved(str);
                }
            } catch (Throwable e) {
                warn(next, str, e);
            } catch (Throwable e2) {
                warn(next, str, e2);
            }
        }
    }

    private void scanFile(File file, Map map) {
        try {
            if (!file.exists()) {
                return;
            }
            if (file.isFile()) {
                if (this._filter == null || (this._filter != null && this._filter.accept(file.getParentFile(), file.getName()))) {
                    map.put(file.getCanonicalPath(), new Long(file.lastModified()));
                }
            } else if (!file.isDirectory()) {
            } else {
                if (this._recursive || this._scanDirs.contains(file)) {
                    File[] listFiles = file.listFiles();
                    for (File scanFile : listFiles) {
                        scanFile(scanFile, map);
                    }
                }
            }
        } catch (Throwable e) {
            Log.warn("Error scanning watched files", e);
        }
    }

    private void warn(Object obj, String str, Throwable th) {
        Log.warn(th);
        Log.warn(new StringBuffer().append(obj).append(" failed on '").append(str).toString());
    }

    public void addListener(Listener listener) {
        synchronized (this) {
            if (listener != null) {
                this._listeners.add(listener);
            }
        }
    }

    public FilenameFilter getFilenameFilter() {
        return this._filter;
    }

    public boolean getRecursive() {
        return this._recursive;
    }

    public File getScanDir() {
        return this._scanDirs == null ? null : (File) this._scanDirs.get(0);
    }

    public List getScanDirs() {
        return this._scanDirs;
    }

    public int getScanInterval() {
        return this._scanInterval;
    }

    public Timer newTimer() {
        return new Timer(true);
    }

    public TimerTask newTimerTask() {
        return new C13511(this);
    }

    public void removeListener(Listener listener) {
        synchronized (this) {
            if (listener != null) {
                this._listeners.remove(listener);
            }
        }
    }

    public void reportDifferences(Map map, Map map2) {
        List arrayList = new ArrayList();
        Set<String> hashSet = new HashSet(map2.keySet());
        for (Entry entry : map.entrySet()) {
            if (!hashSet.contains(entry.getKey())) {
                Log.debug(new StringBuffer().append("File added: ").append(entry.getKey()).toString());
                reportAddition((String) entry.getKey());
                arrayList.add(entry.getKey());
            } else if (map2.get(entry.getKey()).equals(entry.getValue())) {
                hashSet.remove(entry.getKey());
            } else {
                Log.debug(new StringBuffer().append("File changed: ").append(entry.getKey()).toString());
                reportChange((String) entry.getKey());
                hashSet.remove(entry.getKey());
                arrayList.add(entry.getKey());
            }
        }
        if (!hashSet.isEmpty()) {
            for (String str : hashSet) {
                Log.debug(new StringBuffer().append("File removed: ").append(str).toString());
                reportRemoval(str);
                arrayList.add(str);
            }
        }
        if (!arrayList.isEmpty()) {
            reportBulkChanges(arrayList);
        }
    }

    public void scan() {
        scanFiles();
        reportDifferences(this._currentScan, this._prevScan);
        this._prevScan.clear();
        this._prevScan.putAll(this._currentScan);
    }

    public void scanFiles() {
        if (this._scanDirs != null) {
            this._currentScan.clear();
            for (File file : this._scanDirs) {
                if (file != null && file.exists()) {
                    scanFile(file, this._currentScan);
                }
            }
        }
    }

    public void schedule() {
        if (this._running) {
            if (this._timer != null) {
                this._timer.cancel();
            }
            if (this._task != null) {
                this._task.cancel();
            }
            if (getScanInterval() > 0) {
                this._timer = newTimer();
                this._task = newTimerTask();
                this._timer.schedule(this._task, ((long) getScanInterval()) * 1000, ((long) getScanInterval()) * 1000);
            }
        }
    }

    public void setFilenameFilter(FilenameFilter filenameFilter) {
        this._filter = filenameFilter;
    }

    public void setRecursive(boolean z) {
        this._recursive = z;
    }

    public void setReportExistingFilesOnStartup(boolean z) {
        this._reportExisting = z;
    }

    public void setScanDir(File file) {
        this._scanDirs = new ArrayList();
        this._scanDirs.add(file);
    }

    public void setScanDirs(List list) {
        this._scanDirs = list;
    }

    public void setScanInterval(int i) {
        synchronized (this) {
            this._scanInterval = i;
            schedule();
        }
    }

    public void start() {
        synchronized (this) {
            if (!this._running) {
                this._running = true;
                if (this._reportExisting) {
                    scan();
                } else {
                    scanFiles();
                    this._prevScan.putAll(this._currentScan);
                }
                schedule();
            }
        }
    }

    public void stop() {
        synchronized (this) {
            if (this._running) {
                this._running = false;
                if (this._timer != null) {
                    this._timer.cancel();
                }
                if (this._task != null) {
                    this._task.cancel();
                }
                this._task = null;
                this._timer = null;
            }
        }
    }
}
