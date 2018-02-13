package org.mortbay.util.ajax;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import org.mortbay.log.Log;
import org.mortbay.util.DateCache;
import org.mortbay.util.ajax.JSON.Convertor;
import org.mortbay.util.ajax.JSON.Output;

public class JSONDateConvertor implements Convertor {
    DateCache _dateCache;
    SimpleDateFormat _format;
    private boolean _fromJSON;

    public JSONDateConvertor() {
        this(false);
    }

    public JSONDateConvertor(String str, TimeZone timeZone, boolean z) {
        this._dateCache = new DateCache(str);
        this._dateCache.setTimeZone(timeZone);
        this._fromJSON = z;
        this._format = new SimpleDateFormat(str);
        this._format.setTimeZone(timeZone);
    }

    public JSONDateConvertor(String str, TimeZone timeZone, boolean z, Locale locale) {
        this._dateCache = new DateCache(str, locale);
        this._dateCache.setTimeZone(timeZone);
        this._fromJSON = z;
        this._format = new SimpleDateFormat(str, new DateFormatSymbols(locale));
        this._format.setTimeZone(timeZone);
    }

    public JSONDateConvertor(boolean z) {
        this(DateCache.DEFAULT_FORMAT, TimeZone.getTimeZone("GMT"), z);
    }

    public Object fromJSON(Map map) {
        if (this._fromJSON) {
            try {
                Object parseObject;
                synchronized (this._format) {
                    parseObject = this._format.parseObject((String) map.get("value"));
                }
                return parseObject;
            } catch (Throwable e) {
                Log.warn(e);
                return null;
            }
        }
        throw new UnsupportedOperationException();
    }

    public void toJSON(Object obj, Output output) {
        Object format = this._dateCache.format((Date) obj);
        if (this._fromJSON) {
            output.addClass(obj.getClass());
            output.add("value", format);
            return;
        }
        output.add(format);
    }
}
