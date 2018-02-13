package org.mortbay.util;

import com.google.api.client.http.ExponentialBackOffPolicy;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateCache {
    public static String DEFAULT_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy";
    private static long __hitWindow = 3600;
    private DateFormatSymbols _dfs;
    private String _formatString;
    private long _lastMinutes;
    private int _lastMs;
    private String _lastResult;
    private long _lastSeconds;
    private Locale _locale;
    private SimpleDateFormat _minFormat;
    private String _minFormatString;
    private String _secFormatString;
    private String _secFormatString0;
    private String _secFormatString1;
    private SimpleDateFormat _tzFormat;
    private String _tzFormatString;

    public DateCache() {
        this(DEFAULT_FORMAT);
        getFormat().setTimeZone(TimeZone.getDefault());
    }

    public DateCache(String str) {
        this._lastMinutes = -1;
        this._lastSeconds = -1;
        this._lastMs = -1;
        this._lastResult = null;
        this._locale = null;
        this._dfs = null;
        this._formatString = str;
        setTimeZone(TimeZone.getDefault());
    }

    public DateCache(String str, DateFormatSymbols dateFormatSymbols) {
        this._lastMinutes = -1;
        this._lastSeconds = -1;
        this._lastMs = -1;
        this._lastResult = null;
        this._locale = null;
        this._dfs = null;
        this._formatString = str;
        this._dfs = dateFormatSymbols;
        setTimeZone(TimeZone.getDefault());
    }

    public DateCache(String str, Locale locale) {
        this._lastMinutes = -1;
        this._lastSeconds = -1;
        this._lastMs = -1;
        this._lastResult = null;
        this._locale = null;
        this._dfs = null;
        this._formatString = str;
        this._locale = locale;
        setTimeZone(TimeZone.getDefault());
    }

    private void setMinFormatString() {
        if (this._tzFormatString.indexOf("ss.SSS") >= 0) {
            throw new IllegalStateException("ms not supported");
        }
        int indexOf = this._tzFormatString.indexOf("ss");
        String substring = this._tzFormatString.substring(0, indexOf);
        this._minFormatString = new StringBuffer().append(substring).append("'ss'").append(this._tzFormatString.substring(indexOf + 2)).toString();
    }

    private void setTzFormatString(TimeZone timeZone) {
        int indexOf = this._formatString.indexOf("ZZZ");
        if (indexOf >= 0) {
            String substring = this._formatString.substring(0, indexOf);
            String substring2 = this._formatString.substring(indexOf + 3);
            indexOf = timeZone.getRawOffset();
            StringBuffer stringBuffer = new StringBuffer(this._formatString.length() + 10);
            stringBuffer.append(substring);
            stringBuffer.append("'");
            if (indexOf >= 0) {
                stringBuffer.append('+');
            } else {
                indexOf = -indexOf;
                stringBuffer.append('-');
            }
            indexOf /= ExponentialBackOffPolicy.DEFAULT_MAX_INTERVAL_MILLIS;
            int i = indexOf / 60;
            indexOf %= 60;
            if (i < 10) {
                stringBuffer.append('0');
            }
            stringBuffer.append(i);
            if (indexOf < 10) {
                stringBuffer.append('0');
            }
            stringBuffer.append(indexOf);
            stringBuffer.append('\'');
            stringBuffer.append(substring2);
            this._tzFormatString = stringBuffer.toString();
        } else {
            this._tzFormatString = this._formatString;
        }
        setMinFormatString();
    }

    public String format(long j) {
        String format;
        synchronized (this) {
            long j2 = j / 1000;
            if (j2 < this._lastSeconds || (this._lastSeconds > 0 && j2 > this._lastSeconds + __hitWindow)) {
                format = this._tzFormat.format(new Date(j));
            } else if (this._lastSeconds == j2) {
                format = this._lastResult;
            } else {
                Date date = new Date(j);
                long j3 = j2 / 60;
                if (this._lastMinutes != j3) {
                    this._lastMinutes = j3;
                    this._secFormatString = this._minFormat.format(date);
                    int indexOf = this._secFormatString.indexOf("ss");
                    this._secFormatString0 = this._secFormatString.substring(0, indexOf);
                    this._secFormatString1 = this._secFormatString.substring(indexOf + 2);
                }
                this._lastSeconds = j2;
                StringBuffer stringBuffer = new StringBuffer(this._secFormatString.length());
                synchronized (stringBuffer) {
                    stringBuffer.append(this._secFormatString0);
                    int i = (int) (j2 % 60);
                    if (i < 10) {
                        stringBuffer.append('0');
                    }
                    stringBuffer.append(i);
                    stringBuffer.append(this._secFormatString1);
                    this._lastResult = stringBuffer.toString();
                }
                format = this._lastResult;
            }
        }
        return format;
    }

    public String format(Date date) {
        String format;
        synchronized (this) {
            format = format(date.getTime());
        }
        return format;
    }

    public void format(long j, StringBuffer stringBuffer) {
        stringBuffer.append(format(j));
    }

    public SimpleDateFormat getFormat() {
        return this._minFormat;
    }

    public String getFormatString() {
        return this._formatString;
    }

    public TimeZone getTimeZone() {
        return this._tzFormat.getTimeZone();
    }

    public int lastMs() {
        return this._lastMs;
    }

    public String now() {
        long currentTimeMillis = System.currentTimeMillis();
        this._lastMs = (int) (currentTimeMillis % 1000);
        return format(currentTimeMillis);
    }

    public void setTimeZone(TimeZone timeZone) {
        setTzFormatString(timeZone);
        if (this._locale != null) {
            this._tzFormat = new SimpleDateFormat(this._tzFormatString, this._locale);
            this._minFormat = new SimpleDateFormat(this._minFormatString, this._locale);
        } else if (this._dfs != null) {
            this._tzFormat = new SimpleDateFormat(this._tzFormatString, this._dfs);
            this._minFormat = new SimpleDateFormat(this._minFormatString, this._dfs);
        } else {
            this._tzFormat = new SimpleDateFormat(this._tzFormatString);
            this._minFormat = new SimpleDateFormat(this._minFormatString);
        }
        this._tzFormat.setTimeZone(timeZone);
        this._minFormat.setTimeZone(timeZone);
        this._lastSeconds = -1;
        this._lastMinutes = -1;
    }

    public void setTimeZoneID(String str) {
        setTimeZone(TimeZone.getTimeZone(str));
    }
}
