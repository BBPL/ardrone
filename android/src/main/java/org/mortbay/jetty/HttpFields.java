package org.mortbay.jetty;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.TimeZone;
import javax.servlet.http.Cookie;
import org.apache.http.impl.cookie.DateUtils;
import org.mortbay.io.Buffer;
import org.mortbay.io.BufferCache.CachedBuffer;
import org.mortbay.io.BufferDateCache;
import org.mortbay.io.BufferUtil;
import org.mortbay.io.ByteArrayBuffer;
import org.mortbay.io.View;
import org.mortbay.util.LazyList;
import org.mortbay.util.QuotedStringTokenizer;
import org.mortbay.util.StringMap;
import org.mortbay.util.StringUtil;

public class HttpFields {
    private static String[] DAYS = new String[]{"Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    private static String[] MONTHS = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", "Jan"};
    public static final String __01Jan1970 = formatDate(0, true).trim();
    public static final Buffer __01Jan1970_BUFFER = new ByteArrayBuffer(__01Jan1970);
    private static TimeZone __GMT = TimeZone.getTimeZone("GMT");
    public static final BufferDateCache __dateCache = new BufferDateCache("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
    private static SimpleDateFormat[] __dateReceive = new SimpleDateFormat[__dateReceiveFmt.length];
    private static final String[] __dateReceiveFmt = new String[]{"EEE, dd MMM yyyy HH:mm:ss zzz", "EEE, dd-MMM-yy HH:mm:ss", "EEE MMM dd HH:mm:ss yyyy", "EEE, dd MMM yyyy HH:mm:ss", "EEE dd MMM yyyy HH:mm:ss zzz", "EEE dd MMM yyyy HH:mm:ss", "EEE MMM dd yyyy HH:mm:ss zzz", "EEE MMM dd yyyy HH:mm:ss", "EEE MMM-dd-yyyy HH:mm:ss zzz", "EEE MMM-dd-yyyy HH:mm:ss", "dd MMM yyyy HH:mm:ss zzz", "dd MMM yyyy HH:mm:ss", "dd-MMM-yy HH:mm:ss zzz", "dd-MMM-yy HH:mm:ss", "MMM dd HH:mm:ss yyyy zzz", "MMM dd HH:mm:ss yyyy", "EEE MMM dd HH:mm:ss yyyy zzz", "EEE, MMM dd HH:mm:ss yyyy zzz", "EEE, MMM dd HH:mm:ss yyyy", DateUtils.PATTERN_RFC1036, "EEE dd-MMM-yy HH:mm:ss zzz", "EEE dd-MMM-yy HH:mm:ss"};
    private static int __dateReceiveInit = 3;
    private static Float __one = new Float("1.0");
    private static StringMap __qualities = new StringMap();
    public static final String __separators = ", \t";
    private static Float __zero = new Float("0.0");
    protected HashMap _bufferMap = new HashMap(32);
    private Calendar _calendar;
    private StringBuffer _dateBuffer;
    protected SimpleDateFormat[] _dateReceive = new SimpleDateFormat[__dateReceive.length];
    protected ArrayList _fields = new ArrayList(20);
    protected int _revision;

    class C13241 implements Enumeration {
        Field field = null;
        int f365i = 0;
        private final HttpFields this$0;
        private final int val$revision;

        C13241(HttpFields httpFields, int i) {
            this.this$0 = httpFields;
            this.val$revision = i;
        }

        public boolean hasMoreElements() {
            if (this.field != null) {
                return true;
            }
            while (this.f365i < this.this$0._fields.size()) {
                ArrayList arrayList = this.this$0._fields;
                int i = this.f365i;
                this.f365i = i + 1;
                Field field = (Field) arrayList.get(i);
                if (field != null && Field.access$000(field) == null && Field.access$100(field) == this.val$revision) {
                    this.field = field;
                    return true;
                }
            }
            return false;
        }

        public Object nextElement() throws NoSuchElementException {
            if (this.field != null || hasMoreElements()) {
                String to8859_1_String = BufferUtil.to8859_1_String(Field.access$200(this.field));
                this.field = null;
                return to8859_1_String;
            }
            throw new NoSuchElementException();
        }
    }

    class C13252 implements Iterator {
        Field field = null;
        int f366i = 0;
        private final HttpFields this$0;
        private final int val$revision;

        C13252(HttpFields httpFields, int i) {
            this.this$0 = httpFields;
            this.val$revision = i;
        }

        public boolean hasNext() {
            if (this.field != null) {
                return true;
            }
            while (this.f366i < this.this$0._fields.size()) {
                ArrayList arrayList = this.this$0._fields;
                int i = this.f366i;
                this.f366i = i + 1;
                Field field = (Field) arrayList.get(i);
                if (field != null && Field.access$100(field) == this.val$revision) {
                    this.field = field;
                    return true;
                }
            }
            return false;
        }

        public Object next() {
            if (this.field != null || hasNext()) {
                Field field = this.field;
                this.field = null;
                return field;
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    class C13263 implements Enumeration {
        Field f367f = this.val$field;
        private final HttpFields this$0;
        private final Field val$field;
        private final int val$revision;

        C13263(HttpFields httpFields, Field field, int i) {
            this.this$0 = httpFields;
            this.val$field = field;
            this.val$revision = i;
        }

        public boolean hasMoreElements() {
            while (this.f367f != null && Field.access$100(this.f367f) != this.val$revision) {
                this.f367f = Field.access$400(this.f367f);
            }
            return this.f367f != null;
        }

        public Object nextElement() throws NoSuchElementException {
            if (this.f367f == null) {
                throw new NoSuchElementException();
            }
            Field field = this.f367f;
            do {
                this.f367f = Field.access$400(this.f367f);
                if (this.f367f == null) {
                    break;
                }
            } while (Field.access$100(this.f367f) != this.val$revision);
            return field.getValue();
        }
    }

    class C13274 implements Enumeration {
        Field f368f = this.val$field;
        private final HttpFields this$0;
        private final Field val$field;
        private final int val$revision;

        C13274(HttpFields httpFields, Field field, int i) {
            this.this$0 = httpFields;
            this.val$field = field;
            this.val$revision = i;
        }

        public boolean hasMoreElements() {
            while (this.f368f != null && Field.access$100(this.f368f) != this.val$revision) {
                this.f368f = Field.access$400(this.f368f);
            }
            return this.f368f != null;
        }

        public Object nextElement() throws NoSuchElementException {
            if (this.f368f == null) {
                throw new NoSuchElementException();
            }
            Field field = this.f368f;
            this.f368f = Field.access$400(this.f368f);
            while (this.f368f != null && Field.access$100(this.f368f) != this.val$revision) {
                this.f368f = Field.access$400(this.f368f);
            }
            return field.getValue();
        }
    }

    class C13285 implements Enumeration {
        private final HttpFields this$0;
        QuotedStringTokenizer tok = null;
        private final Enumeration val$e;
        private final String val$separators;

        C13285(HttpFields httpFields, Enumeration enumeration, String str) {
            this.this$0 = httpFields;
            this.val$e = enumeration;
            this.val$separators = str;
        }

        public boolean hasMoreElements() {
            if (this.tok != null && this.tok.hasMoreElements()) {
                return true;
            }
            while (this.val$e.hasMoreElements()) {
                this.tok = new QuotedStringTokenizer((String) this.val$e.nextElement(), this.val$separators, false, false);
                if (this.tok.hasMoreElements()) {
                    return true;
                }
            }
            this.tok = null;
            return false;
        }

        public Object nextElement() throws NoSuchElementException {
            if (hasMoreElements()) {
                String str = (String) this.tok.nextElement();
                return str != null ? str.trim() : str;
            } else {
                throw new NoSuchElementException();
            }
        }
    }

    public static final class Field {
        private Buffer _name;
        private Field _next;
        private long _numValue;
        private Field _prev;
        private int _revision;
        private String _stringValue;
        private Buffer _value;

        private Field(Buffer buffer, Buffer buffer2, long j, int i) {
            this._name = buffer.asImmutableBuffer();
            if (!buffer2.isImmutable()) {
                Object view = new View(buffer2);
            }
            this._value = buffer2;
            this._next = null;
            this._prev = null;
            this._revision = i;
            this._numValue = j;
            this._stringValue = null;
        }

        Field(Buffer buffer, Buffer buffer2, long j, int i, C13241 c13241) {
            this(buffer, buffer2, j, i);
        }

        static Field access$000(Field field) {
            return field._prev;
        }

        static Field access$002(Field field, Field field2) {
            field._prev = field2;
            return field2;
        }

        static int access$100(Field field) {
            return field._revision;
        }

        static Buffer access$200(Field field) {
            return field._name;
        }

        static Buffer access$300(Field field) {
            return field._value;
        }

        static Field access$400(Field field) {
            return field._next;
        }

        static Field access$402(Field field, Field field2) {
            field._next = field2;
            return field2;
        }

        static void access$500(Field field, Buffer buffer, long j, int i) {
            field.reset(buffer, j, i);
        }

        static void access$600(Field field) {
            field.clear();
        }

        static long access$800(Field field) {
            return field._numValue;
        }

        static long access$802(Field field, long j) {
            field._numValue = j;
            return j;
        }

        static void access$900(Field field) {
            field.destroy();
        }

        private void clear() {
            this._revision = -1;
        }

        private void destroy() {
            this._name = null;
            this._value = null;
            this._next = null;
            this._prev = null;
            this._stringValue = null;
        }

        private void reset(Buffer buffer, long j, int i) {
            this._revision = i;
            if (this._value == null) {
                if (!buffer.isImmutable()) {
                    Object view = new View(buffer);
                }
                this._value = buffer;
                this._numValue = j;
                this._stringValue = null;
            } else if (buffer.isImmutable()) {
                this._value = buffer;
                this._numValue = j;
                this._stringValue = null;
            } else {
                if (this._value instanceof View) {
                    ((View) this._value).update(buffer);
                } else {
                    this._value = new View(buffer);
                }
                this._numValue = j;
                if (this._stringValue == null) {
                    return;
                }
                if (this._stringValue.length() != buffer.length()) {
                    this._stringValue = null;
                    return;
                }
                int length = buffer.length();
                while (true) {
                    int i2 = length - 1;
                    if (length <= 0) {
                        return;
                    }
                    if (buffer.peek(buffer.getIndex() + i2) != this._stringValue.charAt(i2)) {
                        this._stringValue = null;
                        return;
                    }
                    length = i2;
                }
            }
        }

        public int getIntValue() {
            return (int) getLongValue();
        }

        public long getLongValue() {
            if (this._numValue == -1) {
                this._numValue = BufferUtil.toLong(this._value);
            }
            return this._numValue;
        }

        public String getName() {
            return BufferUtil.to8859_1_String(this._name);
        }

        Buffer getNameBuffer() {
            return this._name;
        }

        public int getNameOrdinal() {
            return HttpHeaders.CACHE.getOrdinal(this._name);
        }

        public String getValue() {
            if (this._stringValue == null) {
                this._stringValue = BufferUtil.to8859_1_String(this._value);
            }
            return this._stringValue;
        }

        public Buffer getValueBuffer() {
            return this._value;
        }

        public int getValueOrdinal() {
            return HttpHeaderValues.CACHE.getOrdinal(this._value);
        }

        public void put(Buffer buffer) throws IOException {
            int index;
            int i;
            byte peek;
            if ((this._name instanceof CachedBuffer ? ((CachedBuffer) this._name).getOrdinal() : -1) < 0) {
                index = this._name.getIndex();
                int putIndex = this._name.putIndex();
                while (index < putIndex) {
                    i = index + 1;
                    peek = this._name.peek(index);
                    switch (peek) {
                        case (byte) 10:
                        case (byte) 13:
                        case (byte) 58:
                            index = i;
                            break;
                        default:
                            buffer.put(peek);
                            index = i;
                            break;
                    }
                }
            }
            buffer.put(this._name);
            buffer.put((byte) HttpTokens.COLON);
            buffer.put((byte) 32);
            if ((this._value instanceof CachedBuffer ? ((CachedBuffer) this._value).getOrdinal() : -1) < 0 && this._numValue < 0) {
                index = this._value.getIndex();
                int putIndex2 = this._value.putIndex();
                while (index < putIndex2) {
                    i = index + 1;
                    peek = this._value.peek(index);
                    switch (peek) {
                        case (byte) 10:
                        case (byte) 13:
                            index = i;
                            break;
                        default:
                            buffer.put(peek);
                            index = i;
                            break;
                    }
                }
            }
            buffer.put(this._value);
            BufferUtil.putCRLF(buffer);
        }

        public String toString() {
            return new StringBuffer().append("[").append(this._prev == null ? HttpVersions.HTTP_0_9 : "<-").append(getName()).append("=").append(this._revision).append("=").append(this._value).append(this._next == null ? HttpVersions.HTTP_0_9 : "->").append("]").toString();
        }
    }

    static {
        int i = 0;
        __GMT.setID("GMT");
        __dateCache.setTimeZone(__GMT);
        while (i < __dateReceiveInit) {
            __dateReceive[i] = new SimpleDateFormat(__dateReceiveFmt[i], Locale.US);
            __dateReceive[i].setTimeZone(__GMT);
            i++;
        }
        __qualities.put(null, __one);
        __qualities.put("1.0", __one);
        __qualities.put("1", __one);
        __qualities.put("0.9", new Float("0.9"));
        __qualities.put("0.8", new Float("0.8"));
        __qualities.put("0.7", new Float("0.7"));
        __qualities.put("0.66", new Float("0.66"));
        __qualities.put("0.6", new Float("0.6"));
        __qualities.put("0.5", new Float("0.5"));
        __qualities.put("0.4", new Float("0.4"));
        __qualities.put("0.33", new Float("0.33"));
        __qualities.put("0.3", new Float("0.3"));
        __qualities.put("0.2", new Float("0.2"));
        __qualities.put("0.1", new Float("0.1"));
        __qualities.put("0", __zero);
        __qualities.put("0.0", __zero);
    }

    private void add(Buffer buffer, Buffer buffer2, long j) throws IllegalArgumentException {
        if (buffer2 == null) {
            throw new IllegalArgumentException("null value");
        }
        Field field;
        Buffer lookup = !(buffer instanceof CachedBuffer) ? HttpHeaders.CACHE.lookup(buffer) : buffer;
        Field field2 = (Field) this._bufferMap.get(lookup);
        if (field2 != null) {
            C13241 c13241 = null;
            while (field2 != null && Field.access$100(field2) == this._revision) {
                Field field3 = field2;
                field2 = Field.access$400(field2);
                Object obj = field3;
            }
            field = c13241;
        } else {
            field = null;
        }
        if (field2 != null) {
            Field.access$500(field2, buffer2, j, this._revision);
            return;
        }
        Field field4 = new Field(lookup, buffer2, j, this._revision, null);
        if (field != null) {
            Field.access$002(field4, field);
            Field.access$402(field, field4);
        } else {
            this._bufferMap.put(field4.getNameBuffer(), field4);
        }
        this._fields.add(field4);
    }

    public static String formatDate(long j, boolean z) {
        StringBuffer stringBuffer = new StringBuffer(32);
        Calendar gregorianCalendar = new GregorianCalendar(__GMT);
        gregorianCalendar.setTimeInMillis(j);
        formatDate(stringBuffer, gregorianCalendar, z);
        return stringBuffer.toString();
    }

    public static String formatDate(StringBuffer stringBuffer, long j, boolean z) {
        Calendar gregorianCalendar = new GregorianCalendar(__GMT);
        gregorianCalendar.setTimeInMillis(j);
        formatDate(stringBuffer, gregorianCalendar, z);
        return stringBuffer.toString();
    }

    public static String formatDate(Calendar calendar, boolean z) {
        StringBuffer stringBuffer = new StringBuffer(32);
        formatDate(stringBuffer, calendar, z);
        return stringBuffer.toString();
    }

    public static void formatDate(StringBuffer stringBuffer, Calendar calendar, boolean z) {
        int i = calendar.get(7);
        int i2 = calendar.get(5);
        int i3 = calendar.get(2);
        int i4 = calendar.get(1);
        int i5 = i4 / 100;
        i4 %= 100;
        int timeInMillis = (int) ((calendar.getTimeInMillis() / 1000) % 86400);
        int i6 = timeInMillis / 60;
        int i7 = i6 / 60;
        stringBuffer.append(DAYS[i]);
        stringBuffer.append(',');
        stringBuffer.append(' ');
        StringUtil.append2digits(stringBuffer, i2);
        if (z) {
            stringBuffer.append('-');
            stringBuffer.append(MONTHS[i3]);
            stringBuffer.append('-');
            StringUtil.append2digits(stringBuffer, i5);
            StringUtil.append2digits(stringBuffer, i4);
        } else {
            stringBuffer.append(' ');
            stringBuffer.append(MONTHS[i3]);
            stringBuffer.append(' ');
            StringUtil.append2digits(stringBuffer, i5);
            StringUtil.append2digits(stringBuffer, i4);
        }
        stringBuffer.append(' ');
        StringUtil.append2digits(stringBuffer, i7);
        stringBuffer.append(':');
        StringUtil.append2digits(stringBuffer, i6 % 60);
        stringBuffer.append(':');
        StringUtil.append2digits(stringBuffer, timeInMillis % 60);
        stringBuffer.append(" GMT");
    }

    private Field getField(String str) {
        return (Field) this._bufferMap.get(HttpHeaders.CACHE.lookup(str));
    }

    private Field getField(Buffer buffer) {
        return (Field) this._bufferMap.get(buffer);
    }

    public static Float getQuality(String str) {
        if (str == null) {
            return __zero;
        }
        int indexOf = str.indexOf(";");
        int i = indexOf + 1;
        if (indexOf < 0 || i == str.length()) {
            return __one;
        }
        if (str.charAt(i) == 'q') {
            indexOf = (i + 1) + 1;
            Entry entry = __qualities.getEntry(str, indexOf, str.length() - indexOf);
            if (entry != null) {
                return (Float) entry.getValue();
            }
        }
        Object hashMap = new HashMap(3);
        valueParameters(str, hashMap);
        String str2 = (String) hashMap.get("q");
        Float f = (Float) __qualities.get(str2);
        if (f == null) {
            try {
                f = new Float(str2);
            } catch (Exception e) {
                f = __one;
            }
        }
        return f;
    }

    public static List qualityList(Enumeration enumeration) {
        List list;
        Object obj = null;
        if (enumeration == null || !enumeration.hasMoreElements()) {
            list = Collections.EMPTY_LIST;
        } else {
            Float quality;
            Object obj2 = null;
            while (enumeration.hasMoreElements()) {
                String obj3 = enumeration.nextElement().toString();
                quality = getQuality(obj3);
                if (((double) quality.floatValue()) >= 0.001d) {
                    obj = LazyList.add(obj, obj3);
                    obj2 = LazyList.add(obj2, quality);
                }
            }
            List list2 = LazyList.getList(obj, false);
            if (list2.size() >= 2) {
                List list3 = LazyList.getList(obj2, false);
                Float f = __zero;
                int size = list2.size();
                quality = f;
                while (true) {
                    int i = size - 1;
                    if (size > 0) {
                        Float f2 = (Float) list3.get(i);
                        if (quality.compareTo(f2) > 0) {
                            Object obj4 = list2.get(i);
                            list2.set(i, list2.get(i + 1));
                            list2.set(i + 1, obj4);
                            list3.set(i, list3.get(i + 1));
                            list3.set(i + 1, f2);
                            f = __zero;
                            size = list2.size();
                            quality = f;
                        } else {
                            quality = f2;
                            size = i;
                        }
                    } else {
                        list3.clear();
                        return list2;
                    }
                }
            }
            list = list2;
        }
        return list;
    }

    public static String valueParameters(String str, Map map) {
        if (str == null) {
            return null;
        }
        int indexOf = str.indexOf(59);
        if (indexOf < 0) {
            return str;
        }
        if (map == null) {
            return str.substring(0, indexOf).trim();
        }
        StringTokenizer quotedStringTokenizer = new QuotedStringTokenizer(str.substring(indexOf), ";", false, true);
        while (quotedStringTokenizer.hasMoreTokens()) {
            StringTokenizer quotedStringTokenizer2 = new QuotedStringTokenizer(quotedStringTokenizer.nextToken(), "= ");
            if (quotedStringTokenizer2.hasMoreTokens()) {
                map.put(quotedStringTokenizer2.nextToken(), quotedStringTokenizer2.hasMoreTokens() ? quotedStringTokenizer2.nextToken() : null);
            }
        }
        return str.substring(0, indexOf).trim();
    }

    public void add(String str, String str2) throws IllegalArgumentException {
        add(HttpHeaders.CACHE.lookup(str), HttpHeaderValues.CACHE.lookup(str2), -1);
    }

    public void add(Buffer buffer, Buffer buffer2) throws IllegalArgumentException {
        add(buffer, buffer2, -1);
    }

    public void add(HttpFields httpFields) {
        if (httpFields != null) {
            Enumeration fieldNames = httpFields.getFieldNames();
            while (fieldNames.hasMoreElements()) {
                String str = (String) fieldNames.nextElement();
                Enumeration values = httpFields.getValues(str);
                while (values.hasMoreElements()) {
                    add(str, (String) values.nextElement());
                }
            }
        }
    }

    public void addDateField(String str, long j) {
        if (this._dateBuffer == null) {
            this._dateBuffer = new StringBuffer(32);
            this._calendar = new GregorianCalendar(__GMT);
        }
        this._dateBuffer.setLength(0);
        this._calendar.setTimeInMillis(j);
        formatDate(this._dateBuffer, this._calendar, false);
        add(HttpHeaders.CACHE.lookup(str), new ByteArrayBuffer(this._dateBuffer.toString()), j);
    }

    public void addLongField(String str, long j) {
        add(HttpHeaders.CACHE.lookup(str), BufferUtil.toBuffer(j), j);
    }

    public void addLongField(Buffer buffer, long j) {
        add(buffer, BufferUtil.toBuffer(j), j);
    }

    public void addSetCookie(Cookie cookie) {
        String name = cookie.getName();
        String value = cookie.getValue();
        int version = cookie.getVersion();
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("Bad cookie name");
        }
        StringBuffer stringBuffer = new StringBuffer(128);
        synchronized (stringBuffer) {
            QuotedStringTokenizer.quoteIfNeeded(stringBuffer, name);
            stringBuffer.append('=');
            if (value != null && value.length() > 0) {
                QuotedStringTokenizer.quoteIfNeeded(stringBuffer, value);
            }
            if (version > 0) {
                stringBuffer.append(";Version=");
                stringBuffer.append(version);
                name = cookie.getComment();
                if (name != null && name.length() > 0) {
                    stringBuffer.append(";Comment=");
                    QuotedStringTokenizer.quoteIfNeeded(stringBuffer, name);
                }
            }
            name = cookie.getPath();
            if (name != null && name.length() > 0) {
                stringBuffer.append(";Path=");
                if (name.startsWith("\"")) {
                    stringBuffer.append(name);
                } else {
                    QuotedStringTokenizer.quoteIfNeeded(stringBuffer, name);
                }
            }
            name = cookie.getDomain();
            if (name != null && name.length() > 0) {
                stringBuffer.append(";Domain=");
                QuotedStringTokenizer.quoteIfNeeded(stringBuffer, name.toLowerCase());
            }
            long maxAge = (long) cookie.getMaxAge();
            if (maxAge >= 0) {
                if (version == 0) {
                    stringBuffer.append(";Expires=");
                    if (maxAge == 0) {
                        stringBuffer.append(__01Jan1970);
                    } else {
                        formatDate(stringBuffer, (maxAge * 1000) + System.currentTimeMillis(), true);
                    }
                } else {
                    stringBuffer.append(";Max-Age=");
                    stringBuffer.append(maxAge);
                }
            } else if (version > 0) {
                stringBuffer.append(";Discard");
            }
            if (cookie.getSecure()) {
                stringBuffer.append(";Secure");
            }
            if (cookie instanceof HttpOnlyCookie) {
                stringBuffer.append(";HttpOnly");
            }
            name = stringBuffer.toString();
        }
        put(HttpHeaders.EXPIRES_BUFFER, __01Jan1970_BUFFER);
        add(HttpHeaders.SET_COOKIE_BUFFER, new ByteArrayBuffer(name));
    }

    public void clear() {
        this._revision++;
        if (this._revision > 1000000) {
            this._revision = 0;
            int size = this._fields.size();
            while (true) {
                int i = size - 1;
                if (size > 0) {
                    Field field = (Field) this._fields.get(i);
                    if (field != null) {
                        Field.access$600(field);
                    }
                    size = i;
                } else {
                    return;
                }
            }
        }
    }

    public boolean containsKey(String str) {
        Field field = getField(str);
        return field != null && Field.access$100(field) == this._revision;
    }

    public boolean containsKey(Buffer buffer) {
        Field field = getField(buffer);
        return field != null && Field.access$100(field) == this._revision;
    }

    public void destroy() {
        if (this._fields != null) {
            int size = this._fields.size();
            while (true) {
                int i = size - 1;
                if (size <= 0) {
                    break;
                }
                Field field = (Field) this._fields.get(i);
                if (field != null) {
                    this._bufferMap.remove(field.getNameBuffer());
                    Field.access$900(field);
                }
                size = i;
            }
        }
        this._fields = null;
        this._dateBuffer = null;
        this._calendar = null;
        this._dateReceive = null;
    }

    public Buffer get(Buffer buffer) {
        Field field = getField(buffer);
        return (field == null || Field.access$100(field) != this._revision) ? null : Field.access$300(field);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public long getDateField(java.lang.String r10) {
        /*
        r9 = this;
        r0 = -1;
        r2 = 0;
        r4 = r9.getField(r10);
        if (r4 == 0) goto L_0x0011;
    L_0x0009:
        r3 = org.mortbay.jetty.HttpFields.Field.access$100(r4);
        r5 = r9._revision;
        if (r3 == r5) goto L_0x0012;
    L_0x0011:
        return r0;
    L_0x0012:
        r6 = org.mortbay.jetty.HttpFields.Field.access$800(r4);
        r3 = (r6 > r0 ? 1 : (r6 == r0 ? 0 : -1));
        if (r3 == 0) goto L_0x001f;
    L_0x001a:
        r0 = org.mortbay.jetty.HttpFields.Field.access$800(r4);
        goto L_0x0011;
    L_0x001f:
        r3 = org.mortbay.jetty.HttpFields.Field.access$300(r4);
        r3 = org.mortbay.io.BufferUtil.to8859_1_String(r3);
        r5 = 0;
        r3 = valueParameters(r3, r5);
        if (r3 == 0) goto L_0x0011;
    L_0x002e:
        r1 = r2;
    L_0x002f:
        r0 = __dateReceiveInit;
        if (r1 >= r0) goto L_0x005f;
    L_0x0033:
        r0 = r9._dateReceive;
        r0 = r0[r1];
        if (r0 != 0) goto L_0x0047;
    L_0x0039:
        r5 = r9._dateReceive;
        r0 = __dateReceive;
        r0 = r0[r1];
        r0 = r0.clone();
        r0 = (java.text.SimpleDateFormat) r0;
        r5[r1] = r0;
    L_0x0047:
        r0 = r9._dateReceive;	 Catch:{ Exception -> 0x005a }
        r0 = r0[r1];	 Catch:{ Exception -> 0x005a }
        r0 = r0.parseObject(r3);	 Catch:{ Exception -> 0x005a }
        r0 = (java.util.Date) r0;	 Catch:{ Exception -> 0x005a }
        r6 = r0.getTime();	 Catch:{ Exception -> 0x005a }
        r0 = org.mortbay.jetty.HttpFields.Field.access$802(r4, r6);	 Catch:{ Exception -> 0x005a }
        goto L_0x0011;
    L_0x005a:
        r0 = move-exception;
        r0 = r1 + 1;
        r1 = r0;
        goto L_0x002f;
    L_0x005f:
        r0 = " GMT";
        r0 = r3.endsWith(r0);
        if (r0 == 0) goto L_0x008e;
    L_0x0067:
        r0 = r3.length();
        r0 = r0 + -4;
        r3 = r3.substring(r2, r0);
        r1 = r2;
    L_0x0072:
        r0 = __dateReceiveInit;
        if (r1 >= r0) goto L_0x008e;
    L_0x0076:
        r0 = r9._dateReceive;	 Catch:{ Exception -> 0x0089 }
        r0 = r0[r1];	 Catch:{ Exception -> 0x0089 }
        r0 = r0.parseObject(r3);	 Catch:{ Exception -> 0x0089 }
        r0 = (java.util.Date) r0;	 Catch:{ Exception -> 0x0089 }
        r6 = r0.getTime();	 Catch:{ Exception -> 0x0089 }
        r0 = org.mortbay.jetty.HttpFields.Field.access$802(r4, r6);	 Catch:{ Exception -> 0x0089 }
        goto L_0x0011;
    L_0x0089:
        r0 = move-exception;
        r0 = r1 + 1;
        r1 = r0;
        goto L_0x0072;
    L_0x008e:
        r1 = r3;
        r5 = __dateReceive;
        monitor-enter(r5);
        r0 = __dateReceiveInit;	 Catch:{ all -> 0x00e1 }
        r3 = r0;
    L_0x0095:
        r0 = r9._dateReceive;	 Catch:{ all -> 0x00e1 }
        r0 = r0.length;	 Catch:{ all -> 0x00e1 }
        if (r3 >= r0) goto L_0x00e9;
    L_0x009a:
        r0 = r9._dateReceive;	 Catch:{ all -> 0x00e1 }
        r0 = r0[r3];	 Catch:{ all -> 0x00e1 }
        if (r0 != 0) goto L_0x00cc;
    L_0x00a0:
        r0 = __dateReceive;	 Catch:{ all -> 0x00e1 }
        r0 = r0[r3];	 Catch:{ all -> 0x00e1 }
        if (r0 != 0) goto L_0x00be;
    L_0x00a6:
        r0 = __dateReceive;	 Catch:{ all -> 0x00e1 }
        r6 = new java.text.SimpleDateFormat;	 Catch:{ all -> 0x00e1 }
        r7 = __dateReceiveFmt;	 Catch:{ all -> 0x00e1 }
        r7 = r7[r3];	 Catch:{ all -> 0x00e1 }
        r8 = java.util.Locale.US;	 Catch:{ all -> 0x00e1 }
        r6.<init>(r7, r8);	 Catch:{ all -> 0x00e1 }
        r0[r3] = r6;	 Catch:{ all -> 0x00e1 }
        r0 = __dateReceive;	 Catch:{ all -> 0x00e1 }
        r0 = r0[r3];	 Catch:{ all -> 0x00e1 }
        r6 = __GMT;	 Catch:{ all -> 0x00e1 }
        r0.setTimeZone(r6);	 Catch:{ all -> 0x00e1 }
    L_0x00be:
        r6 = r9._dateReceive;	 Catch:{ all -> 0x00e1 }
        r0 = __dateReceive;	 Catch:{ all -> 0x00e1 }
        r0 = r0[r3];	 Catch:{ all -> 0x00e1 }
        r0 = r0.clone();	 Catch:{ all -> 0x00e1 }
        r0 = (java.text.SimpleDateFormat) r0;	 Catch:{ all -> 0x00e1 }
        r6[r3] = r0;	 Catch:{ all -> 0x00e1 }
    L_0x00cc:
        r0 = r9._dateReceive;	 Catch:{ Exception -> 0x00e4 }
        r0 = r0[r3];	 Catch:{ Exception -> 0x00e4 }
        r0 = r0.parseObject(r1);	 Catch:{ Exception -> 0x00e4 }
        r0 = (java.util.Date) r0;	 Catch:{ Exception -> 0x00e4 }
        r6 = r0.getTime();	 Catch:{ Exception -> 0x00e4 }
        r0 = org.mortbay.jetty.HttpFields.Field.access$802(r4, r6);	 Catch:{ Exception -> 0x00e4 }
        monitor-exit(r5);	 Catch:{ all -> 0x00e1 }
        goto L_0x0011;
    L_0x00e1:
        r0 = move-exception;
        monitor-exit(r5);	 Catch:{ all -> 0x00e1 }
        throw r0;
    L_0x00e4:
        r0 = move-exception;
        r0 = r3 + 1;
        r3 = r0;
        goto L_0x0095;
    L_0x00e9:
        r0 = " GMT";
        r0 = r1.endsWith(r0);	 Catch:{ all -> 0x00e1 }
        if (r0 == 0) goto L_0x0116;
    L_0x00f1:
        r0 = 0;
        r3 = r1.length();	 Catch:{ all -> 0x00e1 }
        r3 = r3 + -4;
        r1 = r1.substring(r0, r3);	 Catch:{ all -> 0x00e1 }
    L_0x00fc:
        r0 = r9._dateReceive;	 Catch:{ all -> 0x00e1 }
        r0 = r0.length;	 Catch:{ all -> 0x00e1 }
        if (r2 >= r0) goto L_0x0116;
    L_0x0101:
        r0 = r9._dateReceive;	 Catch:{ Exception -> 0x0130 }
        r0 = r0[r2];	 Catch:{ Exception -> 0x0130 }
        r0 = r0.parseObject(r1);	 Catch:{ Exception -> 0x0130 }
        r0 = (java.util.Date) r0;	 Catch:{ Exception -> 0x0130 }
        r6 = r0.getTime();	 Catch:{ Exception -> 0x0130 }
        r0 = org.mortbay.jetty.HttpFields.Field.access$802(r4, r6);	 Catch:{ Exception -> 0x0130 }
        monitor-exit(r5);	 Catch:{ all -> 0x00e1 }
        goto L_0x0011;
    L_0x0116:
        monitor-exit(r5);	 Catch:{ all -> 0x00e1 }
        r0 = new java.lang.IllegalArgumentException;
        r2 = new java.lang.StringBuffer;
        r2.<init>();
        r3 = "Cannot convert date: ";
        r2 = r2.append(r3);
        r1 = r2.append(r1);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
    L_0x0130:
        r0 = move-exception;
        r2 = r2 + 1;
        goto L_0x00fc;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mortbay.jetty.HttpFields.getDateField(java.lang.String):long");
    }

    public Enumeration getFieldNames() {
        return new C13241(this, this._revision);
    }

    public Iterator getFields() {
        return new C13252(this, this._revision);
    }

    public long getLongField(String str) throws NumberFormatException {
        Field field = getField(str);
        return (field == null || Field.access$100(field) != this._revision) ? -1 : field.getLongValue();
    }

    public long getLongField(Buffer buffer) throws NumberFormatException {
        Field field = getField(buffer);
        return (field == null || Field.access$100(field) != this._revision) ? -1 : field.getLongValue();
    }

    public String getStringField(String str) {
        Field field = getField(str);
        return (field == null || Field.access$100(field) != this._revision) ? null : field.getValue();
    }

    public String getStringField(Buffer buffer) {
        Field field = getField(buffer);
        return (field == null || Field.access$100(field) != this._revision) ? null : BufferUtil.to8859_1_String(Field.access$300(field));
    }

    public Enumeration getValues(String str) {
        Field field = getField(str);
        return field == null ? null : new C13263(this, field, this._revision);
    }

    public Enumeration getValues(String str, String str2) {
        Enumeration values = getValues(str);
        return values == null ? null : new C13285(this, values, str2);
    }

    public Enumeration getValues(Buffer buffer) {
        Field field = getField(buffer);
        return field == null ? null : new C13274(this, field, this._revision);
    }

    public void put(String str, String str2) {
        Buffer lookup = HttpHeaders.CACHE.lookup(str);
        Buffer buffer = null;
        if (str2 != null) {
            buffer = HttpHeaderValues.CACHE.lookup(str2);
        }
        put(lookup, buffer, -1);
    }

    public void put(String str, List list) {
        if (list == null || list.size() == 0) {
            remove(str);
            return;
        }
        Buffer lookup = HttpHeaders.CACHE.lookup(str);
        Object obj = list.get(0);
        if (obj != null) {
            put(lookup, HttpHeaderValues.CACHE.lookup(obj.toString()));
        } else {
            remove(lookup);
        }
        if (list.size() > 1) {
            Iterator it = list.iterator();
            it.next();
            while (it.hasNext()) {
                Object next = it.next();
                if (next != null) {
                    put(lookup, HttpHeaderValues.CACHE.lookup(next.toString()));
                }
            }
        }
    }

    public void put(Buffer buffer) throws IOException {
        for (int i = 0; i < this._fields.size(); i++) {
            Field field = (Field) this._fields.get(i);
            if (field != null && Field.access$100(field) == this._revision) {
                field.put(buffer);
            }
        }
        BufferUtil.putCRLF(buffer);
    }

    public void put(Buffer buffer, String str) {
        put(buffer, HttpHeaderValues.CACHE.lookup(str), -1);
    }

    public void put(Buffer buffer, Buffer buffer2) {
        put(buffer, buffer2, -1);
    }

    public void put(Buffer buffer, Buffer buffer2, long j) {
        if (buffer2 == null) {
            remove(buffer);
            return;
        }
        Buffer lookup = !(buffer instanceof CachedBuffer) ? HttpHeaders.CACHE.lookup(buffer) : buffer;
        Field field = (Field) this._bufferMap.get(lookup);
        if (field != null) {
            Field.access$500(field, buffer2, j, this._revision);
            for (field = Field.access$400(field); field != null; field = Field.access$400(field)) {
                Field.access$600(field);
            }
            return;
        }
        Field field2 = new Field(lookup, buffer2, j, this._revision, null);
        this._fields.add(field2);
        this._bufferMap.put(field2.getNameBuffer(), field2);
    }

    public void putDateField(String str, long j) {
        putDateField(HttpHeaders.CACHE.lookup(str), j);
    }

    public void putDateField(Buffer buffer, long j) {
        if (this._dateBuffer == null) {
            this._dateBuffer = new StringBuffer(32);
            this._calendar = new GregorianCalendar(__GMT);
        }
        this._dateBuffer.setLength(0);
        this._calendar.setTimeInMillis(j);
        formatDate(this._dateBuffer, this._calendar, false);
        put(buffer, new ByteArrayBuffer(this._dateBuffer.toString()), j);
    }

    public void putLongField(String str, long j) {
        put(HttpHeaders.CACHE.lookup(str), BufferUtil.toBuffer(j), j);
    }

    public void putLongField(Buffer buffer, long j) {
        put(buffer, BufferUtil.toBuffer(j), j);
    }

    public void remove(String str) {
        remove(HttpHeaders.CACHE.lookup(str));
    }

    public void remove(Buffer buffer) {
        Field field = (Field) this._bufferMap.get(buffer);
        if (field != null) {
            while (field != null) {
                Field.access$600(field);
                field = Field.access$400(field);
            }
        }
    }

    public String toString() {
        try {
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < this._fields.size(); i++) {
                Field field = (Field) this._fields.get(i);
                if (field != null && Field.access$100(field) == this._revision) {
                    String name = field.getName();
                    if (name != null) {
                        stringBuffer.append(name);
                    }
                    stringBuffer.append(": ");
                    String value = field.getValue();
                    if (value != null) {
                        stringBuffer.append(value);
                    }
                    stringBuffer.append("\r\n");
                }
            }
            stringBuffer.append("\r\n");
            return stringBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
