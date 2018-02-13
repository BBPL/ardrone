package org.apache.commons.codec.language;

import java.util.Locale;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.sanselan.formats.tiff.constants.ExifTagConstants;
import org.mortbay.jetty.HttpVersions;

public class Metaphone implements StringEncoder {
    private static final String FRONTV = "EIY";
    private static final String VARSON = "CSPTG";
    private static final String VOWELS = "AEIOU";
    private int maxCodeLen = 4;

    private boolean isLastChar(int i, int i2) {
        return i2 + 1 == i;
    }

    private boolean isNextChar(StringBuffer stringBuffer, int i, char c) {
        return i >= 0 && i < stringBuffer.length() - 1 && stringBuffer.charAt(i + 1) == c;
    }

    private boolean isPreviousChar(StringBuffer stringBuffer, int i, char c) {
        return i > 0 && i < stringBuffer.length() && stringBuffer.charAt(i - 1) == c;
    }

    private boolean isVowel(StringBuffer stringBuffer, int i) {
        return VOWELS.indexOf(stringBuffer.charAt(i)) >= 0;
    }

    private boolean regionMatch(StringBuffer stringBuffer, int i, String str) {
        return (i < 0 || (str.length() + i) - 1 >= stringBuffer.length()) ? false : stringBuffer.substring(i, str.length() + i).equals(str);
    }

    public Object encode(Object obj) throws EncoderException {
        if (obj instanceof String) {
            return metaphone((String) obj);
        }
        throw new EncoderException("Parameter supplied to Metaphone encode is not of type java.lang.String");
    }

    public String encode(String str) {
        return metaphone(str);
    }

    public int getMaxCodeLen() {
        return this.maxCodeLen;
    }

    public boolean isMetaphoneEqual(String str, String str2) {
        return metaphone(str).equals(metaphone(str2));
    }

    public String metaphone(String str) {
        if (str == null || str.length() == 0) {
            return HttpVersions.HTTP_0_9;
        }
        if (str.length() == 1) {
            return str.toUpperCase(Locale.ENGLISH);
        }
        char[] toCharArray = str.toUpperCase(Locale.ENGLISH).toCharArray();
        StringBuffer stringBuffer = new StringBuffer(40);
        StringBuffer stringBuffer2 = new StringBuffer(10);
        switch (toCharArray[0]) {
            case ExifTagConstants.FLASH_VALUE_FIRED_RED_EYE_REDUCTION /*65*/:
                if (toCharArray[1] != 'E') {
                    stringBuffer.append(toCharArray);
                    break;
                }
                stringBuffer.append(toCharArray, 1, toCharArray.length - 1);
                break;
            case ExifTagConstants.FLASH_VALUE_FIRED_RED_EYE_REDUCTION_RETURN_DETECTED /*71*/:
            case 'K':
            case ExifTagConstants.FLASH_VALUE_OFF_RED_EYE_REDUCTION /*80*/:
                if (toCharArray[1] != 'N') {
                    stringBuffer.append(toCharArray);
                    break;
                }
                stringBuffer.append(toCharArray, 1, toCharArray.length - 1);
                break;
            case 'W':
                if (toCharArray[1] != 'R') {
                    if (toCharArray[1] != 'H') {
                        stringBuffer.append(toCharArray);
                        break;
                    }
                    stringBuffer.append(toCharArray, 1, toCharArray.length - 1);
                    stringBuffer.setCharAt(0, 'W');
                    break;
                }
                stringBuffer.append(toCharArray, 1, toCharArray.length - 1);
                break;
            case ExifTagConstants.FLASH_VALUE_AUTO_DID_NOT_FIRE_RED_EYE_REDUCTION /*88*/:
                toCharArray[0] = 'S';
                stringBuffer.append(toCharArray);
                break;
            default:
                stringBuffer.append(toCharArray);
                break;
        }
        int length = stringBuffer.length();
        int i = 0;
        while (stringBuffer2.length() < getMaxCodeLen() && i < length) {
            char charAt = stringBuffer.charAt(i);
            if (charAt == 'C' || !isPreviousChar(stringBuffer, i, charAt)) {
                switch (charAt) {
                    case ExifTagConstants.FLASH_VALUE_FIRED_RED_EYE_REDUCTION /*65*/:
                    case ExifTagConstants.FLASH_VALUE_FIRED_RED_EYE_REDUCTION_RETURN_NOT_DETECTED /*69*/:
                    case 'I':
                    case ExifTagConstants.FLASH_VALUE_ON_RED_EYE_REDUCTION_RETURN_DETECTED /*79*/:
                    case 'U':
                        if (i == 0) {
                            stringBuffer2.append(charAt);
                            break;
                        }
                        break;
                    case 'B':
                        if (!(isPreviousChar(stringBuffer, i, 'M') && isLastChar(length, i))) {
                            stringBuffer2.append(charAt);
                            break;
                        }
                    case 'C':
                        if (!isPreviousChar(stringBuffer, i, 'S') || isLastChar(length, i) || FRONTV.indexOf(stringBuffer.charAt(i + 1)) < 0) {
                            if (!regionMatch(stringBuffer, i, "CIA")) {
                                if (isLastChar(length, i) || FRONTV.indexOf(stringBuffer.charAt(i + 1)) < 0) {
                                    if (!isPreviousChar(stringBuffer, i, 'S') || !isNextChar(stringBuffer, i, 'H')) {
                                        if (isNextChar(stringBuffer, i, 'H')) {
                                            if (i != 0 || length < 3 || !isVowel(stringBuffer, 2)) {
                                                stringBuffer2.append('X');
                                                break;
                                            }
                                            stringBuffer2.append('K');
                                            break;
                                        }
                                        stringBuffer2.append('K');
                                        break;
                                    }
                                    stringBuffer2.append('K');
                                    break;
                                }
                                stringBuffer2.append('S');
                                break;
                            }
                            stringBuffer2.append('X');
                            break;
                        }
                        break;
                    case 'D':
                        if (!isLastChar(length, i + 1) && isNextChar(stringBuffer, i, 'G') && FRONTV.indexOf(stringBuffer.charAt(i + 2)) >= 0) {
                            stringBuffer2.append('J');
                            i += 2;
                            break;
                        }
                        stringBuffer2.append('T');
                        break;
                        break;
                    case 'F':
                    case 'J':
                    case 'L':
                    case 'M':
                    case 'N':
                    case 'R':
                        stringBuffer2.append(charAt);
                        break;
                    case ExifTagConstants.FLASH_VALUE_FIRED_RED_EYE_REDUCTION_RETURN_DETECTED /*71*/:
                        if (!(isLastChar(length, i + 1) && isNextChar(stringBuffer, i, 'H')) && ((isLastChar(length, i + 1) || !isNextChar(stringBuffer, i, 'H') || isVowel(stringBuffer, i + 2)) && (i <= 0 || !(regionMatch(stringBuffer, i, "GN") || regionMatch(stringBuffer, i, "GNED"))))) {
                            int i2 = isPreviousChar(stringBuffer, i, 'G') ? 1 : 0;
                            if (!isLastChar(length, i) && FRONTV.indexOf(stringBuffer.charAt(i + 1)) >= 0 && i2 == 0) {
                                stringBuffer2.append('J');
                                break;
                            }
                            stringBuffer2.append('K');
                            break;
                        }
                        break;
                    case 'H':
                        if (!isLastChar(length, i) && ((i <= 0 || VARSON.indexOf(stringBuffer.charAt(i - 1)) < 0) && isVowel(stringBuffer, i + 1))) {
                            stringBuffer2.append('H');
                            break;
                        }
                    case 'K':
                        if (i > 0) {
                            if (!isPreviousChar(stringBuffer, i, 'C')) {
                                stringBuffer2.append(charAt);
                                break;
                            }
                        }
                        stringBuffer2.append(charAt);
                        break;
                        break;
                    case ExifTagConstants.FLASH_VALUE_OFF_RED_EYE_REDUCTION /*80*/:
                        if (!isNextChar(stringBuffer, i, 'H')) {
                            stringBuffer2.append(charAt);
                            break;
                        }
                        stringBuffer2.append('F');
                        break;
                    case 'Q':
                        stringBuffer2.append('K');
                        break;
                    case 'S':
                        if (!regionMatch(stringBuffer, i, "SH") && !regionMatch(stringBuffer, i, "SIO") && !regionMatch(stringBuffer, i, "SIA")) {
                            stringBuffer2.append('S');
                            break;
                        }
                        stringBuffer2.append('X');
                        break;
                        break;
                    case 'T':
                        if (!regionMatch(stringBuffer, i, "TIA") && !regionMatch(stringBuffer, i, "TIO")) {
                            if (!regionMatch(stringBuffer, i, "TCH")) {
                                if (!regionMatch(stringBuffer, i, "TH")) {
                                    stringBuffer2.append('T');
                                    break;
                                }
                                stringBuffer2.append('0');
                                break;
                            }
                        }
                        stringBuffer2.append('X');
                        break;
                        break;
                    case 'V':
                        stringBuffer2.append('F');
                        break;
                    case 'W':
                    case ExifTagConstants.FLASH_VALUE_AUTO_FIRED_RED_EYE_REDUCTION /*89*/:
                        if (!isLastChar(length, i) && isVowel(stringBuffer, i + 1)) {
                            stringBuffer2.append(charAt);
                            break;
                        }
                    case ExifTagConstants.FLASH_VALUE_AUTO_DID_NOT_FIRE_RED_EYE_REDUCTION /*88*/:
                        stringBuffer2.append('K');
                        stringBuffer2.append('S');
                        break;
                    case 'Z':
                        stringBuffer2.append('S');
                        break;
                }
                i++;
            } else {
                i++;
            }
            if (stringBuffer2.length() > getMaxCodeLen()) {
                stringBuffer2.setLength(getMaxCodeLen());
            }
        }
        return stringBuffer2.toString();
    }

    public void setMaxCodeLen(int i) {
        this.maxCodeLen = i;
    }
}
