package com.google.api.client.xml.atom;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpMediaType;
import com.google.api.client.util.Charsets;
import com.google.api.client.util.Lists;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.escape.PercentEscaper;
import com.google.api.client.xml.Xml.CustomizeParser;
import java.util.Arrays;

public final class Atom {
    public static final String ATOM_NAMESPACE = "http://www.w3.org/2005/Atom";
    public static final String MEDIA_TYPE = new HttpMediaType("application/atom+xml").setCharsetParameter(Charsets.UTF_8).build();
    private static final PercentEscaper SLUG_ESCAPER = new PercentEscaper(" !\"#$&'()*+,-./:;<=>?@[\\]^_`{|}~", false);

    static final class StopAtAtomEntry extends CustomizeParser {
        static final StopAtAtomEntry INSTANCE = new StopAtAtomEntry();

        StopAtAtomEntry() {
        }

        public boolean stopBeforeStartTag(String str, String str2) {
            return "entry".equals(str2) && Atom.ATOM_NAMESPACE.equals(str);
        }
    }

    private Atom() {
    }

    public static void checkContentType(String str) {
        Preconditions.checkArgument(str != null);
        Preconditions.checkArgument(HttpMediaType.equalsIgnoreParameters(MEDIA_TYPE, str), "Wrong content type: expected <" + MEDIA_TYPE + "> but got <%s>", str);
    }

    public static void setSlugHeader(HttpHeaders httpHeaders, String str) {
        if (str == null) {
            httpHeaders.remove("Slug");
            return;
        }
        httpHeaders.set("Slug", Lists.newArrayList(Arrays.asList(new String[]{SLUG_ESCAPER.escape(str)})));
    }
}
