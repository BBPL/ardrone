package com.google.android.gms.common.images;

import android.app.ActivityManager;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.os.ResultReceiver;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.util.Log;
import android.widget.ImageView;
import com.google.android.gms.common.images.C0062a.C0061a;
import com.google.android.gms.internal.C0055w;
import com.google.android.gms.internal.C0219h;
import com.google.android.gms.internal.as;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ImageManager {
    private static final Object aq = new Object();
    private static HashSet<Uri> ar = new HashSet();
    private static ImageManager as;
    private static ImageManager at;
    private final ExecutorService au = Executors.newFixedThreadPool(4);
    private final C0056b av;
    private final Map<C0062a, ImageReceiver> aw;
    private final Map<Uri, ImageReceiver> ax;
    private final Context mContext;
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    private final class ImageReceiver extends ResultReceiver {
        final /* synthetic */ ImageManager aA;
        private final ArrayList<C0062a> ay;
        boolean az = false;
        private final Uri mUri;

        ImageReceiver(ImageManager imageManager, Uri uri) {
            this.aA = imageManager;
            super(new Handler(Looper.getMainLooper()));
            this.mUri = uri;
            this.ay = new ArrayList();
        }

        public void m63c(C0062a c0062a) {
            C0219h.m1133a(!this.az, "Cannot add an ImageRequest when mHandlingRequests is true");
            C0219h.m1135f("ImageReceiver.addImageRequest() must be called in the main thread");
            this.ay.add(c0062a);
        }

        public void m64d(C0062a c0062a) {
            C0219h.m1133a(!this.az, "Cannot remove an ImageRequest when mHandlingRequests is true");
            C0219h.m1135f("ImageReceiver.removeImageRequest() must be called in the main thread");
            this.ay.remove(c0062a);
        }

        public void onReceiveResult(int i, Bundle bundle) {
            this.aA.au.execute(new C0057c(this.aA, this.mUri, (ParcelFileDescriptor) bundle.getParcelable("com.google.android.gms.extra.fileDescriptor")));
        }

        public void m65q() {
            Intent intent = new Intent("com.google.android.gms.common.images.LOAD_IMAGE");
            intent.putExtra("com.google.android.gms.extras.uri", this.mUri);
            intent.putExtra("com.google.android.gms.extras.resultReceiver", this);
            intent.putExtra("com.google.android.gms.extras.priority", 3);
            this.aA.mContext.sendBroadcast(intent);
        }
    }

    public interface OnImageLoadedListener {
        void onImageLoaded(Uri uri, Drawable drawable);
    }

    private static final class C0054a {
        static int m66a(ActivityManager activityManager) {
            return activityManager.getLargeMemoryClass();
        }
    }

    private static final class C0056b extends C0055w<C0061a, Bitmap> {
        public C0056b(Context context) {
            super(C0056b.m68e(context));
        }

        private static int m68e(Context context) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
            int memoryClass = (((context.getApplicationInfo().flags & AccessibilityEventCompat.TYPE_TOUCH_INTERACTION_START) != 0 ? 1 : null) == null || !as.an()) ? activityManager.getMemoryClass() : C0054a.m66a(activityManager);
            return (int) (((float) (memoryClass * AccessibilityEventCompat.TYPE_TOUCH_INTERACTION_START)) * 0.33f);
        }

        protected int m69a(C0061a c0061a, Bitmap bitmap) {
            return bitmap.getHeight() * bitmap.getRowBytes();
        }

        protected void m70a(boolean z, C0061a c0061a, Bitmap bitmap, Bitmap bitmap2) {
            super.entryRemoved(z, c0061a, bitmap, bitmap2);
        }

        protected /* synthetic */ void entryRemoved(boolean z, Object obj, Object obj2, Object obj3) {
            m70a(z, (C0061a) obj, (Bitmap) obj2, (Bitmap) obj3);
        }

        protected /* synthetic */ int sizeOf(Object obj, Object obj2) {
            return m69a((C0061a) obj, (Bitmap) obj2);
        }
    }

    private final class C0057c implements Runnable {
        final /* synthetic */ ImageManager aA;
        private final ParcelFileDescriptor aB;
        private final Uri mUri;

        public C0057c(ImageManager imageManager, Uri uri, ParcelFileDescriptor parcelFileDescriptor) {
            this.aA = imageManager;
            this.mUri = uri;
            this.aB = parcelFileDescriptor;
        }

        public void run() {
            Bitmap bitmap = null;
            boolean z = false;
            C0219h.m1136g("LoadBitmapFromDiskRunnable can't be executed in the main thread");
            if (this.aB != null) {
                try {
                    bitmap = BitmapFactory.decodeFileDescriptor(this.aB.getFileDescriptor());
                } catch (Throwable e) {
                    Log.e("ImageManager", "OOM while loading bitmap for uri: " + this.mUri, e);
                    z = true;
                }
                try {
                    this.aB.close();
                } catch (Throwable e2) {
                    Log.e("ImageManager", "closed failed", e2);
                }
            }
            CountDownLatch countDownLatch = new CountDownLatch(1);
            this.aA.mHandler.post(new C0060f(this.aA, this.mUri, bitmap, z, countDownLatch));
            try {
                countDownLatch.await();
            } catch (InterruptedException e3) {
                Log.w("ImageManager", "Latch interrupted while posting " + this.mUri);
            }
        }
    }

    private final class C0058d implements Runnable {
        final /* synthetic */ ImageManager aA;
        private final C0062a aC;

        public C0058d(ImageManager imageManager, C0062a c0062a) {
            this.aA = imageManager;
            this.aC = c0062a;
        }

        public void run() {
            C0219h.m1135f("LoadImageRunnable must be executed on the main thread");
            this.aA.m78b(this.aC);
            C0061a c0061a = this.aC.aG;
            if (c0061a.uri == null) {
                this.aC.m95b(this.aA.mContext, true);
                return;
            }
            Bitmap a = this.aA.m74a(c0061a);
            if (a != null) {
                this.aC.m92a(this.aA.mContext, a, true);
                return;
            }
            this.aC.m96f(this.aA.mContext);
            ImageReceiver imageReceiver = (ImageReceiver) this.aA.ax.get(c0061a.uri);
            if (imageReceiver == null) {
                imageReceiver = new ImageReceiver(this.aA, c0061a.uri);
                this.aA.ax.put(c0061a.uri, imageReceiver);
            }
            imageReceiver.m63c(this.aC);
            if (this.aC.aJ != 1) {
                this.aA.aw.put(this.aC, imageReceiver);
            }
            synchronized (ImageManager.aq) {
                if (!ImageManager.ar.contains(c0061a.uri)) {
                    ImageManager.ar.add(c0061a.uri);
                    imageReceiver.m65q();
                }
            }
        }
    }

    private static final class C0059e implements ComponentCallbacks2 {
        private final C0056b av;

        public C0059e(C0056b c0056b) {
            this.av = c0056b;
        }

        public void onConfigurationChanged(Configuration configuration) {
        }

        public void onLowMemory() {
            this.av.evictAll();
        }

        public void onTrimMemory(int i) {
            if (i >= 60) {
                this.av.evictAll();
            } else if (i >= 20) {
                this.av.trimToSize(this.av.size() / 2);
            }
        }
    }

    private final class C0060f implements Runnable {
        final /* synthetic */ ImageManager aA;
        private final Bitmap aD;
        private final CountDownLatch aE;
        private boolean aF;
        private final Uri mUri;

        public C0060f(ImageManager imageManager, Uri uri, Bitmap bitmap, boolean z, CountDownLatch countDownLatch) {
            this.aA = imageManager;
            this.mUri = uri;
            this.aD = bitmap;
            this.aF = z;
            this.aE = countDownLatch;
        }

        private void m71a(ImageReceiver imageReceiver, boolean z) {
            imageReceiver.az = true;
            ArrayList a = imageReceiver.ay;
            int size = a.size();
            for (int i = 0; i < size; i++) {
                C0062a c0062a = (C0062a) a.get(i);
                if (z) {
                    c0062a.m92a(this.aA.mContext, this.aD, false);
                } else {
                    c0062a.m95b(this.aA.mContext, false);
                }
                if (c0062a.aJ != 1) {
                    this.aA.aw.remove(c0062a);
                }
            }
            imageReceiver.az = false;
        }

        public void run() {
            C0219h.m1135f("OnBitmapLoadedRunnable must be executed in the main thread");
            boolean z = this.aD != null;
            if (this.aA.av != null) {
                if (this.aF) {
                    this.aA.av.evictAll();
                    System.gc();
                    this.aF = false;
                    this.aA.mHandler.post(this);
                    return;
                } else if (z) {
                    this.aA.av.put(new C0061a(this.mUri), this.aD);
                }
            }
            ImageReceiver imageReceiver = (ImageReceiver) this.aA.ax.remove(this.mUri);
            if (imageReceiver != null) {
                m71a(imageReceiver, z);
            }
            this.aE.countDown();
            synchronized (ImageManager.aq) {
                ImageManager.ar.remove(this.mUri);
            }
        }
    }

    private ImageManager(Context context, boolean z) {
        this.mContext = context.getApplicationContext();
        if (z) {
            this.av = new C0056b(this.mContext);
            if (as.aq()) {
                m83n();
            }
        } else {
            this.av = null;
        }
        this.aw = new HashMap();
        this.ax = new HashMap();
    }

    private Bitmap m74a(C0061a c0061a) {
        return this.av == null ? null : (Bitmap) this.av.get(c0061a);
    }

    public static ImageManager m75a(Context context, boolean z) {
        if (z) {
            if (at == null) {
                at = new ImageManager(context, true);
            }
            return at;
        }
        if (as == null) {
            as = new ImageManager(context, false);
        }
        return as;
    }

    private boolean m78b(C0062a c0062a) {
        C0219h.m1135f("ImageManager.cleanupHashMaps() must be called in the main thread");
        if (c0062a.aJ == 1) {
            return true;
        }
        ImageReceiver imageReceiver = (ImageReceiver) this.aw.get(c0062a);
        if (imageReceiver == null) {
            return true;
        }
        if (imageReceiver.az) {
            return false;
        }
        this.aw.remove(c0062a);
        imageReceiver.m64d(c0062a);
        return true;
    }

    public static ImageManager create(Context context) {
        return m75a(context, false);
    }

    private void m83n() {
        this.mContext.registerComponentCallbacks(new C0059e(this.av));
    }

    public void m86a(C0062a c0062a) {
        C0219h.m1135f("ImageManager.loadImage() must be called in the main thread");
        boolean b = m78b(c0062a);
        Runnable c0058d = new C0058d(this, c0062a);
        if (b) {
            c0058d.run();
        } else {
            this.mHandler.post(c0058d);
        }
    }

    public void loadImage(ImageView imageView, int i) {
        C0062a c0062a = new C0062a(i);
        c0062a.m93a(imageView);
        m86a(c0062a);
    }

    public void loadImage(ImageView imageView, Uri uri) {
        C0062a c0062a = new C0062a(uri);
        c0062a.m93a(imageView);
        m86a(c0062a);
    }

    public void loadImage(ImageView imageView, Uri uri, int i) {
        C0062a c0062a = new C0062a(uri);
        c0062a.m97j(i);
        c0062a.m93a(imageView);
        m86a(c0062a);
    }

    public void loadImage(OnImageLoadedListener onImageLoadedListener, Uri uri) {
        C0062a c0062a = new C0062a(uri);
        c0062a.m94a(onImageLoadedListener);
        m86a(c0062a);
    }

    public void loadImage(OnImageLoadedListener onImageLoadedListener, Uri uri, int i) {
        C0062a c0062a = new C0062a(uri);
        c0062a.m97j(i);
        c0062a.m94a(onImageLoadedListener);
        m86a(c0062a);
    }
}
