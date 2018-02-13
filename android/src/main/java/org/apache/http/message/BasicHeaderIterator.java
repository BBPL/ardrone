package org.apache.http.message;

import java.util.NoSuchElementException;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
public class BasicHeaderIterator implements HeaderIterator {
    protected final Header[] allHeaders;
    protected int currentIndex;
    protected String headerName;

    public BasicHeaderIterator(Header[] headerArr, String str) {
        if (headerArr == null) {
            throw new IllegalArgumentException("Header array must not be null.");
        }
        this.allHeaders = headerArr;
        this.headerName = str;
        this.currentIndex = findNext(-1);
    }

    protected boolean filterHeader(int i) {
        return this.headerName == null || this.headerName.equalsIgnoreCase(this.allHeaders[i].getName());
    }

    protected int findNext(int i) {
        if (i < -1) {
            return -1;
        }
        int length = this.allHeaders.length;
        boolean z = false;
        int i2 = i;
        while (!z && i2 < length - 1) {
            i = i2 + 1;
            z = filterHeader(i);
            i2 = i;
        }
        return z ? i2 : -1;
    }

    public boolean hasNext() {
        return this.currentIndex >= 0;
    }

    public final Object next() throws NoSuchElementException {
        return nextHeader();
    }

    public Header nextHeader() throws NoSuchElementException {
        int i = this.currentIndex;
        if (i < 0) {
            throw new NoSuchElementException("Iteration already finished.");
        }
        this.currentIndex = findNext(i);
        return this.allHeaders[i];
    }

    public void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Removing headers is not supported.");
    }
}
