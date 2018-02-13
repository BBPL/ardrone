package com.parrot.freeflight.mediauploadservice;

import android.content.Context;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class SerializationUtils {
    public static final Map<String, String> restoreMap(Context context, String str, Map<String, String> map) throws IOException, JSONException {
        Throwable th;
        BufferedInputStream bufferedInputStream;
        try {
            File fileStreamPath = context.getFileStreamPath(str);
            if (!(fileStreamPath.exists() && fileStreamPath.isFile())) {
                fileStreamPath.createNewFile();
            }
            bufferedInputStream = new BufferedInputStream(context.openFileInput(str));
            try {
                if (bufferedInputStream.available() > 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    byte[] bArr = new byte[1024];
                    while (true) {
                        int read = bufferedInputStream.read(bArr);
                        if (read == -1) {
                            break;
                        }
                        stringBuilder.append(new String(bArr, 0, read));
                    }
                    JSONObject jSONObject = new JSONObject(stringBuilder.toString());
                    map.clear();
                    Iterator keys = jSONObject.keys();
                    while (keys.hasNext()) {
                        String str2 = (String) keys.next();
                        map.put(str2, jSONObject.getString(str2));
                    }
                } else {
                    map.clear();
                }
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                return map;
            } catch (Throwable th2) {
                th = th2;
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            bufferedInputStream = null;
            if (bufferedInputStream != null) {
                bufferedInputStream.close();
            }
            throw th;
        }
    }

    public static final void saveMap(Context context, String str, Map<String, String> map) throws IOException {
        BufferedOutputStream bufferedOutputStream;
        Throwable th;
        try {
            File fileStreamPath = context.getFileStreamPath(str);
            if (!(fileStreamPath.exists() && fileStreamPath.isFile())) {
                fileStreamPath.createNewFile();
            }
            bufferedOutputStream = new BufferedOutputStream(context.openFileOutput(str, 0));
            try {
                bufferedOutputStream.write(new JSONObject(map).toString().getBytes());
                bufferedOutputStream.flush();
                bufferedOutputStream.close();
            } catch (Throwable th2) {
                th = th2;
                bufferedOutputStream.flush();
                bufferedOutputStream.close();
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            bufferedOutputStream = null;
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
            throw th;
        }
    }
}
