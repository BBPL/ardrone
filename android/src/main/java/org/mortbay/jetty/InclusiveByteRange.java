package org.mortbay.jetty;

import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;
import org.mortbay.log.Log;
import org.mortbay.util.LazyList;
import org.mortbay.util.URIUtil;

public class InclusiveByteRange {
    long first = 0;
    long last = 0;

    public InclusiveByteRange(long j, long j2) {
        this.first = j;
        this.last = j2;
    }

    public static List satisfiableRanges(Enumeration enumeration, long j) {
        Object obj = null;
        while (enumeration.hasMoreElements()) {
            StringTokenizer stringTokenizer = new StringTokenizer((String) enumeration.nextElement(), "=,", false);
            String str = null;
            while (stringTokenizer.hasMoreTokens()) {
                try {
                    Object trim = stringTokenizer.nextToken().trim();
                    int indexOf = trim.indexOf(45);
                    if (indexOf >= 0 && trim.indexOf("-", indexOf + 1) < 0) {
                        long parseLong;
                        long j2;
                        if (indexOf == 0) {
                            if (indexOf + 1 >= trim.length()) {
                                Log.warn("Bad range format: {}", trim);
                                break;
                            }
                            parseLong = Long.parseLong(trim.substring(indexOf + 1).trim());
                            j2 = -1;
                        } else if (indexOf + 1 < trim.length()) {
                            j2 = Long.parseLong(trim.substring(0, indexOf).trim());
                            parseLong = Long.parseLong(trim.substring(indexOf + 1).trim());
                        } else {
                            j2 = Long.parseLong(trim.substring(0, indexOf).trim());
                            parseLong = -1;
                        }
                        if ((j2 == -1 && parseLong == -1) || (j2 != -1 && parseLong != -1 && j2 > parseLong)) {
                            break;
                        } else if (j2 < j) {
                            obj = LazyList.add(obj, new InclusiveByteRange(j2, parseLong));
                        }
                    } else if (!HttpHeaderValues.BYTES.equals(trim)) {
                        Log.warn("Bad range format: {}", trim);
                        break;
                    }
                } catch (Throwable e) {
                    Throwable th = e;
                    String str2 = str;
                    Throwable th2 = th;
                    Log.warn(new StringBuffer().append("Bad range format: ").append(str2).toString());
                    Log.ignore(th2);
                }
            }
        }
        return LazyList.getList(obj, true);
    }

    public static String to416HeaderRangeString(long j) {
        StringBuffer stringBuffer = new StringBuffer(40);
        stringBuffer.append("bytes */");
        stringBuffer.append(j);
        return stringBuffer.toString();
    }

    public long getFirst() {
        return this.first;
    }

    public long getFirst(long j) {
        if (this.first >= 0) {
            return this.first;
        }
        long j2 = j - this.last;
        return j2 < 0 ? 0 : j2;
    }

    public long getLast() {
        return this.last;
    }

    public long getLast(long j) {
        return this.first < 0 ? j - 1 : (this.last < 0 || this.last >= j) ? j - 1 : this.last;
    }

    public long getSize(long j) {
        return (getLast(j) - getFirst(j)) + 1;
    }

    public String toHeaderRangeString(long j) {
        StringBuffer stringBuffer = new StringBuffer(40);
        stringBuffer.append("bytes ");
        stringBuffer.append(getFirst(j));
        stringBuffer.append('-');
        stringBuffer.append(getLast(j));
        stringBuffer.append(URIUtil.SLASH);
        stringBuffer.append(j);
        return stringBuffer.toString();
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer(60);
        stringBuffer.append(Long.toString(this.first));
        stringBuffer.append(":");
        stringBuffer.append(Long.toString(this.last));
        return stringBuffer.toString();
    }
}
