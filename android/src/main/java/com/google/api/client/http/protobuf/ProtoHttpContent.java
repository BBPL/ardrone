package com.google.api.client.http.protobuf;

import com.google.api.client.http.AbstractHttpContent;
import com.google.api.client.http.HttpMediaType;
import com.google.api.client.protobuf.ProtocolBuffers;
import com.google.api.client.util.Preconditions;
import com.google.protobuf.MessageLite;
import java.io.IOException;
import java.io.OutputStream;

public class ProtoHttpContent extends AbstractHttpContent {
    private final MessageLite message;
    private String type = ProtocolBuffers.CONTENT_TYPE;

    public ProtoHttpContent(MessageLite messageLite) {
        super(ProtocolBuffers.CONTENT_TYPE);
        this.message = (MessageLite) Preconditions.checkNotNull(messageLite);
    }

    public long getLength() throws IOException {
        return (long) this.message.getSerializedSize();
    }

    public final MessageLite getMessage() {
        return this.message;
    }

    public String getType() {
        return this.type;
    }

    public ProtoHttpContent setMediaType(HttpMediaType httpMediaType) {
        super.setMediaType(httpMediaType);
        return this;
    }

    public ProtoHttpContent setType(String str) {
        this.type = str;
        return this;
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        this.message.writeTo(outputStream);
        outputStream.flush();
    }
}
