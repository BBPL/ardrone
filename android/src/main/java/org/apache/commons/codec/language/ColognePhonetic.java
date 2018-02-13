package org.apache.commons.codec.language;

import java.util.Locale;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class ColognePhonetic implements StringEncoder {
    private static final char[][] PREPROCESS_MAP;

    private abstract class CologneBuffer {
        protected final char[] data;
        protected int length = 0;

        public CologneBuffer(int i) {
            this.data = new char[i];
            this.length = 0;
        }

        public CologneBuffer(char[] cArr) {
            this.data = cArr;
            this.length = cArr.length;
        }

        protected abstract char[] copyData(int i, int i2);

        public int length() {
            return this.length;
        }

        public String toString() {
            return new String(copyData(0, this.length));
        }
    }

    private class CologneInputBuffer extends CologneBuffer {
        public CologneInputBuffer(char[] cArr) {
            super(cArr);
        }

        public void addLeft(char c) {
            this.length++;
            this.data[getNextPos()] = c;
        }

        protected char[] copyData(int i, int i2) {
            Object obj = new char[i2];
            System.arraycopy(this.data, (this.data.length - this.length) + i, obj, 0, i2);
            return obj;
        }

        public char getNextChar() {
            return this.data[getNextPos()];
        }

        protected int getNextPos() {
            return this.data.length - this.length;
        }

        public char removeNext() {
            char nextChar = getNextChar();
            this.length--;
            return nextChar;
        }
    }

    private class CologneOutputBuffer extends CologneBuffer {
        public CologneOutputBuffer(int i) {
            super(i);
        }

        public void addRight(char c) {
            this.data[this.length] = c;
            this.length++;
        }

        protected char[] copyData(int i, int i2) {
            Object obj = new char[i2];
            System.arraycopy(this.data, i, obj, 0, i2);
            return obj;
        }
    }

    static {
        char[] cArr = new char[]{'ß', 'S'};
        PREPROCESS_MAP = new char[][]{new char[]{'Ä', 'A'}, new char[]{'Ü', 'U'}, new char[]{'Ö', 'O'}, cArr};
    }

    private static boolean arrayContains(char[] cArr, char c) {
        for (char c2 : cArr) {
            if (c2 == c) {
                return true;
            }
        }
        return false;
    }

    private String preprocess(String str) {
        char[] toCharArray = str.toUpperCase(Locale.GERMAN).toCharArray();
        for (int i = 0; i < toCharArray.length; i++) {
            if (toCharArray[i] > 'Z') {
                for (char[] cArr : PREPROCESS_MAP) {
                    if (toCharArray[i] == cArr[0]) {
                        toCharArray[i] = cArr[1];
                        break;
                    }
                }
            }
        }
        return new String(toCharArray);
    }

    public String colognePhonetic(String str) {
        if (str == null) {
            return null;
        }
        String preprocess = preprocess(str);
        CologneOutputBuffer cologneOutputBuffer = new CologneOutputBuffer(preprocess.length() * 2);
        CologneInputBuffer cologneInputBuffer = new CologneInputBuffer(preprocess.toCharArray());
        int length = cologneInputBuffer.length();
        char c = '/';
        char c2 = '-';
        while (length > 0) {
            char c3;
            int i;
            char removeNext = cologneInputBuffer.removeNext();
            length = cologneInputBuffer.length();
            char nextChar = length > 0 ? cologneInputBuffer.getNextChar() : '-';
            int i2;
            if (arrayContains(new char[]{'A', 'E', 'I', 'J', 'O', 'U', 'Y'}, removeNext)) {
                i2 = length;
                c3 = '0';
                i = i2;
            } else if (removeNext == 'H' || removeNext < 'A' || removeNext > 'Z') {
                if (c != '/') {
                    i = length;
                    c3 = '-';
                }
            } else if (removeNext == 'B' || (removeNext == 'P' && nextChar != 'H')) {
                i2 = length;
                c3 = '1';
                i = i2;
            } else if ((removeNext == 'D' || removeNext == 'T') && !arrayContains(new char[]{'S', 'C', 'Z'}, nextChar)) {
                i2 = length;
                c3 = '2';
                i = i2;
            } else if (arrayContains(new char[]{'W', 'F', 'P', 'V'}, removeNext)) {
                i2 = length;
                c3 = '3';
                i = i2;
            } else if (arrayContains(new char[]{'G', 'K', 'Q'}, removeNext)) {
                i = length;
                c3 = '4';
            } else if (removeNext == 'X' && !arrayContains(new char[]{'C', 'K', 'Q'}, c2)) {
                cologneInputBuffer.addLeft('S');
                i = length + 1;
                c3 = '4';
            } else if (removeNext == 'S' || removeNext == 'Z') {
                i = length;
                c3 = '8';
            } else if (removeNext == 'C') {
                if (c == '/') {
                    if (arrayContains(new char[]{'A', 'H', 'K', 'L', 'O', 'Q', 'R', 'U', 'X'}, nextChar)) {
                        i = length;
                        c3 = '4';
                    } else {
                        i = length;
                        c3 = '8';
                    }
                } else if (arrayContains(new char[]{'S', 'Z'}, c2) || !arrayContains(new char[]{'A', 'H', 'O', 'U', 'K', 'Q', 'X'}, nextChar)) {
                    i = length;
                    c3 = '8';
                } else {
                    i = length;
                    c3 = '4';
                }
            } else if (arrayContains(new char[]{'T', 'D', 'X'}, removeNext)) {
                i = length;
                c3 = '8';
            } else if (removeNext == 'R') {
                i2 = length;
                c3 = '7';
                i = i2;
            } else if (removeNext == 'L') {
                i2 = length;
                c3 = '5';
                i = i2;
            } else if (removeNext == 'M' || removeNext == 'N') {
                i2 = length;
                c3 = '6';
                i = i2;
            } else {
                i = length;
                c3 = removeNext;
            }
            if (c3 != '-' && ((c != c3 && (c3 != '0' || c == '/')) || c3 < '0' || c3 > '8')) {
                cologneOutputBuffer.addRight(c3);
            }
            c = c3;
            c2 = removeNext;
            length = i;
        }
        return cologneOutputBuffer.toString();
    }

    public Object encode(Object obj) throws EncoderException {
        if (obj instanceof String) {
            return encode((String) obj);
        }
        throw new EncoderException("This method's parameter was expected to be of the type " + String.class.getName() + ". But actually it was of the type " + obj.getClass().getName() + ".");
    }

    public String encode(String str) {
        return colognePhonetic(str);
    }

    public boolean isEncodeEqual(String str, String str2) {
        return colognePhonetic(str).equals(colognePhonetic(str2));
    }
}
