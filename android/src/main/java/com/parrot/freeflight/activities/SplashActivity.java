package com.parrot.freeflight.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.CamcorderProfile;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.VideoView;
import com.parrot.ardronetool.utils.DeviceCapabilitiesUtils;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.academy.services.restoremedia.RestoreFlightMediaService;
import com.parrot.freeflight.activities.base.ParrotActivity;

public class SplashActivity extends ParrotActivity implements Runnable {
    public static final int SPLASH_TIME = 4000;
    private static final String TAG = SplashActivity.class.getSimpleName();
    public static final String VIDEO_FILE_NAME = "arfreeflight";
    private boolean isActive;
    protected boolean playIntro = false;
    private LinearLayout splashBackgroundView;
    private Thread thread;
    private int timeElapsed = 0;
    private View videoContainer;
    private VideoView videoView;

    class C11081 implements OnCompletionListener {
        C11081() {
        }

        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.release();
        }
    }

    class C11112 implements Runnable {

        class C11091 implements OnTouchListener {
            C11091() {
            }

            public boolean onTouch(View view, MotionEvent motionEvent) {
                SplashActivity.this.videoView.stopPlayback();
                SplashActivity.this.videoView.setEnabled(false);
                SplashActivity.this.videoContainer.setVisibility(8);
                SplashActivity.this.startActivity(new Intent(SplashActivity.this, ConnectActivity.class));
                SplashActivity.this.finish();
                return true;
            }
        }

        class C11102 implements OnCompletionListener {
            C11102() {
            }

            public void onCompletion(MediaPlayer mediaPlayer) {
                SplashActivity.this.proceedToNextActivity();
            }
        }

        C11112() {
        }

        public void run() {
            SplashActivity.this.splashBackgroundView.setVisibility(8);
            SplashActivity.this.videoContainer.setVisibility(0);
            SplashActivity.this.videoView.setOnTouchListener(new C11091());
            SplashActivity.this.videoView.setOnCompletionListener(new C11102());
            SplashActivity.this.videoView.start();
        }
    }

    private void dumpDeviceInfo() {
        Log.i(TAG, "============== Device info >>>>>>>>");
        Log.i(TAG, "Brand: " + Build.BRAND);
        Log.i(TAG, "Device: " + Build.DEVICE);
        Log.i(TAG, "Manufacturer: " + Build.MANUFACTURER);
        Log.i(TAG, "Model: " + Build.MODEL);
        Log.i(TAG, "Product: " + Build.PRODUCT);
        Log.i(TAG, "Type: " + Build.TYPE);
        Log.i(TAG, "<<<<<<<< Device info ==============");
    }

    @SuppressLint({"NewApi"})
    private void dumpVideoCapabilitiesInfo() {
        Log.i(TAG, "=== DEVICE VIDEO SUPPORT ====>>>>>>>>>");
        if (VERSION.SDK_INT >= 16) {
            Log.i(TAG, "Codecs available to the system: ");
            for (int i = 0; i < MediaCodecList.getCodecCount(); i++) {
                MediaCodecInfo codecInfoAt = MediaCodecList.getCodecInfoAt(i);
                String[] supportedTypes = codecInfoAt.getSupportedTypes();
                StringBuilder stringBuilder = new StringBuilder();
                for (int i2 = 0; i2 < supportedTypes.length; i2++) {
                    stringBuilder.append(supportedTypes[i2]);
                    if (i2 < supportedTypes.length - 1) {
                        stringBuilder.append(", ");
                    }
                }
                Log.i(TAG, codecInfoAt.getName() + " , supported types: " + stringBuilder.toString());
            }
        }
        if (VERSION.SDK_INT >= 11) {
            if (CamcorderProfile.hasProfile(5)) {
                Log.i(TAG, "Device supports HD video [720p]");
            } else if (CamcorderProfile.hasProfile(4)) {
                Log.i(TAG, "Device supports regular video [480p]");
            } else if (CamcorderProfile.hasProfile(7)) {
                Log.i(TAG, "Device supports low quality video [240p]");
            } else {
                Log.w(TAG, "Can't determine video support of this device.");
            }
        }
        CamcorderProfile camcorderProfile = CamcorderProfile.get(1);
        if (camcorderProfile != null) {
            Log.i(TAG, "Highest video frame size for this device is [" + camcorderProfile.videoFrameWidth + ", " + camcorderProfile.videoFrameHeight + "]");
        } else {
            Log.w(TAG, "Unable to determine highest possible video frame size.");
        }
        Log.i(TAG, "<<<<<<<<<=== DEVICE VIDEO SUPPORT ===");
    }

    private boolean isNvidiaShield() {
        return Build.MODEL.equals("SHIELD");
    }

    private void playIntro() {
        runOnUiThread(new C11112());
    }

    private void proceedToNextActivity() {
        if (this.videoContainer != null) {
            this.videoContainer.setVisibility(8);
        }
        startActivity(new Intent(this, DashboardActivity.class));
    }

    private boolean rawResourceExists(String str) {
        return getResources().getIdentifier(VIDEO_FILE_NAME, "raw", getPackageName()) != 0;
    }

    private void stopThreadAndJoin() {
        this.isActive = false;
        try {
            if (this.thread != null) {
                this.thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (!isFinishing()) {
            setContentView(C0984R.layout.splash_screen);
            if (isNvidiaShield()) {
                ((ImageView) findViewById(C0984R.id.imageView)).setImageResource(C0984R.drawable.splashscreen_nvidia_shield);
                MediaPlayer create = MediaPlayer.create(this, C0984R.raw.nvidia_intro);
                create.setOnCompletionListener(new C11081());
                create.start();
            }
            this.videoContainer = findViewById(C0984R.id.videoContainer);
            this.videoView = (VideoView) findViewById(C0984R.id.videoView);
            if (this.videoView != null) {
                if (rawResourceExists(VIDEO_FILE_NAME)) {
                    this.playIntro = true;
                    this.videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/raw/" + VIDEO_FILE_NAME));
                } else {
                    this.playIntro = false;
                }
            }
            this.splashBackgroundView = (LinearLayout) findViewById(C0984R.id.splash);
            this.isActive = true;
            this.thread = new Thread(this);
            this.thread.start();
            DeviceCapabilitiesUtils.dumpScreenSizeInDips(this);
        }
    }

    protected void onDestroy() {
        stopThreadAndJoin();
        super.onDestroy();
    }

    protected void onPause() {
        super.onPause();
        stopThreadAndJoin();
    }

    protected void onResume() {
        super.onResume();
        dumpDeviceInfo();
        dumpVideoCapabilitiesInfo();
        startService(new Intent(this, RestoreFlightMediaService.class));
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() != 1) {
            return super.onTouchEvent(motionEvent);
        }
        this.isActive = false;
        return true;
    }

    public void run() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x002f in list []
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:42)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
        /*
        r2 = this;
    L_0x0000:
        r0 = r2.isActive;	 Catch:{ InterruptedException -> 0x001a, all -> 0x0033 }
        if (r0 == 0) goto L_0x0023;	 Catch:{ InterruptedException -> 0x001a, all -> 0x0033 }
    L_0x0004:
        r0 = r2.timeElapsed;	 Catch:{ InterruptedException -> 0x001a, all -> 0x0033 }
        r1 = 4000; // 0xfa0 float:5.605E-42 double:1.9763E-320;	 Catch:{ InterruptedException -> 0x001a, all -> 0x0033 }
        if (r0 >= r1) goto L_0x0023;	 Catch:{ InterruptedException -> 0x001a, all -> 0x0033 }
    L_0x000a:
        r0 = 100;	 Catch:{ InterruptedException -> 0x001a, all -> 0x0033 }
        java.lang.Thread.sleep(r0);	 Catch:{ InterruptedException -> 0x001a, all -> 0x0033 }
        r0 = r2.isActive;	 Catch:{ InterruptedException -> 0x001a, all -> 0x0033 }
        if (r0 == 0) goto L_0x0000;	 Catch:{ InterruptedException -> 0x001a, all -> 0x0033 }
    L_0x0013:
        r0 = r2.timeElapsed;	 Catch:{ InterruptedException -> 0x001a, all -> 0x0033 }
        r0 = r0 + 100;	 Catch:{ InterruptedException -> 0x001a, all -> 0x0033 }
        r2.timeElapsed = r0;	 Catch:{ InterruptedException -> 0x001a, all -> 0x0033 }
        goto L_0x0000;
    L_0x001a:
        r0 = move-exception;
        r0 = r2.playIntro;
        if (r0 == 0) goto L_0x002f;
    L_0x001f:
        r2.playIntro();
    L_0x0022:
        return;
    L_0x0023:
        r0 = r2.playIntro;
        if (r0 == 0) goto L_0x002b;
    L_0x0027:
        r2.playIntro();
        goto L_0x0022;
    L_0x002b:
        r2.proceedToNextActivity();
        goto L_0x0022;
    L_0x002f:
        r2.proceedToNextActivity();
        goto L_0x0022;
    L_0x0033:
        r0 = move-exception;
        r1 = r2.playIntro;
        if (r1 == 0) goto L_0x003c;
    L_0x0038:
        r2.playIntro();
    L_0x003b:
        throw r0;
    L_0x003c:
        r2.proceedToNextActivity();
        goto L_0x003b;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.parrot.freeflight.activities.SplashActivity.run():void");
    }
}
