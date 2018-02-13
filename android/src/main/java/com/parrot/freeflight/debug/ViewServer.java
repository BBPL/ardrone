package com.parrot.freeflight.debug;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.mortbay.jetty.HttpVersions;

public class ViewServer implements Runnable {
    private static final String BUILD_TYPE_USER = "user";
    private static final String COMMAND_PROTOCOL_VERSION = "PROTOCOL";
    private static final String COMMAND_SERVER_VERSION = "SERVER";
    private static final String COMMAND_WINDOW_MANAGER_AUTOLIST = "AUTOLIST";
    private static final String COMMAND_WINDOW_MANAGER_GET_FOCUS = "GET_FOCUS";
    private static final String COMMAND_WINDOW_MANAGER_LIST = "LIST";
    private static final String LOG_TAG = "LocalViewServer";
    private static final String VALUE_PROTOCOL_VERSION = "4";
    private static final String VALUE_SERVER_VERSION = "4";
    private static final int VIEW_SERVER_DEFAULT_PORT = 4939;
    private static final int VIEW_SERVER_MAX_CONNECTIONS = 10;
    private static ViewServer sServer;
    private final ReentrantReadWriteLock mFocusLock;
    private View mFocusedWindow;
    private final List<WindowListener> mListeners;
    private final int mPort;
    private ServerSocket mServer;
    private Thread mThread;
    private ExecutorService mThreadPool;
    private final HashMap<View, String> mWindows;
    private final ReentrantReadWriteLock mWindowsLock;

    private static class NoopViewServer extends ViewServer {
        private NoopViewServer() {
            super();
        }

        public void addWindow(Activity activity) {
        }

        public void addWindow(View view, String str) {
        }

        public boolean isRunning() {
            return false;
        }

        public void removeWindow(Activity activity) {
        }

        public void removeWindow(View view) {
        }

        public void run() {
        }

        public void setFocusedWindow(Activity activity) {
        }

        public void setFocusedWindow(View view) {
        }

        public boolean start() throws IOException {
            return false;
        }

        public boolean stop() {
            return false;
        }
    }

    private static class UncloseableOuputStream extends OutputStream {
        private final OutputStream mStream;

        UncloseableOuputStream(OutputStream outputStream) {
            this.mStream = outputStream;
        }

        public void close() throws IOException {
        }

        public boolean equals(Object obj) {
            return this.mStream.equals(obj);
        }

        public void flush() throws IOException {
            this.mStream.flush();
        }

        public int hashCode() {
            return this.mStream.hashCode();
        }

        public String toString() {
            return this.mStream.toString();
        }

        public void write(int i) throws IOException {
            this.mStream.write(i);
        }

        public void write(byte[] bArr) throws IOException {
            this.mStream.write(bArr);
        }

        public void write(byte[] bArr, int i, int i2) throws IOException {
            this.mStream.write(bArr, i, i2);
        }
    }

    private interface WindowListener {
        void focusChanged();

        void windowsChanged();
    }

    private class ViewServerWorker implements Runnable, WindowListener {
        private Socket mClient;
        private final Object[] mLock = new Object[0];
        private boolean mNeedFocusedWindowUpdate;
        private boolean mNeedWindowListUpdate;

        public ViewServerWorker(Socket socket) {
            this.mClient = socket;
            this.mNeedWindowListUpdate = false;
            this.mNeedFocusedWindowUpdate = false;
        }

        private View findWindow(int i) {
            View access$400;
            if (i == -1) {
                ViewServer.this.mWindowsLock.readLock().lock();
                try {
                    access$400 = ViewServer.this.mFocusedWindow;
                    return access$400;
                } finally {
                    ViewServer.this.mWindowsLock.readLock().unlock();
                }
            } else {
                ViewServer.this.mWindowsLock.readLock().lock();
                try {
                    for (Entry entry : ViewServer.this.mWindows.entrySet()) {
                        if (System.identityHashCode(entry.getKey()) == i) {
                            access$400 = (View) entry.getKey();
                            return access$400;
                        }
                    }
                    ViewServer.this.mWindowsLock.readLock().unlock();
                    return null;
                } finally {
                    ViewServer.this.mWindowsLock.readLock().unlock();
                }
            }
        }

        private boolean getFocusedWindow(Socket socket) {
            boolean z;
            BufferedWriter bufferedWriter;
            Throwable th;
            BufferedWriter bufferedWriter2;
            try {
                bufferedWriter2 = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()), 8192);
                try {
                    ViewServer.this.mFocusLock.readLock().lock();
                    View access$400 = ViewServer.this.mFocusedWindow;
                    ViewServer.this.mFocusLock.readLock().unlock();
                    if (access$400 != null) {
                        ViewServer.this.mWindowsLock.readLock().lock();
                        String str = (String) ViewServer.this.mWindows.get(ViewServer.this.mFocusedWindow);
                        ViewServer.this.mWindowsLock.readLock().unlock();
                        bufferedWriter2.write(Integer.toHexString(System.identityHashCode(access$400)));
                        bufferedWriter2.write(32);
                        bufferedWriter2.append(str);
                    }
                    bufferedWriter2.write(10);
                    bufferedWriter2.flush();
                    if (bufferedWriter2 != null) {
                        try {
                            bufferedWriter2.close();
                            z = true;
                        } catch (IOException e) {
                            z = false;
                        }
                    } else {
                        z = true;
                    }
                } catch (Exception e2) {
                    bufferedWriter = bufferedWriter2;
                    if (bufferedWriter == null) {
                        try {
                            bufferedWriter.close();
                            return false;
                        } catch (IOException e3) {
                            return false;
                        }
                    }
                    z = false;
                    return z;
                } catch (Throwable th2) {
                    th = th2;
                    if (bufferedWriter2 != null) {
                        try {
                            bufferedWriter2.close();
                        } catch (IOException e4) {
                        }
                    }
                    throw th;
                }
            } catch (Exception e5) {
                bufferedWriter = null;
                if (bufferedWriter == null) {
                    z = false;
                    return z;
                }
                bufferedWriter.close();
                return false;
            } catch (Throwable th3) {
                th = th3;
                bufferedWriter2 = null;
                if (bufferedWriter2 != null) {
                    bufferedWriter2.close();
                }
                throw th;
            }
            return z;
        }

        private boolean listWindows(Socket socket) {
            BufferedWriter bufferedWriter;
            Throwable th;
            BufferedWriter bufferedWriter2 = null;
            try {
                ViewServer.this.mWindowsLock.readLock().lock();
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()), 8192);
                try {
                    boolean z;
                    for (Entry entry : ViewServer.this.mWindows.entrySet()) {
                        bufferedWriter.write(Integer.toHexString(System.identityHashCode(entry.getKey())));
                        bufferedWriter.write(32);
                        bufferedWriter.append((CharSequence) entry.getValue());
                        bufferedWriter.write(10);
                    }
                    bufferedWriter.write("DONE.\n");
                    bufferedWriter.flush();
                    ViewServer.this.mWindowsLock.readLock().unlock();
                    if (bufferedWriter != null) {
                        try {
                            bufferedWriter.close();
                            z = true;
                        } catch (IOException e) {
                            z = false;
                        }
                    } else {
                        z = true;
                    }
                    return z;
                } catch (Exception e2) {
                    ViewServer.this.mWindowsLock.readLock().unlock();
                    if (bufferedWriter != null) {
                        return false;
                    }
                    try {
                        bufferedWriter.close();
                        return false;
                    } catch (IOException e3) {
                        return false;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    bufferedWriter2 = bufferedWriter;
                    ViewServer.this.mWindowsLock.readLock().unlock();
                    if (bufferedWriter2 != null) {
                        try {
                            bufferedWriter2.close();
                        } catch (IOException e4) {
                        }
                    }
                    throw th;
                }
            } catch (Exception e5) {
                bufferedWriter = null;
                ViewServer.this.mWindowsLock.readLock().unlock();
                if (bufferedWriter != null) {
                    return false;
                }
                bufferedWriter.close();
                return false;
            } catch (Throwable th3) {
                th = th3;
                ViewServer.this.mWindowsLock.readLock().unlock();
                if (bufferedWriter2 != null) {
                    bufferedWriter2.close();
                }
                throw th;
            }
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private boolean windowCommand(java.net.Socket r12, java.lang.String r13, java.lang.String r14) {
            /*
            r11 = this;
            r3 = 0;
            r1 = 1;
            r4 = 0;
            r0 = 32;
            r0 = r14.indexOf(r0);	 Catch:{ Exception -> 0x0095, all -> 0x00c4 }
            r2 = -1;
            if (r0 != r2) goto L_0x0010;
        L_0x000c:
            r0 = r14.length();	 Catch:{ Exception -> 0x0095, all -> 0x00c4 }
        L_0x0010:
            r2 = 0;
            r2 = r14.substring(r2, r0);	 Catch:{ Exception -> 0x0095, all -> 0x00c4 }
            r5 = 16;
            r6 = java.lang.Long.parseLong(r2, r5);	 Catch:{ Exception -> 0x0095, all -> 0x00c4 }
            r2 = (int) r6;	 Catch:{ Exception -> 0x0095, all -> 0x00c4 }
            r5 = r14.length();	 Catch:{ Exception -> 0x0095, all -> 0x00c4 }
            if (r0 >= r5) goto L_0x002f;
        L_0x0022:
            r0 = r0 + 1;
            r0 = r14.substring(r0);	 Catch:{ Exception -> 0x0095, all -> 0x00c4 }
        L_0x0028:
            r2 = r11.findWindow(r2);	 Catch:{ Exception -> 0x00da, all -> 0x00c4 }
            if (r2 != 0) goto L_0x0032;
        L_0x002e:
            return r4;
        L_0x002f:
            r0 = "";
            goto L_0x0028;
        L_0x0032:
            r5 = android.view.ViewDebug.class;
            r6 = "dispatchCommand";
            r7 = 4;
            r7 = new java.lang.Class[r7];	 Catch:{ Exception -> 0x00da, all -> 0x00c4 }
            r8 = 0;
            r9 = android.view.View.class;
            r7[r8] = r9;	 Catch:{ Exception -> 0x00da, all -> 0x00c4 }
            r8 = 1;
            r9 = java.lang.String.class;
            r7[r8] = r9;	 Catch:{ Exception -> 0x00da, all -> 0x00c4 }
            r8 = 2;
            r9 = java.lang.String.class;
            r7[r8] = r9;	 Catch:{ Exception -> 0x00da, all -> 0x00c4 }
            r8 = 3;
            r9 = java.io.OutputStream.class;
            r7[r8] = r9;	 Catch:{ Exception -> 0x00da, all -> 0x00c4 }
            r5 = r5.getDeclaredMethod(r6, r7);	 Catch:{ Exception -> 0x00da, all -> 0x00c4 }
            r6 = 1;
            r5.setAccessible(r6);	 Catch:{ Exception -> 0x00da, all -> 0x00c4 }
            r6 = 0;
            r7 = 4;
            r7 = new java.lang.Object[r7];	 Catch:{ Exception -> 0x00da, all -> 0x00c4 }
            r8 = 0;
            r7[r8] = r2;	 Catch:{ Exception -> 0x00da, all -> 0x00c4 }
            r2 = 1;
            r7[r2] = r13;	 Catch:{ Exception -> 0x00da, all -> 0x00c4 }
            r2 = 2;
            r7[r2] = r0;	 Catch:{ Exception -> 0x00da, all -> 0x00c4 }
            r2 = 3;
            r8 = new com.parrot.freeflight.debug.ViewServer$UncloseableOuputStream;	 Catch:{ Exception -> 0x00da, all -> 0x00c4 }
            r9 = r12.getOutputStream();	 Catch:{ Exception -> 0x00da, all -> 0x00c4 }
            r8.<init>(r9);	 Catch:{ Exception -> 0x00da, all -> 0x00c4 }
            r7[r2] = r8;	 Catch:{ Exception -> 0x00da, all -> 0x00c4 }
            r5.invoke(r6, r7);	 Catch:{ Exception -> 0x00da, all -> 0x00c4 }
            r2 = r12.isOutputShutdown();	 Catch:{ Exception -> 0x00da, all -> 0x00c4 }
            if (r2 != 0) goto L_0x00e3;
        L_0x0077:
            r2 = new java.io.BufferedWriter;	 Catch:{ Exception -> 0x00da, all -> 0x00c4 }
            r5 = new java.io.OutputStreamWriter;	 Catch:{ Exception -> 0x00da, all -> 0x00c4 }
            r6 = r12.getOutputStream();	 Catch:{ Exception -> 0x00da, all -> 0x00c4 }
            r5.<init>(r6);	 Catch:{ Exception -> 0x00da, all -> 0x00c4 }
            r2.<init>(r5);	 Catch:{ Exception -> 0x00da, all -> 0x00c4 }
            r3 = "DONE\n";
            r2.write(r3);	 Catch:{ Exception -> 0x00cc, all -> 0x00d5 }
            r2.flush();	 Catch:{ Exception -> 0x00cc, all -> 0x00d5 }
        L_0x008d:
            if (r2 == 0) goto L_0x00e1;
        L_0x008f:
            r2.close();	 Catch:{ IOException -> 0x00c1 }
            r0 = r1;
        L_0x0093:
            r4 = r0;
            goto L_0x002e;
        L_0x0095:
            r0 = move-exception;
            r1 = r14;
        L_0x0097:
            r2 = r3;
        L_0x0098:
            r3 = "LocalViewServer";
            r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00d7 }
            r5.<init>();	 Catch:{ all -> 0x00d7 }
            r6 = "Could not send command ";
            r5 = r5.append(r6);	 Catch:{ all -> 0x00d7 }
            r5 = r5.append(r13);	 Catch:{ all -> 0x00d7 }
            r6 = " with parameters ";
            r5 = r5.append(r6);	 Catch:{ all -> 0x00d7 }
            r1 = r5.append(r1);	 Catch:{ all -> 0x00d7 }
            r1 = r1.toString();	 Catch:{ all -> 0x00d7 }
            android.util.Log.w(r3, r1, r0);	 Catch:{ all -> 0x00d7 }
            if (r2 == 0) goto L_0x00df;
        L_0x00bc:
            r2.close();	 Catch:{ IOException -> 0x00d1 }
            r0 = r4;
            goto L_0x0093;
        L_0x00c1:
            r0 = move-exception;
        L_0x00c2:
            r0 = r4;
            goto L_0x0093;
        L_0x00c4:
            r0 = move-exception;
        L_0x00c5:
            r2 = r3;
        L_0x00c6:
            if (r2 == 0) goto L_0x00cb;
        L_0x00c8:
            r2.close();	 Catch:{ IOException -> 0x00d3 }
        L_0x00cb:
            throw r0;
        L_0x00cc:
            r1 = move-exception;
            r10 = r1;
            r1 = r0;
            r0 = r10;
            goto L_0x0098;
        L_0x00d1:
            r0 = move-exception;
            goto L_0x00c2;
        L_0x00d3:
            r1 = move-exception;
            goto L_0x00cb;
        L_0x00d5:
            r0 = move-exception;
            goto L_0x00c6;
        L_0x00d7:
            r0 = move-exception;
            r3 = r2;
            goto L_0x00c5;
        L_0x00da:
            r1 = move-exception;
            r10 = r1;
            r1 = r0;
            r0 = r10;
            goto L_0x0097;
        L_0x00df:
            r0 = r4;
            goto L_0x0093;
        L_0x00e1:
            r0 = r1;
            goto L_0x0093;
        L_0x00e3:
            r2 = r3;
            goto L_0x008d;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.parrot.freeflight.debug.ViewServer.ViewServerWorker.windowCommand(java.net.Socket, java.lang.String, java.lang.String):boolean");
        }

        private boolean windowManagerAutolistLoop() {
            Throwable e;
            BufferedWriter bufferedWriter = null;
            ViewServer.this.addWindowListener(this);
            BufferedWriter bufferedWriter2;
            try {
                bufferedWriter2 = new BufferedWriter(new OutputStreamWriter(this.mClient.getOutputStream()));
                while (!Thread.interrupted()) {
                    try {
                        boolean z;
                        boolean z2;
                        synchronized (this.mLock) {
                            while (!this.mNeedWindowListUpdate && !this.mNeedFocusedWindowUpdate) {
                                this.mLock.wait();
                            }
                            if (this.mNeedWindowListUpdate) {
                                this.mNeedWindowListUpdate = false;
                                z = true;
                            } else {
                                z = false;
                            }
                            if (this.mNeedFocusedWindowUpdate) {
                                this.mNeedFocusedWindowUpdate = false;
                                z2 = true;
                            } else {
                                z2 = false;
                            }
                        }
                        if (z) {
                            bufferedWriter2.write("LIST UPDATE\n");
                            bufferedWriter2.flush();
                        }
                        if (z2) {
                            bufferedWriter2.write("FOCUS UPDATE\n");
                            bufferedWriter2.flush();
                        }
                    } catch (Exception e2) {
                        e = e2;
                    } catch (Throwable th) {
                        e = th;
                    }
                }
                if (bufferedWriter2 != null) {
                    try {
                        bufferedWriter2.close();
                    } catch (IOException e3) {
                    }
                }
                ViewServer.this.removeWindowListener(this);
            } catch (Exception e4) {
                e = e4;
                bufferedWriter2 = null;
                try {
                    Log.w(ViewServer.LOG_TAG, "Connection error: ", e);
                    if (bufferedWriter2 != null) {
                        try {
                            bufferedWriter2.close();
                        } catch (IOException e5) {
                        }
                    }
                    ViewServer.this.removeWindowListener(this);
                    return true;
                } catch (Throwable th2) {
                    e = th2;
                    bufferedWriter = bufferedWriter2;
                    bufferedWriter2 = bufferedWriter;
                    if (bufferedWriter2 != null) {
                        try {
                            bufferedWriter2.close();
                        } catch (IOException e6) {
                        }
                    }
                    ViewServer.this.removeWindowListener(this);
                    throw e;
                }
            } catch (Throwable th3) {
                e = th3;
                bufferedWriter2 = bufferedWriter;
                if (bufferedWriter2 != null) {
                    bufferedWriter2.close();
                }
                ViewServer.this.removeWindowListener(this);
                throw e;
            }
            return true;
        }

        public void focusChanged() {
            synchronized (this.mLock) {
                this.mNeedFocusedWindowUpdate = true;
                this.mLock.notifyAll();
            }
        }

        public void run() {
            BufferedReader bufferedReader;
            Throwable e;
            Throwable th;
            BufferedReader bufferedReader2 = null;
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(this.mClient.getInputStream()), 1024);
                try {
                    String str;
                    String readLine = bufferedReader.readLine();
                    int indexOf = readLine.indexOf(32);
                    if (indexOf == -1) {
                        str = HttpVersions.HTTP_0_9;
                    } else {
                        String substring = readLine.substring(0, indexOf);
                        str = readLine.substring(indexOf + 1);
                        readLine = substring;
                    }
                    boolean access$200 = ViewServer.COMMAND_PROTOCOL_VERSION.equalsIgnoreCase(readLine) ? ViewServer.writeValue(this.mClient, "4") : ViewServer.COMMAND_SERVER_VERSION.equalsIgnoreCase(readLine) ? ViewServer.writeValue(this.mClient, "4") : ViewServer.COMMAND_WINDOW_MANAGER_LIST.equalsIgnoreCase(readLine) ? listWindows(this.mClient) : ViewServer.COMMAND_WINDOW_MANAGER_GET_FOCUS.equalsIgnoreCase(readLine) ? getFocusedWindow(this.mClient) : ViewServer.COMMAND_WINDOW_MANAGER_AUTOLIST.equalsIgnoreCase(readLine) ? windowManagerAutolistLoop() : windowCommand(this.mClient, readLine, str);
                    if (!access$200) {
                        Log.w(ViewServer.LOG_TAG, "An error occurred with the command: " + readLine);
                    }
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    }
                    if (this.mClient != null) {
                        try {
                            this.mClient.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                        }
                    }
                } catch (IOException e3) {
                    e = e3;
                    try {
                        Log.w(ViewServer.LOG_TAG, "Connection error: ", e);
                        if (bufferedReader != null) {
                            try {
                                bufferedReader.close();
                            } catch (IOException e222) {
                                e222.printStackTrace();
                            }
                        }
                        if (this.mClient != null) {
                            try {
                                this.mClient.close();
                            } catch (IOException e2222) {
                                e2222.printStackTrace();
                            }
                        }
                    } catch (Throwable th2) {
                        e = th2;
                        bufferedReader2 = bufferedReader;
                        th = e;
                        if (bufferedReader2 != null) {
                            try {
                                bufferedReader2.close();
                            } catch (IOException e22222) {
                                e22222.printStackTrace();
                            }
                        }
                        if (this.mClient != null) {
                            try {
                                this.mClient.close();
                            } catch (IOException e222222) {
                                e222222.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (Throwable e4) {
                    bufferedReader2 = bufferedReader;
                    th = e4;
                    if (bufferedReader2 != null) {
                        bufferedReader2.close();
                    }
                    if (this.mClient != null) {
                        this.mClient.close();
                    }
                    throw th;
                }
            } catch (IOException e5) {
                e4 = e5;
                bufferedReader = null;
                Log.w(ViewServer.LOG_TAG, "Connection error: ", e4);
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (this.mClient != null) {
                    this.mClient.close();
                }
            } catch (Throwable th3) {
                e4 = th3;
                th = e4;
                if (bufferedReader2 != null) {
                    bufferedReader2.close();
                }
                if (this.mClient != null) {
                    this.mClient.close();
                }
                throw th;
            }
        }

        public void windowsChanged() {
            synchronized (this.mLock) {
                this.mNeedWindowListUpdate = true;
                this.mLock.notifyAll();
            }
        }
    }

    private ViewServer() {
        this.mListeners = new CopyOnWriteArrayList();
        this.mWindows = new HashMap();
        this.mWindowsLock = new ReentrantReadWriteLock();
        this.mFocusLock = new ReentrantReadWriteLock();
        this.mPort = -1;
    }

    private ViewServer(int i) {
        this.mListeners = new CopyOnWriteArrayList();
        this.mWindows = new HashMap();
        this.mWindowsLock = new ReentrantReadWriteLock();
        this.mFocusLock = new ReentrantReadWriteLock();
        this.mPort = i;
    }

    private void addWindowListener(WindowListener windowListener) {
        if (!this.mListeners.contains(windowListener)) {
            this.mListeners.add(windowListener);
        }
    }

    private void fireFocusChangedEvent() {
        for (WindowListener focusChanged : this.mListeners) {
            focusChanged.focusChanged();
        }
    }

    private void fireWindowsChangedEvent() {
        for (WindowListener windowsChanged : this.mListeners) {
            windowsChanged.windowsChanged();
        }
    }

    public static ViewServer get(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        if (!BUILD_TYPE_USER.equals(Build.TYPE) || (applicationInfo.flags & 2) == 0) {
            sServer = new NoopViewServer();
        } else {
            if (sServer == null) {
                sServer = new ViewServer((int) VIEW_SERVER_DEFAULT_PORT);
            }
            if (!sServer.isRunning()) {
                try {
                    sServer.start();
                } catch (Throwable e) {
                    Log.d(LOG_TAG, "Error:", e);
                }
            }
        }
        return sServer;
    }

    private void removeWindowListener(WindowListener windowListener) {
        this.mListeners.remove(windowListener);
    }

    private static boolean writeValue(Socket socket, String str) {
        BufferedWriter bufferedWriter;
        BufferedWriter bufferedWriter2;
        Throwable th;
        boolean z = true;
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()), 8192);
            try {
                bufferedWriter.write(str);
                bufferedWriter.write("\n");
                bufferedWriter.flush();
                if (bufferedWriter != null) {
                    try {
                        bufferedWriter.close();
                    } catch (IOException e) {
                        z = false;
                    }
                }
            } catch (Exception e2) {
                bufferedWriter2 = bufferedWriter;
                if (bufferedWriter2 == null) {
                    try {
                        bufferedWriter2.close();
                        return false;
                    } catch (IOException e3) {
                        return false;
                    }
                }
                z = false;
                return z;
            } catch (Throwable th2) {
                th = th2;
                if (bufferedWriter != null) {
                    try {
                        bufferedWriter.close();
                    } catch (IOException e4) {
                    }
                }
                throw th;
            }
        } catch (Exception e5) {
            bufferedWriter2 = null;
            if (bufferedWriter2 == null) {
                z = false;
                return z;
            }
            bufferedWriter2.close();
            return false;
        } catch (Throwable th3) {
            th = th3;
            bufferedWriter = null;
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            throw th;
        }
        return z;
    }

    public void addWindow(Activity activity) {
        Object charSequence = activity.getTitle().toString();
        addWindow(activity.getWindow().getDecorView(), TextUtils.isEmpty(charSequence) ? activity.getClass().getCanonicalName() + "/0x" + System.identityHashCode(activity) : charSequence + "(" + activity.getClass().getCanonicalName() + ")");
    }

    public void addWindow(View view, String str) {
        this.mWindowsLock.writeLock().lock();
        try {
            this.mWindows.put(view.getRootView(), str);
            fireWindowsChangedEvent();
        } finally {
            this.mWindowsLock.writeLock().unlock();
        }
    }

    public boolean isRunning() {
        return this.mThread != null && this.mThread.isAlive();
    }

    public void removeWindow(Activity activity) {
        removeWindow(activity.getWindow().getDecorView());
    }

    public void removeWindow(View view) {
        this.mWindowsLock.writeLock().lock();
        try {
            this.mWindows.remove(view.getRootView());
            fireWindowsChangedEvent();
        } finally {
            this.mWindowsLock.writeLock().unlock();
        }
    }

    public void run() {
        try {
            this.mServer = new ServerSocket(this.mPort, 10, InetAddress.getLocalHost());
        } catch (Throwable e) {
            Log.w(LOG_TAG, "Starting ServerSocket error: ", e);
        }
        while (Thread.currentThread() == this.mThread) {
            try {
                Socket accept = this.mServer.accept();
                if (this.mThreadPool != null) {
                    this.mThreadPool.submit(new ViewServerWorker(accept));
                } else {
                    try {
                        accept.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
            } catch (Throwable e3) {
                Log.w(LOG_TAG, "Connection error: ", e3);
            }
        }
    }

    public void setFocusedWindow(Activity activity) {
        setFocusedWindow(activity.getWindow().getDecorView());
    }

    public void setFocusedWindow(View view) {
        this.mFocusLock.writeLock().lock();
        try {
            this.mFocusedWindow = view == null ? null : view.getRootView();
            fireFocusChangedEvent();
        } finally {
            this.mFocusLock.writeLock().unlock();
        }
    }

    public boolean start() throws IOException {
        if (this.mThread != null) {
            return false;
        }
        this.mThread = new Thread(this, "Local View Server [port=" + this.mPort + "]");
        this.mThreadPool = Executors.newFixedThreadPool(10);
        this.mThread.start();
        return true;
    }

    public boolean stop() {
        if (this.mThread != null) {
            this.mThread.interrupt();
            if (this.mThreadPool != null) {
                try {
                    this.mThreadPool.shutdownNow();
                } catch (SecurityException e) {
                    Log.w(LOG_TAG, "Could not stop all view server threads");
                }
            }
            this.mThreadPool = null;
            this.mThread = null;
            try {
                this.mServer.close();
                this.mServer = null;
                return true;
            } catch (IOException e2) {
                Log.w(LOG_TAG, "Could not close the view server");
            }
        }
        this.mWindowsLock.writeLock().lock();
        try {
            this.mWindows.clear();
            this.mFocusLock.writeLock().lock();
            try {
                this.mFocusedWindow = null;
                return false;
            } finally {
                this.mFocusLock.writeLock().unlock();
            }
        } finally {
            this.mWindowsLock.writeLock().unlock();
        }
    }
}
