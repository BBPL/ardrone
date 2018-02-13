package com.google.api.client.extensions.java7.auth.oauth2;

import com.google.api.client.extensions.java6.auth.oauth2.FileCredentialStore;
import com.google.api.client.json.JsonFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileCredentialStoreJava7 extends FileCredentialStore {
    public FileCredentialStoreJava7(File file, JsonFactory jsonFactory) throws IOException {
        super(file, jsonFactory);
    }

    protected boolean isSymbolicLink(File file) throws IOException {
        return Files.isSymbolicLink(file.toPath());
    }
}
