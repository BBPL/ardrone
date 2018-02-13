package com.google.api.client.auth.oauth2;

import com.google.api.client.auth.oauth2.Credential.AccessMethod;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.UrlEncodedContent;
import com.google.api.client.util.Data;
import com.google.api.client.util.Preconditions;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class BearerToken {
    static final String PARAM_NAME = "access_token";

    static final class AuthorizationHeaderAccessMethod implements AccessMethod {
        static final String HEADER_PREFIX = "Bearer ";

        AuthorizationHeaderAccessMethod() {
        }

        public String getAccessTokenFromRequest(HttpRequest httpRequest) {
            List<String> authorizationAsList = httpRequest.getHeaders().getAuthorizationAsList();
            if (authorizationAsList != null) {
                for (String str : authorizationAsList) {
                    if (str.startsWith(HEADER_PREFIX)) {
                        return str.substring(HEADER_PREFIX.length());
                    }
                }
            }
            return null;
        }

        public void intercept(HttpRequest httpRequest, String str) throws IOException {
            httpRequest.getHeaders().setAuthorization(HEADER_PREFIX + str);
        }
    }

    static final class FormEncodedBodyAccessMethod implements AccessMethod {
        FormEncodedBodyAccessMethod() {
        }

        private static Map<String, Object> getData(HttpRequest httpRequest) {
            return Data.mapOf(UrlEncodedContent.getContent(httpRequest).getData());
        }

        public String getAccessTokenFromRequest(HttpRequest httpRequest) {
            Object obj = getData(httpRequest).get(BearerToken.PARAM_NAME);
            return obj == null ? null : obj.toString();
        }

        public void intercept(HttpRequest httpRequest, String str) throws IOException {
            Preconditions.checkArgument(!"GET".equals(httpRequest.getRequestMethod()), "HTTP GET method is not supported");
            getData(httpRequest).put(BearerToken.PARAM_NAME, str);
        }
    }

    static final class QueryParameterAccessMethod implements AccessMethod {
        QueryParameterAccessMethod() {
        }

        public String getAccessTokenFromRequest(HttpRequest httpRequest) {
            Object obj = httpRequest.getUrl().get(BearerToken.PARAM_NAME);
            return obj == null ? null : obj.toString();
        }

        public void intercept(HttpRequest httpRequest, String str) throws IOException {
            httpRequest.getUrl().set(BearerToken.PARAM_NAME, (Object) str);
        }
    }

    public static AccessMethod authorizationHeaderAccessMethod() {
        return new AuthorizationHeaderAccessMethod();
    }

    public static AccessMethod formEncodedBodyAccessMethod() {
        return new FormEncodedBodyAccessMethod();
    }

    public static AccessMethod queryParameterAccessMethod() {
        return new QueryParameterAccessMethod();
    }
}
