package com.google.api.client.extensions.java6.auth.oauth2;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import com.google.api.client.util.Maps;
import com.google.api.client.util.Preconditions;
import java.util.Map;

public class FilePersistedCredentials extends GenericJson {
    @Key
    private Map<String, FilePersistedCredential> credentials = Maps.newHashMap();

    public FilePersistedCredentials clone() {
        return (FilePersistedCredentials) super.clone();
    }

    void delete(String str) {
        Preconditions.checkNotNull(str);
        this.credentials.remove(str);
    }

    boolean load(String str, Credential credential) {
        Preconditions.checkNotNull(str);
        FilePersistedCredential filePersistedCredential = (FilePersistedCredential) this.credentials.get(str);
        if (filePersistedCredential == null) {
            return false;
        }
        filePersistedCredential.load(credential);
        return true;
    }

    public FilePersistedCredentials set(String str, Object obj) {
        return (FilePersistedCredentials) super.set(str, obj);
    }

    void store(String str, Credential credential) {
        Preconditions.checkNotNull(str);
        FilePersistedCredential filePersistedCredential = (FilePersistedCredential) this.credentials.get(str);
        if (filePersistedCredential == null) {
            filePersistedCredential = new FilePersistedCredential();
            this.credentials.put(str, filePersistedCredential);
        }
        filePersistedCredential.store(credential);
    }
}
