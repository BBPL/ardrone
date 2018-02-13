package org.mortbay.jetty.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

public class PKCS12Import {
    static void dumpChain(Certificate[] certificateArr) {
        for (int i = 0; i < certificateArr.length; i++) {
            if (certificateArr[i] instanceof X509Certificate) {
                X509Certificate x509Certificate = (X509Certificate) certificateArr[i];
                System.err.println(new StringBuffer().append("subject: ").append(x509Certificate.getSubjectDN()).toString());
                System.err.println(new StringBuffer().append("issuer: ").append(x509Certificate.getIssuerDN()).toString());
            }
        }
    }

    public static void main(String[] strArr) throws Exception {
        int i = 0;
        if (strArr.length < 1) {
            System.err.println("usage: java PKCS12Import {pkcs12file} [newjksfile]");
            System.exit(1);
        }
        File file = new File(strArr[0]);
        File file2 = strArr.length > 1 ? new File(strArr[1]) : new File("newstore.jks");
        if (!file.canRead()) {
            System.err.println(new StringBuffer().append("Unable to access input keystore: ").append(file.getPath()).toString());
            System.exit(2);
        }
        if (file2.exists() && !file2.canWrite()) {
            System.err.println(new StringBuffer().append("Output file is not writable: ").append(file2.getPath()).toString());
            System.exit(2);
        }
        KeyStore instance = KeyStore.getInstance("pkcs12");
        KeyStore instance2 = KeyStore.getInstance("jks");
        System.out.print("Enter input keystore passphrase: ");
        char[] readPassphrase = readPassphrase();
        System.out.print("Enter output keystore passphrase: ");
        char[] readPassphrase2 = readPassphrase();
        instance.load(new FileInputStream(file), readPassphrase);
        instance2.load(file2.exists() ? new FileInputStream(file2) : null, readPassphrase2);
        Enumeration aliases = instance.aliases();
        while (aliases.hasMoreElements()) {
            String str = (String) aliases.nextElement();
            System.err.println(new StringBuffer().append("Alias ").append(i).append(": ").append(str).toString());
            if (instance.isKeyEntry(str)) {
                System.err.println(new StringBuffer().append("Adding key for alias ").append(str).toString());
                instance2.setKeyEntry(str, instance.getKey(str, readPassphrase), readPassphrase2, instance.getCertificateChain(str));
            }
            i++;
        }
        OutputStream fileOutputStream = new FileOutputStream(file2);
        instance2.store(fileOutputStream, readPassphrase2);
        fileOutputStream.close();
    }

    static char[] readPassphrase() throws IOException {
        Object obj;
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        Object obj2 = new char[256];
        int i = 0;
        while (i < obj2.length) {
            char read = (char) inputStreamReader.read();
            switch (read) {
                case '\n':
                case '\r':
                    break;
                default:
                    obj2[i] = read;
                    i++;
            }
            obj = new char[i];
            System.arraycopy(obj2, 0, obj, 0, i);
            return obj;
        }
        obj = new char[i];
        System.arraycopy(obj2, 0, obj, 0, i);
        return obj;
    }
}
