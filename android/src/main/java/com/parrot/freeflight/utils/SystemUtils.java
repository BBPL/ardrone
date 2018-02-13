package com.parrot.freeflight.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import java.io.FileWriter;
import java.io.IOException;
import org.mortbay.util.URIUtil;

public final class SystemUtils {
    private SystemUtils() {
    }

    public static void appendStrToFile(String str, String str2) {
        IOException e;
        Throwable th;
        FileWriter fileWriter = null;
        FileWriter fileWriter2;
        try {
            fileWriter2 = new FileWriter(Environment.getExternalStorageDirectory() + URIUtil.SLASH + str, true);
            try {
                fileWriter2.write(str2);
                fileWriter2.write("\n");
                if (fileWriter2 != null) {
                    try {
                        fileWriter2.flush();
                    } catch (Exception e2) {
                        Log.d("SystemUtils", "Failed to flush file. Exception: " + e2.toString());
                    }
                    try {
                        fileWriter2.close();
                    } catch (Exception e22) {
                        Log.d("SystemUtils", "Failed to close file. Exception: " + e22.toString());
                    }
                }
            } catch (IOException e3) {
                e = e3;
                try {
                    e.printStackTrace();
                    if (fileWriter2 != null) {
                        try {
                            fileWriter2.flush();
                        } catch (Exception e222) {
                            Log.d("SystemUtils", "Failed to flush file. Exception: " + e222.toString());
                        }
                        try {
                            fileWriter2.close();
                        } catch (Exception e2222) {
                            Log.d("SystemUtils", "Failed to close file. Exception: " + e2222.toString());
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    fileWriter = fileWriter2;
                    fileWriter2 = fileWriter;
                    if (fileWriter2 != null) {
                        try {
                            fileWriter2.flush();
                        } catch (Exception e4) {
                            Log.d("SystemUtils", "Failed to flush file. Exception: " + e4.toString());
                        }
                        try {
                            fileWriter2.close();
                        } catch (Exception e5) {
                            Log.d("SystemUtils", "Failed to close file. Exception: " + e5.toString());
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                if (fileWriter2 != null) {
                    fileWriter2.flush();
                    fileWriter2.close();
                }
                throw th;
            }
        } catch (IOException e6) {
            e = e6;
            fileWriter2 = null;
            e.printStackTrace();
            if (fileWriter2 != null) {
                fileWriter2.flush();
                fileWriter2.close();
            }
        } catch (Throwable th4) {
            th = th4;
            fileWriter2 = fileWriter;
            if (fileWriter2 != null) {
                fileWriter2.flush();
                fileWriter2.close();
            }
            throw th;
        }
    }

    public static String getDeviceName() {
        String str = Build.MANUFACTURER;
        String str2 = Build.MODEL;
        return str2.startsWith(str) ? StringUtils.capitalize(str2) : StringUtils.capitalize(str) + " " + str2;
    }

    public static long getFreeMemory(Context context) {
        MemoryInfo memoryInfo = new MemoryInfo();
        ((ActivityManager) context.getSystemService("activity")).getMemoryInfo(memoryInfo);
        return memoryInfo.availMem;
    }

    public static boolean hasTouchScreen(Context context) {
        return context.getPackageManager().hasSystemFeature("android.hardware.touchscreen");
    }

    public static void hideKeyboard(Activity activity) {
        try {
            View currentFocus = activity.getCurrentFocus();
            if (currentFocus != null) {
                ((InputMethodManager) activity.getSystemService("input_method")).hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
            }
        } catch (Exception e) {
        }
    }

    public static boolean isAnyNull(Object... objArr) {
        for (Object obj : objArr) {
            if (obj == null) {
                return true;
            }
        }
        return false;
    }

    public static boolean isGoogleTV(Context context) {
        return context.getPackageManager().hasSystemFeature("com.google.android.tv");
    }

    public static boolean isMoverioGlasses() {
        return Build.PRODUCT.equalsIgnoreCase("EPSON_embt1");
    }

    public static boolean isNook() {
        return Build.BRAND.equalsIgnoreCase("nook");
    }

    public static boolean isTablet(Context context) {
        return ((context.getResources().getConfiguration().screenLayout & 15) == 4) || ((context.getResources().getConfiguration().screenLayout & 15) == 3);
    }
}
