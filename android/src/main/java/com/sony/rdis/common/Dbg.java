package com.sony.rdis.common;

import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Dbg {
    private static final boolean ENABLE_PRINT_STACK_TRACE = false;
    private static List<String> mTags = new ArrayList();

    public static void m1740d(String str, String str2) {
        if (isEnabled(str)) {
            Log.d(str, str2);
        }
    }

    public static void m1741d(String str, String str2, Throwable th) {
        if (isEnabled(str)) {
            Log.d(str, str2, th);
        }
    }

    public static void disable(String str) {
        disableTag(str);
    }

    private static void disableTag(String str) {
        synchronized (Dbg.class) {
            try {
                Iterator it = mTags.iterator();
                while (true) {
                    Object hasNext = it.hasNext();
                    if (hasNext == null) {
                        break;
                    } else if (((String) it.next()).equals(str)) {
                        it.remove();
                    }
                }
            } finally {
                Class cls = Dbg.class;
            }
        }
    }

    public static void m1742e(String str, String str2) {
        Log.e(str, str2);
    }

    public static void m1743e(String str, String str2, Throwable th) {
        Log.e(str, str2, th);
    }

    public static void enable(String str) {
        enableTag(str);
    }

    public static void enableController(boolean z) {
        if (z) {
            enable("RDIS_COMMON");
            enable("RDIS_CONTROLLER");
            enable("PETIT_RDIS_CONTROLLER");
            return;
        }
        disable("RDIS_COMMON");
        disable("RDIS_CONTROLLER");
        disable("PETIT_RDIS_CONTROLLER");
    }

    public static void enableReceiver(boolean z) {
        if (z) {
            enable("RDIS_COMMON");
            enable("RDIS_LIB");
            enable("RdisSensorManager");
            return;
        }
        disable("RDIS_COMMON");
        disable("RDIS_LIB");
        disable("RdisSensorManager");
    }

    private static void enableTag(String str) {
        synchronized (Dbg.class) {
            try {
                if (!isEnabled(str)) {
                    mTags.add(str);
                }
            } catch (Throwable th) {
                Class cls = Dbg.class;
            }
        }
    }

    public static void enableUtility(boolean z) {
        enableReceiver(z);
        if (z) {
            enable("RDIS_UTIL");
        } else {
            disable("RDIS_UTIL");
        }
    }

    public static void m1744i(String str, String str2) {
        if (isEnabled(str)) {
            Log.i(str, str2);
        }
    }

    public static void m1745i(String str, String str2, Throwable th) {
        if (isEnabled(str)) {
            Log.i(str, str2, th);
        }
    }

    private static boolean isEnabled(String str) {
        boolean z;
        synchronized (Dbg.class) {
            try {
                for (String equals : mTags) {
                    if (equals.equals(str)) {
                        z = true;
                        break;
                    }
                }
                z = false;
            } catch (Throwable th) {
                Class cls = Dbg.class;
            }
        }
        return z;
    }

    public static void printStackTrace(Exception exception) {
    }

    public static void m1746v(String str, String str2) {
        if (isEnabled(str)) {
            Log.v(str, str2);
        }
    }

    public static void m1747v(String str, String str2, Throwable th) {
        if (isEnabled(str)) {
            Log.v(str, str2, th);
        }
    }

    public static void m1748w(String str, String str2) {
        if (isEnabled(str)) {
            Log.w(str, str2);
        }
    }

    public static void m1749w(String str, String str2, Throwable th) {
        if (isEnabled(str)) {
            Log.w(str, str2, th);
        }
    }
}
