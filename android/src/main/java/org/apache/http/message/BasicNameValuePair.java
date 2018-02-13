package org.apache.http.message;

import java.io.Serializable;
import org.apache.http.NameValuePair;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.LangUtils;

@Immutable
public class BasicNameValuePair implements NameValuePair, Cloneable, Serializable {
    private static final long serialVersionUID = -6437800749411518984L;
    private final String name;
    private final String value;

    public BasicNameValuePair(String str, String str2) {
        if (str == null) {
            throw new IllegalArgumentException("Name may not be null");
        }
        this.name = str;
        this.value = str2;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public boolean equals(Object obj) {
        if (this != obj) {
            if (!(obj instanceof NameValuePair)) {
                return false;
            }
            BasicNameValuePair basicNameValuePair = (BasicNameValuePair) obj;
            if (!this.name.equals(basicNameValuePair.name)) {
                return false;
            }
            if (!LangUtils.equals(this.value, basicNameValuePair.value)) {
                return false;
            }
        }
        return true;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public int hashCode() {
        return LangUtils.hashCode(LangUtils.hashCode(17, this.name), this.value);
    }

    public String toString() {
        if (this.value == null) {
            return this.name;
        }
        StringBuilder stringBuilder = new StringBuilder((this.name.length() + 1) + this.value.length());
        stringBuilder.append(this.name);
        stringBuilder.append("=");
        stringBuilder.append(this.value);
        return stringBuilder.toString();
    }
}
