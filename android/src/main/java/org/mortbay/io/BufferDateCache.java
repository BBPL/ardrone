package org.mortbay.io;

import java.text.DateFormatSymbols;
import java.util.Locale;
import org.mortbay.util.DateCache;

public class BufferDateCache extends DateCache {
    Buffer _buffer;
    String _last;

    public BufferDateCache(String str) {
        super(str);
    }

    public BufferDateCache(String str, DateFormatSymbols dateFormatSymbols) {
        super(str, dateFormatSymbols);
    }

    public BufferDateCache(String str, Locale locale) {
        super(str, locale);
    }

    public Buffer formatBuffer(long j) {
        Buffer buffer;
        synchronized (this) {
            String format = super.format(j);
            if (format == this._last) {
                buffer = this._buffer;
            } else {
                this._last = format;
                this._buffer = new ByteArrayBuffer(format);
                buffer = this._buffer;
            }
        }
        return buffer;
    }
}
