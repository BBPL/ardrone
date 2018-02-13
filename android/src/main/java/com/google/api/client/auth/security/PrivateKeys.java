package com.google.api.client.auth.security;

import com.google.api.client.util.Base64;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

@Deprecated
public class PrivateKeys {
    private static final String BEGIN = "-----BEGIN PRIVATE KEY-----";
    private static final String END = "-----END PRIVATE KEY-----";

    private PrivateKeys() {
    }

    @Deprecated
    public static PrivateKey loadFromKeyStore(InputStream inputStream, String str, String str2, String str3) throws IOException, GeneralSecurityException {
        return loadFromKeyStore(KeyStore.getInstance(KeyStore.getDefaultType()), inputStream, str, str2, str3);
    }

    @Deprecated
    public static PrivateKey loadFromKeyStore(KeyStore keyStore, InputStream inputStream, String str, String str2, String str3) throws IOException, GeneralSecurityException {
        try {
            keyStore.load(inputStream, str.toCharArray());
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(str2, str3.toCharArray());
            return privateKey;
        } finally {
            inputStream.close();
        }
    }

    @Deprecated
    public static PrivateKey loadFromP12File(File file, String str, String str2, String str3) throws GeneralSecurityException, IOException {
        return loadFromKeyStore(KeyStore.getInstance("PKCS12"), new FileInputStream(file), str, str2, str3);
    }

    @Deprecated
    public static PrivateKey loadFromPkcs8PemFile(File file) throws IOException, GeneralSecurityException {
        return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(readFromPemFormattedFile(file)));
    }

    @Deprecated
    public static byte[] readFromPemFormattedFile(File file) throws IOException, GeneralSecurityException {
        byte[] bArr = new byte[((int) file.length())];
        DataInputStream dataInputStream = new DataInputStream(new FileInputStream(file));
        try {
            dataInputStream.readFully(bArr);
            String str = new String(bArr);
            if (!str.contains(BEGIN) || !str.contains(END)) {
                throw new GeneralSecurityException("File missing required BEGIN PRIVATE KEY or END PRIVATE KEY tags.");
            } else if (str.indexOf(BEGIN) + BEGIN.length() <= str.indexOf(END)) {
                return Base64.decodeBase64(str.substring(str.indexOf(BEGIN) + BEGIN.length(), str.indexOf(END)));
            } else {
                throw new GeneralSecurityException("END PRIVATE KEY tag found before BEGIN PRIVATE KEY tag.");
            }
        } finally {
            dataInputStream.close();
        }
    }
}
