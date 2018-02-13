package com.google.api.client.extensions.jdo.auth.oauth2;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.CredentialStore;
import com.google.api.client.util.Preconditions;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

public class JdoCredentialStore implements CredentialStore {
    private final PersistenceManagerFactory persistenceManagerFactory;

    public JdoCredentialStore(PersistenceManagerFactory persistenceManagerFactory) {
        this.persistenceManagerFactory = (PersistenceManagerFactory) Preconditions.checkNotNull(persistenceManagerFactory);
    }

    public void delete(String str, Credential credential) {
        PersistenceManager persistenceManager = this.persistenceManagerFactory.getPersistenceManager();
        try {
            persistenceManager.deletePersistent(new JdoPersistedCredential(str, credential));
        } finally {
            persistenceManager.close();
        }
    }

    public boolean load(String str, Credential credential) {
        PersistenceManager persistenceManager = this.persistenceManagerFactory.getPersistenceManager();
        try {
            ((JdoPersistedCredential) persistenceManager.getObjectById(JdoPersistedCredential.class, str)).load(credential);
            return true;
        } catch (JDOObjectNotFoundException e) {
            return false;
        } finally {
            persistenceManager.close();
        }
    }

    public void store(String str, Credential credential) {
        PersistenceManager persistenceManager = this.persistenceManagerFactory.getPersistenceManager();
        try {
            persistenceManager.makePersistent(new JdoPersistedCredential(str, credential));
        } finally {
            persistenceManager.close();
        }
    }
}
