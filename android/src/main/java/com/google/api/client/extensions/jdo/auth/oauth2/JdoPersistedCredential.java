package com.google.api.client.extensions.jdo.auth.oauth2;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.util.Preconditions;
import com.google.common.base.Ascii;
import javax.jdo.JDOFatalInternalException;
import javax.jdo.PersistenceManager;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.identity.StringIdentity;
import javax.jdo.spi.JDOImplHelper;
import javax.jdo.spi.PersistenceCapable.ObjectIdFieldConsumer;
import javax.jdo.spi.PersistenceCapable.ObjectIdFieldSupplier;
import javax.jdo.spi.StateManager;

@PersistenceCapable
class JdoPersistedCredential implements javax.jdo.spi.PersistenceCapable {
    private static final byte[] jdoFieldFlags = __jdoFieldFlagsInit();
    private static final String[] jdoFieldNames = __jdoFieldNamesInit();
    private static final Class[] jdoFieldTypes = __jdoFieldTypesInit();
    private static final int jdoInheritedFieldCount = __jdoGetInheritedFieldCount();
    private static final Class jdoPersistenceCapableSuperclass = __jdoPersistenceCapableSuperclassInit();
    @Persistent
    private String accessToken;
    @Persistent
    private Long expirationTimeMillis;
    protected transient byte jdoFlags;
    protected transient StateManager jdoStateManager;
    @Persistent
    private String refreshToken;
    @PrimaryKey
    private String userId;

    static {
        JDOImplHelper.registerClass(___jdo$loadClass("com.google.api.client.extensions.jdo.auth.oauth2.JdoPersistedCredential"), jdoFieldNames, jdoFieldTypes, jdoFieldFlags, jdoPersistenceCapableSuperclass, new JdoPersistedCredential());
    }

    protected JdoPersistedCredential() {
    }

    JdoPersistedCredential(String str, Credential credential) {
        this.userId = (String) Preconditions.checkNotNull(str);
        this.accessToken = credential.getAccessToken();
        this.refreshToken = credential.getRefreshToken();
        this.expirationTimeMillis = credential.getExpirationTimeMilliseconds();
    }

    public static Class ___jdo$loadClass(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }

    private static final byte[] __jdoFieldFlagsInit() {
        return new byte[]{Ascii.NAK, Ascii.NAK, Ascii.NAK, Ascii.CAN};
    }

    private static final String[] __jdoFieldNamesInit() {
        return new String[]{"accessToken", "expirationTimeMillis", "refreshToken", "userId"};
    }

    private static final Class[] __jdoFieldTypesInit() {
        return new Class[]{___jdo$loadClass("java.lang.String"), ___jdo$loadClass("java.lang.Long"), ___jdo$loadClass("java.lang.String"), ___jdo$loadClass("java.lang.String")};
    }

    protected static int __jdoGetInheritedFieldCount() {
        return 0;
    }

    private static Class __jdoPersistenceCapableSuperclassInit() {
        return null;
    }

    protected static int jdoGetManagedFieldCount() {
        return 4;
    }

    private static String jdoGetaccessToken(JdoPersistedCredential jdoPersistedCredential) {
        return (jdoPersistedCredential.jdoFlags <= (byte) 0 || jdoPersistedCredential.jdoStateManager == null || jdoPersistedCredential.jdoStateManager.isLoaded(jdoPersistedCredential, 0)) ? jdoPersistedCredential.accessToken : jdoPersistedCredential.jdoStateManager.getStringField(jdoPersistedCredential, 0, jdoPersistedCredential.accessToken);
    }

    private static Long jdoGetexpirationTimeMillis(JdoPersistedCredential jdoPersistedCredential) {
        return (jdoPersistedCredential.jdoFlags <= (byte) 0 || jdoPersistedCredential.jdoStateManager == null || jdoPersistedCredential.jdoStateManager.isLoaded(jdoPersistedCredential, 1)) ? jdoPersistedCredential.expirationTimeMillis : (Long) jdoPersistedCredential.jdoStateManager.getObjectField(jdoPersistedCredential, 1, jdoPersistedCredential.expirationTimeMillis);
    }

    private static String jdoGetrefreshToken(JdoPersistedCredential jdoPersistedCredential) {
        return (jdoPersistedCredential.jdoFlags <= (byte) 0 || jdoPersistedCredential.jdoStateManager == null || jdoPersistedCredential.jdoStateManager.isLoaded(jdoPersistedCredential, 2)) ? jdoPersistedCredential.refreshToken : jdoPersistedCredential.jdoStateManager.getStringField(jdoPersistedCredential, 2, jdoPersistedCredential.refreshToken);
    }

    private static String jdoGetuserId(JdoPersistedCredential jdoPersistedCredential) {
        return jdoPersistedCredential.userId;
    }

    private static void jdoSetaccessToken(JdoPersistedCredential jdoPersistedCredential, String str) {
        if (jdoPersistedCredential.jdoFlags == (byte) 0 || jdoPersistedCredential.jdoStateManager == null) {
            jdoPersistedCredential.accessToken = str;
        } else {
            jdoPersistedCredential.jdoStateManager.setStringField(jdoPersistedCredential, 0, jdoPersistedCredential.accessToken, str);
        }
    }

    private static void jdoSetexpirationTimeMillis(JdoPersistedCredential jdoPersistedCredential, Long l) {
        if (jdoPersistedCredential.jdoFlags == (byte) 0 || jdoPersistedCredential.jdoStateManager == null) {
            jdoPersistedCredential.expirationTimeMillis = l;
        } else {
            jdoPersistedCredential.jdoStateManager.setObjectField(jdoPersistedCredential, 1, jdoPersistedCredential.expirationTimeMillis, l);
        }
    }

    private static void jdoSetrefreshToken(JdoPersistedCredential jdoPersistedCredential, String str) {
        if (jdoPersistedCredential.jdoFlags == (byte) 0 || jdoPersistedCredential.jdoStateManager == null) {
            jdoPersistedCredential.refreshToken = str;
        } else {
            jdoPersistedCredential.jdoStateManager.setStringField(jdoPersistedCredential, 2, jdoPersistedCredential.refreshToken, str);
        }
    }

    private static void jdoSetuserId(JdoPersistedCredential jdoPersistedCredential, String str) {
        if (jdoPersistedCredential.jdoStateManager == null) {
            jdoPersistedCredential.userId = str;
        } else {
            jdoPersistedCredential.jdoStateManager.setStringField(jdoPersistedCredential, 3, jdoPersistedCredential.userId, str);
        }
    }

    private Object jdoSuperClone() throws CloneNotSupportedException {
        JdoPersistedCredential jdoPersistedCredential = (JdoPersistedCredential) super.clone();
        jdoPersistedCredential.jdoFlags = (byte) 0;
        jdoPersistedCredential.jdoStateManager = null;
        return jdoPersistedCredential;
    }

    protected final void jdoCopyField(JdoPersistedCredential jdoPersistedCredential, int i) {
        switch (i) {
            case 0:
                this.accessToken = jdoPersistedCredential.accessToken;
                return;
            case 1:
                this.expirationTimeMillis = jdoPersistedCredential.expirationTimeMillis;
                return;
            case 2:
                this.refreshToken = jdoPersistedCredential.refreshToken;
                return;
            case 3:
                this.userId = jdoPersistedCredential.userId;
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
        } else if (obj instanceof JdoPersistedCredential) {
            JdoPersistedCredential jdoPersistedCredential = (JdoPersistedCredential) obj;
            if (this.jdoStateManager != jdoPersistedCredential.jdoStateManager) {
                throw new IllegalArgumentException("state managers do not match");
            }
            int length = iArr.length - 1;
            if (length >= 0) {
                do {
                    jdoCopyField(jdoPersistedCredential, iArr[length]);
                    length--;
                } while (length >= 0);
            }
        } else {
            throw new IllegalArgumentException("object is not an object of type com.google.api.client.extensions.jdo.auth.oauth2.JdoPersistedCredential");
        }
    }

    protected void jdoCopyKeyFieldsFromObjectId(Object obj) {
        if (obj instanceof StringIdentity) {
            this.userId = ((StringIdentity) obj).getKey();
            return;
        }
        throw new ClassCastException("key class is not javax.jdo.identity.StringIdentity or null");
    }

    public void jdoCopyKeyFieldsFromObjectId(ObjectIdFieldConsumer objectIdFieldConsumer, Object obj) {
        if (objectIdFieldConsumer == null) {
            throw new IllegalArgumentException("ObjectIdFieldConsumer is null");
        } else if (obj instanceof StringIdentity) {
            objectIdFieldConsumer.storeStringField(3, ((StringIdentity) obj).getKey());
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
        JdoPersistedCredential jdoPersistedCredential = new JdoPersistedCredential();
        jdoPersistedCredential.jdoFlags = (byte) 1;
        jdoPersistedCredential.jdoStateManager = stateManager;
        return jdoPersistedCredential;
    }

    public javax.jdo.spi.PersistenceCapable jdoNewInstance(StateManager stateManager, Object obj) {
        JdoPersistedCredential jdoPersistedCredential = new JdoPersistedCredential();
        jdoPersistedCredential.jdoFlags = (byte) 1;
        jdoPersistedCredential.jdoStateManager = stateManager;
        jdoPersistedCredential.jdoCopyKeyFieldsFromObjectId(obj);
        return jdoPersistedCredential;
    }

    public final Object jdoNewObjectIdInstance() {
        return new StringIdentity(getClass(), this.userId);
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
                this.jdoStateManager.providedStringField(this, i, this.accessToken);
                return;
            case 1:
                this.jdoStateManager.providedObjectField(this, i, this.expirationTimeMillis);
                return;
            case 2:
                this.jdoStateManager.providedStringField(this, i, this.refreshToken);
                return;
            case 3:
                this.jdoStateManager.providedStringField(this, i, this.userId);
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
                this.accessToken = this.jdoStateManager.replacingStringField(this, i);
                return;
            case 1:
                this.expirationTimeMillis = (Long) this.jdoStateManager.replacingObjectField(this, i);
                return;
            case 2:
                this.refreshToken = this.jdoStateManager.replacingStringField(this, i);
                return;
            case 3:
                this.userId = this.jdoStateManager.replacingStringField(this, i);
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

    void load(Credential credential) {
        credential.setAccessToken(jdoGetaccessToken(this));
        credential.setRefreshToken(jdoGetrefreshToken(this));
        credential.setExpirationTimeMilliseconds(jdoGetexpirationTimeMillis(this));
    }
}
