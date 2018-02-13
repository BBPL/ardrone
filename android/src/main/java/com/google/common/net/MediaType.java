package com.google.common.net;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Ascii;
import com.google.common.base.CharMatcher;
import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Joiner.MapJoiner;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@GwtCompatible
@Immutable
@Beta
public final class MediaType {
    public static final MediaType ANY_APPLICATION_TYPE = createConstant(APPLICATION_TYPE, "*");
    public static final MediaType ANY_AUDIO_TYPE = createConstant(AUDIO_TYPE, "*");
    public static final MediaType ANY_IMAGE_TYPE = createConstant(IMAGE_TYPE, "*");
    public static final MediaType ANY_TEXT_TYPE = createConstant(TEXT_TYPE, "*");
    public static final MediaType ANY_TYPE = createConstant("*", "*");
    public static final MediaType ANY_VIDEO_TYPE = createConstant(VIDEO_TYPE, "*");
    private static final String APPLICATION_TYPE = "application";
    public static final MediaType ATOM_UTF_8 = createConstantUtf8(APPLICATION_TYPE, "atom+xml");
    private static final String AUDIO_TYPE = "audio";
    public static final MediaType BMP = createConstant(IMAGE_TYPE, "bmp");
    public static final MediaType BZIP2 = createConstant(APPLICATION_TYPE, "x-bzip2");
    public static final MediaType CACHE_MANIFEST_UTF_8 = createConstantUtf8(TEXT_TYPE, "cache-manifest");
    private static final String CHARSET_ATTRIBUTE = "charset";
    public static final MediaType CSS_UTF_8 = createConstantUtf8(TEXT_TYPE, "css");
    public static final MediaType CSV_UTF_8 = createConstantUtf8(TEXT_TYPE, "csv");
    public static final MediaType FORM_DATA = createConstant(APPLICATION_TYPE, "x-www-form-urlencoded");
    public static final MediaType GIF = createConstant(IMAGE_TYPE, "gif");
    public static final MediaType GZIP = createConstant(APPLICATION_TYPE, "x-gzip");
    public static final MediaType HTML_UTF_8 = createConstantUtf8(TEXT_TYPE, "html");
    public static final MediaType ICO = createConstant(IMAGE_TYPE, "vnd.microsoft.icon");
    private static final String IMAGE_TYPE = "image";
    public static final MediaType I_CALENDAR_UTF_8 = createConstantUtf8(TEXT_TYPE, "calendar");
    public static final MediaType JAVASCRIPT_UTF_8 = createConstantUtf8(APPLICATION_TYPE, "javascript");
    public static final MediaType JPEG = createConstant(IMAGE_TYPE, "jpeg");
    public static final MediaType JSON_UTF_8 = createConstantUtf8(APPLICATION_TYPE, "json");
    public static final MediaType KML = createConstant(APPLICATION_TYPE, "vnd.google-earth.kml+xml");
    public static final MediaType KMZ = createConstant(APPLICATION_TYPE, "vnd.google-earth.kmz");
    private static final ImmutableMap<MediaType, MediaType> KNOWN_TYPES = new Builder().put(ANY_TYPE, ANY_TYPE).put(ANY_TEXT_TYPE, ANY_TEXT_TYPE).put(ANY_IMAGE_TYPE, ANY_IMAGE_TYPE).put(ANY_AUDIO_TYPE, ANY_AUDIO_TYPE).put(ANY_VIDEO_TYPE, ANY_VIDEO_TYPE).put(ANY_APPLICATION_TYPE, ANY_APPLICATION_TYPE).put(CACHE_MANIFEST_UTF_8, CACHE_MANIFEST_UTF_8).put(CSS_UTF_8, CSS_UTF_8).put(CSV_UTF_8, CSV_UTF_8).put(HTML_UTF_8, HTML_UTF_8).put(I_CALENDAR_UTF_8, I_CALENDAR_UTF_8).put(PLAIN_TEXT_UTF_8, PLAIN_TEXT_UTF_8).put(TEXT_JAVASCRIPT_UTF_8, TEXT_JAVASCRIPT_UTF_8).put(VCARD_UTF_8, VCARD_UTF_8).put(WML_UTF_8, WML_UTF_8).put(XML_UTF_8, XML_UTF_8).put(BMP, BMP).put(GIF, GIF).put(ICO, ICO).put(JPEG, JPEG).put(PNG, PNG).put(SVG_UTF_8, SVG_UTF_8).put(TIFF, TIFF).put(WEBP, WEBP).put(MP4_AUDIO, MP4_AUDIO).put(MPEG_AUDIO, MPEG_AUDIO).put(OGG_AUDIO, OGG_AUDIO).put(WEBM_AUDIO, WEBM_AUDIO).put(MP4_VIDEO, MP4_VIDEO).put(MPEG_VIDEO, MPEG_VIDEO).put(OGG_VIDEO, OGG_VIDEO).put(QUICKTIME, QUICKTIME).put(WEBM_VIDEO, WEBM_VIDEO).put(WMV, WMV).put(ATOM_UTF_8, ATOM_UTF_8).put(BZIP2, BZIP2).put(FORM_DATA, FORM_DATA).put(GZIP, GZIP).put(JAVASCRIPT_UTF_8, JAVASCRIPT_UTF_8).put(JSON_UTF_8, JSON_UTF_8).put(KML, KML).put(KMZ, KMZ).put(MBOX, MBOX).put(MICROSOFT_EXCEL, MICROSOFT_EXCEL).put(MICROSOFT_POWERPOINT, MICROSOFT_POWERPOINT).put(MICROSOFT_WORD, MICROSOFT_WORD).put(OCTET_STREAM, OCTET_STREAM).put(OGG_CONTAINER, OGG_CONTAINER).put(OOXML_DOCUMENT, OOXML_DOCUMENT).put(OOXML_PRESENTATION, OOXML_PRESENTATION).put(OOXML_SHEET, OOXML_SHEET).put(OPENDOCUMENT_GRAPHICS, OPENDOCUMENT_GRAPHICS).put(OPENDOCUMENT_PRESENTATION, OPENDOCUMENT_PRESENTATION).put(OPENDOCUMENT_SPREADSHEET, OPENDOCUMENT_SPREADSHEET).put(OPENDOCUMENT_TEXT, OPENDOCUMENT_TEXT).put(PDF, PDF).put(POSTSCRIPT, POSTSCRIPT).put(RTF_UTF_8, RTF_UTF_8).put(SHOCKWAVE_FLASH, SHOCKWAVE_FLASH).put(SKETCHUP, SKETCHUP).put(TAR, TAR).put(XHTML_UTF_8, XHTML_UTF_8).put(ZIP, ZIP).build();
    private static final CharMatcher LINEAR_WHITE_SPACE = CharMatcher.anyOf(" \t\r\n");
    public static final MediaType MBOX = createConstant(APPLICATION_TYPE, "mbox");
    public static final MediaType MICROSOFT_EXCEL = createConstant(APPLICATION_TYPE, "vnd.ms-excel");
    public static final MediaType MICROSOFT_POWERPOINT = createConstant(APPLICATION_TYPE, "vnd.ms-powerpoint");
    public static final MediaType MICROSOFT_WORD = createConstant(APPLICATION_TYPE, "msword");
    public static final MediaType MP4_AUDIO = createConstant(AUDIO_TYPE, "mp4");
    public static final MediaType MP4_VIDEO = createConstant(VIDEO_TYPE, "mp4");
    public static final MediaType MPEG_AUDIO = createConstant(AUDIO_TYPE, "mpeg");
    public static final MediaType MPEG_VIDEO = createConstant(VIDEO_TYPE, "mpeg");
    public static final MediaType OCTET_STREAM = createConstant(APPLICATION_TYPE, "octet-stream");
    public static final MediaType OGG_AUDIO = createConstant(AUDIO_TYPE, "ogg");
    public static final MediaType OGG_CONTAINER = createConstant(APPLICATION_TYPE, "ogg");
    public static final MediaType OGG_VIDEO = createConstant(VIDEO_TYPE, "ogg");
    public static final MediaType OOXML_DOCUMENT = createConstant(APPLICATION_TYPE, "vnd.openxmlformats-officedocument.wordprocessingml.document");
    public static final MediaType OOXML_PRESENTATION = createConstant(APPLICATION_TYPE, "vnd.openxmlformats-officedocument.presentationml.presentation");
    public static final MediaType OOXML_SHEET = createConstant(APPLICATION_TYPE, "vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    public static final MediaType OPENDOCUMENT_GRAPHICS = createConstant(APPLICATION_TYPE, "vnd.oasis.opendocument.graphics");
    public static final MediaType OPENDOCUMENT_PRESENTATION = createConstant(APPLICATION_TYPE, "vnd.oasis.opendocument.presentation");
    public static final MediaType OPENDOCUMENT_SPREADSHEET = createConstant(APPLICATION_TYPE, "vnd.oasis.opendocument.spreadsheet");
    public static final MediaType OPENDOCUMENT_TEXT = createConstant(APPLICATION_TYPE, "vnd.oasis.opendocument.text");
    private static final MapJoiner PARAMETER_JOINER = Joiner.on("; ").withKeyValueSeparator("=");
    public static final MediaType PDF = createConstant(APPLICATION_TYPE, "pdf");
    public static final MediaType PLAIN_TEXT_UTF_8 = createConstantUtf8(TEXT_TYPE, "plain");
    public static final MediaType PNG = createConstant(IMAGE_TYPE, "png");
    public static final MediaType POSTSCRIPT = createConstant(APPLICATION_TYPE, "postscript");
    public static final MediaType QUICKTIME = createConstant(VIDEO_TYPE, "quicktime");
    private static final CharMatcher QUOTED_TEXT_MATCHER = CharMatcher.ASCII.and(CharMatcher.noneOf("\"\\\r"));
    public static final MediaType RTF_UTF_8 = createConstantUtf8(APPLICATION_TYPE, "rtf");
    public static final MediaType SHOCKWAVE_FLASH = createConstant(APPLICATION_TYPE, "x-shockwave-flash");
    public static final MediaType SKETCHUP = createConstant(APPLICATION_TYPE, "vnd.sketchup.skp");
    public static final MediaType SVG_UTF_8 = createConstantUtf8(IMAGE_TYPE, "svg+xml");
    public static final MediaType TAR = createConstant(APPLICATION_TYPE, "x-tar");
    public static final MediaType TEXT_JAVASCRIPT_UTF_8 = createConstantUtf8(TEXT_TYPE, "javascript");
    private static final String TEXT_TYPE = "text";
    public static final MediaType TIFF = createConstant(IMAGE_TYPE, "tiff");
    private static final CharMatcher TOKEN_MATCHER = CharMatcher.ASCII.and(CharMatcher.JAVA_ISO_CONTROL.negate()).and(CharMatcher.isNot(' ')).and(CharMatcher.noneOf("()<>@,;:\\\"/[]?="));
    private static final ImmutableListMultimap<String, String> UTF_8_CONSTANT_PARAMETERS = ImmutableListMultimap.of(CHARSET_ATTRIBUTE, Ascii.toLowerCase(Charsets.UTF_8.name()));
    public static final MediaType VCARD_UTF_8 = createConstantUtf8(TEXT_TYPE, "vcard");
    private static final String VIDEO_TYPE = "video";
    public static final MediaType WEBM_AUDIO = createConstant(AUDIO_TYPE, "webm");
    public static final MediaType WEBM_VIDEO = createConstant(VIDEO_TYPE, "webm");
    public static final MediaType WEBP = createConstant(IMAGE_TYPE, "webp");
    private static final String WILDCARD = "*";
    public static final MediaType WML_UTF_8 = createConstantUtf8(TEXT_TYPE, "vnd.wap.wml");
    public static final MediaType WMV = createConstant(VIDEO_TYPE, "x-ms-wmv");
    public static final MediaType XHTML_UTF_8 = createConstantUtf8(APPLICATION_TYPE, "xhtml+xml");
    public static final MediaType XML_UTF_8 = createConstantUtf8(TEXT_TYPE, "xml");
    public static final MediaType ZIP = createConstant(APPLICATION_TYPE, "zip");
    private final ImmutableListMultimap<String, String> parameters;
    private final String subtype;
    private final String type;

    class C08161 implements Function<Collection<String>, ImmutableMultiset<String>> {
        C08161() {
        }

        public ImmutableMultiset<String> apply(Collection<String> collection) {
            return ImmutableMultiset.copyOf((Iterable) collection);
        }
    }

    class C08172 implements Function<String, String> {
        C08172() {
        }

        public String apply(String str) {
            return MediaType.TOKEN_MATCHER.matchesAllOf(str) ? str : MediaType.escapeAndQuote(str);
        }
    }

    private static final class Tokenizer {
        final String input;
        int position = 0;

        Tokenizer(String str) {
            this.input = str;
        }

        char consumeCharacter(char c) {
            Preconditions.checkState(hasMore());
            Preconditions.checkState(previewChar() == c);
            this.position++;
            return c;
        }

        char consumeCharacter(CharMatcher charMatcher) {
            Preconditions.checkState(hasMore());
            char previewChar = previewChar();
            Preconditions.checkState(charMatcher.matches(previewChar));
            this.position++;
            return previewChar;
        }

        String consumeToken(CharMatcher charMatcher) {
            int i = this.position;
            String consumeTokenIfPresent = consumeTokenIfPresent(charMatcher);
            Preconditions.checkState(this.position != i);
            return consumeTokenIfPresent;
        }

        String consumeTokenIfPresent(CharMatcher charMatcher) {
            Preconditions.checkState(hasMore());
            int i = this.position;
            this.position = charMatcher.negate().indexIn(this.input, i);
            return hasMore() ? this.input.substring(i, this.position) : this.input.substring(i);
        }

        boolean hasMore() {
            return this.position >= 0 && this.position < this.input.length();
        }

        char previewChar() {
            Preconditions.checkState(hasMore());
            return this.input.charAt(this.position);
        }
    }

    private MediaType(String str, String str2, ImmutableListMultimap<String, String> immutableListMultimap) {
        this.type = str;
        this.subtype = str2;
        this.parameters = immutableListMultimap;
    }

    public static MediaType create(String str, String str2) {
        return create(str, str2, ImmutableListMultimap.of());
    }

    private static MediaType create(String str, String str2, Multimap<String, String> multimap) {
        Preconditions.checkNotNull(str);
        Preconditions.checkNotNull(str2);
        Preconditions.checkNotNull(multimap);
        String normalizeToken = normalizeToken(str);
        String normalizeToken2 = normalizeToken(str2);
        boolean z = !"*".equals(normalizeToken) || "*".equals(normalizeToken2);
        Preconditions.checkArgument(z, "A wildcard type cannot be used with a non-wildcard subtype");
        ImmutableListMultimap.Builder builder = ImmutableListMultimap.builder();
        for (Entry entry : multimap.entries()) {
            Object normalizeToken3 = normalizeToken((String) entry.getKey());
            builder.put(normalizeToken3, normalizeParameterValue(normalizeToken3, (String) entry.getValue()));
        }
        MediaType mediaType = new MediaType(normalizeToken, normalizeToken2, builder.build());
        return (MediaType) Objects.firstNonNull(KNOWN_TYPES.get(mediaType), mediaType);
    }

    static MediaType createApplicationType(String str) {
        return create(APPLICATION_TYPE, str);
    }

    static MediaType createAudioType(String str) {
        return create(AUDIO_TYPE, str);
    }

    private static MediaType createConstant(String str, String str2) {
        return new MediaType(str, str2, ImmutableListMultimap.of());
    }

    private static MediaType createConstantUtf8(String str, String str2) {
        return new MediaType(str, str2, UTF_8_CONSTANT_PARAMETERS);
    }

    static MediaType createImageType(String str) {
        return create(IMAGE_TYPE, str);
    }

    static MediaType createTextType(String str) {
        return create(TEXT_TYPE, str);
    }

    static MediaType createVideoType(String str) {
        return create(VIDEO_TYPE, str);
    }

    private static String escapeAndQuote(String str) {
        StringBuilder append = new StringBuilder(str.length() + 16).append('\"');
        for (char c : str.toCharArray()) {
            if (c == '\r' || c == '\\' || c == '\"') {
                append.append('\\');
            }
            append.append(c);
        }
        return append.append('\"').toString();
    }

    private static String normalizeParameterValue(String str, String str2) {
        return CHARSET_ATTRIBUTE.equals(str) ? Ascii.toLowerCase(str2) : str2;
    }

    private static String normalizeToken(String str) {
        Preconditions.checkArgument(TOKEN_MATCHER.matchesAllOf(str));
        return Ascii.toLowerCase(str);
    }

    private Map<String, ImmutableMultiset<String>> parametersAsMap() {
        return Maps.transformValues(this.parameters.asMap(), new C08161());
    }

    public static MediaType parse(String str) {
        Preconditions.checkNotNull(str);
        Tokenizer tokenizer = new Tokenizer(str);
        try {
            String consumeToken = tokenizer.consumeToken(TOKEN_MATCHER);
            tokenizer.consumeCharacter('/');
            String consumeToken2 = tokenizer.consumeToken(TOKEN_MATCHER);
            ImmutableListMultimap.Builder builder = ImmutableListMultimap.builder();
            while (tokenizer.hasMore()) {
                Object stringBuilder;
                tokenizer.consumeCharacter(';');
                tokenizer.consumeTokenIfPresent(LINEAR_WHITE_SPACE);
                Object consumeToken3 = tokenizer.consumeToken(TOKEN_MATCHER);
                tokenizer.consumeCharacter('=');
                if ('\"' == tokenizer.previewChar()) {
                    tokenizer.consumeCharacter('\"');
                    StringBuilder stringBuilder2 = new StringBuilder();
                    while ('\"' != tokenizer.previewChar()) {
                        if ('\\' == tokenizer.previewChar()) {
                            tokenizer.consumeCharacter('\\');
                            stringBuilder2.append(tokenizer.consumeCharacter(CharMatcher.ASCII));
                        } else {
                            stringBuilder2.append(tokenizer.consumeToken(QUOTED_TEXT_MATCHER));
                        }
                    }
                    stringBuilder = stringBuilder2.toString();
                    tokenizer.consumeCharacter('\"');
                } else {
                    stringBuilder = tokenizer.consumeToken(TOKEN_MATCHER);
                }
                builder.put(consumeToken3, stringBuilder);
            }
            return create(consumeToken, consumeToken2, builder.build());
        } catch (Throwable e) {
            throw new IllegalArgumentException(e);
        }
    }

    public Optional<Charset> charset() {
        Iterable copyOf = ImmutableSet.copyOf(this.parameters.get(CHARSET_ATTRIBUTE));
        switch (copyOf.size()) {
            case 0:
                return Optional.absent();
            case 1:
                return Optional.of(Charset.forName((String) Iterables.getOnlyElement(copyOf)));
            default:
                throw new IllegalStateException("Multiple charset values defined: " + copyOf);
        }
    }

    public boolean equals(@Nullable Object obj) {
        if (obj != this) {
            if (!(obj instanceof MediaType)) {
                return false;
            }
            MediaType mediaType = (MediaType) obj;
            if (!this.type.equals(mediaType.type) || !this.subtype.equals(mediaType.subtype)) {
                return false;
            }
            if (!parametersAsMap().equals(mediaType.parametersAsMap())) {
                return false;
            }
        }
        return true;
    }

    public boolean hasWildcard() {
        return "*".equals(this.type) || "*".equals(this.subtype);
    }

    public int hashCode() {
        return Objects.hashCode(this.type, this.subtype, parametersAsMap());
    }

    public boolean is(MediaType mediaType) {
        return (mediaType.type.equals("*") || mediaType.type.equals(this.type)) && ((mediaType.subtype.equals("*") || mediaType.subtype.equals(this.subtype)) && this.parameters.entries().containsAll(mediaType.parameters.entries()));
    }

    public ImmutableListMultimap<String, String> parameters() {
        return this.parameters;
    }

    public String subtype() {
        return this.subtype;
    }

    public String toString() {
        StringBuilder append = new StringBuilder().append(this.type).append('/').append(this.subtype);
        if (!this.parameters.isEmpty()) {
            append.append("; ");
            PARAMETER_JOINER.appendTo(append, Multimaps.transformValues(this.parameters, new C08172()).entries());
        }
        return append.toString();
    }

    public String type() {
        return this.type;
    }

    public MediaType withCharset(Charset charset) {
        Preconditions.checkNotNull(charset);
        return withParameter(CHARSET_ATTRIBUTE, charset.name());
    }

    public MediaType withParameter(String str, String str2) {
        Preconditions.checkNotNull(str);
        Preconditions.checkNotNull(str2);
        Object normalizeToken = normalizeToken(str);
        ImmutableListMultimap.Builder builder = ImmutableListMultimap.builder();
        Iterator it = this.parameters.entries().iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            Object obj = (String) entry.getKey();
            if (!normalizeToken.equals(obj)) {
                builder.put(obj, entry.getValue());
            }
        }
        builder.put(normalizeToken, normalizeParameterValue(normalizeToken, str2));
        MediaType mediaType = new MediaType(this.type, this.subtype, builder.build());
        return (MediaType) Objects.firstNonNull(KNOWN_TYPES.get(mediaType), mediaType);
    }

    public MediaType withParameters(Multimap<String, String> multimap) {
        return create(this.type, this.subtype, multimap);
    }

    public MediaType withoutParameters() {
        return this.parameters.isEmpty() ? this : create(this.type, this.subtype);
    }
}
