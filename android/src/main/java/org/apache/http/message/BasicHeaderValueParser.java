package org.apache.http.message;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.annotation.Immutable;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.CharArrayBuffer;

@Immutable
public class BasicHeaderValueParser implements HeaderValueParser {
    private static final char[] ALL_DELIMITERS = new char[]{PARAM_DELIMITER, ELEM_DELIMITER};
    public static final BasicHeaderValueParser DEFAULT = new BasicHeaderValueParser();
    private static final char ELEM_DELIMITER = ',';
    private static final char PARAM_DELIMITER = ';';

    private static boolean isOneOf(char c, char[] cArr) {
        if (cArr == null) {
            return false;
        }
        for (char c2 : cArr) {
            if (c == c2) {
                return true;
            }
        }
        return false;
    }

    public static final HeaderElement[] parseElements(String str, HeaderValueParser headerValueParser) throws ParseException {
        if (str == null) {
            throw new IllegalArgumentException("Value to parse may not be null");
        }
        if (headerValueParser == null) {
            headerValueParser = DEFAULT;
        }
        CharArrayBuffer charArrayBuffer = new CharArrayBuffer(str.length());
        charArrayBuffer.append(str);
        return headerValueParser.parseElements(charArrayBuffer, new ParserCursor(0, str.length()));
    }

    public static final HeaderElement parseHeaderElement(String str, HeaderValueParser headerValueParser) throws ParseException {
        if (str == null) {
            throw new IllegalArgumentException("Value to parse may not be null");
        }
        if (headerValueParser == null) {
            headerValueParser = DEFAULT;
        }
        CharArrayBuffer charArrayBuffer = new CharArrayBuffer(str.length());
        charArrayBuffer.append(str);
        return headerValueParser.parseHeaderElement(charArrayBuffer, new ParserCursor(0, str.length()));
    }

    public static final NameValuePair parseNameValuePair(String str, HeaderValueParser headerValueParser) throws ParseException {
        if (str == null) {
            throw new IllegalArgumentException("Value to parse may not be null");
        }
        if (headerValueParser == null) {
            headerValueParser = DEFAULT;
        }
        CharArrayBuffer charArrayBuffer = new CharArrayBuffer(str.length());
        charArrayBuffer.append(str);
        return headerValueParser.parseNameValuePair(charArrayBuffer, new ParserCursor(0, str.length()));
    }

    public static final NameValuePair[] parseParameters(String str, HeaderValueParser headerValueParser) throws ParseException {
        if (str == null) {
            throw new IllegalArgumentException("Value to parse may not be null");
        }
        if (headerValueParser == null) {
            headerValueParser = DEFAULT;
        }
        CharArrayBuffer charArrayBuffer = new CharArrayBuffer(str.length());
        charArrayBuffer.append(str);
        return headerValueParser.parseParameters(charArrayBuffer, new ParserCursor(0, str.length()));
    }

    protected HeaderElement createHeaderElement(String str, String str2, NameValuePair[] nameValuePairArr) {
        return new BasicHeaderElement(str, str2, nameValuePairArr);
    }

    protected NameValuePair createNameValuePair(String str, String str2) {
        return new BasicNameValuePair(str, str2);
    }

    public HeaderElement[] parseElements(CharArrayBuffer charArrayBuffer, ParserCursor parserCursor) {
        if (charArrayBuffer == null) {
            throw new IllegalArgumentException("Char array buffer may not be null");
        } else if (parserCursor == null) {
            throw new IllegalArgumentException("Parser cursor may not be null");
        } else {
            List arrayList = new ArrayList();
            while (!parserCursor.atEnd()) {
                HeaderElement parseHeaderElement = parseHeaderElement(charArrayBuffer, parserCursor);
                if (parseHeaderElement.getName().length() != 0 || parseHeaderElement.getValue() != null) {
                    arrayList.add(parseHeaderElement);
                }
            }
            return (HeaderElement[]) arrayList.toArray(new HeaderElement[arrayList.size()]);
        }
    }

    public HeaderElement parseHeaderElement(CharArrayBuffer charArrayBuffer, ParserCursor parserCursor) {
        if (charArrayBuffer == null) {
            throw new IllegalArgumentException("Char array buffer may not be null");
        } else if (parserCursor == null) {
            throw new IllegalArgumentException("Parser cursor may not be null");
        } else {
            NameValuePair parseNameValuePair = parseNameValuePair(charArrayBuffer, parserCursor);
            NameValuePair[] nameValuePairArr = null;
            if (!(parserCursor.atEnd() || charArrayBuffer.charAt(parserCursor.getPos() - 1) == ELEM_DELIMITER)) {
                nameValuePairArr = parseParameters(charArrayBuffer, parserCursor);
            }
            return createHeaderElement(parseNameValuePair.getName(), parseNameValuePair.getValue(), nameValuePairArr);
        }
    }

    public NameValuePair parseNameValuePair(CharArrayBuffer charArrayBuffer, ParserCursor parserCursor) {
        return parseNameValuePair(charArrayBuffer, parserCursor, ALL_DELIMITERS);
    }

    public NameValuePair parseNameValuePair(CharArrayBuffer charArrayBuffer, ParserCursor parserCursor, char[] cArr) {
        Object obj = 1;
        if (charArrayBuffer == null) {
            throw new IllegalArgumentException("Char array buffer may not be null");
        } else if (parserCursor == null) {
            throw new IllegalArgumentException("Parser cursor may not be null");
        } else {
            Object obj2;
            int i;
            String substringTrimmed;
            Object obj3;
            String substringTrimmed2;
            int pos = parserCursor.getPos();
            int pos2 = parserCursor.getPos();
            int upperBound = parserCursor.getUpperBound();
            while (pos < upperBound) {
                char charAt = charArrayBuffer.charAt(pos);
                if (charAt == '=') {
                    obj2 = null;
                    break;
                } else if (isOneOf(charAt, cArr)) {
                    i = 1;
                    break;
                } else {
                    pos++;
                }
            }
            obj2 = null;
            if (pos == upperBound) {
                substringTrimmed = charArrayBuffer.substringTrimmed(pos2, upperBound);
                obj3 = 1;
            } else {
                substringTrimmed2 = charArrayBuffer.substringTrimmed(pos2, pos);
                pos++;
                substringTrimmed = substringTrimmed2;
                obj3 = obj2;
            }
            if (obj3 != null) {
                parserCursor.updatePos(pos);
                return createNameValuePair(substringTrimmed, null);
            }
            obj2 = null;
            Object obj4 = null;
            int i2 = pos;
            while (i2 < upperBound) {
                char charAt2 = charArrayBuffer.charAt(i2);
                Object obj5 = (charAt2 == '\"' && obj4 == null) ? obj2 == null ? 1 : null : obj2;
                if (obj5 == null && obj4 == null && isOneOf(charAt2, cArr)) {
                    break;
                }
                if (obj4 != null) {
                    obj2 = null;
                } else if (obj5 == null || charAt2 != '\\') {
                    obj2 = null;
                } else {
                    i = 1;
                }
                i2++;
                obj4 = obj2;
                obj2 = obj5;
            }
            obj = obj3;
            pos2 = pos;
            while (pos2 < i2 && HTTP.isWhitespace(charArrayBuffer.charAt(pos2))) {
                pos2++;
            }
            i = i2;
            while (i > pos2 && HTTP.isWhitespace(charArrayBuffer.charAt(i - 1))) {
                i--;
            }
            if (i - pos2 >= 2 && charArrayBuffer.charAt(pos2) == '\"' && charArrayBuffer.charAt(i - 1) == '\"') {
                pos2++;
                i--;
            }
            substringTrimmed2 = charArrayBuffer.substring(pos2, i);
            parserCursor.updatePos(obj != null ? i2 + 1 : i2);
            return createNameValuePair(substringTrimmed, substringTrimmed2);
        }
    }

    public NameValuePair[] parseParameters(CharArrayBuffer charArrayBuffer, ParserCursor parserCursor) {
        if (charArrayBuffer == null) {
            throw new IllegalArgumentException("Char array buffer may not be null");
        } else if (parserCursor == null) {
            throw new IllegalArgumentException("Parser cursor may not be null");
        } else {
            int pos = parserCursor.getPos();
            int upperBound = parserCursor.getUpperBound();
            while (pos < upperBound && HTTP.isWhitespace(charArrayBuffer.charAt(pos))) {
                pos++;
            }
            parserCursor.updatePos(pos);
            if (parserCursor.atEnd()) {
                return new NameValuePair[0];
            }
            List arrayList = new ArrayList();
            while (!parserCursor.atEnd()) {
                arrayList.add(parseNameValuePair(charArrayBuffer, parserCursor));
                if (charArrayBuffer.charAt(parserCursor.getPos() - 1) == ELEM_DELIMITER) {
                    break;
                }
            }
            return (NameValuePair[]) arrayList.toArray(new NameValuePair[arrayList.size()]);
        }
    }
}
