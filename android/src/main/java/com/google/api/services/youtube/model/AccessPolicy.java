package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import java.util.List;

public final class AccessPolicy extends GenericJson {
    @Key
    private Boolean allowed;
    @Key
    private List<String> exception;

    public AccessPolicy clone() {
        return (AccessPolicy) super.clone();
    }

    public Boolean getAllowed() {
        return this.allowed;
    }

    public List<String> getException() {
        return this.exception;
    }

    public AccessPolicy set(String str, Object obj) {
        return (AccessPolicy) super.set(str, obj);
    }

    public AccessPolicy setAllowed(Boolean bool) {
        this.allowed = bool;
        return this;
    }

    public AccessPolicy setException(List<String> list) {
        this.exception = list;
        return this;
    }
}
