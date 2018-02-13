package com.parrot.freeflight.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.activities.base.WatchMediaActivity;
import com.parrot.freeflight.vo.MediaVO;

public final class WatchPhotoActivity extends WatchMediaActivity implements OnTouchListener {
    private static final String EXTRA_IS_REMOTE = "EXTRA_IS_REMOTE";
    private static final String TAG = WatchPhotoActivity.class.getSimpleName();
    private boolean isRemote = false;
    private MediaVO media;
    private ProgressBar progress;
    private ImageView view;

    private class DownloadImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
        private DownloadImageAsyncTask() {
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        protected android.graphics.Bitmap doInBackground(java.lang.String... r11) {
            /*
            r10 = this;
            r0 = 0;
            r1 = 0;
            r3 = r11[r1];
            r1 = "Android";
            r4 = android.net.http.AndroidHttpClient.newInstance(r1);
            r5 = new org.apache.http.client.methods.HttpGet;
            r5.<init>(r3);
            r1 = "Authorization";
            r2 = new java.lang.StringBuilder;
            r2.<init>();
            r6 = "Basic ";
            r2 = r2.append(r6);
            r6 = new java.lang.String;
            r7 = new java.lang.StringBuilder;
            r7.<init>();
            r8 = com.parrot.freeflight.academy.utils.AcademyUtils.login;
            r7 = r7.append(r8);
            r8 = ":";
            r7 = r7.append(r8);
            r8 = com.parrot.freeflight.academy.utils.AcademyUtils.password;
            r7 = r7.append(r8);
            r7 = r7.toString();
            r7 = r7.getBytes();
            r8 = 2;
            r7 = android.util.Base64.encode(r7, r8);
            r6.<init>(r7);
            r2 = r2.append(r6);
            r2 = r2.toString();
            r5.addHeader(r1, r2);
            r1 = r4.execute(r5);	 Catch:{ Exception -> 0x00b9 }
            r2 = r1.getStatusLine();	 Catch:{ Exception -> 0x00b9 }
            r2 = r2.getStatusCode();	 Catch:{ Exception -> 0x00b9 }
            r6 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
            if (r2 == r6) goto L_0x0088;
        L_0x0060:
            r1 = "ImageDownloader";
            r6 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00b9 }
            r6.<init>();	 Catch:{ Exception -> 0x00b9 }
            r7 = "Error ";
            r6 = r6.append(r7);	 Catch:{ Exception -> 0x00b9 }
            r2 = r6.append(r2);	 Catch:{ Exception -> 0x00b9 }
            r6 = " while retrieving bitmap from ";
            r2 = r2.append(r6);	 Catch:{ Exception -> 0x00b9 }
            r2 = r2.append(r3);	 Catch:{ Exception -> 0x00b9 }
            r2 = r2.toString();	 Catch:{ Exception -> 0x00b9 }
            android.util.Log.w(r1, r2);	 Catch:{ Exception -> 0x00b9 }
            if (r4 == 0) goto L_0x0087;
        L_0x0084:
            r4.close();
        L_0x0087:
            return r0;
        L_0x0088:
            r6 = r1.getEntity();	 Catch:{ Exception -> 0x00b9 }
            if (r6 == 0) goto L_0x00a8;
        L_0x008e:
            r2 = r6.getContent();	 Catch:{ all -> 0x00ae }
            r1 = new android.graphics.BitmapFactory$Options;	 Catch:{ all -> 0x00e7 }
            r1.<init>();	 Catch:{ all -> 0x00e7 }
            r7 = 1;
            r1.inPurgeable = r7;	 Catch:{ all -> 0x00e7 }
            r7 = 0;
            r1 = android.graphics.BitmapFactory.decodeStream(r2, r7, r1);	 Catch:{ all -> 0x00e7 }
            if (r2 == 0) goto L_0x00a4;
        L_0x00a1:
            r2.close();	 Catch:{ Exception -> 0x00e2 }
        L_0x00a4:
            r6.consumeContent();	 Catch:{ Exception -> 0x00e2 }
            r0 = r1;
        L_0x00a8:
            if (r4 == 0) goto L_0x0087;
        L_0x00aa:
            r4.close();
            goto L_0x0087;
        L_0x00ae:
            r1 = move-exception;
            r2 = r0;
        L_0x00b0:
            if (r2 == 0) goto L_0x00b5;
        L_0x00b2:
            r2.close();	 Catch:{ Exception -> 0x00b9 }
        L_0x00b5:
            r6.consumeContent();	 Catch:{ Exception -> 0x00b9 }
            throw r1;	 Catch:{ Exception -> 0x00b9 }
        L_0x00b9:
            r1 = move-exception;
        L_0x00ba:
            r5.abort();	 Catch:{ all -> 0x00db }
            r2 = "ImageDownloader";
            r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00db }
            r5.<init>();	 Catch:{ all -> 0x00db }
            r6 = "Error while retrieving bitmap from ";
            r5 = r5.append(r6);	 Catch:{ all -> 0x00db }
            r3 = r5.append(r3);	 Catch:{ all -> 0x00db }
            r3 = r3.toString();	 Catch:{ all -> 0x00db }
            android.util.Log.w(r2, r3);	 Catch:{ all -> 0x00db }
            r1.printStackTrace();	 Catch:{ all -> 0x00db }
            if (r4 == 0) goto L_0x0087;
        L_0x00da:
            goto L_0x00aa;
        L_0x00db:
            r0 = move-exception;
            if (r4 == 0) goto L_0x00e1;
        L_0x00de:
            r4.close();
        L_0x00e1:
            throw r0;
        L_0x00e2:
            r0 = move-exception;
            r9 = r0;
            r0 = r1;
            r1 = r9;
            goto L_0x00ba;
        L_0x00e7:
            r1 = move-exception;
            goto L_0x00b0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.parrot.freeflight.activities.WatchPhotoActivity.DownloadImageAsyncTask.doInBackground(java.lang.String[]):android.graphics.Bitmap");
        }

        protected void onPostExecute(Bitmap bitmap) {
            WatchPhotoActivity.this.view.setImageBitmap(bitmap);
        }
    }

    private void hideBar() {
        getParrotActionBar().hide(true);
    }

    private void initPhoto() {
        this.view.setImageURI(this.media.getUri());
    }

    private void showBar() {
        getParrotActionBar().show(true);
    }

    public static void start(Context context, MediaVO mediaVO, String str, boolean z) {
        Intent intent = new Intent(context, WatchPhotoActivity.class);
        intent.putExtra(WatchMediaActivity.EXTRA_MEDIA_OBJ, mediaVO);
        intent.putExtra(WatchMediaActivity.EXTRA_TITLE, str);
        intent.putExtra(EXTRA_IS_REMOTE, z);
        context.startActivity(intent);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0984R.layout.activity_watch_photo);
        this.media = (MediaVO) getIntent().getParcelableExtra(WatchMediaActivity.EXTRA_MEDIA_OBJ);
        this.isRemote = getIntent().getExtras().getBoolean(EXTRA_IS_REMOTE);
        this.view = (ImageView) findViewById(C0984R.id.content);
        this.view.setOnTouchListener(this);
        if (this.isRemote) {
            new DownloadImageAsyncTask().execute(new String[]{this.media.getPath()});
            return;
        }
        initPhoto();
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    protected void onStart() {
        super.onStart();
        showBar();
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (getParrotActionBar().isVisible()) {
            hideBar();
        } else {
            showBar();
        }
        return false;
    }
}
