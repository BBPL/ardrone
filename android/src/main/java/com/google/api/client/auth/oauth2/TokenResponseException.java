package com.google.api.client.auth.oauth2;

import com.google.api.client.http.HttpMediaType;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpResponseException.Builder;
import com.google.api.client.json.Json;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.StringUtils;
import com.google.api.client.util.Strings;
import java.io.IOException;

public class TokenResponseException extends HttpResponseException {
    private static final long serialVersionUID = 4020689092957439244L;
    private final transient TokenErrorResponse details;

    TokenResponseException(Builder builder, TokenErrorResponse tokenErrorResponse) {
        super(builder);
        this.details = tokenErrorResponse;
    }

    public static TokenResponseException from(JsonFactory jsonFactory, HttpResponse httpResponse) {
        StringBuilder computeMessageBuffer;
        TokenErrorResponse tokenErrorResponse;
        IOException e;
        String str = null;
        Builder builder = new Builder(httpResponse.getStatusCode(), httpResponse.getStatusMessage(), httpResponse.getHeaders());
        Preconditions.checkNotNull(jsonFactory);
        String contentType = httpResponse.getContentType();
        try {
            if (httpResponse.isSuccessStatusCode() || contentType == null || !HttpMediaType.equalsIgnoreParameters(Json.MEDIA_TYPE, contentType)) {
                String str2 = str;
                str = httpResponse.parseAsString();
                Object obj = str2;
                computeMessageBuffer = HttpResponseException.computeMessageBuffer(httpResponse);
                if (!Strings.isNullOrEmpty(str)) {
                    computeMessageBuffer.append(StringUtils.LINE_SEPARATOR).append(str);
                    builder.setContent(str);
                }
                builder.setMessage(computeMessageBuffer.toString());
                return new TokenResponseException(builder, tokenErrorResponse);
            }
            tokenErrorResponse = (TokenErrorResponse) new JsonObjectParser(jsonFactory).parseAndClose(httpResponse.getContent(), httpResponse.getContentCharset(), TokenErrorResponse.class);
            try {
                str = tokenErrorResponse.toPrettyString();
            } catch (IOException e2) {
                e = e2;
                e.printStackTrace();
                computeMessageBuffer = HttpResponseException.computeMessageBuffer(httpResponse);
                if (Strings.isNullOrEmpty(str)) {
                    computeMessageBuffer.append(StringUtils.LINE_SEPARATOR).append(str);
                    builder.setContent(str);
                }
                builder.setMessage(computeMessageBuffer.toString());
                return new TokenResponseException(builder, tokenErrorResponse);
            }
            computeMessageBuffer = HttpResponseException.computeMessageBuffer(httpResponse);
            if (Strings.isNullOrEmpty(str)) {
                computeMessageBuffer.append(StringUtils.LINE_SEPARATOR).append(str);
                builder.setContent(str);
            }
            builder.setMessage(computeMessageBuffer.toString());
            return new TokenResponseException(builder, tokenErrorResponse);
        } catch (IOException e3) {
            e = e3;
            tokenErrorResponse = str;
            e.printStackTrace();
            computeMessageBuffer = HttpResponseException.computeMessageBuffer(httpResponse);
            if (Strings.isNullOrEmpty(str)) {
                computeMessageBuffer.append(StringUtils.LINE_SEPARATOR).append(str);
                builder.setContent(str);
            }
            builder.setMessage(computeMessageBuffer.toString());
            return new TokenResponseException(builder, tokenErrorResponse);
        }
    }

    public final TokenErrorResponse getDetails() {
        return this.details;
    }
}
