package org.mortbay.io;

import java.io.IOException;

public interface Connection {
    void handle() throws IOException;

    boolean isIdle();
}
