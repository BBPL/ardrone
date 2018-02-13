package com.google.api.client.extensions.auth.helpers.oauth;

import com.google.api.client.auth.oauth.OAuthAuthorizeTemporaryTokenUrl;
import com.google.api.client.auth.oauth.OAuthCredentialsResponse;
import com.google.api.client.auth.oauth.OAuthGetAccessToken;
import com.google.api.client.auth.oauth.OAuthGetTemporaryToken;
import com.google.api.client.auth.oauth.OAuthHmacSigner;
import com.google.api.client.auth.oauth.OAuthSigner;
import com.google.api.client.extensions.auth.helpers.Credential;
import com.google.api.client.extensions.auth.helpers.ThreeLeggedFlow;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.Preconditions;
import com.google.common.base.Ascii;
import java.io.IOException;
import javax.jdo.JDOFatalInternalException;
import javax.jdo.JDOObjectNotFoundException;
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
public class OAuthHmacThreeLeggedFlow implements ThreeLeggedFlow, javax.jdo.spi.PersistenceCapable {
    private static final byte[] jdoFieldFlags = __jdoFieldFlagsInit();
    private static final String[] jdoFieldNames = __jdoFieldNamesInit();
    private static final Class[] jdoFieldTypes = __jdoFieldTypesInit();
    private static final int jdoInheritedFieldCount = __jdoGetInheritedFieldCount();
    private static final Class jdoPersistenceCapableSuperclass = __jdoPersistenceCapableSuperclassInit();
    @Persistent
    private String authorizationServerUrl;
    @Persistent
    private final String authorizationUrl;
    @Persistent
    private String consumerKey;
    @Persistent
    private String consumerSecret;
    protected transient byte jdoFlags;
    protected transient StateManager jdoStateManager;
    @Persistent
    private String tempToken;
    @Persistent
    private String tempTokenSecret;
    @NotPersistent
    private HttpTransport transport;
    @PrimaryKey
    private String userId;

    static {
        JDOImplHelper.registerClass(___jdo$loadClass("com.google.api.client.extensions.auth.helpers.oauth.OAuthHmacThreeLeggedFlow"), jdoFieldNames, jdoFieldTypes, jdoFieldFlags, jdoPersistenceCapableSuperclass, new OAuthHmacThreeLeggedFlow());
    }

    protected OAuthHmacThreeLeggedFlow() {
    }

    public OAuthHmacThreeLeggedFlow(String str, String str2, String str3, String str4, String str5, String str6, HttpTransport httpTransport) throws IOException {
        this.userId = str;
        this.consumerSecret = str3;
        this.consumerKey = str2;
        this.transport = httpTransport;
        this.authorizationServerUrl = str4;
        OAuthGetTemporaryToken oAuthGetTemporaryToken = new OAuthGetTemporaryToken(str6);
        OAuthSigner oAuthHmacSigner = new OAuthHmacSigner();
        oAuthHmacSigner.clientSharedSecret = str3;
        oAuthGetTemporaryToken.signer = oAuthHmacSigner;
        oAuthGetTemporaryToken.consumerKey = str2;
        oAuthGetTemporaryToken.callback = str6;
        oAuthGetTemporaryToken.transport = this.transport;
        OAuthCredentialsResponse execute = oAuthGetTemporaryToken.execute();
        this.tempToken = execute.token;
        this.tempTokenSecret = execute.tokenSecret;
        OAuthAuthorizeTemporaryTokenUrl oAuthAuthorizeTemporaryTokenUrl = new OAuthAuthorizeTemporaryTokenUrl(str5);
        oAuthAuthorizeTemporaryTokenUrl.temporaryToken = execute.token;
        this.authorizationUrl = oAuthAuthorizeTemporaryTokenUrl.build();
    }

    public static Class ___jdo$loadClass(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }

    private static final byte[] __jdoFieldFlagsInit() {
        return new byte[]{Ascii.NAK, Ascii.NAK, Ascii.NAK, Ascii.NAK, Ascii.NAK, Ascii.CAN};
    }

    private static final String[] __jdoFieldNamesInit() {
        return new String[]{"authorizationServerUrl", "consumerKey", "consumerSecret", "tempToken", "tempTokenSecret", "userId"};
    }

    private static final Class[] __jdoFieldTypesInit() {
        return new Class[]{___jdo$loadClass("java.lang.String"), ___jdo$loadClass("java.lang.String"), ___jdo$loadClass("java.lang.String"), ___jdo$loadClass("java.lang.String"), ___jdo$loadClass("java.lang.String"), ___jdo$loadClass("java.lang.String")};
    }

    protected static int __jdoGetInheritedFieldCount() {
        return 0;
    }

    private static Class __jdoPersistenceCapableSuperclassInit() {
        return null;
    }

    protected static int jdoGetManagedFieldCount() {
        return 6;
    }

    private static String jdoGetauthorizationServerUrl(OAuthHmacThreeLeggedFlow oAuthHmacThreeLeggedFlow) {
        return (oAuthHmacThreeLeggedFlow.jdoFlags <= (byte) 0 || oAuthHmacThreeLeggedFlow.jdoStateManager == null || oAuthHmacThreeLeggedFlow.jdoStateManager.isLoaded(oAuthHmacThreeLeggedFlow, 0)) ? oAuthHmacThreeLeggedFlow.authorizationServerUrl : oAuthHmacThreeLeggedFlow.jdoStateManager.getStringField(oAuthHmacThreeLeggedFlow, 0, oAuthHmacThreeLeggedFlow.authorizationServerUrl);
    }

    private static String jdoGetconsumerKey(OAuthHmacThreeLeggedFlow oAuthHmacThreeLeggedFlow) {
        return (oAuthHmacThreeLeggedFlow.jdoFlags <= (byte) 0 || oAuthHmacThreeLeggedFlow.jdoStateManager == null || oAuthHmacThreeLeggedFlow.jdoStateManager.isLoaded(oAuthHmacThreeLeggedFlow, 1)) ? oAuthHmacThreeLeggedFlow.consumerKey : oAuthHmacThreeLeggedFlow.jdoStateManager.getStringField(oAuthHmacThreeLeggedFlow, 1, oAuthHmacThreeLeggedFlow.consumerKey);
    }

    private static String jdoGetconsumerSecret(OAuthHmacThreeLeggedFlow oAuthHmacThreeLeggedFlow) {
        return (oAuthHmacThreeLeggedFlow.jdoFlags <= (byte) 0 || oAuthHmacThreeLeggedFlow.jdoStateManager == null || oAuthHmacThreeLeggedFlow.jdoStateManager.isLoaded(oAuthHmacThreeLeggedFlow, 2)) ? oAuthHmacThreeLeggedFlow.consumerSecret : oAuthHmacThreeLeggedFlow.jdoStateManager.getStringField(oAuthHmacThreeLeggedFlow, 2, oAuthHmacThreeLeggedFlow.consumerSecret);
    }

    private static String jdoGettempToken(OAuthHmacThreeLeggedFlow oAuthHmacThreeLeggedFlow) {
        return (oAuthHmacThreeLeggedFlow.jdoFlags <= (byte) 0 || oAuthHmacThreeLeggedFlow.jdoStateManager == null || oAuthHmacThreeLeggedFlow.jdoStateManager.isLoaded(oAuthHmacThreeLeggedFlow, 3)) ? oAuthHmacThreeLeggedFlow.tempToken : oAuthHmacThreeLeggedFlow.jdoStateManager.getStringField(oAuthHmacThreeLeggedFlow, 3, oAuthHmacThreeLeggedFlow.tempToken);
    }

    private static String jdoGettempTokenSecret(OAuthHmacThreeLeggedFlow oAuthHmacThreeLeggedFlow) {
        return (oAuthHmacThreeLeggedFlow.jdoFlags <= (byte) 0 || oAuthHmacThreeLeggedFlow.jdoStateManager == null || oAuthHmacThreeLeggedFlow.jdoStateManager.isLoaded(oAuthHmacThreeLeggedFlow, 4)) ? oAuthHmacThreeLeggedFlow.tempTokenSecret : oAuthHmacThreeLeggedFlow.jdoStateManager.getStringField(oAuthHmacThreeLeggedFlow, 4, oAuthHmacThreeLeggedFlow.tempTokenSecret);
    }

    private static String jdoGetuserId(OAuthHmacThreeLeggedFlow oAuthHmacThreeLeggedFlow) {
        return oAuthHmacThreeLeggedFlow.userId;
    }

    private static void jdoSetauthorizationServerUrl(OAuthHmacThreeLeggedFlow oAuthHmacThreeLeggedFlow, String str) {
        if (oAuthHmacThreeLeggedFlow.jdoFlags == (byte) 0 || oAuthHmacThreeLeggedFlow.jdoStateManager == null) {
            oAuthHmacThreeLeggedFlow.authorizationServerUrl = str;
        } else {
            oAuthHmacThreeLeggedFlow.jdoStateManager.setStringField(oAuthHmacThreeLeggedFlow, 0, oAuthHmacThreeLeggedFlow.authorizationServerUrl, str);
        }
    }

    private static void jdoSetconsumerKey(OAuthHmacThreeLeggedFlow oAuthHmacThreeLeggedFlow, String str) {
        if (oAuthHmacThreeLeggedFlow.jdoFlags == (byte) 0 || oAuthHmacThreeLeggedFlow.jdoStateManager == null) {
            oAuthHmacThreeLeggedFlow.consumerKey = str;
        } else {
            oAuthHmacThreeLeggedFlow.jdoStateManager.setStringField(oAuthHmacThreeLeggedFlow, 1, oAuthHmacThreeLeggedFlow.consumerKey, str);
        }
    }

    private static void jdoSetconsumerSecret(OAuthHmacThreeLeggedFlow oAuthHmacThreeLeggedFlow, String str) {
        if (oAuthHmacThreeLeggedFlow.jdoFlags == (byte) 0 || oAuthHmacThreeLeggedFlow.jdoStateManager == null) {
            oAuthHmacThreeLeggedFlow.consumerSecret = str;
        } else {
            oAuthHmacThreeLeggedFlow.jdoStateManager.setStringField(oAuthHmacThreeLeggedFlow, 2, oAuthHmacThreeLeggedFlow.consumerSecret, str);
        }
    }

    private static void jdoSettempToken(OAuthHmacThreeLeggedFlow oAuthHmacThreeLeggedFlow, String str) {
        if (oAuthHmacThreeLeggedFlow.jdoFlags == (byte) 0 || oAuthHmacThreeLeggedFlow.jdoStateManager == null) {
            oAuthHmacThreeLeggedFlow.tempToken = str;
        } else {
            oAuthHmacThreeLeggedFlow.jdoStateManager.setStringField(oAuthHmacThreeLeggedFlow, 3, oAuthHmacThreeLeggedFlow.tempToken, str);
        }
    }

    private static void jdoSettempTokenSecret(OAuthHmacThreeLeggedFlow oAuthHmacThreeLeggedFlow, String str) {
        if (oAuthHmacThreeLeggedFlow.jdoFlags == (byte) 0 || oAuthHmacThreeLeggedFlow.jdoStateManager == null) {
            oAuthHmacThreeLeggedFlow.tempTokenSecret = str;
        } else {
            oAuthHmacThreeLeggedFlow.jdoStateManager.setStringField(oAuthHmacThreeLeggedFlow, 4, oAuthHmacThreeLeggedFlow.tempTokenSecret, str);
        }
    }

    private static void jdoSetuserId(OAuthHmacThreeLeggedFlow oAuthHmacThreeLeggedFlow, String str) {
        if (oAuthHmacThreeLeggedFlow.jdoStateManager == null) {
            oAuthHmacThreeLeggedFlow.userId = str;
        } else {
            oAuthHmacThreeLeggedFlow.jdoStateManager.setStringField(oAuthHmacThreeLeggedFlow, 5, oAuthHmacThreeLeggedFlow.userId, str);
        }
    }

    private Object jdoSuperClone() throws CloneNotSupportedException {
        OAuthHmacThreeLeggedFlow oAuthHmacThreeLeggedFlow = (OAuthHmacThreeLeggedFlow) super.clone();
        oAuthHmacThreeLeggedFlow.jdoFlags = (byte) 0;
        oAuthHmacThreeLeggedFlow.jdoStateManager = null;
        return oAuthHmacThreeLeggedFlow;
    }

    public Credential complete(String str) throws IOException {
        Preconditions.checkNotNull(this.transport, "Must call setHttpTransport before calling complete.");
        OAuthGetAccessToken oAuthGetAccessToken = new OAuthGetAccessToken(jdoGetauthorizationServerUrl(this));
        oAuthGetAccessToken.temporaryToken = jdoGettempToken(this);
        oAuthGetAccessToken.transport = this.transport;
        Object oAuthHmacSigner = new OAuthHmacSigner();
        oAuthHmacSigner.clientSharedSecret = jdoGetconsumerSecret(this);
        oAuthHmacSigner.tokenSharedSecret = jdoGettempTokenSecret(this);
        oAuthGetAccessToken.signer = oAuthHmacSigner;
        oAuthGetAccessToken.consumerKey = jdoGetconsumerKey(this);
        oAuthGetAccessToken.verifier = str;
        OAuthCredentialsResponse execute = oAuthGetAccessToken.execute();
        oAuthHmacSigner.tokenSharedSecret = execute.tokenSecret;
        return new OAuthHmacCredential(jdoGetuserId(this), jdoGetconsumerKey(this), jdoGetconsumerSecret(this), execute.tokenSecret, execute.token);
    }

    public String getAuthorizationUrl() {
        return this.authorizationUrl;
    }

    protected final void jdoCopyField(OAuthHmacThreeLeggedFlow oAuthHmacThreeLeggedFlow, int i) {
        switch (i) {
            case 0:
                this.authorizationServerUrl = oAuthHmacThreeLeggedFlow.authorizationServerUrl;
                return;
            case 1:
                this.consumerKey = oAuthHmacThreeLeggedFlow.consumerKey;
                return;
            case 2:
                this.consumerSecret = oAuthHmacThreeLeggedFlow.consumerSecret;
                return;
            case 3:
                this.tempToken = oAuthHmacThreeLeggedFlow.tempToken;
                return;
            case 4:
                this.tempTokenSecret = oAuthHmacThreeLeggedFlow.tempTokenSecret;
                return;
            case 5:
                this.userId = oAuthHmacThreeLeggedFlow.userId;
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
        } else if (obj instanceof OAuthHmacThreeLeggedFlow) {
            OAuthHmacThreeLeggedFlow oAuthHmacThreeLeggedFlow = (OAuthHmacThreeLeggedFlow) obj;
            if (this.jdoStateManager != oAuthHmacThreeLeggedFlow.jdoStateManager) {
                throw new IllegalArgumentException("state managers do not match");
            }
            int length = iArr.length - 1;
            if (length >= 0) {
                do {
                    jdoCopyField(oAuthHmacThreeLeggedFlow, iArr[length]);
                    length--;
                } while (length >= 0);
            }
        } else {
            throw new IllegalArgumentException("object is not an object of type com.google.api.client.extensions.auth.helpers.oauth.OAuthHmacThreeLeggedFlow");
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
            objectIdFieldConsumer.storeStringField(5, ((StringIdentity) obj).getKey());
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
        OAuthHmacThreeLeggedFlow oAuthHmacThreeLeggedFlow = new OAuthHmacThreeLeggedFlow();
        oAuthHmacThreeLeggedFlow.jdoFlags = (byte) 1;
        oAuthHmacThreeLeggedFlow.jdoStateManager = stateManager;
        return oAuthHmacThreeLeggedFlow;
    }

    public javax.jdo.spi.PersistenceCapable jdoNewInstance(StateManager stateManager, Object obj) {
        OAuthHmacThreeLeggedFlow oAuthHmacThreeLeggedFlow = new OAuthHmacThreeLeggedFlow();
        oAuthHmacThreeLeggedFlow.jdoFlags = (byte) 1;
        oAuthHmacThreeLeggedFlow.jdoStateManager = stateManager;
        oAuthHmacThreeLeggedFlow.jdoCopyKeyFieldsFromObjectId(obj);
        return oAuthHmacThreeLeggedFlow;
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
                this.jdoStateManager.providedStringField(this, i, this.authorizationServerUrl);
                return;
            case 1:
                this.jdoStateManager.providedStringField(this, i, this.consumerKey);
                return;
            case 2:
                this.jdoStateManager.providedStringField(this, i, this.consumerSecret);
                return;
            case 3:
                this.jdoStateManager.providedStringField(this, i, this.tempToken);
                return;
            case 4:
                this.jdoStateManager.providedStringField(this, i, this.tempTokenSecret);
                return;
            case 5:
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
                this.authorizationServerUrl = this.jdoStateManager.replacingStringField(this, i);
                return;
            case 1:
                this.consumerKey = this.jdoStateManager.replacingStringField(this, i);
                return;
            case 2:
                this.consumerSecret = this.jdoStateManager.replacingStringField(this, i);
                return;
            case 3:
                this.tempToken = this.jdoStateManager.replacingStringField(this, i);
                return;
            case 4:
                this.tempTokenSecret = this.jdoStateManager.replacingStringField(this, i);
                return;
            case 5:
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

    public Credential loadCredential(PersistenceManager persistenceManager) {
        try {
            return (Credential) persistenceManager.getObjectById(OAuthHmacCredential.class, jdoGetuserId(this));
        } catch (JDOObjectNotFoundException e) {
            return null;
        }
    }

    public void setHttpTransport(HttpTransport httpTransport) {
        this.transport = (HttpTransport) Preconditions.checkNotNull(httpTransport);
    }

    public void setJsonFactory(JsonFactory jsonFactory) {
    }
}
