package org.mortbay.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import org.mortbay.log.Log;

public class TypeUtil {
    public static int CR = 13;
    public static int LF = 10;
    static Class class$java$lang$Boolean;
    static Class class$java$lang$Byte;
    static Class class$java$lang$Character;
    static Class class$java$lang$Double;
    static Class class$java$lang$Float;
    static Class class$java$lang$Integer;
    static Class class$java$lang$Long;
    static Class class$java$lang$Short;
    static Class class$java$lang$String;
    private static final HashMap class2Name = new HashMap();
    private static final HashMap class2Value = new HashMap();
    private static int intCacheSize = Integer.getInteger("org.mortbay.util.TypeUtil.IntegerCacheSize", 600).intValue();
    private static Integer[] integerCache = new Integer[intCacheSize];
    private static String[] integerStrCache = new String[intCacheSize];
    private static Long[] longCache = new Long[longCacheSize];
    private static int longCacheSize = Integer.getInteger("org.mortbay.util.TypeUtil.LongCacheSize", 64).intValue();
    private static Integer minusOne = new Integer(-1);
    private static Long minusOneL = new Long(-1);
    private static final HashMap name2Class = new HashMap();
    private static Class[] stringArg;

    static {
        Object class$;
        Class class$2;
        name2Class.put("boolean", Boolean.TYPE);
        name2Class.put("byte", Byte.TYPE);
        name2Class.put("char", Character.TYPE);
        name2Class.put("double", Double.TYPE);
        name2Class.put("float", Float.TYPE);
        name2Class.put("int", Integer.TYPE);
        name2Class.put("long", Long.TYPE);
        name2Class.put("short", Short.TYPE);
        name2Class.put("void", Void.TYPE);
        name2Class.put("java.lang.Boolean.TYPE", Boolean.TYPE);
        name2Class.put("java.lang.Byte.TYPE", Byte.TYPE);
        name2Class.put("java.lang.Character.TYPE", Character.TYPE);
        name2Class.put("java.lang.Double.TYPE", Double.TYPE);
        name2Class.put("java.lang.Float.TYPE", Float.TYPE);
        name2Class.put("java.lang.Integer.TYPE", Integer.TYPE);
        name2Class.put("java.lang.Long.TYPE", Long.TYPE);
        name2Class.put("java.lang.Short.TYPE", Short.TYPE);
        name2Class.put("java.lang.Void.TYPE", Void.TYPE);
        HashMap hashMap = name2Class;
        if (class$java$lang$Boolean == null) {
            class$ = class$("java.lang.Boolean");
            class$java$lang$Boolean = class$;
        } else {
            class$ = class$java$lang$Boolean;
        }
        hashMap.put("java.lang.Boolean", class$);
        hashMap = name2Class;
        if (class$java$lang$Byte == null) {
            class$ = class$("java.lang.Byte");
            class$java$lang$Byte = class$;
        } else {
            class$ = class$java$lang$Byte;
        }
        hashMap.put("java.lang.Byte", class$);
        hashMap = name2Class;
        if (class$java$lang$Character == null) {
            class$ = class$("java.lang.Character");
            class$java$lang$Character = class$;
        } else {
            class$ = class$java$lang$Character;
        }
        hashMap.put("java.lang.Character", class$);
        hashMap = name2Class;
        if (class$java$lang$Double == null) {
            class$ = class$("java.lang.Double");
            class$java$lang$Double = class$;
        } else {
            class$ = class$java$lang$Double;
        }
        hashMap.put("java.lang.Double", class$);
        hashMap = name2Class;
        if (class$java$lang$Float == null) {
            class$ = class$("java.lang.Float");
            class$java$lang$Float = class$;
        } else {
            class$ = class$java$lang$Float;
        }
        hashMap.put("java.lang.Float", class$);
        hashMap = name2Class;
        if (class$java$lang$Integer == null) {
            class$ = class$("java.lang.Integer");
            class$java$lang$Integer = class$;
        } else {
            class$ = class$java$lang$Integer;
        }
        hashMap.put("java.lang.Integer", class$);
        hashMap = name2Class;
        if (class$java$lang$Long == null) {
            class$ = class$("java.lang.Long");
            class$java$lang$Long = class$;
        } else {
            class$ = class$java$lang$Long;
        }
        hashMap.put("java.lang.Long", class$);
        hashMap = name2Class;
        if (class$java$lang$Short == null) {
            class$ = class$("java.lang.Short");
            class$java$lang$Short = class$;
        } else {
            class$ = class$java$lang$Short;
        }
        hashMap.put("java.lang.Short", class$);
        hashMap = name2Class;
        if (class$java$lang$Boolean == null) {
            class$ = class$("java.lang.Boolean");
            class$java$lang$Boolean = class$;
        } else {
            class$ = class$java$lang$Boolean;
        }
        hashMap.put("Boolean", class$);
        hashMap = name2Class;
        if (class$java$lang$Byte == null) {
            class$ = class$("java.lang.Byte");
            class$java$lang$Byte = class$;
        } else {
            class$ = class$java$lang$Byte;
        }
        hashMap.put("Byte", class$);
        hashMap = name2Class;
        if (class$java$lang$Character == null) {
            class$ = class$("java.lang.Character");
            class$java$lang$Character = class$;
        } else {
            class$ = class$java$lang$Character;
        }
        hashMap.put("Character", class$);
        hashMap = name2Class;
        if (class$java$lang$Double == null) {
            class$ = class$("java.lang.Double");
            class$java$lang$Double = class$;
        } else {
            class$ = class$java$lang$Double;
        }
        hashMap.put("Double", class$);
        hashMap = name2Class;
        if (class$java$lang$Float == null) {
            class$ = class$("java.lang.Float");
            class$java$lang$Float = class$;
        } else {
            class$ = class$java$lang$Float;
        }
        hashMap.put("Float", class$);
        hashMap = name2Class;
        if (class$java$lang$Integer == null) {
            class$ = class$("java.lang.Integer");
            class$java$lang$Integer = class$;
        } else {
            class$ = class$java$lang$Integer;
        }
        hashMap.put("Integer", class$);
        hashMap = name2Class;
        if (class$java$lang$Long == null) {
            class$ = class$("java.lang.Long");
            class$java$lang$Long = class$;
        } else {
            class$ = class$java$lang$Long;
        }
        hashMap.put("Long", class$);
        hashMap = name2Class;
        if (class$java$lang$Short == null) {
            class$ = class$("java.lang.Short");
            class$java$lang$Short = class$;
        } else {
            class$ = class$java$lang$Short;
        }
        hashMap.put("Short", class$);
        name2Class.put(null, Void.TYPE);
        hashMap = name2Class;
        if (class$java$lang$String == null) {
            class$ = class$("java.lang.String");
            class$java$lang$String = class$;
        } else {
            class$ = class$java$lang$String;
        }
        hashMap.put("string", class$);
        hashMap = name2Class;
        if (class$java$lang$String == null) {
            class$ = class$("java.lang.String");
            class$java$lang$String = class$;
        } else {
            class$ = class$java$lang$String;
        }
        hashMap.put("String", class$);
        hashMap = name2Class;
        if (class$java$lang$String == null) {
            class$ = class$("java.lang.String");
            class$java$lang$String = class$;
        } else {
            class$ = class$java$lang$String;
        }
        hashMap.put("java.lang.String", class$);
        class2Name.put(Boolean.TYPE, "boolean");
        class2Name.put(Byte.TYPE, "byte");
        class2Name.put(Character.TYPE, "char");
        class2Name.put(Double.TYPE, "double");
        class2Name.put(Float.TYPE, "float");
        class2Name.put(Integer.TYPE, "int");
        class2Name.put(Long.TYPE, "long");
        class2Name.put(Short.TYPE, "short");
        class2Name.put(Void.TYPE, "void");
        hashMap = class2Name;
        if (class$java$lang$Boolean == null) {
            class$ = class$("java.lang.Boolean");
            class$java$lang$Boolean = class$;
        } else {
            class$ = class$java$lang$Boolean;
        }
        hashMap.put(class$, "java.lang.Boolean");
        hashMap = class2Name;
        if (class$java$lang$Byte == null) {
            class$ = class$("java.lang.Byte");
            class$java$lang$Byte = class$;
        } else {
            class$ = class$java$lang$Byte;
        }
        hashMap.put(class$, "java.lang.Byte");
        hashMap = class2Name;
        if (class$java$lang$Character == null) {
            class$ = class$("java.lang.Character");
            class$java$lang$Character = class$;
        } else {
            class$ = class$java$lang$Character;
        }
        hashMap.put(class$, "java.lang.Character");
        hashMap = class2Name;
        if (class$java$lang$Double == null) {
            class$ = class$("java.lang.Double");
            class$java$lang$Double = class$;
        } else {
            class$ = class$java$lang$Double;
        }
        hashMap.put(class$, "java.lang.Double");
        hashMap = class2Name;
        if (class$java$lang$Float == null) {
            class$ = class$("java.lang.Float");
            class$java$lang$Float = class$;
        } else {
            class$ = class$java$lang$Float;
        }
        hashMap.put(class$, "java.lang.Float");
        hashMap = class2Name;
        if (class$java$lang$Integer == null) {
            class$ = class$("java.lang.Integer");
            class$java$lang$Integer = class$;
        } else {
            class$ = class$java$lang$Integer;
        }
        hashMap.put(class$, "java.lang.Integer");
        hashMap = class2Name;
        if (class$java$lang$Long == null) {
            class$ = class$("java.lang.Long");
            class$java$lang$Long = class$;
        } else {
            class$ = class$java$lang$Long;
        }
        hashMap.put(class$, "java.lang.Long");
        hashMap = class2Name;
        if (class$java$lang$Short == null) {
            class$ = class$("java.lang.Short");
            class$java$lang$Short = class$;
        } else {
            class$ = class$java$lang$Short;
        }
        hashMap.put(class$, "java.lang.Short");
        class2Name.put(null, "void");
        hashMap = class2Name;
        if (class$java$lang$String == null) {
            class$ = class$("java.lang.String");
            class$java$lang$String = class$;
        } else {
            class$ = class$java$lang$String;
        }
        hashMap.put(class$, "java.lang.String");
        try {
            Object obj;
            Class cls;
            Class[] clsArr = new Class[1];
            if (class$java$lang$String == null) {
                class$2 = class$("java.lang.String");
                class$java$lang$String = class$2;
            } else {
                class$2 = class$java$lang$String;
            }
            clsArr[0] = class$2;
            hashMap = class2Value;
            Class cls2 = Boolean.TYPE;
            if (class$java$lang$Boolean == null) {
                class$2 = class$("java.lang.Boolean");
                class$java$lang$Boolean = class$2;
            } else {
                class$2 = class$java$lang$Boolean;
            }
            hashMap.put(cls2, class$2.getMethod("valueOf", clsArr));
            hashMap = class2Value;
            cls2 = Byte.TYPE;
            if (class$java$lang$Byte == null) {
                class$2 = class$("java.lang.Byte");
                class$java$lang$Byte = class$2;
            } else {
                class$2 = class$java$lang$Byte;
            }
            hashMap.put(cls2, class$2.getMethod("valueOf", clsArr));
            hashMap = class2Value;
            cls2 = Double.TYPE;
            if (class$java$lang$Double == null) {
                class$2 = class$("java.lang.Double");
                class$java$lang$Double = class$2;
            } else {
                class$2 = class$java$lang$Double;
            }
            hashMap.put(cls2, class$2.getMethod("valueOf", clsArr));
            hashMap = class2Value;
            cls2 = Float.TYPE;
            if (class$java$lang$Float == null) {
                class$2 = class$("java.lang.Float");
                class$java$lang$Float = class$2;
            } else {
                class$2 = class$java$lang$Float;
            }
            hashMap.put(cls2, class$2.getMethod("valueOf", clsArr));
            hashMap = class2Value;
            cls2 = Integer.TYPE;
            if (class$java$lang$Integer == null) {
                class$2 = class$("java.lang.Integer");
                class$java$lang$Integer = class$2;
            } else {
                class$2 = class$java$lang$Integer;
            }
            hashMap.put(cls2, class$2.getMethod("valueOf", clsArr));
            hashMap = class2Value;
            cls2 = Long.TYPE;
            if (class$java$lang$Long == null) {
                class$2 = class$("java.lang.Long");
                class$java$lang$Long = class$2;
            } else {
                class$2 = class$java$lang$Long;
            }
            hashMap.put(cls2, class$2.getMethod("valueOf", clsArr));
            hashMap = class2Value;
            cls2 = Short.TYPE;
            if (class$java$lang$Short == null) {
                class$2 = class$("java.lang.Short");
                class$java$lang$Short = class$2;
            } else {
                class$2 = class$java$lang$Short;
            }
            hashMap.put(cls2, class$2.getMethod("valueOf", clsArr));
            HashMap hashMap2 = class2Value;
            if (class$java$lang$Boolean == null) {
                class$2 = class$("java.lang.Boolean");
                class$java$lang$Boolean = class$2;
                obj = class$2;
            } else {
                cls = class$java$lang$Boolean;
            }
            if (class$java$lang$Boolean == null) {
                class$2 = class$("java.lang.Boolean");
                class$java$lang$Boolean = class$2;
            } else {
                class$2 = class$java$lang$Boolean;
            }
            hashMap2.put(obj, class$2.getMethod("valueOf", clsArr));
            hashMap2 = class2Value;
            if (class$java$lang$Byte == null) {
                class$2 = class$("java.lang.Byte");
                class$java$lang$Byte = class$2;
                obj = class$2;
            } else {
                cls = class$java$lang$Byte;
            }
            if (class$java$lang$Byte == null) {
                class$2 = class$("java.lang.Byte");
                class$java$lang$Byte = class$2;
            } else {
                class$2 = class$java$lang$Byte;
            }
            hashMap2.put(obj, class$2.getMethod("valueOf", clsArr));
            hashMap2 = class2Value;
            if (class$java$lang$Double == null) {
                class$2 = class$("java.lang.Double");
                class$java$lang$Double = class$2;
                obj = class$2;
            } else {
                cls = class$java$lang$Double;
            }
            if (class$java$lang$Double == null) {
                class$2 = class$("java.lang.Double");
                class$java$lang$Double = class$2;
            } else {
                class$2 = class$java$lang$Double;
            }
            hashMap2.put(obj, class$2.getMethod("valueOf", clsArr));
            hashMap2 = class2Value;
            if (class$java$lang$Float == null) {
                class$2 = class$("java.lang.Float");
                class$java$lang$Float = class$2;
                obj = class$2;
            } else {
                cls = class$java$lang$Float;
            }
            if (class$java$lang$Float == null) {
                class$2 = class$("java.lang.Float");
                class$java$lang$Float = class$2;
            } else {
                class$2 = class$java$lang$Float;
            }
            hashMap2.put(obj, class$2.getMethod("valueOf", clsArr));
            hashMap2 = class2Value;
            if (class$java$lang$Integer == null) {
                class$2 = class$("java.lang.Integer");
                class$java$lang$Integer = class$2;
                obj = class$2;
            } else {
                cls = class$java$lang$Integer;
            }
            if (class$java$lang$Integer == null) {
                class$2 = class$("java.lang.Integer");
                class$java$lang$Integer = class$2;
            } else {
                class$2 = class$java$lang$Integer;
            }
            hashMap2.put(obj, class$2.getMethod("valueOf", clsArr));
            hashMap2 = class2Value;
            if (class$java$lang$Long == null) {
                class$2 = class$("java.lang.Long");
                class$java$lang$Long = class$2;
                obj = class$2;
            } else {
                cls = class$java$lang$Long;
            }
            if (class$java$lang$Long == null) {
                class$2 = class$("java.lang.Long");
                class$java$lang$Long = class$2;
            } else {
                class$2 = class$java$lang$Long;
            }
            hashMap2.put(obj, class$2.getMethod("valueOf", clsArr));
            hashMap2 = class2Value;
            if (class$java$lang$Short == null) {
                class$2 = class$("java.lang.Short");
                class$java$lang$Short = class$2;
                obj = class$2;
            } else {
                cls = class$java$lang$Short;
            }
            if (class$java$lang$Short == null) {
                class$2 = class$("java.lang.Short");
                class$java$lang$Short = class$2;
            } else {
                class$2 = class$java$lang$Short;
            }
            hashMap2.put(obj, class$2.getMethod("valueOf", clsArr));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (class$java$lang$String == null) {
            class$2 = class$("java.lang.String");
            class$java$lang$String = class$2;
        } else {
            class$2 = class$java$lang$String;
        }
        stringArg = new Class[]{class$2};
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (Throwable e) {
            throw new NoClassDefFoundError().initCause(e);
        }
    }

    public static byte convertHexDigit(byte b) {
        return (b < (byte) 48 || b > (byte) 57) ? (b < (byte) 97 || b > (byte) 102) ? (b < (byte) 65 || b > (byte) 70) ? (byte) 0 : (byte) ((b - 65) + 10) : (byte) ((b - 97) + 10) : (byte) (b - 48);
    }

    public static void dump(Class cls) {
        System.err.println(new StringBuffer().append("Dump: ").append(cls).toString());
        dump(cls.getClassLoader());
    }

    public static void dump(ClassLoader classLoader) {
        System.err.println("Dump Loaders:");
        while (classLoader != null) {
            System.err.println(new StringBuffer().append("  loader ").append(classLoader).toString());
            classLoader = classLoader.getParent();
        }
    }

    public static byte[] fromHexString(String str) {
        if (str.length() % 2 != 0) {
            throw new IllegalArgumentException(str);
        }
        byte[] bArr = new byte[(str.length() / 2)];
        for (int i = 0; i < bArr.length; i++) {
            bArr[i] = (byte) (Integer.parseInt(str.substring(i * 2, (i * 2) + 2), 16) & 255);
        }
        return bArr;
    }

    public static Class fromName(String str) {
        return (Class) name2Class.get(str);
    }

    public static URL jarFor(String str) {
        try {
            String url = Loader.getResource(null, new StringBuffer().append(str.replace('.', '/')).append(".class").toString(), false).toString();
            if (url.startsWith("jar:file:")) {
                return new URL(url.substring(4, url.indexOf("!/")));
            }
        } catch (Throwable e) {
            Log.ignore(e);
        }
        return null;
    }

    public static Integer newInteger(int i) {
        if (i < 0 || i >= intCacheSize) {
            return i == -1 ? minusOne : new Integer(i);
        } else {
            if (integerCache[i] == null) {
                integerCache[i] = new Integer(i);
            }
            return integerCache[i];
        }
    }

    public static Long newLong(long j) {
        if (j < 0 || j >= ((long) longCacheSize)) {
            return j == -1 ? minusOneL : new Long(j);
        } else {
            if (longCache[(int) j] == null) {
                longCache[(int) j] = new Long(j);
            }
            return longCache[(int) j];
        }
    }

    public static byte[] parseBytes(String str, int i) {
        byte[] bArr = new byte[(str.length() / 2)];
        for (int i2 = 0; i2 < str.length(); i2 += 2) {
            bArr[i2 / 2] = (byte) parseInt(str, i2, 2, i);
        }
        return bArr;
    }

    public static int parseInt(String str, int i, int i2, int i3) throws NumberFormatException {
        if (i2 < 0) {
            i2 = str.length() - i;
        }
        int i4 = 0;
        int i5 = 0;
        while (i5 < i2) {
            char charAt = str.charAt(i + i5);
            int i6 = charAt - 48;
            if (i6 < 0 || i6 >= i3 || i6 >= 10) {
                i6 = (charAt + 10) - 65;
                if (i6 < 10 || i6 >= i3) {
                    i6 = (charAt + 10) - 97;
                }
            }
            if (i6 < 0 || i6 >= i3) {
                throw new NumberFormatException(str.substring(i, i + i2));
            }
            i5++;
            i4 = i6 + (i4 * i3);
        }
        return i4;
    }

    public static int parseInt(byte[] bArr, int i, int i2, int i3) throws NumberFormatException {
        if (i2 < 0) {
            i2 = bArr.length - i;
        }
        int i4 = 0;
        int i5 = 0;
        while (i5 < i2) {
            char c = (char) (bArr[i + i5] & 255);
            int i6 = c - 48;
            if (i6 < 0 || i6 >= i3 || i6 >= 10) {
                i6 = (c + 10) - 65;
                if (i6 < 10 || i6 >= i3) {
                    i6 = (c + 10) - 97;
                }
            }
            if (i6 < 0 || i6 >= i3) {
                throw new NumberFormatException(new String(bArr, i, i2));
            }
            i5++;
            i4 = i6 + (i4 * i3);
        }
        return i4;
    }

    public static byte[] readLine(InputStream inputStream) throws IOException {
        Object obj = new byte[256];
        int i = 0;
        int i2 = 0;
        while (true) {
            int read = inputStream.read();
            if (read < 0) {
                break;
            }
            i2++;
            if (i2 != 1 || read != LF) {
                if (read == CR || read == LF) {
                    break;
                }
                if (i >= obj.length) {
                    Object obj2 = new byte[(obj.length + 256)];
                    System.arraycopy(obj, 0, obj2, 0, obj.length);
                    obj = obj2;
                }
                obj[i] = (byte) read;
                i++;
            }
        }
        if (read == -1 && i == 0) {
            return null;
        }
        if (read == CR && inputStream.available() >= 1 && inputStream.markSupported()) {
            inputStream.mark(1);
            if (inputStream.read() != LF) {
                inputStream.reset();
            }
        }
        Object obj3 = new byte[i];
        System.arraycopy(obj, 0, obj3, 0, i);
        return obj3;
    }

    public static String toHexString(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer();
        for (byte b : bArr) {
            int i = b & 255;
            int i2 = ((i / 16) % 16) + 48;
            if (i2 > 57) {
                i2 = ((i2 - 48) - 10) + 65;
            }
            stringBuffer.append((char) i2);
            i2 = (i % 16) + 48;
            if (i2 > 57) {
                i2 = ((i2 - 48) - 10) + 97;
            }
            stringBuffer.append((char) i2);
        }
        return stringBuffer.toString();
    }

    public static String toHexString(byte[] bArr, int i, int i2) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i3 = i; i3 < i + i2; i3++) {
            int i4 = bArr[i3] & 255;
            int i5 = ((i4 / 16) % 16) + 48;
            if (i5 > 57) {
                i5 = ((i5 - 48) - 10) + 65;
            }
            stringBuffer.append((char) i5);
            i5 = (i4 % 16) + 48;
            if (i5 > 57) {
                i5 = ((i5 - 48) - 10) + 97;
            }
            stringBuffer.append((char) i5);
        }
        return stringBuffer.toString();
    }

    public static String toName(Class cls) {
        return (String) class2Name.get(cls);
    }

    public static String toString(int i) {
        if (i < 0 || i >= intCacheSize) {
            return i == -1 ? "-1" : Integer.toString(i);
        } else {
            if (integerStrCache[i] == null) {
                integerStrCache[i] = Integer.toString(i);
            }
            return integerStrCache[i];
        }
    }

    public static String toString(long j) {
        if (j < 0 || j >= ((long) intCacheSize)) {
            return j == -1 ? "-1" : Long.toString(j);
        } else {
            if (integerStrCache[(int) j] == null) {
                integerStrCache[(int) j] = Long.toString(j);
            }
            return integerStrCache[(int) j];
        }
    }

    public static String toString(byte[] bArr, int i) {
        StringBuffer stringBuffer = new StringBuffer();
        for (byte b : bArr) {
            int i2 = b & 255;
            int i3 = ((i2 / i) % i) + 48;
            if (i3 > 57) {
                i3 = ((i3 - 48) - 10) + 97;
            }
            stringBuffer.append((char) i3);
            i3 = (i2 % i) + 48;
            if (i3 > 57) {
                i3 = ((i3 - 48) - 10) + 97;
            }
            stringBuffer.append((char) i3);
        }
        return stringBuffer.toString();
    }

    public static Object valueOf(Class cls, String str) {
        try {
            Object class$;
            if (class$java$lang$String == null) {
                class$ = class$("java.lang.String");
                class$java$lang$String = class$;
            } else {
                class$ = class$java$lang$String;
            }
            if (cls.equals(class$)) {
                return str;
            }
            Method method = (Method) class2Value.get(cls);
            if (method != null) {
                return method.invoke(null, new Object[]{str});
            }
            if (!cls.equals(Character.TYPE)) {
                if (class$java$lang$Character == null) {
                    class$ = class$("java.lang.Character");
                    class$java$lang$Character = class$;
                } else {
                    class$ = class$java$lang$Character;
                }
                if (!cls.equals(class$)) {
                    return cls.getConstructor(stringArg).newInstance(new Object[]{str});
                }
            }
            return new Character(str.charAt(0));
        } catch (NoSuchMethodException e) {
            return null;
        } catch (IllegalAccessException e2) {
            return null;
        } catch (InstantiationException e3) {
            return null;
        } catch (InvocationTargetException e4) {
            if (e4.getTargetException() instanceof Error) {
                throw ((Error) e4.getTargetException());
            }
            return null;
        }
    }

    public static Object valueOf(String str, String str2) {
        return valueOf(fromName(str), str2);
    }
}
