package com.google.api.client.testing.http;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.util.Preconditions;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

public class MockHttpTransport extends HttpTransport {
    private Set<String> supportedMethods;

    public static class Builder {
        Set<String> supportedMethods;

        protected Builder() {
        }

        public MockHttpTransport build() {
            return new MockHttpTransport(this);
        }

        public final Set<String> getSupportedMethods() {
            return this.supportedMethods;
        }

        public final Builder setSupportedMethods(Set<String> set) {
            this.supportedMethods = set;
            return this;
        }
    }

    protected MockHttpTransport(Builder builder) {
        this.supportedMethods = builder.supportedMethods;
    }

    @Deprecated
    protected MockHttpTransport(Set<String> set) {
        this.supportedMethods = set;
    }

    public static Builder builder() {
        return new Builder();
    }

    public LowLevelHttpRequest buildRequest(String str, String str2) throws IOException {
        Preconditions.checkArgument(supportsMethod(str), "HTTP method %s not supported", str);
        return new MockLowLevelHttpRequest(str2);
    }

    public final Set<String> getSupportedMethods() {
        return this.supportedMethods == null ? null : Collections.unmodifiableSet(this.supportedMethods);
    }

    public boolean supportsMethod(String str) throws IOException {
        return this.supportedMethods == null || this.supportedMethods.contains(str);
    }
}
