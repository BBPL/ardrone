package org.apache.http.message;

import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.util.LangUtils;

@NotThreadSafe
public class BasicHeaderElement implements HeaderElement, Cloneable {
    private final String name;
    private final NameValuePair[] parameters;
    private final String value;

    public BasicHeaderElement(String str, String str2) {
        this(str, str2, null);
    }

    public BasicHeaderElement(String str, String str2, NameValuePair[] nameValuePairArr) {
        if (str == null) {
            throw new IllegalArgumentException("Name may not be null");
        }
        this.name = str;
        this.value = str2;
        if (nameValuePairArr != null) {
            this.parameters = nameValuePairArr;
        } else {
            this.parameters = new NameValuePair[0];
        }
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public boolean equals(Object obj) {
        if (this != obj) {
            if (!(obj instanceof HeaderElement)) {
                return false;
            }
            BasicHeaderElement basicHeaderElement = (BasicHeaderElement) obj;
            if (!this.name.equals(basicHeaderElement.name) || !LangUtils.equals(this.value, basicHeaderElement.value)) {
                return false;
            }
            if (!LangUtils.equals(this.parameters, basicHeaderElement.parameters)) {
                return false;
            }
        }
        return true;
    }

    public String getName() {
        return this.name;
    }

    public NameValuePair getParameter(int i) {
        return this.parameters[i];
    }

    public NameValuePair getParameterByName(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Name may not be null");
        }
        for (NameValuePair nameValuePair : this.parameters) {
            if (nameValuePair.getName().equalsIgnoreCase(str)) {
                return nameValuePair;
            }
        }
        return null;
    }

    public int getParameterCount() {
        return this.parameters.length;
    }

    public NameValuePair[] getParameters() {
        return (NameValuePair[]) this.parameters.clone();
    }

    public String getValue() {
        return this.value;
    }

    public int hashCode() {
        int hashCode = LangUtils.hashCode(LangUtils.hashCode(17, this.name), this.value);
        for (Object hashCode2 : this.parameters) {
            hashCode = LangUtils.hashCode(hashCode, hashCode2);
        }
        return hashCode;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.name);
        if (this.value != null) {
            stringBuilder.append("=");
            stringBuilder.append(this.value);
        }
        for (Object append : this.parameters) {
            stringBuilder.append("; ");
            stringBuilder.append(append);
        }
        return stringBuilder.toString();
    }
}
