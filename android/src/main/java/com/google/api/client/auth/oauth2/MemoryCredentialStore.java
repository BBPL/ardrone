package com.google.api.client.auth.oauth2;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MemoryCredentialStore implements CredentialStore {
    private final Lock lock = new ReentrantLock();
    private final Map<String, MemoryPersistedCredential> store = new HashMap();

    public void delete(String str, Credential credential) {
        this.lock.lock();
        try {
            this.store.remove(str);
        } finally {
            this.lock.unlock();
        }
    }

    public boolean load(String str, Credential credential) {
        this.lock.lock();
        try {
            MemoryPersistedCredential memoryPersistedCredential = (MemoryPersistedCredential) this.store.get(str);
            if (memoryPersistedCredential != null) {
                memoryPersistedCredential.load(credential);
            }
            boolean z = memoryPersistedCredential != null;
            this.lock.unlock();
            return z;
        } catch (Throwable th) {
            this.lock.unlock();
        }
    }

    public void store(String str, Credential credential) {
        this.lock.lock();
        try {
            MemoryPersistedCredential memoryPersistedCredential = (MemoryPersistedCredential) this.store.get(str);
            if (memoryPersistedCredential == null) {
                memoryPersistedCredential = new MemoryPersistedCredential();
                this.store.put(str, memoryPersistedCredential);
            }
            memoryPersistedCredential.store(credential);
        } finally {
            this.lock.unlock();
        }
    }
}
