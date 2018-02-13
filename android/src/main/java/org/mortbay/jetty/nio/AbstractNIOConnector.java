package org.mortbay.jetty.nio;

import org.mortbay.io.Buffer;
import org.mortbay.io.nio.DirectNIOBuffer;
import org.mortbay.io.nio.IndirectNIOBuffer;
import org.mortbay.jetty.AbstractConnector;

public abstract class AbstractNIOConnector extends AbstractConnector implements NIOConnector {
    private boolean _useDirectBuffers = true;

    public boolean getUseDirectBuffers() {
        return this._useDirectBuffers;
    }

    protected Buffer newBuffer(int i) {
        return i == getHeaderBufferSize() ? new IndirectNIOBuffer(i) : this._useDirectBuffers ? new DirectNIOBuffer(i) : new IndirectNIOBuffer(i);
    }

    public void setUseDirectBuffers(boolean z) {
        this._useDirectBuffers = z;
    }
}
