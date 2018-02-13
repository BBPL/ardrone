package com.sony.rdis.receiver;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.SparseBooleanArray;
import android.view.MotionEvent;
import java.util.ArrayList;
import java.util.Iterator;

public class RdisHIDManager {
    private static ArrayList<ListenerDelegate> sListeners = new ArrayList();
    private Context mContext;

    private class ListenerDelegate {
        private final Handler mHandler;
        public SparseBooleanArray mMobiles = new SparseBooleanArray();
        final RdisOnTouchListener mTouchEventListener;

        ListenerDelegate(int i, RdisOnTouchListener rdisOnTouchListener) {
            this.mTouchEventListener = rdisOnTouchListener;
            this.mHandler = new Handler(RdisHIDManager.this.mContext.getMainLooper(), RdisHIDManager.this) {
                public void handleMessage(Message message) {
                    ListenerDelegate.this.mTouchEventListener.onTouchEvent((MotionEvent) message.obj);
                }
            };
            addMobile(i);
        }

        void addMobile(int i) {
            this.mMobiles.put(i, true);
        }

        Object getListener() {
            return this.mTouchEventListener;
        }

        boolean hasMobile(int i) {
            return this.mMobiles.get(i);
        }

        void onTouchEventLocked(int i, MotionEvent motionEvent) {
            Message obtain = Message.obtain();
            obtain.what = 0;
            obtain.obj = motionEvent;
            this.mHandler.sendMessage(obtain);
        }

        int removeMobile(int i) {
            this.mMobiles.delete(i);
            return this.mMobiles.size();
        }
    }

    public RdisHIDManager(Context context) {
        this.mContext = context;
    }

    public void recvTouchEvent(int i, MotionEvent motionEvent) {
        synchronized (sListeners) {
            if (sListeners.isEmpty()) {
                return;
            }
            Iterator it = sListeners.iterator();
            while (it.hasNext()) {
                ListenerDelegate listenerDelegate = (ListenerDelegate) it.next();
                if (listenerDelegate.hasMobile(i)) {
                    listenerDelegate.onTouchEventLocked(i, motionEvent);
                }
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean registerOnTouchListener(int r5, com.sony.rdis.receiver.RdisOnTouchListener r6) {
        /*
        r4 = this;
        r1 = sListeners;
        monitor-enter(r1);
        r0 = sListeners;	 Catch:{ all -> 0x0033 }
        r2 = r0.iterator();	 Catch:{ all -> 0x0033 }
    L_0x0009:
        r0 = r2.hasNext();	 Catch:{ all -> 0x0033 }
        if (r0 == 0) goto L_0x0031;
    L_0x000f:
        r0 = r2.next();	 Catch:{ all -> 0x0033 }
        r0 = (com.sony.rdis.receiver.RdisHIDManager.ListenerDelegate) r0;	 Catch:{ all -> 0x0033 }
        r3 = r0.getListener();	 Catch:{ all -> 0x0033 }
        if (r3 != r6) goto L_0x0009;
    L_0x001b:
        if (r0 != 0) goto L_0x002a;
    L_0x001d:
        r0 = new com.sony.rdis.receiver.RdisHIDManager$ListenerDelegate;	 Catch:{ all -> 0x002e }
        r0.<init>(r5, r6);	 Catch:{ all -> 0x002e }
        r2 = sListeners;	 Catch:{ all -> 0x0033 }
        r2.add(r0);	 Catch:{ all -> 0x0033 }
    L_0x0027:
        monitor-exit(r1);	 Catch:{ all -> 0x0033 }
        r0 = 1;
        return r0;
    L_0x002a:
        r0.addMobile(r5);	 Catch:{ all -> 0x002e }
        goto L_0x0027;
    L_0x002e:
        r0 = move-exception;
    L_0x002f:
        monitor-exit(r1);	 Catch:{ all -> 0x0033 }
        throw r0;
    L_0x0031:
        r0 = 0;
        goto L_0x001b;
    L_0x0033:
        r0 = move-exception;
        goto L_0x002f;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sony.rdis.receiver.RdisHIDManager.registerOnTouchListener(int, com.sony.rdis.receiver.RdisOnTouchListener):boolean");
    }

    public void unregisterOnTouchListener(int i, RdisOnTouchListener rdisOnTouchListener) {
        if (rdisOnTouchListener != null) {
            synchronized (sListeners) {
                int size = sListeners.size();
                int i2 = 0;
                while (i2 < size) {
                    ListenerDelegate listenerDelegate = (ListenerDelegate) sListeners.get(i2);
                    if (listenerDelegate.getListener() == rdisOnTouchListener) {
                        if (listenerDelegate.removeMobile(i) == 0) {
                            sListeners.remove(i2);
                        }
                    } else {
                        i2++;
                    }
                }
            }
        }
    }
}
