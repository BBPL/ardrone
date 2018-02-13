package com.google.api.client.util;

import com.google.api.client.http.ExponentialBackOffPolicy;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public final class DateTime implements Serializable {
    private static final TimeZone GMT = TimeZone.getTimeZone("GMT");
    private static final long serialVersionUID = 1;
    private final boolean dateOnly;
    private final int tzShift;
    private final long value;

    public DateTime(long j) {
        this(false, j, null);
    }

    public DateTime(long j, int i) {
        this(false, j, Integer.valueOf(i));
    }

    public DateTime(String str) {
        DateTime parseRfc3339 = parseRfc3339(str);
        this.dateOnly = parseRfc3339.dateOnly;
        this.value = parseRfc3339.value;
        this.tzShift = parseRfc3339.tzShift;
    }

    public DateTime(Date date) {
        this(date.getTime());
    }

    public DateTime(Date date, TimeZone timeZone) {
        this(false, date.getTime(), timeZone == null ? null : Integer.valueOf(timeZone.getOffset(date.getTime()) / ExponentialBackOffPolicy.DEFAULT_MAX_INTERVAL_MILLIS));
    }

    public DateTime(boolean z, long j, Integer num) {
        this.dateOnly = z;
        this.value = j;
        int offset = z ? 0 : num == null ? TimeZone.getDefault().getOffset(j) / ExponentialBackOffPolicy.DEFAULT_MAX_INTERVAL_MILLIS : num.intValue();
        this.tzShift = offset;
    }

    private static void appendInt(StringBuilder stringBuilder, int i, int i2) {
        if (i < 0) {
            stringBuilder.append('-');
            i = -i;
        }
        int i3 = i;
        while (i3 > 0) {
            i3 /= 10;
            i2--;
        }
        for (i3 = 0; i3 < i2; i3++) {
            stringBuilder.append('0');
        }
        if (i != 0) {
            stringBuilder.append(i);
        }
    }

    public static DateTime parseRfc3339(String str) throws NumberFormatException {
        try {
            int i;
            int i2;
            int i3;
            int i4;
            int i5;
            Integer valueOf;
            int parseInt = Integer.parseInt(str.substring(0, 4));
            int parseInt2 = Integer.parseInt(str.substring(5, 7));
            int parseInt3 = Integer.parseInt(str.substring(8, 10));
            int length = str.length();
            boolean z = length <= 10 || Character.toUpperCase(str.charAt(10)) != 'T';
            if (z) {
                i = 0;
                i2 = 0;
                i3 = 0;
                i4 = 0;
                i5 = Integer.MAX_VALUE;
            } else {
                i4 = Integer.parseInt(str.substring(11, 13));
                i3 = Integer.parseInt(str.substring(14, 16));
                i2 = Integer.parseInt(str.substring(17, 19));
                if (str.charAt(19) == '.') {
                    i = Integer.parseInt(str.substring(20, 23));
                    i5 = 23;
                } else {
                    i = 0;
                    i5 = 19;
                }
            }
            Calendar gregorianCalendar = new GregorianCalendar(GMT);
            gregorianCalendar.set(parseInt, parseInt2 - 1, parseInt3, i4, i3, i2);
            gregorianCalendar.set(14, i);
            long timeInMillis = gregorianCalendar.getTimeInMillis();
            if (length > i5) {
                int i6;
                if (Character.toUpperCase(str.charAt(i5)) == 'Z') {
                    i6 = 0;
                } else {
                    i6 = (Integer.parseInt(str.substring(i5 + 1, i5 + 3)) * 60) + Integer.parseInt(str.substring(i5 + 4, i5 + 6));
                    if (str.charAt(i5) == '-') {
                        i6 = -i6;
                    }
                    timeInMillis -= ((long) i6) * 60000;
                }
                valueOf = Integer.valueOf(i6);
            } else {
                valueOf = null;
            }
            return new DateTime(z, timeInMillis, valueOf);
        } catch (StringIndexOutOfBoundsException e) {
            throw new NumberFormatException("Invalid date/time format: " + str);
        }
    }

    public boolean equals(Object obj) {
        if (obj != this) {
            if (!(obj instanceof DateTime)) {
                return false;
            }
            DateTime dateTime = (DateTime) obj;
            if (this.dateOnly != dateTime.dateOnly || this.value != dateTime.value) {
                return false;
            }
            if (this.tzShift != dateTime.tzShift) {
                return false;
            }
        }
        return true;
    }

    public int getTimeZoneShift() {
        return this.tzShift;
    }

    public long getValue() {
        return this.value;
    }

    public int hashCode() {
        long j = this.value;
        long j2 = this.dateOnly ? 1 : 0;
        return Arrays.hashCode(new long[]{j, j2, (long) this.tzShift});
    }

    public boolean isDateOnly() {
        return this.dateOnly;
    }

    public String toString() {
        return toStringRfc3339();
    }

    public String toStringRfc3339() {
        StringBuilder stringBuilder = new StringBuilder();
        Calendar gregorianCalendar = new GregorianCalendar(GMT);
        gregorianCalendar.setTimeInMillis(this.value + (((long) this.tzShift) * 60000));
        appendInt(stringBuilder, gregorianCalendar.get(1), 4);
        stringBuilder.append('-');
        appendInt(stringBuilder, gregorianCalendar.get(2) + 1, 2);
        stringBuilder.append('-');
        appendInt(stringBuilder, gregorianCalendar.get(5), 2);
        if (!this.dateOnly) {
            stringBuilder.append('T');
            appendInt(stringBuilder, gregorianCalendar.get(11), 2);
            stringBuilder.append(':');
            appendInt(stringBuilder, gregorianCalendar.get(12), 2);
            stringBuilder.append(':');
            appendInt(stringBuilder, gregorianCalendar.get(13), 2);
            if (gregorianCalendar.isSet(14)) {
                stringBuilder.append('.');
                appendInt(stringBuilder, gregorianCalendar.get(14), 3);
            }
            if (this.tzShift == 0) {
                stringBuilder.append('Z');
            } else {
                int i = this.tzShift;
                if (this.tzShift > 0) {
                    stringBuilder.append('+');
                } else {
                    stringBuilder.append('-');
                    i = -i;
                }
                appendInt(stringBuilder, i / 60, 2);
                stringBuilder.append(':');
                appendInt(stringBuilder, i % 60, 2);
            }
        }
        return stringBuilder.toString();
    }
}
