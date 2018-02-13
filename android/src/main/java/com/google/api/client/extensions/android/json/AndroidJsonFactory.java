package com.google.api.client.extensions.android.json;

import android.annotation.TargetApi;
import android.util.JsonReader;
import android.util.JsonWriter;
import com.google.api.client.extensions.android.AndroidUtils;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonGenerator;
import com.google.api.client.json.JsonParser;
import com.google.api.client.util.Charsets;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.nio.charset.Charset;

@TargetApi(11)
public class AndroidJsonFactory extends JsonFactory {
    public AndroidJsonFactory() {
        AndroidUtils.checkMinimumSdkLevel(11);
    }

    public JsonGenerator createJsonGenerator(OutputStream outputStream, Charset charset) {
        return createJsonGenerator(new OutputStreamWriter(outputStream, charset));
    }

    public JsonGenerator createJsonGenerator(Writer writer) {
        return new AndroidJsonGenerator(this, new JsonWriter(writer));
    }

    public JsonParser createJsonParser(InputStream inputStream) {
        return createJsonParser(new InputStreamReader(inputStream, Charsets.UTF_8));
    }

    public JsonParser createJsonParser(InputStream inputStream, Charset charset) {
        return charset == null ? createJsonParser(inputStream) : createJsonParser(new InputStreamReader(inputStream, charset));
    }

    public JsonParser createJsonParser(Reader reader) {
        return new AndroidJsonParser(this, new JsonReader(reader));
    }

    public JsonParser createJsonParser(String str) {
        return createJsonParser(new StringReader(str));
    }
}
