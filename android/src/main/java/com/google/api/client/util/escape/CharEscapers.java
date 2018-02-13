package com.google.api.client.util.escape;

import java.net.URLDecoder;

public final class CharEscapers {
    private static final Escaper URI_ESCAPER = new PercentEscaper(PercentEscaper.SAFECHARS_URLENCODER, true);
    private static final Escaper URI_PATH_ESCAPER = new PercentEscaper(PercentEscaper.SAFEPATHCHARS_URLENCODER, false);
    private static final Escaper URI_QUERY_STRING_ESCAPER = new PercentEscaper(PercentEscaper.SAFEQUERYSTRINGCHARS_URLENCODER, false);

    private CharEscapers() {
    }

    public static String decodeUri(String str) {
        try {
            return URLDecoder.decode(str, "UTF-8");
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static String escapeUri(String str) {
        return URI_ESCAPER.escape(str);
    }

    public static String escapeUriPath(String str) {
        return URI_PATH_ESCAPER.escape(str);
    }

    public static String escapeUriQuery(String str) {
        return URI_QUERY_STRING_ESCAPER.escape(str);
    }
}
