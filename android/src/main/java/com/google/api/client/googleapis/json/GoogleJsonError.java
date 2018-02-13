package com.google.api.client.googleapis.json;

import com.google.api.client.http.HttpResponse;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser.Builder;
import com.google.api.client.util.Data;
import com.google.api.client.util.Key;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class GoogleJsonError extends GenericJson {
    @Key
    private int code;
    @Key
    private List<ErrorInfo> errors;
    @Key
    private String message;

    public static class ErrorInfo extends GenericJson {
        @Key
        private String domain;
        @Key
        private String location;
        @Key
        private String locationType;
        @Key
        private String message;
        @Key
        private String reason;

        public ErrorInfo clone() {
            return (ErrorInfo) super.clone();
        }

        public final String getDomain() {
            return this.domain;
        }

        public final String getLocation() {
            return this.location;
        }

        public final String getLocationType() {
            return this.locationType;
        }

        public final String getMessage() {
            return this.message;
        }

        public final String getReason() {
            return this.reason;
        }

        public ErrorInfo set(String str, Object obj) {
            return (ErrorInfo) super.set(str, obj);
        }

        public final void setDomain(String str) {
            this.domain = str;
        }

        public final void setLocation(String str) {
            this.location = str;
        }

        public final void setLocationType(String str) {
            this.locationType = str;
        }

        public final void setMessage(String str) {
            this.message = str;
        }

        public final void setReason(String str) {
            this.reason = str;
        }
    }

    static {
        Data.nullOf(ErrorInfo.class);
    }

    public static GoogleJsonError parse(JsonFactory jsonFactory, HttpResponse httpResponse) throws IOException {
        return (GoogleJsonError) new Builder(jsonFactory).setWrapperKeys(Collections.singleton("error")).build().parseAndClose(httpResponse.getContent(), httpResponse.getContentCharset(), GoogleJsonError.class);
    }

    public GoogleJsonError clone() {
        return (GoogleJsonError) super.clone();
    }

    public final int getCode() {
        return this.code;
    }

    public final List<ErrorInfo> getErrors() {
        return this.errors;
    }

    public final String getMessage() {
        return this.message;
    }

    public GoogleJsonError set(String str, Object obj) {
        return (GoogleJsonError) super.set(str, obj);
    }

    public final void setCode(int i) {
        this.code = i;
    }

    public final void setErrors(List<ErrorInfo> list) {
        this.errors = list;
    }

    public final void setMessage(String str) {
        this.message = str;
    }
}
