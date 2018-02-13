package com.google.api.client.extensions.auth.helpers.oauth;

import com.google.api.client.auth.oauth.OAuthHmacSigner;
import com.google.api.client.auth.oauth.OAuthParameters;
import com.google.api.client.auth.oauth.OAuthSigner;
import com.google.api.client.extensions.auth.helpers.Credential;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.common.base.Ascii;
import java.io.IOException;
import javax.jdo.InstanceCallbacks;
import javax.jdo.JDOFatalInternalException;
import javax.jdo.PersistenceManager;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.identity.StringIdentity;
import javax.jdo.spi.JDOImplHelper;
import javax.jdo.spi.PersistenceCapable.ObjectIdFieldConsumer;
import javax.jdo.spi.PersistenceCapable.ObjectIdFieldSupplier;
import javax.jdo.spi.StateManager;

@PersistenceCapable
public final class OAuthHmacCredential implements Credential, InstanceCallbacks, javax.jdo.spi.PersistenceCapable {
    private static final byte[] jdoFieldFlags = __jdoFieldFlagsInit();
    private static final String[] jdoFieldNames = __jdoFieldNamesInit();
    private static final Class[] jdoFieldTypes = __jdoFieldTypesInit();
    private static final int jdoInheritedFieldCount = __jdoGetInheritedFieldCount();
    private static final Class jdoPersistenceCapableSuperclass = __jdoPersistenceCapableSuperclassInit();
    @NotPersistent
    private OAuthParameters authorizer;
    @Persistent
    private String consumerKey;
    protected transient byte jdoFlags;
    protected transient StateManager jdoStateManager;
    @Persistent
    private String sharedSecret;
    @Persistent
    private String token;
    @Persistent
    private String tokenSharedSecret;
    @PrimaryKey
    private String userId;

    static {
        JDOImplHelper.registerClass(___jdo$loadClass("com.google.api.client.extensions.auth.helpers.oauth.OAuthHmacCredential"), jdoFieldNames, jdoFieldTypes, jdoFieldFlags, jdoPersistenceCapableSuperclass, new OAuthHmacCredential());
    }

    protected OAuthHmacCredential() {
    }

    public OAuthHmacCredential(String str, String str2, String str3, String str4, String str5) {
        this.userId = str;
        this.consumerKey = str2;
        this.sharedSecret = str3;
        this.tokenSharedSecret = str4;
        this.token = str5;
        postConstruct();
    }

    public static Class ___jdo$loadClass(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }

    private static final byte[] __jdoFieldFlagsInit() {
        return new byte[]{Ascii.NAK, Ascii.NAK, Ascii.NAK, Ascii.NAK, Ascii.CAN};
    }

    private static final String[] __jdoFieldNamesInit() {
        return new String[]{"consumerKey", "sharedSecret", "token", "tokenSharedSecret", "userId"};
    }

    private static final Class[] __jdoFieldTypesInit() {
        return new Class[]{___jdo$loadClass("java.lang.String"), ___jdo$loadClass("java.lang.String"), ___jdo$loadClass("java.lang.String"), ___jdo$loadClass("java.lang.String"), ___jdo$loadClass("java.lang.String")};
    }

    protected static int __jdoGetInheritedFieldCount() {
        return 0;
    }

    private static Class __jdoPersistenceCapableSuperclassInit() {
        return null;
    }

    protected static int jdoGetManagedFieldCount() {
        return 5;
    }

    private static String jdoGetconsumerKey(OAuthHmacCredential oAuthHmacCredential) {
        return (oAuthHmacCredential.jdoFlags <= (byte) 0 || oAuthHmacCredential.jdoStateManager == null || oAuthHmacCredential.jdoStateManager.isLoaded(oAuthHmacCredential, 0)) ? oAuthHmacCredential.consumerKey : oAuthHmacCredential.jdoStateManager.getStringField(oAuthHmacCredential, 0, oAuthHmacCredential.consumerKey);
    }

    private static String jdoGetsharedSecret(OAuthHmacCredential oAuthHmacCredential) {
        return (oAuthHmacCredential.jdoFlags <= (byte) 0 || oAuthHmacCredential.jdoStateManager == null || oAuthHmacCredential.jdoStateManager.isLoaded(oAuthHmacCredential, 1)) ? oAuthHmacCredential.sharedSecret : oAuthHmacCredential.jdoStateManager.getStringField(oAuthHmacCredential, 1, oAuthHmacCredential.sharedSecret);
    }

    private static String jdoGettoken(OAuthHmacCredential oAuthHmacCredential) {
        return (oAuthHmacCredential.jdoFlags <= (byte) 0 || oAuthHmacCredential.jdoStateManager == null || oAuthHmacCredential.jdoStateManager.isLoaded(oAuthHmacCredential, 2)) ? oAuthHmacCredential.token : oAuthHmacCredential.jdoStateManager.getStringField(oAuthHmacCredential, 2, oAuthHmacCredential.token);
    }

    private static String jdoGettokenSharedSecret(OAuthHmacCredential oAuthHmacCredential) {
        return (oAuthHmacCredential.jdoFlags <= (byte) 0 || oAuthHmacCredential.jdoStateManager == null || oAuthHmacCredential.jdoStateManager.isLoaded(oAuthHmacCredential, 3)) ? oAuthHmacCredential.tokenSharedSecret : oAuthHmacCredential.jdoStateManager.getStringField(oAuthHmacCredential, 3, oAuthHmacCredential.tokenSharedSecret);
    }

    private static String jdoGetuserId(OAuthHmacCredential oAuthHmacCredential) {
        return oAuthHmacCredential.userId;
    }

    private static void jdoSetconsumerKey(OAuthHmacCredential oAuthHmacCredential, String str) {
        if (oAuthHmacCredential.jdoFlags == (byte) 0 || oAuthHmacCredential.jdoStateManager == null) {
            oAuthHmacCredential.consumerKey = str;
        } else {
            oAuthHmacCredential.jdoStateManager.setStringField(oAuthHmacCredential, 0, oAuthHmacCredential.consumerKey, str);
        }
    }

    private static void jdoSetsharedSecret(OAuthHmacCredential oAuthHmacCredential, String str) {
        if (oAuthHmacCredential.jdoFlags == (byte) 0 || oAuthHmacCredential.jdoStateManager == null) {
            oAuthHmacCredential.sharedSecret = str;
        } else {
            oAuthHmacCredential.jdoStateManager.setStringField(oAuthHmacCredential, 1, oAuthHmacCredential.sharedSecret, str);
        }
    }

    private static void jdoSettoken(OAuthHmacCredential oAuthHmacCredential, String str) {
        if (oAuthHmacCredential.jdoFlags == (byte) 0 || oAuthHmacCredential.jdoStateManager == null) {
            oAuthHmacCredential.token = str;
        } else {
            oAuthHmacCredential.jdoStateManager.setStringField(oAuthHmacCredential, 2, oAuthHmacCredential.token, str);
        }
    }

    private static void jdoSettokenSharedSecret(OAuthHmacCredential oAuthHmacCredential, String str) {
        if (oAuthHmacCredential.jdoFlags == (byte) 0 || oAuthHmacCredential.jdoStateManager == null) {
            oAuthHmacCredential.tokenSharedSecret = str;
        } else {
            oAuthHmacCredential.jdoStateManager.setStringField(oAuthHmacCredential, 3, oAuthHmacCredential.tokenSharedSecret, str);
        }
    }

    private static void jdoSetuserId(OAuthHmacCredential oAuthHmacCredential, String str) {
        if (oAuthHmacCredential.jdoStateManager == null) {
            oAuthHmacCredential.userId = str;
        } else {
            oAuthHmacCredential.jdoStateManager.setStringField(oAuthHmacCredential, 4, oAuthHmacCredential.userId, str);
        }
    }

    private Object jdoSuperClone() throws CloneNotSupportedException {
        OAuthHmacCredential oAuthHmacCredential = (OAuthHmacCredential) super.clone();
        oAuthHmacCredential.jdoFlags = (byte) 0;
        oAuthHmacCredential.jdoStateManager = null;
        return oAuthHmacCredential;
    }

    private void postConstruct() {
        OAuthSigner oAuthHmacSigner = new OAuthHmacSigner();
        oAuthHmacSigner.clientSharedSecret = jdoGetsharedSecret(this);
        oAuthHmacSigner.tokenSharedSecret = jdoGettokenSharedSecret(this);
        this.authorizer = new OAuthParameters();
        this.authorizer.consumerKey = jdoGetconsumerKey(this);
        this.authorizer.signer = oAuthHmacSigner;
        this.authorizer.token = jdoGettoken(this);
    }

    public boolean handleResponse(HttpRequest httpRequest, HttpResponse httpResponse, boolean z) {
        if (httpResponse.getStatusCode() == 401) {
            jdoSettoken(this, null);
        }
        return false;
    }

    public void initialize(HttpRequest httpRequest) throws IOException {
        this.authorizer.initialize(httpRequest);
        httpRequest.setUnsuccessfulResponseHandler(this);
    }

    public void intercept(HttpRequest httpRequest) throws IOException {
        this.authorizer.intercept(httpRequest);
    }

    public boolean isInvalid() {
        return jdoGettoken(this) == null;
    }

    protected final void jdoCopyField(OAuthHmacCredential oAuthHmacCredential, int i) {
        switch (i) {
            case 0:
                this.consumerKey = oAuthHmacCredential.consumerKey;
                return;
            case 1:
                this.sharedSecret = oAuthHmacCredential.sharedSecret;
                return;
            case 2:
                this.token = oAuthHmacCredential.token;
                return;
            case 3:
                this.tokenSharedSecret = oAuthHmacCredential.tokenSharedSecret;
                return;
            case 4:
                this.userId = oAuthHmacCredential.userId;
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
        } else if (obj instanceof OAuthHmacCredential) {
            OAuthHmacCredential oAuthHmacCredential = (OAuthHmacCredential) obj;
            if (this.jdoStateManager != oAuthHmacCredential.jdoStateManager) {
                throw new IllegalArgumentException("state managers do not match");
            }
            int length = iArr.length - 1;
            if (length >= 0) {
                do {
                    jdoCopyField(oAuthHmacCredential, iArr[length]);
                    length--;
                } while (length >= 0);
            }
        } else {
            throw new IllegalArgumentException("object is not an object of type com.google.api.client.extensions.auth.helpers.oauth.OAuthHmacCredential");
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
            objectIdFieldConsumer.storeStringField(4, ((StringIdentity) obj).getKey());
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
        OAuthHmacCredential oAuthHmacCredential = new OAuthHmacCredential();
        oAuthHmacCredential.jdoFlags = (byte) 1;
        oAuthHmacCredential.jdoStateManager = stateManager;
        return oAuthHmacCredential;
    }

    public javax.jdo.spi.PersistenceCapable jdoNewInstance(StateManager stateManager, Object obj) {
        OAuthHmacCredential oAuthHmacCredential = new OAuthHmacCredential();
        oAuthHmacCredential.jdoFlags = (byte) 1;
        oAuthHmacCredential.jdoStateManager = stateManager;
        oAuthHmacCredential.jdoCopyKeyFieldsFromObjectId(obj);
        return oAuthHmacCredential;
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

    public void jdoPostLoad() {
        postConstruct();
    }

    public void jdoPreClear() {
    }

    public void jdoPreDelete() {
    }

    protected final void jdoPreSerialize() {
        if (this.jdoStateManager != null) {
            this.jdoStateManager.preSerialize(this);
        }
    }

    public void jdoPreStore() {
    }

    public void jdoProvideField(int i) {
        if (this.jdoStateManager == null) {
            throw new IllegalStateException("state manager is null");
        }
        switch (i) {
            case 0:
                this.jdoStateManager.providedStringField(this, i, this.consumerKey);
                return;
            case 1:
                this.jdoStateManager.providedStringField(this, i, this.sharedSecret);
                return;
            case 2:
                this.jdoStateManager.providedStringField(this, i, this.token);
                return;
            case 3:
                this.jdoStateManager.providedStringField(this, i, this.tokenSharedSecret);
                return;
            case 4:
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
                this.consumerKey = this.jdoStateManager.replacingStringField(this, i);
                return;
            case 1:
                this.sharedSecret = this.jdoStateManager.replacingStringField(this, i);
                return;
            case 2:
                this.token = this.jdoStateManager.replacingStringField(this, i);
                return;
            case 3:
                this.tokenSharedSecret = this.jdoStateManager.replacingStringField(this, i);
                return;
            case 4:
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
}
