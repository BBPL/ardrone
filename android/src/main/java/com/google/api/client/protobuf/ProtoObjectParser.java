package com.google.api.client.protobuf;

import com.google.api.client.util.ObjectParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

public class ProtoObjectParser implements ObjectParser {
    public <T> T parseAndClose(InputStream inputStream, Charset charset, Class<T> cls) throws IOException {
        return ProtocolBuffers.parseAndClose(inputStream, cls);
    }

    public Object parseAndClose(InputStream inputStream, Charset charset, Type type) throws IOException {
        if (type instanceof Class) {
            return parseAndClose(inputStream, charset, (Class) type);
        }
        throw new UnsupportedOperationException("dataType must be of Class<? extends MessageList>");
    }

    public <T> T parseAndClose(Reader reader, Class<T> cls) throws IOException {
        throw new UnsupportedOperationException("protocol buffers must be read from a binary stream");
    }

    public Object parseAndClose(Reader reader, Type type) throws IOException {
        throw new UnsupportedOperationException("protocol buffers must be read from a binary stream");
    }
}
