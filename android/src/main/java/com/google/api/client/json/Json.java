package com.google.api.client.json;

import com.google.api.client.http.HttpMediaType;
import com.google.api.client.util.Charsets;

public class Json {
    public static final String MEDIA_TYPE = new HttpMediaType("application/json").setCharsetParameter(Charsets.UTF_8).build();
}
