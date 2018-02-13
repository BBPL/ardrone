package org.apache.http.message;

import java.util.NoSuchElementException;
import org.apache.http.HeaderIterator;
import org.apache.http.ParseException;
import org.apache.http.TokenIterator;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
public class BasicTokenIterator implements TokenIterator {
    public static final String HTTP_SEPARATORS = " ,;=()<>@:\\\"/[]?{}\t";
    protected String currentHeader;
    protected String currentToken;
    protected final HeaderIterator headerIt;
    protected int searchPos;

    public BasicTokenIterator(HeaderIterator headerIterator) {
        if (headerIterator == null) {
            throw new IllegalArgumentException("Header iterator must not be null.");
        }
        this.headerIt = headerIterator;
        this.searchPos = findNext(-1);
    }

    protected String createToken(String str, int i, int i2) {
        return str.substring(i, i2);
    }

    protected int findNext(int i) throws ParseException {
        int findTokenSeparator;
        if (i >= 0) {
            findTokenSeparator = findTokenSeparator(i);
        } else if (!this.headerIt.hasNext()) {
            return -1;
        } else {
            this.currentHeader = this.headerIt.nextHeader().getValue();
            findTokenSeparator = 0;
        }
        int findTokenStart = findTokenStart(findTokenSeparator);
        if (findTokenStart < 0) {
            this.currentToken = null;
            return -1;
        }
        findTokenSeparator = findTokenEnd(findTokenStart);
        this.currentToken = createToken(this.currentHeader, findTokenStart, findTokenSeparator);
        return findTokenSeparator;
    }

    protected int findTokenEnd(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("Token start position must not be negative: " + i);
        }
        int length = this.currentHeader.length();
        int i2 = i + 1;
        while (i2 < length && isTokenChar(this.currentHeader.charAt(i2))) {
            i2++;
        }
        return i2;
    }

    protected int findTokenSeparator(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("Search position must not be negative: " + i);
        }
        Object obj = null;
        int length = this.currentHeader.length();
        while (obj == null && i < length) {
            char charAt = this.currentHeader.charAt(i);
            if (isTokenSeparator(charAt)) {
                obj = 1;
            } else if (isWhitespace(charAt)) {
                i++;
            } else if (isTokenChar(charAt)) {
                throw new ParseException("Tokens without separator (pos " + i + "): " + this.currentHeader);
            } else {
                throw new ParseException("Invalid character after token (pos " + i + "): " + this.currentHeader);
            }
        }
        return i;
    }

    protected int findTokenStart(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("Search position must not be negative: " + i);
        }
        Object obj = null;
        int i2 = i;
        while (obj == null && this.currentHeader != null) {
            int length = this.currentHeader.length();
            while (obj == null && i2 < length) {
                char charAt = this.currentHeader.charAt(i2);
                if (isTokenSeparator(charAt) || isWhitespace(charAt)) {
                    i2++;
                } else if (isTokenChar(this.currentHeader.charAt(i2))) {
                    obj = 1;
                } else {
                    throw new ParseException("Invalid character before token (pos " + i2 + "): " + this.currentHeader);
                }
            }
            if (obj == null) {
                if (this.headerIt.hasNext()) {
                    this.currentHeader = this.headerIt.nextHeader().getValue();
                    i2 = 0;
                } else {
                    this.currentHeader = null;
                }
            }
        }
        return obj != null ? i2 : -1;
    }

    public boolean hasNext() {
        return this.currentToken != null;
    }

    protected boolean isHttpSeparator(char c) {
        return HTTP_SEPARATORS.indexOf(c) >= 0;
    }

    protected boolean isTokenChar(char c) {
        if (!Character.isLetterOrDigit(c)) {
            if (Character.isISOControl(c)) {
                return false;
            }
            if (isHttpSeparator(c)) {
                return false;
            }
        }
        return true;
    }

    protected boolean isTokenSeparator(char c) {
        return c == ',';
    }

    protected boolean isWhitespace(char c) {
        return c == '\t' || Character.isSpaceChar(c);
    }

    public final Object next() throws NoSuchElementException, ParseException {
        return nextToken();
    }

    public String nextToken() throws NoSuchElementException, ParseException {
        if (this.currentToken == null) {
            throw new NoSuchElementException("Iteration already finished.");
        }
        String str = this.currentToken;
        this.searchPos = findNext(this.searchPos);
        return str;
    }

    public final void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Removing tokens is not supported.");
    }
}
