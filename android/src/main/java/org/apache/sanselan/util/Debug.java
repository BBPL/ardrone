package org.apache.sanselan.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.mortbay.jetty.HttpVersions;

public final class Debug {
    private static long counter = 0;
    public static String newline = "\r\n";
    private static final SimpleDateFormat timestamp = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss:SSS");

    private static final String byteQuadToString(int i) {
        byte b = (byte) ((i >> 24) & 255);
        byte b2 = (byte) ((i >> 16) & 255);
        byte b3 = (byte) ((i >> 8) & 255);
        byte b4 = (byte) ((i >> 0) & 255);
        char c = (char) b;
        char c2 = (char) b2;
        char c3 = (char) b3;
        char c4 = (char) b4;
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(new String(new char[]{c, c2, c3, c4}));
        stringBuffer.append(" bytequad: " + i);
        stringBuffer.append(" b1: " + b);
        stringBuffer.append(" b2: " + b2);
        stringBuffer.append(" b3: " + b3);
        stringBuffer.append(" b4: " + b4);
        return stringBuffer.toString();
    }

    public static boolean compare(String str, Map map, Map map2) {
        return compare(str, map, map2, null, null);
    }

    public static boolean compare(String str, Map map, Map map2, ArrayList arrayList, StringBuffer stringBuffer) {
        boolean z = true;
        if (map == null && map2 == null) {
            log(stringBuffer, new StringBuilder(String.valueOf(str)).append(" both maps null").toString());
            return true;
        } else if (map == null) {
            log(stringBuffer, new StringBuilder(String.valueOf(str)).append(" map a: null, map b: map").toString());
            return false;
        } else if (map2 == null) {
            log(stringBuffer, new StringBuilder(String.valueOf(str)).append(" map a: map, map b: null").toString());
            return false;
        } else {
            int i;
            ArrayList arrayList2 = new ArrayList(map.keySet());
            ArrayList arrayList3 = new ArrayList(map2.keySet());
            if (arrayList != null) {
                arrayList2.removeAll(arrayList);
                arrayList3.removeAll(arrayList);
            }
            for (i = 0; i < arrayList2.size(); i++) {
                Object obj = arrayList2.get(i);
                if (arrayList3.contains(obj)) {
                    arrayList3.remove(obj);
                    Object obj2 = map.get(obj);
                    Object obj3 = map2.get(obj);
                    if (!obj2.equals(obj3)) {
                        log(stringBuffer, new StringBuilder(String.valueOf(str)).append("key(").append(obj).append(") value a: ").append(obj2).append(") !=  b: ").append(obj3).append(")").toString());
                        z = false;
                    }
                } else {
                    log(stringBuffer, new StringBuilder(String.valueOf(str)).append("b is missing key '").append(obj).append("' from a").toString());
                    z = false;
                }
            }
            i = 0;
            while (i < arrayList3.size()) {
                log(stringBuffer, new StringBuilder(String.valueOf(str)).append("a is missing key '").append(arrayList3.get(i)).append("' from b").toString());
                i++;
                z = false;
            }
            if (!z) {
                return z;
            }
            log(stringBuffer, new StringBuilder(String.valueOf(str)).append("a is the same as  b").toString());
            return z;
        }
    }

    public static void debug() {
        newline();
    }

    public static void debug(Class cls, Throwable th) {
        debug(cls.getName(), th);
    }

    public static void debug(Object obj) {
        System.out.println(obj == null ? "null" : obj.toString());
    }

    public static void debug(String str) {
        System.out.println(str);
    }

    public static void debug(String str, double d) {
        debug(new StringBuilder(String.valueOf(str)).append(": ").append(d).toString());
    }

    public static void debug(String str, int i) {
        debug(new StringBuilder(String.valueOf(str)).append(": ").append(i).toString());
    }

    public static void debug(String str, long j) {
        debug(new StringBuilder(String.valueOf(str)).append(" ").append(Long.toString(j)).toString());
    }

    public static void debug(String str, File file) {
        debug(new StringBuilder(String.valueOf(str)).append(": ").append(file == null ? "null" : file.getPath()).toString());
    }

    public static void debug(String str, Object obj) {
        if (obj == null) {
            debug(str, "null");
        } else if (obj instanceof char[]) {
            debug(str, (char[]) obj);
        } else if (obj instanceof byte[]) {
            debug(str, (byte[]) obj);
        } else if (obj instanceof int[]) {
            debug(str, (int[]) obj);
        } else if (obj instanceof String) {
            debug(str, (String) obj);
        } else if (obj instanceof List) {
            debug(str, (List) obj);
        } else if (obj instanceof Map) {
            debug(str, (Map) obj);
        } else if (obj instanceof File) {
            debug(str, (File) obj);
        } else if (obj instanceof Date) {
            debug(str, (Date) obj);
        } else if (obj instanceof Calendar) {
            debug(str, (Calendar) obj);
        } else {
            debug(str, obj.toString());
        }
    }

    public static void debug(String str, String str2) {
        debug(new StringBuilder(String.valueOf(str)).append(" ").append(str2).toString());
    }

    public static void debug(String str, Throwable th) {
        debug(getDebug(str, th));
    }

    public static void debug(String str, Calendar calendar) {
        debug(str, calendar == null ? "null" : new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(calendar.getTime()));
    }

    public static void debug(String str, Date date) {
        debug(str, date == null ? "null" : new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(date));
    }

    public static void debug(String str, List list) {
        StringBuilder stringBuilder = new StringBuilder(" [");
        long j = counter;
        counter = 1 + j;
        String stringBuilder2 = stringBuilder.append(j).append("]").toString();
        debug(new StringBuilder(String.valueOf(str)).append(" (").append(list.size()).append(")").append(stringBuilder2).toString());
        for (int i = 0; i < list.size(); i++) {
            debug("\t" + list.get(i).toString() + stringBuilder2);
        }
        debug();
    }

    public static void debug(String str, Map map) {
        debug(getDebug(str, map));
    }

    public static void debug(String str, boolean z) {
        debug(new StringBuilder(String.valueOf(str)).append(" ").append(z ? "true" : "false").toString());
    }

    public static void debug(String str, byte[] bArr) {
        debug(getDebug(str, bArr));
    }

    public static void debug(String str, byte[] bArr, int i) {
        debug(getDebug(str, bArr, i));
    }

    public static void debug(String str, char[] cArr) {
        debug(getDebug(str, cArr));
    }

    public static void debug(String str, int[] iArr) {
        debug(getDebug(str, iArr));
    }

    public static void debug(String str, Object[] objArr) {
        if (objArr == null) {
            debug(str, "null");
        }
        debug(str, objArr.length);
        int i = 0;
        while (i < objArr.length && i < 10) {
            debug("\t" + i, objArr[i]);
            i++;
        }
        if (objArr.length > 10) {
            debug("\t...");
        }
        debug();
    }

    public static void debug(Throwable th) {
        debug(getDebug(th));
    }

    public static void debug(Throwable th, int i) {
        debug(getDebug(th, i));
    }

    public static void debugByteQuad(String str, int i) {
        System.out.println(new StringBuilder(String.valueOf(str)).append(": ").append("alpha: ").append((i >> 24) & 255).append(", ").append("red: ").append((i >> 16) & 255).append(", ").append("green: ").append((i >> 8) & 255).append(", ").append("blue: ").append((i >> 0) & 255).toString());
    }

    public static void debugIPQuad(String str, int i) {
        System.out.println(new StringBuilder(String.valueOf(str)).append(": ").append("b1: ").append((i >> 24) & 255).append(", ").append("b2: ").append((i >> 16) & 255).append(", ").append("b3: ").append((i >> 8) & 255).append(", ").append("b4: ").append((i >> 0) & 255).toString());
    }

    public static void debugIPQuad(String str, byte[] bArr) {
        System.out.print(new StringBuilder(String.valueOf(str)).append(": ").toString());
        if (bArr == null) {
            System.out.print("null");
        } else {
            for (int i = 0; i < bArr.length; i++) {
                if (i > 0) {
                    System.out.print(".");
                }
                System.out.print(bArr[i] & 255);
            }
        }
        System.out.println();
    }

    public static void dump(String str, Object obj) {
        int i = 0;
        if (obj == null) {
            debug(str, "null");
        } else if (obj instanceof Object[]) {
            Object[] objArr = (Object[]) obj;
            debug(str, objArr);
            while (i < objArr.length) {
                dump(new StringBuilder(String.valueOf(str)).append("\t").append(i).append(": ").toString(), objArr[i]);
                i++;
            }
        } else if (obj instanceof int[]) {
            int[] iArr = (int[]) obj;
            debug(str, iArr);
            while (i < iArr.length) {
                debug(new StringBuilder(String.valueOf(str)).append("\t").append(i).append(": ").toString(), iArr[i]);
                i++;
            }
        } else if (obj instanceof char[]) {
            debug(str, "[" + new String((char[]) obj) + "]");
        } else if (obj instanceof long[]) {
            obj = (long[]) obj;
            debug(str, obj);
            while (i < obj.length) {
                debug(new StringBuilder(String.valueOf(str)).append("\t").append(i).append(": ").toString(), obj[i]);
                i++;
            }
        } else if (obj instanceof boolean[]) {
            obj = (boolean[]) obj;
            debug(str, obj);
            while (i < obj.length) {
                debug(new StringBuilder(String.valueOf(str)).append("\t").append(i).append(": ").toString(), obj[i]);
                i++;
            }
        } else if (obj instanceof byte[]) {
            byte[] bArr = (byte[]) obj;
            debug(str, bArr);
            while (i < bArr.length) {
                debug(new StringBuilder(String.valueOf(str)).append("\t").append(i).append(": ").toString(), bArr[i]);
                i++;
            }
        } else if (obj instanceof float[]) {
            obj = (float[]) obj;
            debug(str, obj);
            while (i < obj.length) {
                debug(new StringBuilder(String.valueOf(str)).append("\t").append(i).append(": ").toString(), (double) obj[i]);
                i++;
            }
        } else if (obj instanceof byte[]) {
            obj = (double[]) obj;
            debug(str, obj);
            while (i < obj.length) {
                debug(new StringBuilder(String.valueOf(str)).append("\t").append(i).append(": ").toString(), obj[i]);
                i++;
            }
        } else if (obj instanceof List) {
            List list = (List) obj;
            debug(str, "list");
            while (i < list.size()) {
                dump(new StringBuilder(String.valueOf(str)).append("\t").append("list: ").append(i).append(": ").toString(), list.get(i));
                i++;
            }
        } else if (obj instanceof Map) {
            Map map = (Map) obj;
            debug(str, "map");
            ArrayList arrayList = new ArrayList(map.keySet());
            Collections.sort(arrayList);
            while (i < arrayList.size()) {
                Object obj2 = arrayList.get(i);
                dump(new StringBuilder(String.valueOf(str)).append("\t").append("map: ").append(obj2).append(" -> ").toString(), map.get(obj2));
                i++;
            }
        } else {
            debug(str, obj.toString());
            debug(new StringBuilder(String.valueOf(str)).append("\t").toString(), obj.getClass().getName());
        }
    }

    public static void dumpStack() {
        debug(getStackTrace(new Exception("Stack trace"), -1, 1));
    }

    public static void dumpStack(int i) {
        debug(getStackTrace(new Exception("Stack trace"), i, 1));
    }

    public static String getDebug(Class cls, Throwable th) {
        return getDebug(cls == null ? "[Unknown]" : cls.getName(), th);
    }

    public static String getDebug(String str) {
        return str;
    }

    public static String getDebug(String str, double d) {
        return getDebug(new StringBuilder(String.valueOf(str)).append(": ").append(d).toString());
    }

    public static String getDebug(String str, int i) {
        return getDebug(new StringBuilder(String.valueOf(str)).append(": ").append(i).toString());
    }

    public static String getDebug(String str, long j) {
        return getDebug(new StringBuilder(String.valueOf(str)).append(" ").append(Long.toString(j)).toString());
    }

    public static String getDebug(String str, File file) {
        return getDebug(new StringBuilder(String.valueOf(str)).append(": ").append(file == null ? "null" : file.getPath()).toString());
    }

    public static String getDebug(String str, Object obj) {
        return obj == null ? getDebug(str, "null") : obj instanceof Calendar ? getDebug(str, (Calendar) obj) : obj instanceof Date ? getDebug(str, (Date) obj) : obj instanceof File ? getDebug(str, (File) obj) : obj instanceof Map ? getDebug(str, (Map) obj) : obj instanceof Map ? getDebug(str, (Map) obj) : obj instanceof String ? getDebug(str, (String) obj) : obj instanceof byte[] ? getDebug(str, (byte[]) obj) : obj instanceof char[] ? getDebug(str, (char[]) obj) : obj instanceof int[] ? getDebug(str, (int[]) obj) : obj instanceof List ? getDebug(str, (List) obj) : getDebug(str, obj.toString());
    }

    public static String getDebug(String str, String str2) {
        return getDebug(new StringBuilder(String.valueOf(str)).append(" ").append(str2).toString());
    }

    public static String getDebug(String str, Throwable th) {
        return new StringBuilder(String.valueOf(str)).append(newline).append(getDebug(th)).toString();
    }

    public static String getDebug(String str, Calendar calendar) {
        return getDebug(str, calendar == null ? "null" : new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(calendar.getTime()));
    }

    public static String getDebug(String str, Date date) {
        return getDebug(str, date == null ? "null" : new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(date));
    }

    public static String getDebug(String str, List list) {
        StringBuffer stringBuffer = new StringBuffer();
        StringBuilder stringBuilder = new StringBuilder(" [");
        long j = counter;
        counter = 1 + j;
        String stringBuilder2 = stringBuilder.append(j).append("]").toString();
        stringBuffer.append(new StringBuilder(String.valueOf(getDebug(new StringBuilder(String.valueOf(str)).append(" (").append(list.size()).append(")").append(stringBuilder2).toString()))).append(newline).toString());
        for (int i = 0; i < list.size(); i++) {
            stringBuffer.append(new StringBuilder(String.valueOf(getDebug("\t" + list.get(i).toString() + stringBuilder2))).append(newline).toString());
        }
        stringBuffer.append(newline);
        return stringBuffer.toString();
    }

    public static String getDebug(String str, Map map) {
        StringBuffer stringBuffer = new StringBuffer();
        if (map == null) {
            return getDebug(new StringBuilder(String.valueOf(str)).append(" map: ").append(null).toString());
        }
        ArrayList arrayList = new ArrayList(map.keySet());
        stringBuffer.append(new StringBuilder(String.valueOf(getDebug(new StringBuilder(String.valueOf(str)).append(" map: ").append(arrayList.size()).toString()))).append(newline).toString());
        for (int i = 0; i < arrayList.size(); i++) {
            Object obj = arrayList.get(i);
            stringBuffer.append(new StringBuilder(String.valueOf(getDebug("\t" + i + ": '" + obj + "' -> '" + map.get(obj) + "'"))).append(newline).toString());
        }
        stringBuffer.append(newline);
        return stringBuffer.toString();
    }

    public static String getDebug(String str, boolean z) {
        return getDebug(new StringBuilder(String.valueOf(str)).append(" ").append(z ? "true" : "false").toString());
    }

    public static String getDebug(String str, byte[] bArr) {
        return getDebug(str, bArr, 250);
    }

    public static String getDebug(String str, byte[] bArr, int i) {
        StringBuffer stringBuffer = new StringBuffer();
        if (bArr == null) {
            stringBuffer.append(new StringBuilder(String.valueOf(str)).append(" (").append(null).append(")").append(newline).toString());
        } else {
            stringBuffer.append(new StringBuilder(String.valueOf(str)).append(" (").append(bArr.length).append(")").append(newline).toString());
            int i2 = 0;
            while (i2 < i && i2 < bArr.length) {
                int i3 = bArr[i2] & 255;
                char c = (i3 == 0 || i3 == 10 || i3 == 11 || i3 == 13) ? ' ' : (char) i3;
                stringBuffer.append("\t" + i2 + ": " + i3 + " (" + c + ", 0x" + Integer.toHexString(i3) + ")" + newline);
                i2++;
            }
            if (bArr.length > i) {
                stringBuffer.append("\t..." + newline);
            }
            stringBuffer.append(newline);
        }
        return stringBuffer.toString();
    }

    public static String getDebug(String str, char[] cArr) {
        StringBuffer stringBuffer = new StringBuffer();
        if (cArr == null) {
            stringBuffer.append(new StringBuilder(String.valueOf(getDebug(new StringBuilder(String.valueOf(str)).append(" (").append(null).append(")").toString()))).append(newline).toString());
        } else {
            stringBuffer.append(new StringBuilder(String.valueOf(getDebug(new StringBuilder(String.valueOf(str)).append(" (").append(cArr.length).append(")").toString()))).append(newline).toString());
            for (int i = 0; i < cArr.length; i++) {
                stringBuffer.append(new StringBuilder(String.valueOf(getDebug("\t" + cArr[i] + " (" + (cArr[i] & 255)))).append(")").append(newline).toString());
            }
            stringBuffer.append(newline);
        }
        return stringBuffer.toString();
    }

    public static String getDebug(String str, int[] iArr) {
        StringBuffer stringBuffer = new StringBuffer();
        if (iArr == null) {
            stringBuffer.append(new StringBuilder(String.valueOf(str)).append(" (").append(null).append(")").append(newline).toString());
        } else {
            stringBuffer.append(new StringBuilder(String.valueOf(str)).append(" (").append(iArr.length).append(")").append(newline).toString());
            for (int i : iArr) {
                stringBuffer.append("\t" + i + newline);
            }
            stringBuffer.append(newline);
        }
        return stringBuffer.toString();
    }

    public static String getDebug(String str, Object[] objArr) {
        StringBuffer stringBuffer = new StringBuffer();
        if (objArr == null) {
            stringBuffer.append(getDebug(str, "null") + newline);
        }
        stringBuffer.append(getDebug(str, objArr.length));
        int i = 0;
        while (i < objArr.length && i < 10) {
            stringBuffer.append(new StringBuilder(String.valueOf(getDebug("\t" + i, objArr[i]))).append(newline).toString());
            i++;
        }
        if (objArr.length > 10) {
            stringBuffer.append(new StringBuilder(String.valueOf(getDebug("\t..."))).append(newline).toString());
        }
        stringBuffer.append(newline);
        return stringBuffer.toString();
    }

    public static String getDebug(Throwable th) {
        return getDebug(th, -1);
    }

    public static String getDebug(Throwable th, int i) {
        StringBuffer stringBuffer = new StringBuffer();
        String toLowerCase = timestamp.format(new Date()).toLowerCase();
        stringBuffer.append(newline);
        stringBuffer.append("Throwable: " + (th == null ? HttpVersions.HTTP_0_9 : "(" + th.getClass().getName() + ")") + ":" + toLowerCase + newline);
        stringBuffer.append("Throwable: " + (th == null ? "null" : th.getLocalizedMessage()) + newline);
        stringBuffer.append(newline);
        stringBuffer.append(getStackTrace(th, i));
        stringBuffer.append("Caught here:" + newline);
        stringBuffer.append(getStackTrace(new Exception(), i, 1));
        stringBuffer.append(newline);
        return stringBuffer.toString();
    }

    public static String getStackTrace(Throwable th) {
        return getStackTrace(th, -1);
    }

    public static String getStackTrace(Throwable th, int i) {
        return getStackTrace(th, i, 0);
    }

    public static String getStackTrace(Throwable th, int i, int i2) {
        StringBuffer stringBuffer = new StringBuffer();
        if (th != null) {
            StackTraceElement[] stackTrace = th.getStackTrace();
            if (stackTrace != null) {
                while (i2 < stackTrace.length && (i < 0 || i2 < i)) {
                    StackTraceElement stackTraceElement = stackTrace[i2];
                    stringBuffer.append("\tat " + stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName() + "(" + stackTraceElement.getFileName() + ":" + stackTraceElement.getLineNumber() + ")" + newline);
                    i2++;
                }
                if (i >= 0 && stackTrace.length > i) {
                    stringBuffer.append("\t..." + newline);
                }
            }
            stringBuffer.append(newline);
        }
        return stringBuffer.toString();
    }

    public static String getType(Object obj) {
        return obj == null ? "null" : obj instanceof Object[] ? "[Object[]: " + ((Object[]) obj).length + "]" : obj instanceof char[] ? "[char[]: " + ((char[]) obj).length + "]" : obj instanceof byte[] ? "[byte[]: " + ((byte[]) obj).length + "]" : obj instanceof short[] ? "[short[]: " + ((short[]) obj).length + "]" : obj instanceof int[] ? "[int[]: " + ((int[]) obj).length + "]" : obj instanceof long[] ? "[long[]: " + ((long[]) obj).length + "]" : obj instanceof float[] ? "[float[]: " + ((float[]) obj).length + "]" : obj instanceof double[] ? "[double[]: " + ((double[]) obj).length + "]" : obj instanceof boolean[] ? "[boolean[]: " + ((boolean[]) obj).length + "]" : obj.getClass().getName();
    }

    public static boolean isArray(Object obj) {
        if (obj != null) {
            if ((obj instanceof Object[]) || (obj instanceof char[]) || (obj instanceof byte[]) || (obj instanceof short[]) || (obj instanceof int[]) || (obj instanceof long[]) || (obj instanceof float[]) || (obj instanceof double[])) {
                return true;
            }
            if (obj instanceof boolean[]) {
                return true;
            }
        }
        return false;
    }

    private static void log(StringBuffer stringBuffer, String str) {
        debug(str);
        if (stringBuffer != null) {
            stringBuffer.append(new StringBuilder(String.valueOf(str)).append(newline).toString());
        }
    }

    public static void newline() {
        System.out.print(newline);
    }

    public static final void purgeMemory() {
        try {
            System.runFinalization();
            Thread.sleep(50);
            System.gc();
            Thread.sleep(50);
        } catch (Throwable th) {
            debug(th);
        }
    }
}
