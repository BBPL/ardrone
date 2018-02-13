package com.google.api.client.json.rpc2;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;

public class JsonRpcRequest extends GenericData {
    @Key
    private Object id;
    @Key
    private final String jsonrpc = "2.0";
    @Key
    private String method;
    @Key
    private Object params;

    public JsonRpcRequest clone() {
        return (JsonRpcRequest) super.clone();
    }

    public Object getId() {
        return this.id;
    }

    public String getMethod() {
        return this.method;
    }

    public Object getParameters() {
        return this.params;
    }

    public String getVersion() {
        return "2.0";
    }

    public JsonRpcRequest set(String str, Object obj) {
        return (JsonRpcRequest) super.set(str, obj);
    }

    public void setId(Object obj) {
        this.id = obj;
    }

    public void setMethod(String str) {
        this.method = str;
    }

    public void setParameters(Object obj) {
        this.params = obj;
    }
}
