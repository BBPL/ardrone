package com.google.api.client.googleapis.extensions.servlet.subscriptions;

import com.google.api.client.googleapis.subscriptions.StoredSubscription;
import com.google.api.client.googleapis.subscriptions.SubscriptionStore;
import com.google.api.client.util.Lists;
import com.google.api.client.util.Preconditions;
import com.google.common.base.Ascii;
import java.util.Iterator;
import java.util.List;
import javax.jdo.JDOFatalInternalException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.identity.StringIdentity;
import javax.jdo.spi.JDOImplHelper;
import javax.jdo.spi.PersistenceCapable.ObjectIdFieldConsumer;
import javax.jdo.spi.PersistenceCapable.ObjectIdFieldSupplier;
import javax.jdo.spi.StateManager;

public final class JdoSubscriptionStore implements SubscriptionStore {
    private final PersistenceManagerFactory persistenceManagerFactory;

    @PersistenceCapable
    private static final class JdoStoredSubscription implements javax.jdo.spi.PersistenceCapable {
        private static final byte[] jdoFieldFlags = __jdoFieldFlagsInit();
        private static final String[] jdoFieldNames = __jdoFieldNamesInit();
        private static final Class[] jdoFieldTypes = __jdoFieldTypesInit();
        private static final int jdoInheritedFieldCount = __jdoGetInheritedFieldCount();
        private static final Class jdoPersistenceCapableSuperclass = __jdoPersistenceCapableSuperclassInit();
        protected transient byte jdoFlags;
        protected transient StateManager jdoStateManager;
        @Persistent(serialized = "true")
        private StoredSubscription subscription;
        @Persistent
        @PrimaryKey
        private String subscriptionId;

        static {
            JDOImplHelper.registerClass(___jdo$loadClass("com.google.api.client.googleapis.extensions.servlet.subscriptions.JdoSubscriptionStore$JdoStoredSubscription"), jdoFieldNames, jdoFieldTypes, jdoFieldFlags, jdoPersistenceCapableSuperclass, new JdoStoredSubscription());
        }

        JdoStoredSubscription() {
        }

        public JdoStoredSubscription(StoredSubscription storedSubscription) {
            setSubscription(storedSubscription);
        }

        public static Class ___jdo$loadClass(String str) {
            try {
                return Class.forName(str);
            } catch (ClassNotFoundException e) {
                throw new NoClassDefFoundError(e.getMessage());
            }
        }

        private static final byte[] __jdoFieldFlagsInit() {
            return new byte[]{Ascii.SUB, Ascii.CAN};
        }

        private static final String[] __jdoFieldNamesInit() {
            return new String[]{"subscription", "subscriptionId"};
        }

        private static final Class[] __jdoFieldTypesInit() {
            return new Class[]{___jdo$loadClass("com.google.api.client.googleapis.subscriptions.StoredSubscription"), ___jdo$loadClass("java.lang.String")};
        }

        protected static int __jdoGetInheritedFieldCount() {
            return 0;
        }

        private static Class __jdoPersistenceCapableSuperclassInit() {
            return null;
        }

        protected static int jdoGetManagedFieldCount() {
            return 2;
        }

        private static StoredSubscription jdoGetsubscription(JdoStoredSubscription jdoStoredSubscription) {
            return (jdoStoredSubscription.jdoStateManager == null || jdoStoredSubscription.jdoStateManager.isLoaded(jdoStoredSubscription, 0)) ? jdoStoredSubscription.subscription : (StoredSubscription) jdoStoredSubscription.jdoStateManager.getObjectField(jdoStoredSubscription, 0, jdoStoredSubscription.subscription);
        }

        private static String jdoGetsubscriptionId(JdoStoredSubscription jdoStoredSubscription) {
            return jdoStoredSubscription.subscriptionId;
        }

        private static void jdoSetsubscription(JdoStoredSubscription jdoStoredSubscription, StoredSubscription storedSubscription) {
            if (jdoStoredSubscription.jdoStateManager == null) {
                jdoStoredSubscription.subscription = storedSubscription;
            } else {
                jdoStoredSubscription.jdoStateManager.setObjectField(jdoStoredSubscription, 0, jdoStoredSubscription.subscription, storedSubscription);
            }
        }

        private static void jdoSetsubscriptionId(JdoStoredSubscription jdoStoredSubscription, String str) {
            if (jdoStoredSubscription.jdoStateManager == null) {
                jdoStoredSubscription.subscriptionId = str;
            } else {
                jdoStoredSubscription.jdoStateManager.setStringField(jdoStoredSubscription, 1, jdoStoredSubscription.subscriptionId, str);
            }
        }

        private Object jdoSuperClone() throws CloneNotSupportedException {
            JdoStoredSubscription jdoStoredSubscription = (JdoStoredSubscription) super.clone();
            jdoStoredSubscription.jdoFlags = (byte) 0;
            jdoStoredSubscription.jdoStateManager = null;
            return jdoStoredSubscription;
        }

        public StoredSubscription getSubscription() {
            return jdoGetsubscription(this);
        }

        public String getSubscriptionId() {
            return jdoGetsubscriptionId(this);
        }

        protected final void jdoCopyField(JdoStoredSubscription jdoStoredSubscription, int i) {
            switch (i) {
                case 0:
                    this.subscription = jdoStoredSubscription.subscription;
                    return;
                case 1:
                    this.subscriptionId = jdoStoredSubscription.subscriptionId;
                    return;
                default:
                    throw new IllegalArgumentException(new StringBuffer("out of field index :").append(i).toString());
            }
        }

        public void jdoCopyFields(Object obj, int[] iArr) {
            if (this.jdoStateManager == null) {
                throw new IllegalStateException("state manager is null");
            } else if (iArr == null) {
                throw new IllegalStateException("fieldNumbers is null");
            } else if (obj instanceof JdoStoredSubscription) {
                JdoStoredSubscription jdoStoredSubscription = (JdoStoredSubscription) obj;
                if (this.jdoStateManager != jdoStoredSubscription.jdoStateManager) {
                    throw new IllegalArgumentException("state managers do not match");
                }
                int length = iArr.length - 1;
                if (length >= 0) {
                    do {
                        jdoCopyField(jdoStoredSubscription, iArr[length]);
                        length--;
                    } while (length >= 0);
                }
            } else {
                throw new IllegalArgumentException("object is not an object of type com.google.api.client.googleapis.extensions.servlet.subscriptions.JdoSubscriptionStore$JdoStoredSubscription");
            }
        }

        protected void jdoCopyKeyFieldsFromObjectId(Object obj) {
            if (obj instanceof StringIdentity) {
                this.subscriptionId = ((StringIdentity) obj).getKey();
                return;
            }
            throw new ClassCastException("key class is not javax.jdo.identity.StringIdentity or null");
        }

        public void jdoCopyKeyFieldsFromObjectId(ObjectIdFieldConsumer objectIdFieldConsumer, Object obj) {
            if (objectIdFieldConsumer == null) {
                throw new IllegalArgumentException("ObjectIdFieldConsumer is null");
            } else if (obj instanceof StringIdentity) {
                objectIdFieldConsumer.storeStringField(1, ((StringIdentity) obj).getKey());
            } else {
                throw new ClassCastException("oid is not instanceof javax.jdo.identity.StringIdentity");
            }
        }

        public final void jdoCopyKeyFieldsToObjectId(Object obj) {
            throw new JDOFatalInternalException("It's illegal to call jdoCopyKeyFieldsToObjectId for a class with SingleFieldIdentity.");
        }

        public final void jdoCopyKeyFieldsToObjectId(ObjectIdFieldSupplier objectIdFieldSupplier, Object obj) {
            throw new JDOFatalInternalException("It's illegal to call jdoCopyKeyFieldsToObjectId for a class with SingleFieldIdentity.");
        }

        public final Object jdoGetObjectId() {
            return this.jdoStateManager != null ? this.jdoStateManager.getObjectId(this) : null;
        }

        public final PersistenceManager jdoGetPersistenceManager() {
            return this.jdoStateManager != null ? this.jdoStateManager.getPersistenceManager(this) : null;
        }

        public final Object jdoGetTransactionalObjectId() {
            return this.jdoStateManager != null ? this.jdoStateManager.getTransactionalObjectId(this) : null;
        }

        public final Object jdoGetVersion() {
            return this.jdoStateManager != null ? this.jdoStateManager.getVersion(this) : null;
        }

        public final boolean jdoIsDeleted() {
            return this.jdoStateManager != null ? this.jdoStateManager.isDeleted(this) : false;
        }

        public boolean jdoIsDetached() {
            return false;
        }

        public final boolean jdoIsDirty() {
            return this.jdoStateManager != null ? this.jdoStateManager.isDirty(this) : false;
        }

        public final boolean jdoIsNew() {
            return this.jdoStateManager != null ? this.jdoStateManager.isNew(this) : false;
        }

        public final boolean jdoIsPersistent() {
            return this.jdoStateManager != null ? this.jdoStateManager.isPersistent(this) : false;
        }

        public final boolean jdoIsTransactional() {
            return this.jdoStateManager != null ? this.jdoStateManager.isTransactional(this) : false;
        }

        public void jdoMakeDirty(String str) {
            if (this.jdoStateManager != null) {
                this.jdoStateManager.makeDirty(this, str);
            }
        }

        public javax.jdo.spi.PersistenceCapable jdoNewInstance(StateManager stateManager) {
            JdoStoredSubscription jdoStoredSubscription = new JdoStoredSubscription();
            jdoStoredSubscription.jdoFlags = (byte) 1;
            jdoStoredSubscription.jdoStateManager = stateManager;
            return jdoStoredSubscription;
        }

        public javax.jdo.spi.PersistenceCapable jdoNewInstance(StateManager stateManager, Object obj) {
            JdoStoredSubscription jdoStoredSubscription = new JdoStoredSubscription();
            jdoStoredSubscription.jdoFlags = (byte) 1;
            jdoStoredSubscription.jdoStateManager = stateManager;
            jdoStoredSubscription.jdoCopyKeyFieldsFromObjectId(obj);
            return jdoStoredSubscription;
        }

        public final Object jdoNewObjectIdInstance() {
            return new StringIdentity(getClass(), this.subscriptionId);
        }

        public final Object jdoNewObjectIdInstance(Object obj) {
            if (obj != null) {
                return !(obj instanceof String) ? new StringIdentity(getClass(), (String) obj) : new StringIdentity(getClass(), (String) obj);
            } else {
                throw new IllegalArgumentException("key is null");
            }
        }

        protected final void jdoPreSerialize() {
            if (this.jdoStateManager != null) {
                this.jdoStateManager.preSerialize(this);
            }
        }

        public void jdoProvideField(int i) {
            if (this.jdoStateManager == null) {
                throw new IllegalStateException("state manager is null");
            }
            switch (i) {
                case 0:
                    this.jdoStateManager.providedObjectField(this, i, this.subscription);
                    return;
                case 1:
                    this.jdoStateManager.providedStringField(this, i, this.subscriptionId);
                    return;
                default:
                    throw new IllegalArgumentException(new StringBuffer("out of field index :").append(i).toString());
            }
        }

        public final void jdoProvideFields(int[] iArr) {
            if (iArr == null) {
                throw new IllegalArgumentException("argment is null");
            }
            int length = iArr.length - 1;
            if (length >= 0) {
                do {
                    jdoProvideField(iArr[length]);
                    length--;
                } while (length >= 0);
            }
        }

        public void jdoReplaceField(int i) {
            if (this.jdoStateManager == null) {
                throw new IllegalStateException("state manager is null");
            }
            switch (i) {
                case 0:
                    this.subscription = (StoredSubscription) this.jdoStateManager.replacingObjectField(this, i);
                    return;
                case 1:
                    this.subscriptionId = this.jdoStateManager.replacingStringField(this, i);
                    return;
                default:
                    throw new IllegalArgumentException(new StringBuffer("out of field index :").append(i).toString());
            }
        }

        public final void jdoReplaceFields(int[] iArr) {
            if (iArr == null) {
                throw new IllegalArgumentException("argument is null");
            }
            int length = iArr.length;
            if (length > 0) {
                int i = 0;
                do {
                    jdoReplaceField(iArr[i]);
                    i++;
                } while (i < length);
            }
        }

        public final void jdoReplaceFlags() {
            if (this.jdoStateManager != null) {
                this.jdoFlags = this.jdoStateManager.replacingFlags(this);
            }
        }

        public final void jdoReplaceStateManager(StateManager stateManager) {
            synchronized (this) {
                if (this.jdoStateManager != null) {
                    this.jdoStateManager = this.jdoStateManager.replacingStateManager(this, stateManager);
                } else {
                    JDOImplHelper.checkAuthorizedStateManager(stateManager);
                    this.jdoStateManager = stateManager;
                    this.jdoFlags = (byte) 1;
                }
            }
        }

        public void setSubscription(StoredSubscription storedSubscription) {
            jdoSetsubscription(this, storedSubscription);
            jdoSetsubscriptionId(this, storedSubscription.getId());
        }
    }

    public JdoSubscriptionStore(PersistenceManagerFactory persistenceManagerFactory) {
        this.persistenceManagerFactory = persistenceManagerFactory;
    }

    private JdoStoredSubscription getStoredSubscription(String str) {
        PersistenceManager persistenceManager = this.persistenceManagerFactory.getPersistenceManager();
        try {
            Query newQuery = persistenceManager.newQuery(JdoStoredSubscription.class);
            newQuery.setFilter("subscriptionId == idParam");
            newQuery.declareParameters("String idParam");
            newQuery.setRange(0, 1);
            Iterator it = ((Iterable) newQuery.execute(str)).iterator();
            if (it.hasNext()) {
                JdoStoredSubscription jdoStoredSubscription = (JdoStoredSubscription) it.next();
                return jdoStoredSubscription;
            }
            persistenceManager.close();
            return null;
        } finally {
            persistenceManager.close();
        }
    }

    public StoredSubscription getSubscription(String str) {
        JdoStoredSubscription storedSubscription = getStoredSubscription(str);
        return storedSubscription == null ? null : storedSubscription.getSubscription();
    }

    public List<StoredSubscription> listSubscriptions() {
        List<StoredSubscription> newArrayList = Lists.newArrayList();
        PersistenceManager persistenceManager = this.persistenceManagerFactory.getPersistenceManager();
        try {
            for (JdoStoredSubscription subscription : (Iterable) persistenceManager.newQuery(JdoStoredSubscription.class).execute()) {
                newArrayList.add(subscription.getSubscription());
            }
            return newArrayList;
        } finally {
            persistenceManager.close();
        }
    }

    public void removeSubscription(StoredSubscription storedSubscription) {
        JdoStoredSubscription storedSubscription2 = getStoredSubscription(storedSubscription.getId());
        if (storedSubscription2 != null) {
            PersistenceManager persistenceManager = this.persistenceManagerFactory.getPersistenceManager();
            try {
                persistenceManager.deletePersistent(storedSubscription2);
            } finally {
                persistenceManager.close();
            }
        }
    }

    public void storeSubscription(StoredSubscription storedSubscription) {
        Preconditions.checkNotNull(storedSubscription);
        JdoStoredSubscription storedSubscription2 = getStoredSubscription(storedSubscription.getId());
        PersistenceManager persistenceManager = this.persistenceManagerFactory.getPersistenceManager();
        if (storedSubscription2 != null) {
            try {
                storedSubscription2.setSubscription(storedSubscription);
            } catch (Throwable th) {
                Throwable th2 = th;
                persistenceManager.close();
                throw th2;
            }
        }
        try {
            persistenceManager.makePersistent(new JdoStoredSubscription(storedSubscription));
        } catch (Throwable th3) {
            th2 = th3;
            persistenceManager.close();
            throw th2;
        }
        persistenceManager.close();
    }
}
