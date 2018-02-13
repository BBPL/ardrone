package com.parrot.freeflight.track_3d_viewer.utils;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class Utils {
    public static String formatIntWithSpaces(int i) {
        DecimalFormat decimalFormat = new DecimalFormat();
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setGroupingSeparator(' ');
        decimalFormat.setGroupingSize(3);
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
        return decimalFormat.format((long) i);
    }

    public static float[] loadFloatArray(String str, Context context) throws IOException {
        Throwable th;
        InputStream inputStream = null;
        try {
            InputStream open = context.getResources().getAssets().open(str, 1);
            try {
                DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(open));
                float[] fArr = new float[(dataInputStream.available() / 4)];
                int i = 0;
                while (dataInputStream.available() > 0) {
                    fArr[i] = dataInputStream.readFloat();
                    i++;
                }
                dataInputStream.close();
                if (open != null) {
                    try {
                        open.close();
                    } catch (Exception e) {
                        e.getMessage();
                        return fArr;
                    }
                }
                return fArr;
            } catch (Throwable th2) {
                th = th2;
                inputStream = open;
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception e2) {
                        e2.getMessage();
                    }
                }
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            if (inputStream != null) {
                inputStream.close();
            }
            throw th;
        }
    }

    public static short[] loadShortArray(String str, Context context) throws IOException {
        Throwable th;
        InputStream inputStream = null;
        try {
            InputStream open = context.getResources().getAssets().open(str, 1);
            try {
                DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(open));
                short[] sArr = new short[(dataInputStream.available() / 2)];
                int i = 0;
                while (dataInputStream.available() > 0) {
                    sArr[i] = dataInputStream.readShort();
                    i++;
                }
                dataInputStream.close();
                if (open != null) {
                    try {
                        open.close();
                    } catch (Exception e) {
                        e.getMessage();
                        return sArr;
                    }
                }
                return sArr;
            } catch (Throwable th2) {
                th = th2;
                inputStream = open;
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception e2) {
                        e2.getMessage();
                    }
                }
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            if (inputStream != null) {
                inputStream.close();
            }
            throw th;
        }
    }

    public static String readTextFileFromRawResource(Context context, int i) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(i)));
        StringBuilder stringBuilder = new StringBuilder();
        while (true) {
            try {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    return stringBuilder.toString();
                }
                stringBuilder.append(readLine);
                stringBuilder.append('\n');
            } catch (IOException e) {
                return null;
            }
        }
    }

    public static void showAlertDialog(Context context, String str, String str2, final Runnable runnable) {
        new Builder(context).setTitle(str).setMessage(str2).setCancelable(false).setNegativeButton(context.getString(17039370), new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (runnable != null) {
                    runnable.run();
                }
            }
        }).create().show();
    }
}
