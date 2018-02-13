package com.parrot.freeflight.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import com.parrot.ardronetool.ftp.FTPClient;
import com.parrot.ardronetool.ftp.FTPClientStatus;
import com.parrot.ardronetool.ftp.FTPClientStatus.FTPStatus;
import com.parrot.ardronetool.ftp.FTPOperation;
import com.parrot.ardronetool.ftp.FTPProgressListener;
import java.io.File;

public class FTPUtils {
    private static final String TAG = "FTPUtils";

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String downloadFile(android.content.Context r7, java.lang.String r8, int r9, java.lang.String r10) {
        /*
        r0 = 0;
        r2 = new com.parrot.ardronetool.ftp.FTPClient;	 Catch:{ all -> 0x0114 }
        r2.<init>();	 Catch:{ all -> 0x0114 }
        r1 = r2.connect(r8, r9);	 Catch:{ all -> 0x014f }
        if (r1 != 0) goto L_0x001f;
    L_0x000c:
        r1 = "FTPUtils";
        r3 = "downloadFile failed. Can't connect";
        android.util.Log.w(r1, r3);	 Catch:{ all -> 0x014f }
        if (r2 == 0) goto L_0x001e;
    L_0x0015:
        r1 = r2.isConnected();
        if (r1 == 0) goto L_0x001e;
    L_0x001b:
        r2.disconnect();
    L_0x001e:
        return r0;
    L_0x001f:
        r1 = com.parrot.freeflight.utils.CacheUtils.createTempFile(r7);	 Catch:{ all -> 0x014f }
        if (r1 != 0) goto L_0x005f;
    L_0x0025:
        r3 = "FTPUtils";
        r4 = "downloadFile failed. Can't connect";
        android.util.Log.w(r3, r4);	 Catch:{ all -> 0x0154 }
        if (r1 == 0) goto L_0x0056;
    L_0x002e:
        r3 = r1.exists();
        if (r3 == 0) goto L_0x0056;
    L_0x0034:
        r3 = r1.delete();
        if (r3 != 0) goto L_0x0056;
    L_0x003a:
        r3 = "FTPUtils";
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "Can'd delete temp file ";
        r4 = r4.append(r5);
        r1 = r1.getAbsolutePath();
        r1 = r4.append(r1);
        r1 = r1.toString();
        android.util.Log.w(r3, r1);
    L_0x0056:
        if (r2 == 0) goto L_0x001e;
    L_0x0058:
        r1 = r2.isConnected();
        if (r1 == 0) goto L_0x001e;
    L_0x005e:
        goto L_0x001b;
    L_0x005f:
        r3 = r1.getAbsolutePath();	 Catch:{ all -> 0x0154 }
        r3 = r2.getSync(r10, r3);	 Catch:{ all -> 0x0154 }
        if (r3 != 0) goto L_0x009c;
    L_0x0069:
        if (r1 == 0) goto L_0x0093;
    L_0x006b:
        r3 = r1.exists();
        if (r3 == 0) goto L_0x0093;
    L_0x0071:
        r3 = r1.delete();
        if (r3 != 0) goto L_0x0093;
    L_0x0077:
        r3 = "FTPUtils";
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "Can'd delete temp file ";
        r4 = r4.append(r5);
        r1 = r1.getAbsolutePath();
        r1 = r4.append(r1);
        r1 = r1.toString();
        android.util.Log.w(r3, r1);
    L_0x0093:
        if (r2 == 0) goto L_0x001e;
    L_0x0095:
        r1 = r2.isConnected();
        if (r1 == 0) goto L_0x001e;
    L_0x009b:
        goto L_0x001b;
    L_0x009c:
        r3 = r1.exists();	 Catch:{ all -> 0x0154 }
        if (r3 != 0) goto L_0x00d6;
    L_0x00a2:
        if (r1 == 0) goto L_0x00cc;
    L_0x00a4:
        r3 = r1.exists();
        if (r3 == 0) goto L_0x00cc;
    L_0x00aa:
        r3 = r1.delete();
        if (r3 != 0) goto L_0x00cc;
    L_0x00b0:
        r3 = "FTPUtils";
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "Can'd delete temp file ";
        r4 = r4.append(r5);
        r1 = r1.getAbsolutePath();
        r1 = r4.append(r1);
        r1 = r1.toString();
        android.util.Log.w(r3, r1);
    L_0x00cc:
        if (r2 == 0) goto L_0x001e;
    L_0x00ce:
        r1 = r2.isConnected();
        if (r1 == 0) goto L_0x001e;
    L_0x00d4:
        goto L_0x001b;
    L_0x00d6:
        r3 = com.parrot.freeflight.utils.CacheUtils.readFromFile(r1);	 Catch:{ all -> 0x0154 }
        if (r3 == 0) goto L_0x00e0;
    L_0x00dc:
        r0 = r3.toString();	 Catch:{ all -> 0x0154 }
    L_0x00e0:
        if (r1 == 0) goto L_0x010a;
    L_0x00e2:
        r3 = r1.exists();
        if (r3 == 0) goto L_0x010a;
    L_0x00e8:
        r3 = r1.delete();
        if (r3 != 0) goto L_0x010a;
    L_0x00ee:
        r3 = "FTPUtils";
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "Can'd delete temp file ";
        r4 = r4.append(r5);
        r1 = r1.getAbsolutePath();
        r1 = r4.append(r1);
        r1 = r1.toString();
        android.util.Log.w(r3, r1);
    L_0x010a:
        if (r2 == 0) goto L_0x001e;
    L_0x010c:
        r1 = r2.isConnected();
        if (r1 == 0) goto L_0x001e;
    L_0x0112:
        goto L_0x001b;
    L_0x0114:
        r1 = move-exception;
        r2 = r0;
        r6 = r1;
        r1 = r0;
        r0 = r6;
    L_0x0119:
        if (r1 == 0) goto L_0x0143;
    L_0x011b:
        r3 = r1.exists();
        if (r3 == 0) goto L_0x0143;
    L_0x0121:
        r3 = r1.delete();
        if (r3 != 0) goto L_0x0143;
    L_0x0127:
        r3 = "FTPUtils";
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "Can'd delete temp file ";
        r4 = r4.append(r5);
        r1 = r1.getAbsolutePath();
        r1 = r4.append(r1);
        r1 = r1.toString();
        android.util.Log.w(r3, r1);
    L_0x0143:
        if (r2 == 0) goto L_0x014e;
    L_0x0145:
        r1 = r2.isConnected();
        if (r1 == 0) goto L_0x014e;
    L_0x014b:
        r2.disconnect();
    L_0x014e:
        throw r0;
    L_0x014f:
        r1 = move-exception;
        r6 = r1;
        r1 = r0;
        r0 = r6;
        goto L_0x0119;
    L_0x0154:
        r0 = move-exception;
        goto L_0x0119;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.parrot.freeflight.utils.FTPUtils.downloadFile(android.content.Context, java.lang.String, int, java.lang.String):java.lang.String");
    }

    public static boolean uploadFile(Context context, String str, int i, String str2, String str3, final ProgressListener progressListener) {
        boolean z = false;
        Log.d(TAG, "Uploading file " + str2 + " to " + str + ":" + i);
        AssetManager assets = context.getAssets();
        File createTempFile = CacheUtils.createTempFile(context);
        FTPClient fTPClient = new FTPClient();
        if (createTempFile == null) {
            if (!(createTempFile == null || !createTempFile.exists() || createTempFile.delete())) {
                Log.w(TAG, "Can't delete file " + createTempFile.getAbsolutePath());
            }
            if (!fTPClient.isConnected()) {
                return false;
            }
        }
        try {
            if (!CacheUtils.copyFileFromAssetsToStorage(assets, str2, createTempFile)) {
                Log.e(TAG, "uploadFile() Can't copy file " + str2 + " to " + createTempFile.getAbsolutePath());
                if (!(createTempFile == null || !createTempFile.exists() || createTempFile.delete())) {
                    Log.w(TAG, "Can't delete file " + createTempFile.getAbsolutePath());
                }
                if (!fTPClient.isConnected()) {
                    return false;
                }
            } else if (fTPClient.connect(str, i)) {
                final Object obj = new Object();
                fTPClient.setProgressListener(new FTPProgressListener() {
                    public void onStatusChanged(FTPStatus fTPStatus, float f, FTPOperation fTPOperation) {
                        if (fTPStatus == FTPStatus.FTP_PROGRESS) {
                            progressListener.onProgress(Math.round(f));
                            return;
                        }
                        synchronized (obj) {
                            obj.notify();
                        }
                    }
                });
                fTPClient.put(createTempFile.getAbsolutePath(), str3);
                synchronized (obj) {
                    try {
                        obj.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (FTPClientStatus.isFailure(fTPClient.getReplyStatus())) {
                    Log.e(TAG, "uploadFile() Failed to upload file to ftp " + str + ":" + i);
                    if (!(createTempFile == null || !createTempFile.exists() || createTempFile.delete())) {
                        Log.w(TAG, "Can't delete file " + createTempFile.getAbsolutePath());
                    }
                    if (!fTPClient.isConnected()) {
                        return false;
                    }
                } else {
                    if (!(createTempFile == null || !createTempFile.exists() || createTempFile.delete())) {
                        Log.w(TAG, "Can't delete file " + createTempFile.getAbsolutePath());
                    }
                    if (!fTPClient.isConnected()) {
                        return true;
                    }
                    z = true;
                }
            } else {
                Log.e(TAG, "uploadFile() Can't connect to " + str + ":" + i);
                if (!(createTempFile == null || !createTempFile.exists() || createTempFile.delete())) {
                    Log.w(TAG, "Can't delete file " + createTempFile.getAbsolutePath());
                }
                if (!fTPClient.isConnected()) {
                    return false;
                }
            }
        } catch (Throwable th) {
            if (!(createTempFile == null || !createTempFile.exists() || createTempFile.delete())) {
                Log.w(TAG, "Can't delete file " + createTempFile.getAbsolutePath());
            }
            if (fTPClient.isConnected()) {
                fTPClient.disconnect();
            }
        }
        fTPClient.disconnect();
        return z;
    }

    public static boolean uploadFileSync(android.content.Context r6, java.lang.String r7, int r8, java.lang.String r9, java.lang.String r10) {
        /* JADX: method processing error */
/*
Error: java.util.NoSuchElementException
	at java.util.HashMap$HashIterator.nextNode(HashMap.java:1439)
	at java.util.HashMap$KeyIterator.next(HashMap.java:1461)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.applyRemove(BlockFinallyExtract.java:535)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.extractFinally(BlockFinallyExtract.java:175)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.processExceptionHandler(BlockFinallyExtract.java:79)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:51)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
        /*
        r0 = 0;
        r1 = r6.getAssets();
        r2 = com.parrot.freeflight.utils.CacheUtils.createTempFile(r6);
        r3 = new com.parrot.ardronetool.ftp.FTPClient;
        r3.<init>();
        if (r2 != 0) goto L_0x0044;
    L_0x0010:
        if (r2 == 0) goto L_0x003a;
    L_0x0012:
        r1 = r2.exists();
        if (r1 == 0) goto L_0x003a;
    L_0x0018:
        r1 = r2.delete();
        if (r1 != 0) goto L_0x003a;
    L_0x001e:
        r1 = "FTPUtils";
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "Can't delete file ";
        r4 = r4.append(r5);
        r2 = r2.getAbsolutePath();
        r2 = r4.append(r2);
        r2 = r2.toString();
        android.util.Log.w(r1, r2);
    L_0x003a:
        r1 = r3.isConnected();
        if (r1 == 0) goto L_0x0043;
    L_0x0040:
        r3.disconnect();
    L_0x0043:
        return r0;
    L_0x0044:
        r1 = com.parrot.freeflight.utils.CacheUtils.copyFileFromAssetsToStorage(r1, r9, r2);	 Catch:{ all -> 0x016a }
        if (r1 != 0) goto L_0x00a4;	 Catch:{ all -> 0x016a }
    L_0x004a:
        r1 = "FTPUtils";	 Catch:{ all -> 0x016a }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x016a }
        r4.<init>();	 Catch:{ all -> 0x016a }
        r5 = "uploadFile() Can't copy file ";	 Catch:{ all -> 0x016a }
        r4 = r4.append(r5);	 Catch:{ all -> 0x016a }
        r4 = r4.append(r9);	 Catch:{ all -> 0x016a }
        r5 = " to ";	 Catch:{ all -> 0x016a }
        r4 = r4.append(r5);	 Catch:{ all -> 0x016a }
        r5 = r2.getAbsolutePath();	 Catch:{ all -> 0x016a }
        r4 = r4.append(r5);	 Catch:{ all -> 0x016a }
        r4 = r4.toString();	 Catch:{ all -> 0x016a }
        android.util.Log.e(r1, r4);	 Catch:{ all -> 0x016a }
        if (r2 == 0) goto L_0x009a;
    L_0x0072:
        r1 = r2.exists();
        if (r1 == 0) goto L_0x009a;
    L_0x0078:
        r1 = r2.delete();
        if (r1 != 0) goto L_0x009a;
    L_0x007e:
        r1 = "FTPUtils";
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "Can't delete file ";
        r4 = r4.append(r5);
        r2 = r2.getAbsolutePath();
        r2 = r4.append(r2);
        r2 = r2.toString();
        android.util.Log.w(r1, r2);
    L_0x009a:
        r1 = r3.isConnected();
        if (r1 == 0) goto L_0x0043;
    L_0x00a0:
        r3.disconnect();
        goto L_0x0043;
    L_0x00a4:
        r1 = r3.connect(r7, r8);	 Catch:{ all -> 0x016a }
        if (r1 != 0) goto L_0x0101;	 Catch:{ all -> 0x016a }
    L_0x00aa:
        r1 = "FTPUtils";	 Catch:{ all -> 0x016a }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x016a }
        r4.<init>();	 Catch:{ all -> 0x016a }
        r5 = "uploadFile() Can't connect to ";	 Catch:{ all -> 0x016a }
        r4 = r4.append(r5);	 Catch:{ all -> 0x016a }
        r4 = r4.append(r7);	 Catch:{ all -> 0x016a }
        r5 = ":";	 Catch:{ all -> 0x016a }
        r4 = r4.append(r5);	 Catch:{ all -> 0x016a }
        r4 = r4.append(r8);	 Catch:{ all -> 0x016a }
        r4 = r4.toString();	 Catch:{ all -> 0x016a }
        android.util.Log.e(r1, r4);	 Catch:{ all -> 0x016a }
        if (r2 == 0) goto L_0x00f6;
    L_0x00ce:
        r1 = r2.exists();
        if (r1 == 0) goto L_0x00f6;
    L_0x00d4:
        r1 = r2.delete();
        if (r1 != 0) goto L_0x00f6;
    L_0x00da:
        r1 = "FTPUtils";
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "Can't delete file ";
        r4 = r4.append(r5);
        r2 = r2.getAbsolutePath();
        r2 = r4.append(r2);
        r2 = r2.toString();
        android.util.Log.w(r1, r2);
    L_0x00f6:
        r1 = r3.isConnected();
        if (r1 == 0) goto L_0x0043;
    L_0x00fc:
        r3.disconnect();
        goto L_0x0043;
    L_0x0101:
        r1 = r2.getAbsolutePath();	 Catch:{ all -> 0x016a }
        r1 = r3.putSync(r1, r10);	 Catch:{ all -> 0x016a }
        r4 = r3.getReplyStatus();	 Catch:{ all -> 0x016a }
        r4 = com.parrot.ardronetool.ftp.FTPClientStatus.isFailure(r4);	 Catch:{ all -> 0x016a }
        if (r4 == 0) goto L_0x019f;	 Catch:{ all -> 0x016a }
    L_0x0113:
        r1 = "FTPUtils";	 Catch:{ all -> 0x016a }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x016a }
        r4.<init>();	 Catch:{ all -> 0x016a }
        r5 = "uploadFile() Failed to upload file to ftp ";	 Catch:{ all -> 0x016a }
        r4 = r4.append(r5);	 Catch:{ all -> 0x016a }
        r4 = r4.append(r7);	 Catch:{ all -> 0x016a }
        r5 = ":";	 Catch:{ all -> 0x016a }
        r4 = r4.append(r5);	 Catch:{ all -> 0x016a }
        r4 = r4.append(r8);	 Catch:{ all -> 0x016a }
        r4 = r4.toString();	 Catch:{ all -> 0x016a }
        android.util.Log.e(r1, r4);	 Catch:{ all -> 0x016a }
        if (r2 == 0) goto L_0x015f;
    L_0x0137:
        r1 = r2.exists();
        if (r1 == 0) goto L_0x015f;
    L_0x013d:
        r1 = r2.delete();
        if (r1 != 0) goto L_0x015f;
    L_0x0143:
        r1 = "FTPUtils";
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "Can't delete file ";
        r4 = r4.append(r5);
        r2 = r2.getAbsolutePath();
        r2 = r4.append(r2);
        r2 = r2.toString();
        android.util.Log.w(r1, r2);
    L_0x015f:
        r1 = r3.isConnected();
        if (r1 == 0) goto L_0x0043;
    L_0x0165:
        r3.disconnect();
        goto L_0x0043;
    L_0x016a:
        r0 = move-exception;
        if (r2 == 0) goto L_0x0195;
    L_0x016d:
        r1 = r2.exists();
        if (r1 == 0) goto L_0x0195;
    L_0x0173:
        r1 = r2.delete();
        if (r1 != 0) goto L_0x0195;
    L_0x0179:
        r1 = "FTPUtils";
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "Can't delete file ";
        r4 = r4.append(r5);
        r2 = r2.getAbsolutePath();
        r2 = r4.append(r2);
        r2 = r2.toString();
        android.util.Log.w(r1, r2);
    L_0x0195:
        r1 = r3.isConnected();
        if (r1 == 0) goto L_0x019e;
    L_0x019b:
        r3.disconnect();
    L_0x019e:
        throw r0;
    L_0x019f:
        if (r2 == 0) goto L_0x01c9;
    L_0x01a1:
        r0 = r2.exists();
        if (r0 == 0) goto L_0x01c9;
    L_0x01a7:
        r0 = r2.delete();
        if (r0 != 0) goto L_0x01c9;
    L_0x01ad:
        r0 = "FTPUtils";
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "Can't delete file ";
        r4 = r4.append(r5);
        r2 = r2.getAbsolutePath();
        r2 = r4.append(r2);
        r2 = r2.toString();
        android.util.Log.w(r0, r2);
    L_0x01c9:
        r0 = r3.isConnected();
        if (r0 == 0) goto L_0x01d5;
    L_0x01cf:
        r3.disconnect();
        r0 = r1;
        goto L_0x0043;
    L_0x01d5:
        r0 = r1;
        goto L_0x0043;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.parrot.freeflight.utils.FTPUtils.uploadFileSync(android.content.Context, java.lang.String, int, java.lang.String, java.lang.String):boolean");
    }
}
