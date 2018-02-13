package com.parrot.ardronetool;

import android.content.res.AssetManager;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;

public class PlfFile {
    private static final String TAG = "PlfFile";
    private AssetManager assetManager;
    private String fileName;

    public PlfFile(AssetManager assetManager, String str) {
        this.fileName = str;
        this.assetManager = assetManager;
    }

    private byte[] getHeader(int i) {
        IOException e;
        Throwable th;
        byte[] bArr = new byte[i];
        InputStream open;
        try {
            open = this.assetManager.open(this.fileName);
            try {
                if (open.read(bArr, 0, bArr.length) == -1) {
                    Log.w(TAG, "End of " + this.fileName + " is reached.");
                }
                if (open == null) {
                    return bArr;
                }
                try {
                    open.close();
                    return bArr;
                } catch (IOException e2) {
                    e2.printStackTrace();
                    return bArr;
                }
            } catch (IOException e3) {
                e = e3;
                try {
                    e.printStackTrace();
                    if (open != null) {
                        try {
                            open.close();
                        } catch (IOException e4) {
                            e4.printStackTrace();
                        }
                    }
                    return null;
                } catch (Throwable th2) {
                    th = th2;
                    if (open != null) {
                        try {
                            open.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                        }
                    }
                    throw th;
                }
            }
        } catch (IOException e5) {
            e4 = e5;
            open = null;
            e4.printStackTrace();
            if (open != null) {
                open.close();
            }
            return null;
        } catch (Throwable th3) {
            th = th3;
            open = null;
            if (open != null) {
                open.close();
            }
            throw th;
        }
    }

    private native String getVersionNative();

    public String getVersion() {
        return getVersionNative();
    }
}
