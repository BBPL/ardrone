package com.google.api.client.protobuf;

import com.google.api.client.util.Throwables;
import com.google.protobuf.MessageLite;
import java.io.IOException;
import java.io.InputStream;

public class ProtocolBuffers {
    public static final String ALT_CONTENT_TYPE = "application/x-protobuffer";
    public static final String CONTENT_TYPE = "application/x-protobuf";

    private ProtocolBuffers() {
    }

    public static <T extends MessageLite> T parseAndClose(InputStream inputStream, Class<T> cls) throws IOException {
        try {
            MessageLite messageLite = (MessageLite) cls.cast(cls.getDeclaredMethod("parseFrom", new Class[]{InputStream.class}).invoke(null, new Object[]{inputStream}));
            inputStream.close();
            return messageLite;
        } catch (Throwable e) {
            Throwables.propagateIfPossible(e, IOException.class);
            IOException iOException = new IOException("Error parsing message of type " + cls);
            iOException.initCause(e);
            throw iOException;
        } catch (Throwable th) {
            inputStream.close();
        }
    }
}
