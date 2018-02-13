package com.google.android.gms.common;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class C0046a implements ServiceConnection {
    private final BlockingQueue<IBinder> f40A = new LinkedBlockingQueue();
    boolean f41z = false;

    public IBinder m32e() throws InterruptedException {
        if (this.f41z) {
            throw new IllegalStateException();
        }
        this.f41z = true;
        return (IBinder) this.f40A.take();
    }

    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        try {
            this.f40A.put(iBinder);
        } catch (InterruptedException e) {
        }
    }

    public void onServiceDisconnected(ComponentName componentName) {
    }
}
