package com.google.api.client.googleapis.extensions.appengine.subscriptions;

import com.google.api.client.googleapis.subscriptions.StoredSubscription;
import com.google.api.client.googleapis.subscriptions.SubscriptionStore;
import com.google.api.client.util.Lists;
import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.List;

public class AppEngineSubscriptionStore implements SubscriptionStore {
    private static final String FIELD_SUBSCRIPTION = "serializedSubscription";
    private static final String KIND = AppEngineSubscriptionStore.class.getName();

    private <T> T deserialize(Blob blob, Class<T> cls) throws IOException {
        InputStream byteArrayInputStream = new ByteArrayInputStream(blob.getBytes());
        try {
            T readObject = new ObjectInputStream(byteArrayInputStream).readObject();
            if (cls.isAssignableFrom(readObject.getClass())) {
                byteArrayInputStream.close();
                return readObject;
            }
            byteArrayInputStream.close();
            return null;
        } catch (Throwable e) {
            throw new IOException("Failed to deserialize object", e);
        } catch (Throwable th) {
            byteArrayInputStream.close();
        }
    }

    private StoredSubscription getSubscriptionFromEntity(Entity entity) throws IOException {
        return (StoredSubscription) deserialize((Blob) entity.getProperty(FIELD_SUBSCRIPTION), StoredSubscription.class);
    }

    private Blob serialize(Object obj) throws IOException {
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            new ObjectOutputStream(byteArrayOutputStream).writeObject(obj);
            Blob blob = new Blob(byteArrayOutputStream.toByteArray());
            return blob;
        } finally {
            byteArrayOutputStream.close();
        }
    }

    public StoredSubscription getSubscription(String str) throws IOException {
        try {
            return getSubscriptionFromEntity(DatastoreServiceFactory.getDatastoreService().get(KeyFactory.createKey(KIND, str)));
        } catch (EntityNotFoundException e) {
            return null;
        }
    }

    public List<StoredSubscription> listSubscriptions() throws IOException {
        List<StoredSubscription> newArrayList = Lists.newArrayList();
        for (Entity subscriptionFromEntity : DatastoreServiceFactory.getDatastoreService().prepare(new Query(KIND)).asIterable()) {
            newArrayList.add(getSubscriptionFromEntity(subscriptionFromEntity));
        }
        return newArrayList;
    }

    public void removeSubscription(StoredSubscription storedSubscription) throws IOException {
        if (storedSubscription != null) {
            DatastoreServiceFactory.getDatastoreService().delete(new Key[]{KeyFactory.createKey(KIND, storedSubscription.getId())});
        }
    }

    public void storeSubscription(StoredSubscription storedSubscription) throws IOException {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Entity entity = new Entity(KIND, storedSubscription.getId());
        entity.setProperty(FIELD_SUBSCRIPTION, serialize(storedSubscription));
        datastoreService.put(entity);
    }
}
