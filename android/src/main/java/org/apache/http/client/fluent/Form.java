package org.apache.http.client.fluent;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class Form {
    private final List<NameValuePair> params = new ArrayList();

    Form() {
    }

    public static Form form() {
        return new Form();
    }

    public Form add(String str, String str2) {
        this.params.add(new BasicNameValuePair(str, str2));
        return this;
    }

    public List<NameValuePair> build() {
        return new ArrayList(this.params);
    }
}
