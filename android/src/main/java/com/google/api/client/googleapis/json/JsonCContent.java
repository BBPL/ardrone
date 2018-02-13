package com.google.api.client.googleapis.json;

import com.google.api.client.http.HttpMediaType;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonGenerator;
import com.sony.rdis.receiver.ServiceComuncationProtocol;
import java.io.IOException;
import java.io.OutputStream;

@Deprecated
public final class JsonCContent extends JsonHttpContent {
    public JsonCContent(JsonFactory jsonFactory, Object obj) {
        super(jsonFactory, obj);
    }

    public JsonCContent setMediaType(HttpMediaType httpMediaType) {
        super.setMediaType(httpMediaType);
        return this;
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        JsonGenerator createJsonGenerator = getJsonFactory().createJsonGenerator(outputStream, getCharset());
        createJsonGenerator.writeStartObject();
        createJsonGenerator.writeFieldName(ServiceComuncationProtocol.DATA);
        createJsonGenerator.serialize(getData());
        createJsonGenerator.writeEndObject();
        createJsonGenerator.flush();
    }
}
