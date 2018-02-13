package com.google.android.gms.gcm;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import com.google.android.gms.common.GooglePlayServicesUtil;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class GoogleCloudMessaging {
    public static final String ERROR_MAIN_THREAD = "MAIN_THREAD";
    public static final String ERROR_SERVICE_NOT_AVAILABLE = "SERVICE_NOT_AVAILABLE";
    public static final String MESSAGE_TYPE_DELETED = "deleted_messages";
    public static final String MESSAGE_TYPE_MESSAGE = "gcm";
    public static final String MESSAGE_TYPE_SEND_ERROR = "send_error";
    static GoogleCloudMessaging fh;
    private Context fi;
    private PendingIntent fj;
    final BlockingQueue<Intent> fk = new LinkedBlockingQueue();
    private Handler fl = new Handler(this, Looper.getMainLooper()) {
        final /* synthetic */ GoogleCloudMessaging fn;

        public void handleMessage(Message message) {
            this.fn.fk.add((Intent) message.obj);
        }
    };
    private Messenger fm = new Messenger(this.fl);

    private void aO() {
        Intent intent = new Intent("com.google.android.c2dm.intent.UNREGISTER");
        intent.setPackage(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE);
        this.fk.clear();
        intent.putExtra("google.messenger", this.fm);
        m245c(intent);
        this.fi.startService(intent);
    }

    private void m243b(String... strArr) {
        String c = m244c(strArr);
        Intent intent = new Intent("com.google.android.c2dm.intent.REGISTER");
        intent.setPackage(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE);
        intent.putExtra("google.messenger", this.fm);
        m245c(intent);
        intent.putExtra("sender", c);
        this.fi.startService(intent);
    }

    public static GoogleCloudMessaging getInstance(Context context) {
        synchronized (GoogleCloudMessaging.class) {
            try {
                if (fh == null) {
                    fh = new GoogleCloudMessaging();
                    fh.fi = context;
                }
                GoogleCloudMessaging googleCloudMessaging = fh;
                return googleCloudMessaging;
            } finally {
                Object obj = GoogleCloudMessaging.class;
            }
        }
    }

    void aP() {
        synchronized (this) {
            if (this.fj != null) {
                this.fj.cancel();
                this.fj = null;
            }
        }
    }

    String m244c(String... strArr) {
        if (strArr == null || strArr.length == 0) {
            throw new IllegalArgumentException("No senderIds");
        }
        StringBuilder stringBuilder = new StringBuilder(strArr[0]);
        for (int i = 1; i < strArr.length; i++) {
            stringBuilder.append(',').append(strArr[i]);
        }
        return stringBuilder.toString();
    }

    void m245c(Intent intent) {
        synchronized (this) {
            if (this.fj == null) {
                this.fj = PendingIntent.getBroadcast(this.fi, 0, new Intent(), 0);
            }
            intent.putExtra("app", this.fj);
        }
    }

    public void close() {
        aP();
    }

    public String getMessageType(Intent intent) {
        if (!"com.google.android.c2dm.intent.RECEIVE".equals(intent.getAction())) {
            return null;
        }
        String stringExtra = intent.getStringExtra("message_type");
        return stringExtra == null ? MESSAGE_TYPE_MESSAGE : stringExtra;
    }

    public String register(String... strArr) throws IOException {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            throw new IOException(ERROR_MAIN_THREAD);
        }
        this.fk.clear();
        m243b(strArr);
        try {
            Intent intent = (Intent) this.fk.poll(5000, TimeUnit.MILLISECONDS);
            if (intent == null) {
                throw new IOException(ERROR_SERVICE_NOT_AVAILABLE);
            }
            String stringExtra = intent.getStringExtra("registration_id");
            if (stringExtra != null) {
                return stringExtra;
            }
            intent.getStringExtra("error");
            String stringExtra2 = intent.getStringExtra("error");
            if (stringExtra2 != null) {
                throw new IOException(stringExtra2);
            }
            throw new IOException(ERROR_SERVICE_NOT_AVAILABLE);
        } catch (InterruptedException e) {
            throw new IOException(e.getMessage());
        }
    }

    public void send(String str, String str2, long j, Bundle bundle) throws IOException {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            throw new IOException(ERROR_MAIN_THREAD);
        } else if (str == null) {
            throw new IllegalArgumentException("Missing 'to'");
        } else {
            Intent intent = new Intent("com.google.android.gcm.intent.SEND");
            intent.putExtras(bundle);
            m245c(intent);
            intent.putExtra("google.to", str);
            intent.putExtra("google.message_id", str2);
            intent.putExtra("google.ttl", Long.toString(j));
            this.fi.sendOrderedBroadcast(intent, null);
        }
    }

    public void send(String str, String str2, Bundle bundle) throws IOException {
        send(str, str2, -1, bundle);
    }

    public void unregister() throws IOException {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            throw new IOException(ERROR_MAIN_THREAD);
        }
        aO();
        try {
            Intent intent = (Intent) this.fk.poll(5000, TimeUnit.MILLISECONDS);
            if (intent == null) {
                throw new IOException(ERROR_SERVICE_NOT_AVAILABLE);
            } else if (intent.getStringExtra("unregistered") == null) {
                String stringExtra = intent.getStringExtra("error");
                if (stringExtra != null) {
                    throw new IOException(stringExtra);
                }
                throw new IOException(ERROR_SERVICE_NOT_AVAILABLE);
            }
        } catch (InterruptedException e) {
            throw new IOException(e.getMessage());
        }
    }
}
