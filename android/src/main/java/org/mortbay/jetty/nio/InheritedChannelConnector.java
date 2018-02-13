package org.mortbay.jetty.nio;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.channels.Channel;
import java.nio.channels.ServerSocketChannel;
import org.mortbay.log.Log;

public class InheritedChannelConnector extends SelectChannelConnector {
    static Class class$java$lang$System;

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (Throwable e) {
            throw new NoClassDefFoundError().initCause(e);
        }
    }

    public void open() throws IOException {
        synchronized (this) {
            try {
                Class class$;
                if (class$java$lang$System == null) {
                    class$ = class$("java.lang.System");
                    class$java$lang$System = class$;
                } else {
                    class$ = class$java$lang$System;
                }
                Method method = class$.getMethod("inheritedChannel", null);
                if (method != null) {
                    Channel channel = (Channel) method.invoke(null, null);
                    if (channel instanceof ServerSocketChannel) {
                        this._acceptChannel = (ServerSocketChannel) channel;
                    }
                }
                if (this._acceptChannel != null) {
                    this._acceptChannel.configureBlocking(false);
                }
            } catch (Throwable e) {
                Log.warn(e);
            }
            if (this._acceptChannel == null) {
                super.open();
            } else {
                throw new IOException("No System.inheritedChannel()");
            }
        }
    }
}
