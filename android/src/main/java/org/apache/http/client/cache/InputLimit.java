package org.apache.http.client.cache;

import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
public class InputLimit {
    private boolean reached = false;
    private final long value;

    public InputLimit(long j) {
        this.value = j;
    }

    public long getValue() {
        return this.value;
    }

    public boolean isReached() {
        return this.reached;
    }

    public void reached() {
        this.reached = true;
    }
}
