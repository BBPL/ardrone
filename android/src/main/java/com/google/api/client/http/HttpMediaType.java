package com.google.api.client.http;

import com.google.api.client.util.Preconditions;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class HttpMediaType {
    private static final Pattern FULL_MEDIA_TYPE_REGEX = Pattern.compile("\\s*(" + "[^\\s/=;\"]+" + ")/(" + "[^\\s/=;\"]+" + ")" + "\\s*(" + ";.*" + ")?", 32);
    private static final Pattern PARAMETER_REGEX = Pattern.compile("\\s*;\\s*(" + "[^\\s/=;\"]+" + ")" + "=(" + ("\"([^\"]*)\"" + "|" + "[^\\s;\"]*") + ")");
    private static final Pattern TOKEN_REGEX = Pattern.compile("[\\p{ASCII}&&[^\\p{Cntrl} ;/=\\[\\]\\(\\)\\<\\>\\@\\,\\:\\\"\\?\\=]]+");
    private static final Pattern TYPE_REGEX = Pattern.compile("[\\w!#$&.+\\-\\^_]+|[*]");
    private String cachedBuildResult;
    private final SortedMap<String, String> parameters = new TreeMap();
    private String subType = "octet-stream";
    private String type = "application";

    public HttpMediaType(String str) {
        fromString(str);
    }

    public HttpMediaType(String str, String str2) {
        setType(str);
        setSubType(str2);
    }

    public static boolean equalsIgnoreParameters(String str, String str2) {
        return (str == null && str2 == null) || !(str == null || str2 == null || !new HttpMediaType(str).equalsIgnoreParameters(new HttpMediaType(str2)));
    }

    private HttpMediaType fromString(String str) {
        Matcher matcher = FULL_MEDIA_TYPE_REGEX.matcher(str);
        Preconditions.checkArgument(matcher.matches(), "Type must be in the 'maintype/subtype; parameter=value' format");
        setType(matcher.group(1));
        setSubType(matcher.group(2));
        CharSequence group = matcher.group(3);
        if (group != null) {
            Matcher matcher2 = PARAMETER_REGEX.matcher(group);
            while (matcher2.find()) {
                String group2 = matcher2.group(1);
                String group3 = matcher2.group(3);
                if (group3 == null) {
                    group3 = matcher2.group(2);
                }
                setParameter(group2, group3);
            }
        }
        return this;
    }

    static boolean matchesToken(String str) {
        return TOKEN_REGEX.matcher(str).matches();
    }

    private static String quoteString(String str) {
        return "\"" + str.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }

    public String build() {
        if (this.cachedBuildResult != null) {
            return this.cachedBuildResult;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.type);
        stringBuilder.append('/');
        stringBuilder.append(this.subType);
        if (this.parameters != null) {
            for (Entry entry : this.parameters.entrySet()) {
                String str = (String) entry.getValue();
                stringBuilder.append("; ");
                stringBuilder.append((String) entry.getKey());
                stringBuilder.append("=");
                if (!matchesToken(str)) {
                    str = quoteString(str);
                }
                stringBuilder.append(str);
            }
        }
        this.cachedBuildResult = stringBuilder.toString();
        return this.cachedBuildResult;
    }

    public void clearParameters() {
        this.cachedBuildResult = null;
        this.parameters.clear();
    }

    public boolean equals(Object obj) {
        if (obj instanceof HttpMediaType) {
            HttpMediaType httpMediaType = (HttpMediaType) obj;
            if (equalsIgnoreParameters(httpMediaType) && this.parameters.equals(httpMediaType.parameters)) {
                return true;
            }
        }
        return false;
    }

    public boolean equalsIgnoreParameters(HttpMediaType httpMediaType) {
        return httpMediaType != null && getType().equalsIgnoreCase(httpMediaType.getType()) && getSubType().equalsIgnoreCase(httpMediaType.getSubType());
    }

    public Charset getCharsetParameter() {
        String parameter = getParameter("charset");
        return parameter == null ? null : Charset.forName(parameter);
    }

    public String getParameter(String str) {
        return (String) this.parameters.get(str.toLowerCase());
    }

    public Map<String, String> getParameters() {
        return Collections.unmodifiableMap(this.parameters);
    }

    public String getSubType() {
        return this.subType;
    }

    public String getType() {
        return this.type;
    }

    public int hashCode() {
        return build().hashCode();
    }

    public HttpMediaType removeParameter(String str) {
        this.cachedBuildResult = null;
        this.parameters.remove(str.toLowerCase());
        return this;
    }

    public HttpMediaType setCharsetParameter(Charset charset) {
        setParameter("charset", charset == null ? null : charset.name());
        return this;
    }

    public HttpMediaType setParameter(String str, String str2) {
        if (str2 == null) {
            removeParameter(str);
        } else {
            Preconditions.checkArgument(TOKEN_REGEX.matcher(str).matches(), "Name contains reserved characters");
            this.cachedBuildResult = null;
            this.parameters.put(str.toLowerCase(), str2);
        }
        return this;
    }

    public HttpMediaType setSubType(String str) {
        Preconditions.checkArgument(TYPE_REGEX.matcher(str).matches(), "Subtype contains reserved characters");
        this.subType = str;
        this.cachedBuildResult = null;
        return this;
    }

    public HttpMediaType setType(String str) {
        Preconditions.checkArgument(TYPE_REGEX.matcher(str).matches(), "Type contains reserved characters");
        this.type = str;
        this.cachedBuildResult = null;
        return this;
    }

    public String toString() {
        return build();
    }
}
