package org.mortbay.util.ajax;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.sanselan.formats.tiff.constants.ExifTagConstants;
import org.mortbay.log.Log;
import org.mortbay.util.IO;
import org.mortbay.util.Loader;
import org.mortbay.util.QuotedStringTokenizer;
import org.mortbay.util.TypeUtil;

public class JSON {
    private static JSON __default = new JSON();
    static Class class$java$lang$Object;
    static Class class$org$mortbay$util$ajax$JSON;
    static Class class$org$mortbay$util$ajax$JSON$Convertible;
    private Map _convertors = Collections.synchronizedMap(new HashMap());
    private int _stringBufferSize = 256;

    public interface Convertible {
        void fromJSON(Map map);

        void toJSON(Output output);
    }

    class C13541 implements Convertible {
        private final JSON this$0;
        private final Convertor val$convertor;
        private final Object val$object;

        C13541(JSON json, Convertor convertor, Object obj) {
            this.this$0 = json;
            this.val$convertor = convertor;
            this.val$object = obj;
        }

        public void fromJSON(Map map) {
        }

        public void toJSON(Output output) {
            this.val$convertor.toJSON(this.val$object, output);
        }
    }

    public interface Output {
        void add(Object obj);

        void add(String str, double d);

        void add(String str, long j);

        void add(String str, Object obj);

        void add(String str, boolean z);

        void addClass(Class cls);
    }

    class C13552 implements Output {
        private final JSON this$0;
        private final StringBuffer val$buffer;
        private final char[] val$c;

        C13552(JSON json, char[] cArr, StringBuffer stringBuffer) {
            this.this$0 = json;
            this.val$c = cArr;
            this.val$buffer = stringBuffer;
        }

        public void add(Object obj) {
            if (this.val$c[0] == '\u0000') {
                throw new IllegalStateException();
            }
            this.this$0.append(this.val$buffer, obj);
            this.val$c[0] = '\u0000';
        }

        public void add(String str, double d) {
            if (this.val$c[0] == '\u0000') {
                throw new IllegalStateException();
            }
            this.val$buffer.append(this.val$c);
            QuotedStringTokenizer.quote(this.val$buffer, str);
            this.val$buffer.append(':');
            this.this$0.appendNumber(this.val$buffer, new Double(d));
            this.val$c[0] = ',';
        }

        public void add(String str, long j) {
            if (this.val$c[0] == '\u0000') {
                throw new IllegalStateException();
            }
            this.val$buffer.append(this.val$c);
            QuotedStringTokenizer.quote(this.val$buffer, str);
            this.val$buffer.append(':');
            this.this$0.appendNumber(this.val$buffer, TypeUtil.newLong(j));
            this.val$c[0] = ',';
        }

        public void add(String str, Object obj) {
            if (this.val$c[0] == '\u0000') {
                throw new IllegalStateException();
            }
            this.val$buffer.append(this.val$c);
            QuotedStringTokenizer.quote(this.val$buffer, str);
            this.val$buffer.append(':');
            this.this$0.append(this.val$buffer, obj);
            this.val$c[0] = ',';
        }

        public void add(String str, boolean z) {
            if (this.val$c[0] == '\u0000') {
                throw new IllegalStateException();
            }
            this.val$buffer.append(this.val$c);
            QuotedStringTokenizer.quote(this.val$buffer, str);
            this.val$buffer.append(':');
            this.this$0.appendBoolean(this.val$buffer, z ? Boolean.TRUE : Boolean.FALSE);
            this.val$c[0] = ',';
        }

        public void addClass(Class cls) {
            if (this.val$c[0] == '\u0000') {
                throw new IllegalStateException();
            }
            this.val$buffer.append(this.val$c);
            this.val$buffer.append("\"class\":");
            this.this$0.append(this.val$buffer, cls.getName());
            this.val$c[0] = ',';
        }
    }

    public interface Convertor {
        Object fromJSON(Map map);

        void toJSON(Object obj, Output output);
    }

    public interface Generator {
        void addJSON(StringBuffer stringBuffer);
    }

    public static class Literal implements Generator {
        private String _json;

        public Literal(String str) {
            if (Log.isDebugEnabled()) {
                JSON.parse(str);
            }
            this._json = str;
        }

        public void addJSON(StringBuffer stringBuffer) {
            stringBuffer.append(this._json);
        }

        public String toString() {
            return this._json;
        }
    }

    public interface Source {
        boolean hasNext();

        char next();

        char peek();

        char[] scratchBuffer();
    }

    public static class ReaderSource implements Source {
        private int _next = -1;
        private Reader _reader;
        private char[] scratch;

        public ReaderSource(Reader reader) {
            this._reader = reader;
        }

        private void getNext() {
            if (this._next < 0) {
                try {
                    this._next = this._reader.read();
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
        }

        public boolean hasNext() {
            getNext();
            if (this._next >= 0) {
                return true;
            }
            this.scratch = null;
            return false;
        }

        public char next() {
            getNext();
            char c = (char) this._next;
            this._next = -1;
            return c;
        }

        public char peek() {
            getNext();
            return (char) this._next;
        }

        public char[] scratchBuffer() {
            if (this.scratch == null) {
                this.scratch = new char[1024];
            }
            return this.scratch;
        }

        public void setReader(Reader reader) {
            this._reader = reader;
            this._next = -1;
        }
    }

    public static class StringSource implements Source {
        private int index;
        private char[] scratch;
        private final String string;

        public StringSource(String str) {
            this.string = str;
        }

        public boolean hasNext() {
            if (this.index < this.string.length()) {
                return true;
            }
            this.scratch = null;
            return false;
        }

        public char next() {
            String str = this.string;
            int i = this.index;
            this.index = i + 1;
            return str.charAt(i);
        }

        public char peek() {
            return this.string.charAt(this.index);
        }

        public char[] scratchBuffer() {
            if (this.scratch == null) {
                this.scratch = new char[this.string.length()];
            }
            return this.scratch;
        }

        public String toString() {
            return new StringBuffer().append(this.string.substring(0, this.index)).append("|||").append(this.string.substring(this.index)).toString();
        }
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (Throwable e) {
            throw new NoClassDefFoundError().initCause(e);
        }
    }

    protected static void complete(String str, Source source) {
        int i = 0;
        while (source.hasNext() && i < str.length()) {
            char next = source.next();
            if (next != str.charAt(i)) {
                throw new IllegalStateException(new StringBuffer().append("Unexpected '").append(next).append(" while seeking  \"").append(str).append("\"").toString());
            }
            i++;
        }
        if (i < str.length()) {
            throw new IllegalStateException(new StringBuffer().append("Expected \"").append(str).append("\"").toString());
        }
    }

    public static JSON getDefault() {
        return __default;
    }

    public static Object parse(InputStream inputStream) throws IOException {
        return __default.parse(new StringSource(IO.toString(inputStream)), false);
    }

    public static Object parse(InputStream inputStream, boolean z) throws IOException {
        return __default.parse(new StringSource(IO.toString(inputStream)), z);
    }

    public static Object parse(Reader reader) throws IOException {
        return __default.parse(new ReaderSource(reader), false);
    }

    public static Object parse(Reader reader, boolean z) throws IOException {
        return __default.parse(new ReaderSource(reader), z);
    }

    public static Object parse(String str) {
        return __default.parse(new StringSource(str), false);
    }

    public static Object parse(String str, boolean z) {
        return __default.parse(new StringSource(str), z);
    }

    public static void registerConvertor(Class cls, Convertor convertor) {
        __default.addConvertor(cls, convertor);
    }

    public static void setDefault(JSON json) {
        __default = json;
    }

    public static String toString(Object obj) {
        String stringBuffer;
        StringBuffer stringBuffer2 = new StringBuffer(__default.getStringBufferSize());
        synchronized (stringBuffer2) {
            __default.append(stringBuffer2, obj);
            stringBuffer = stringBuffer2.toString();
        }
        return stringBuffer;
    }

    public static String toString(Map map) {
        String stringBuffer;
        StringBuffer stringBuffer2 = new StringBuffer(__default.getStringBufferSize());
        synchronized (stringBuffer2) {
            __default.appendMap(stringBuffer2, map);
            stringBuffer = stringBuffer2.toString();
        }
        return stringBuffer;
    }

    public static String toString(Object[] objArr) {
        String stringBuffer;
        StringBuffer stringBuffer2 = new StringBuffer(__default.getStringBufferSize());
        synchronized (stringBuffer2) {
            __default.appendArray(stringBuffer2, (Object) objArr);
            stringBuffer = stringBuffer2.toString();
        }
        return stringBuffer;
    }

    public void addConvertor(Class cls, Convertor convertor) {
        this._convertors.put(cls.getName(), convertor);
    }

    public void addConvertorFor(String str, Convertor convertor) {
        this._convertors.put(str, convertor);
    }

    public void append(StringBuffer stringBuffer, Object obj) {
        if (obj == null) {
            stringBuffer.append("null");
        } else if (obj instanceof Convertible) {
            appendJSON(stringBuffer, (Convertible) obj);
        } else if (obj instanceof Generator) {
            appendJSON(stringBuffer, (Generator) obj);
        } else if (obj instanceof Map) {
            appendMap(stringBuffer, (Map) obj);
        } else if (obj instanceof Collection) {
            appendArray(stringBuffer, (Collection) obj);
        } else if (obj.getClass().isArray()) {
            appendArray(stringBuffer, obj);
        } else if (obj instanceof Number) {
            appendNumber(stringBuffer, (Number) obj);
        } else if (obj instanceof Boolean) {
            appendBoolean(stringBuffer, (Boolean) obj);
        } else if (obj instanceof String) {
            appendString(stringBuffer, (String) obj);
        } else {
            Convertor convertor = getConvertor(obj.getClass());
            if (convertor != null) {
                appendJSON(stringBuffer, convertor, obj);
            } else {
                appendString(stringBuffer, obj.toString());
            }
        }
    }

    public void appendArray(StringBuffer stringBuffer, Object obj) {
        if (obj == null) {
            appendNull(stringBuffer);
            return;
        }
        stringBuffer.append('[');
        int length = Array.getLength(obj);
        for (int i = 0; i < length; i++) {
            if (i != 0) {
                stringBuffer.append(',');
            }
            append(stringBuffer, Array.get(obj, i));
        }
        stringBuffer.append(']');
    }

    public void appendArray(StringBuffer stringBuffer, Collection collection) {
        if (collection == null) {
            appendNull(stringBuffer);
            return;
        }
        stringBuffer.append('[');
        Object obj = 1;
        for (Object append : collection) {
            if (obj == null) {
                stringBuffer.append(',');
            }
            obj = null;
            append(stringBuffer, append);
        }
        stringBuffer.append(']');
    }

    public void appendBoolean(StringBuffer stringBuffer, Boolean bool) {
        if (bool == null) {
            appendNull(stringBuffer);
        } else {
            stringBuffer.append(bool.booleanValue() ? "true" : "false");
        }
    }

    public void appendJSON(StringBuffer stringBuffer, Convertible convertible) {
        char[] cArr = new char[]{'{'};
        convertible.toJSON(new C13552(this, cArr, stringBuffer));
        if (cArr[0] == '{') {
            stringBuffer.append("{}");
        } else if (cArr[0] != '\u0000') {
            stringBuffer.append("}");
        }
    }

    public void appendJSON(StringBuffer stringBuffer, Convertor convertor, Object obj) {
        appendJSON(stringBuffer, new C13541(this, convertor, obj));
    }

    public void appendJSON(StringBuffer stringBuffer, Generator generator) {
        generator.addJSON(stringBuffer);
    }

    public void appendMap(StringBuffer stringBuffer, Map map) {
        if (map == null) {
            appendNull(stringBuffer);
            return;
        }
        stringBuffer.append('{');
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            QuotedStringTokenizer.quote(stringBuffer, entry.getKey().toString());
            stringBuffer.append(':');
            append(stringBuffer, entry.getValue());
            if (it.hasNext()) {
                stringBuffer.append(',');
            }
        }
        stringBuffer.append('}');
    }

    public void appendNull(StringBuffer stringBuffer) {
        stringBuffer.append("null");
    }

    public void appendNumber(StringBuffer stringBuffer, Number number) {
        if (number == null) {
            appendNull(stringBuffer);
        } else {
            stringBuffer.append(number);
        }
    }

    public void appendString(StringBuffer stringBuffer, String str) {
        if (str == null) {
            appendNull(stringBuffer);
        } else {
            QuotedStringTokenizer.quote(stringBuffer, str);
        }
    }

    protected JSON contextFor(String str) {
        return this;
    }

    protected JSON contextForArray() {
        return this;
    }

    protected Object convertTo(Class cls, Map map) {
        if (cls != null) {
            Class class$;
            if (class$org$mortbay$util$ajax$JSON$Convertible == null) {
                class$ = class$("org.mortbay.util.ajax.JSON$Convertible");
                class$org$mortbay$util$ajax$JSON$Convertible = class$;
            } else {
                class$ = class$org$mortbay$util$ajax$JSON$Convertible;
            }
            if (class$.isAssignableFrom(cls)) {
                try {
                    Convertible convertible = (Convertible) cls.newInstance();
                    convertible.fromJSON(map);
                    return convertible;
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
        }
        Convertor convertor = getConvertor(cls);
        return convertor != null ? convertor.fromJSON(map) : map;
    }

    public Object fromJSON(String str) {
        return parse(new StringSource(str));
    }

    protected org.mortbay.util.ajax.JSON.Convertor getConvertor(java.lang.Class r6) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxOverflowException: Regions stack size limit reached
	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:37)
	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:61)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
        /*
        r5 = this;
        r0 = r5._convertors;
        r1 = r6.getName();
        r0 = r0.get(r1);
        r0 = (org.mortbay.util.ajax.JSON.Convertor) r0;
        if (r0 != 0) goto L_0x0063;
    L_0x000e:
        r1 = __default;
        if (r5 == r1) goto L_0x0063;
    L_0x0012:
        r0 = __default;
        r0 = r0.getConvertor(r6);
        r1 = r0;
    L_0x0019:
        if (r1 != 0) goto L_0x0062;
    L_0x001b:
        if (r6 == 0) goto L_0x0062;
    L_0x001d:
        r0 = class$java$lang$Object;
        if (r0 != 0) goto L_0x004b;
    L_0x0021:
        r0 = "java.lang.Object";
        r0 = class$(r0);
        class$java$lang$Object = r0;
    L_0x0029:
        if (r6 == r0) goto L_0x0062;
    L_0x002b:
        r2 = r6.getInterfaces();
        r0 = 0;
        r4 = r1;
        r1 = r0;
        r0 = r4;
    L_0x0033:
        if (r0 != 0) goto L_0x004e;
    L_0x0035:
        if (r2 == 0) goto L_0x004e;
    L_0x0037:
        r3 = r2.length;
        if (r1 >= r3) goto L_0x004e;
    L_0x003a:
        r0 = r5._convertors;
        r3 = r2[r1];
        r3 = r3.getName();
        r0 = r0.get(r3);
        r0 = (org.mortbay.util.ajax.JSON.Convertor) r0;
        r1 = r1 + 1;
        goto L_0x0033;
    L_0x004b:
        r0 = class$java$lang$Object;
        goto L_0x0029;
    L_0x004e:
        if (r0 != 0) goto L_0x0063;
    L_0x0050:
        r6 = r6.getSuperclass();
        r0 = r5._convertors;
        r1 = r6.getName();
        r0 = r0.get(r1);
        r0 = (org.mortbay.util.ajax.JSON.Convertor) r0;
        r1 = r0;
        goto L_0x0019;
    L_0x0062:
        return r1;
    L_0x0063:
        r1 = r0;
        goto L_0x0019;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mortbay.util.ajax.JSON.getConvertor(java.lang.Class):org.mortbay.util.ajax.JSON$Convertor");
    }

    public Convertor getConvertorFor(String str) {
        Convertor convertor = (Convertor) this._convertors.get(str);
        return (convertor != null || this == __default) ? convertor : __default.getConvertorFor(str);
    }

    public int getStringBufferSize() {
        return this._stringBufferSize;
    }

    protected Object handleUnknown(Source source, char c) {
        throw new IllegalStateException(new StringBuffer().append("unknown char '").append(c).append("'(").append(c).append(") in ").append(source).toString());
    }

    protected Object[] newArray(int i) {
        return new Object[i];
    }

    protected Map newMap() {
        return new HashMap();
    }

    public Object parse(Source source) {
        Number parseNumber;
        Object obj = null;
        while (source.hasNext()) {
            char peek = source.peek();
            int i;
            if (obj == 1) {
                switch (peek) {
                    case '*':
                        i = 2;
                        break;
                    case '/':
                        obj = -1;
                        break;
                    default:
                        break;
                }
            } else if (obj > 1) {
                switch (peek) {
                    case '*':
                        i = 3;
                        break;
                    case '/':
                        if (obj != 3) {
                            i = 2;
                            break;
                        }
                        obj = null;
                        break;
                    default:
                        i = 2;
                        break;
                }
            } else if (obj < null) {
                switch (peek) {
                    case '\n':
                    case '\r':
                        obj = null;
                        break;
                    default:
                        break;
                }
            } else {
                switch (peek) {
                    case '\"':
                        return parseString(source);
                    case '-':
                        return parseNumber(source);
                    case '/':
                        i = 1;
                        continue;
                    case 'N':
                        complete("NaN", source);
                        return null;
                    case '[':
                        return parseArray(source);
                    case 'f':
                        complete("false", source);
                        return Boolean.FALSE;
                    case 'n':
                        complete("null", source);
                        return null;
                    case 't':
                        complete("true", source);
                        return Boolean.TRUE;
                    case 'u':
                        complete("undefined", source);
                        return null;
                    case '{':
                        return parseObject(source);
                    default:
                        if (Character.isDigit(peek)) {
                            parseNumber = parseNumber(source);
                            break;
                        } else if (!Character.isWhitespace(peek)) {
                            return handleUnknown(source, peek);
                        } else {
                            continue;
                        }
                }
                return parseNumber;
            }
            source.next();
        }
        parseNumber = null;
        return parseNumber;
    }

    public Object parse(Source source, boolean z) {
        if (!z) {
            return parse(source);
        }
        Object obj = null;
        Object obj2 = null;
        Object obj3 = 1;
        while (source.hasNext()) {
            char peek = source.peek();
            int i;
            if (obj2 == 1) {
                switch (peek) {
                    case '*':
                        if (obj3 != 1) {
                            i = 2;
                            break;
                        }
                        int i2 = 2;
                        obj2 = null;
                        break;
                    case '/':
                        obj2 = -1;
                        break;
                }
            } else if (obj2 > 1) {
                switch (peek) {
                    case '*':
                        i = 3;
                        break;
                    case '/':
                        if (obj2 != 3) {
                            i = 2;
                            break;
                        } else if (obj3 != 2) {
                            obj2 = null;
                            break;
                        } else {
                            return obj;
                        }
                    default:
                        i = 2;
                        break;
                }
            } else if (obj2 < null) {
                switch (peek) {
                    case '\n':
                    case '\r':
                        obj2 = null;
                        break;
                    default:
                        break;
                }
            } else if (!Character.isWhitespace(peek)) {
                if (peek == '/') {
                    i = 1;
                } else if (peek == '*') {
                    i = 3;
                } else if (obj == null) {
                    obj = parse(source);
                }
            }
            source.next();
        }
        return obj;
    }

    protected Object parseArray(Source source) {
        if (source.next() != '[') {
            throw new IllegalStateException();
        }
        ArrayList arrayList = null;
        Object obj = null;
        int i = 1;
        int i2 = 0;
        while (source.hasNext()) {
            char peek = source.peek();
            switch (peek) {
                case ',':
                    if (i == 0) {
                        source.next();
                        i = 1;
                        break;
                    }
                    throw new IllegalStateException();
                case ExifTagConstants.FLASH_VALUE_AUTO_FIRED_RED_EYE_REDUCTION_RETURN_NOT_DETECTED /*93*/:
                    source.next();
                    switch (i2) {
                        case 0:
                            return newArray(0);
                        case 1:
                            Object newArray = newArray(1);
                            Array.set(newArray, 0, obj);
                            return newArray;
                        default:
                            return arrayList.toArray(newArray(arrayList.size()));
                    }
                default:
                    if (!Character.isWhitespace(peek)) {
                        i = i2 + 1;
                        if (i2 != 0) {
                            if (arrayList != null) {
                                arrayList.add(contextForArray().parse(source));
                                obj = null;
                                i2 = i;
                                i = 0;
                                break;
                            }
                            arrayList = new ArrayList();
                            arrayList.add(obj);
                            arrayList.add(contextForArray().parse(source));
                            obj = null;
                            i2 = i;
                            i = 0;
                            break;
                        }
                        obj = contextForArray().parse(source);
                        i2 = i;
                        i = 0;
                        break;
                    }
                    source.next();
                    break;
            }
        }
        throw new IllegalStateException("unexpected end of array");
    }

    public Number parseNumber(Source source) {
        StringBuffer stringBuffer;
        Object obj = null;
        long j = 0;
        while (source.hasNext()) {
            char peek = source.peek();
            switch (peek) {
                case '+':
                case '-':
                    if (j != 0) {
                        throw new IllegalStateException("bad number");
                    }
                    obj = 1;
                    source.next();
                    continue;
                case '.':
                case ExifTagConstants.FLASH_VALUE_FIRED_RED_EYE_REDUCTION_RETURN_NOT_DETECTED /*69*/:
                case 'e':
                    stringBuffer = new StringBuffer(16);
                    if (obj != null) {
                        stringBuffer.append('-');
                    }
                    stringBuffer.append(j);
                    stringBuffer.append(peek);
                    source.next();
                    break;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    j = (j * 10) + ((long) (peek - 48));
                    source.next();
                    continue;
                default:
                    stringBuffer = null;
                    break;
            }
            if (stringBuffer != null) {
                if (obj != null) {
                    j *= -1;
                }
                return TypeUtil.newLong(j);
            }
            Number d;
            synchronized (stringBuffer) {
                while (source.hasNext()) {
                    char peek2 = source.peek();
                    switch (peek2) {
                        case '+':
                        case '-':
                        case '.':
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                        case ExifTagConstants.FLASH_VALUE_FIRED_RED_EYE_REDUCTION_RETURN_NOT_DETECTED /*69*/:
                        case 'e':
                            stringBuffer.append(peek2);
                            source.next();
                        default:
                            break;
                    }
                    d = new Double(stringBuffer.toString());
                }
                d = new Double(stringBuffer.toString());
            }
            return d;
        }
        stringBuffer = null;
        if (stringBuffer != null) {
            synchronized (stringBuffer) {
                while (source.hasNext()) {
                    char peek22 = source.peek();
                    switch (peek22) {
                        case '+':
                        case '-':
                        case '.':
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                        case ExifTagConstants.FLASH_VALUE_FIRED_RED_EYE_REDUCTION_RETURN_NOT_DETECTED /*69*/:
                        case 'e':
                            stringBuffer.append(peek22);
                            source.next();
                        default:
                            break;
                    }
                    d = new Double(stringBuffer.toString());
                }
                d = new Double(stringBuffer.toString());
            }
            return d;
        }
        if (obj != null) {
            j *= -1;
        }
        return TypeUtil.newLong(j);
    }

    protected Object parseObject(Source source) {
        if (source.next() != '{') {
            throw new IllegalStateException();
        }
        String parseString;
        Object convertTo;
        Map newMap = newMap();
        char seekTo = seekTo("\"}", source);
        while (source.hasNext()) {
            if (seekTo != '}') {
                parseString = parseString(source);
                seekTo(':', source);
                source.next();
                newMap.put(parseString, contextFor(parseString).parse(source));
                seekTo(",}", source);
                if (source.next() == '}') {
                    break;
                }
                seekTo = seekTo("\"}", source);
            } else {
                source.next();
                break;
            }
        }
        parseString = (String) newMap.get("class");
        if (parseString != null) {
            try {
                Class class$;
                if (class$org$mortbay$util$ajax$JSON == null) {
                    class$ = class$("org.mortbay.util.ajax.JSON");
                    class$org$mortbay$util$ajax$JSON = class$;
                } else {
                    class$ = class$org$mortbay$util$ajax$JSON;
                }
                convertTo = convertTo(Loader.loadClass(class$, parseString), newMap);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return newMap;
            }
        }
        Map map = newMap;
        return convertTo;
    }

    protected String parseString(Source source) {
        if (source.next() != '\"') {
            throw new IllegalStateException();
        }
        int i;
        StringBuffer stringBuffer;
        String stringBuffer2;
        char[] scratchBuffer = source.scratchBuffer();
        if (scratchBuffer != null) {
            StringBuffer stringBuffer3;
            int i2 = 0;
            i = 0;
            while (source.hasNext()) {
                if (i >= scratchBuffer.length) {
                    stringBuffer3 = new StringBuffer(scratchBuffer.length * 2);
                    stringBuffer3.append(scratchBuffer, 0, i);
                    if (stringBuffer3 == null) {
                        return toString(scratchBuffer, 0, i);
                    }
                    i = i2;
                    stringBuffer = stringBuffer3;
                } else {
                    char next = source.next();
                    if (i2 != 0) {
                        switch (next) {
                            case '\"':
                                scratchBuffer[i] = '\"';
                                i++;
                                i2 = 0;
                                break;
                            case '/':
                                scratchBuffer[i] = '/';
                                i++;
                                i2 = 0;
                                break;
                            case '\\':
                                scratchBuffer[i] = '\\';
                                i++;
                                i2 = 0;
                                break;
                            case 'b':
                                scratchBuffer[i] = '\b';
                                i++;
                                i2 = 0;
                                break;
                            case 'f':
                                scratchBuffer[i] = '\f';
                                i++;
                                i2 = 0;
                                break;
                            case 'n':
                                scratchBuffer[i] = '\n';
                                i++;
                                i2 = 0;
                                break;
                            case 'r':
                                scratchBuffer[i] = '\r';
                                i++;
                                i2 = 0;
                                break;
                            case 't':
                                scratchBuffer[i] = '\t';
                                i++;
                                i2 = 0;
                                break;
                            case 'u':
                                scratchBuffer[i] = (char) ((((TypeUtil.convertHexDigit((byte) source.next()) << 12) + (TypeUtil.convertHexDigit((byte) source.next()) << 8)) + (TypeUtil.convertHexDigit((byte) source.next()) << 4)) + TypeUtil.convertHexDigit((byte) source.next()));
                                i++;
                                i2 = 0;
                                break;
                            default:
                                i2 = i + 1;
                                scratchBuffer[i] = next;
                                i = i2;
                                i2 = 0;
                                break;
                        }
                    } else if (next == '\\') {
                        i2 = 1;
                    } else if (next == '\"') {
                        return toString(scratchBuffer, 0, i);
                    } else {
                        int i3 = i + 1;
                        scratchBuffer[i] = next;
                        i = i3;
                    }
                }
            }
            stringBuffer3 = null;
            if (stringBuffer3 == null) {
                return toString(scratchBuffer, 0, i);
            }
            i = i2;
            stringBuffer = stringBuffer3;
        } else {
            stringBuffer = new StringBuffer(getStringBufferSize());
            i = 0;
        }
        synchronized (stringBuffer) {
            while (source.hasNext()) {
                char next2 = source.next();
                if (i != 0) {
                    switch (next2) {
                        case '\"':
                            stringBuffer.append('\"');
                            i = 0;
                            break;
                        case '/':
                            stringBuffer.append('/');
                            i = 0;
                            break;
                        case '\\':
                            stringBuffer.append('\\');
                            i = 0;
                            break;
                        case 'b':
                            stringBuffer.append('\b');
                            i = 0;
                            break;
                        case 'f':
                            stringBuffer.append('\f');
                            i = 0;
                            break;
                        case 'n':
                            stringBuffer.append('\n');
                            i = 0;
                            break;
                        case 'r':
                            stringBuffer.append('\r');
                            i = 0;
                            break;
                        case 't':
                            stringBuffer.append('\t');
                            i = 0;
                            break;
                        case 'u':
                            stringBuffer.append((char) ((((TypeUtil.convertHexDigit((byte) source.next()) << 12) + (TypeUtil.convertHexDigit((byte) source.next()) << 8)) + (TypeUtil.convertHexDigit((byte) source.next()) << 4)) + TypeUtil.convertHexDigit((byte) source.next())));
                            i = 0;
                            break;
                        default:
                            stringBuffer.append(next2);
                            i = 0;
                            break;
                    }
                } else if (next2 == '\\') {
                    i = 1;
                } else if (next2 == '\"') {
                    stringBuffer2 = stringBuffer.toString();
                } else {
                    stringBuffer.append(next2);
                }
            }
            stringBuffer2 = stringBuffer.toString();
        }
        return stringBuffer2;
    }

    protected char seekTo(String str, Source source) {
        while (source.hasNext()) {
            char peek = source.peek();
            if (str.indexOf(peek) >= 0) {
                return peek;
            }
            if (Character.isWhitespace(peek)) {
                source.next();
            } else {
                throw new IllegalStateException(new StringBuffer().append("Unexpected '").append(peek).append("' while seeking one of '").append(str).append("'").toString());
            }
        }
        throw new IllegalStateException(new StringBuffer().append("Expected one of '").append(str).append("'").toString());
    }

    protected void seekTo(char c, Source source) {
        while (source.hasNext()) {
            char peek = source.peek();
            if (peek != c) {
                if (Character.isWhitespace(peek)) {
                    source.next();
                } else {
                    throw new IllegalStateException(new StringBuffer().append("Unexpected '").append(peek).append(" while seeking '").append(c).append("'").toString());
                }
            }
            return;
        }
        throw new IllegalStateException(new StringBuffer().append("Expected '").append(c).append("'").toString());
    }

    public void setStringBufferSize(int i) {
        this._stringBufferSize = i;
    }

    public String toJSON(Object obj) {
        String stringBuffer;
        StringBuffer stringBuffer2 = new StringBuffer(getStringBufferSize());
        synchronized (stringBuffer2) {
            append(stringBuffer2, obj);
            stringBuffer = stringBuffer2.toString();
        }
        return stringBuffer;
    }

    protected String toString(char[] cArr, int i, int i2) {
        return new String(cArr, i, i2);
    }
}
