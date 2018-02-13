package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public final class PlayerRestrictionDetails extends GenericJson {
    @Key
    private String reason;
    @Key
    private Boolean restricted;
    @Key
    private String restriction;

    public PlayerRestrictionDetails clone() {
        return (PlayerRestrictionDetails) super.clone();
    }

    public String getReason() {
        return this.reason;
    }

    public Boolean getRestricted() {
        return this.restricted;
    }

    public String getRestriction() {
        return this.restriction;
    }

    public PlayerRestrictionDetails set(String str, Object obj) {
        return (PlayerRestrictionDetails) super.set(str, obj);
    }

    public PlayerRestrictionDetails setReason(String str) {
        this.reason = str;
        return this;
    }

    public PlayerRestrictionDetails setRestricted(Boolean bool) {
        this.restricted = bool;
        return this;
    }

    public PlayerRestrictionDetails setRestriction(String str) {
        this.restriction = str;
        return this;
    }
}
