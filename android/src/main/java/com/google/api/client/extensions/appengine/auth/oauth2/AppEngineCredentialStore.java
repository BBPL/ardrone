package com.google.api.client.extensions.appengine.auth.oauth2;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.CredentialStore;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class AppEngineCredentialStore implements CredentialStore {
    private static final String KIND = AppEngineCredentialStore.class.getName();

    public void delete(String str, Credential credential) {
        DatastoreServiceFactory.getDatastoreService().delete(new Key[]{KeyFactory.createKey(KIND, str)});
    }

    public boolean load(String str, Credential credential) {
        try {
            Entity entity = DatastoreServiceFactory.getDatastoreService().get(KeyFactory.createKey(KIND, str));
            credential.setAccessToken((String) entity.getProperty("accessToken"));
            credential.setRefreshToken((String) entity.getProperty("refreshToken"));
            credential.setExpirationTimeMilliseconds((Long) entity.getProperty("expirationTimeMillis"));
            return true;
        } catch (EntityNotFoundException e) {
            return false;
        }
    }

    public void store(String str, Credential credential) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Entity entity = new Entity(KIND, str);
        entity.setProperty("accessToken", credential.getAccessToken());
        entity.setProperty("refreshToken", credential.getRefreshToken());
        entity.setProperty("expirationTimeMillis", credential.getExpirationTimeMilliseconds());
        datastoreService.put(entity);
    }
}
