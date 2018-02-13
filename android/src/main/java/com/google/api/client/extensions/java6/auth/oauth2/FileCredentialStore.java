package com.google.api.client.extensions.java6.auth.oauth2;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.CredentialStore;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonGenerator;
import com.google.api.client.util.Charsets;
import com.google.api.client.util.Preconditions;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public class FileCredentialStore implements CredentialStore {
    private static final boolean IS_WINDOWS = (File.separatorChar == '\\');
    private static final Logger LOGGER = Logger.getLogger(FileCredentialStore.class.getName());
    private FilePersistedCredentials credentials = new FilePersistedCredentials();
    private final File file;
    private final JsonFactory jsonFactory;
    private final Lock lock = new ReentrantLock();

    public FileCredentialStore(File file, JsonFactory jsonFactory) throws IOException {
        this.file = (File) Preconditions.checkNotNull(file);
        this.jsonFactory = (JsonFactory) Preconditions.checkNotNull(jsonFactory);
        File parentFile = file.getCanonicalFile().getParentFile();
        if (parentFile != null && !parentFile.exists() && !parentFile.mkdirs()) {
            throw new IOException("unable to create parent directory: " + parentFile);
        } else if (isSymbolicLink(file)) {
            throw new IOException("unable to use a symbolic link: " + file);
        } else if (file.createNewFile()) {
            if (!(file.setReadable(false, false) && file.setWritable(false, false) && file.setExecutable(false, false))) {
                LOGGER.warning("unable to change file permissions for everybody: " + file);
            }
            if (file.setReadable(true) && file.setWritable(true)) {
                save();
                return;
            }
            throw new IOException("unable to set file permissions: " + file);
        } else {
            loadCredentials(file);
        }
    }

    private void loadCredentials(File file) throws IOException {
        InputStream fileInputStream = new FileInputStream(file);
        try {
            this.credentials = (FilePersistedCredentials) this.jsonFactory.fromInputStream(fileInputStream, FilePersistedCredentials.class);
        } finally {
            fileInputStream.close();
        }
    }

    private void save() throws IOException {
        OutputStream fileOutputStream = new FileOutputStream(this.file);
        try {
            JsonGenerator createJsonGenerator = this.jsonFactory.createJsonGenerator(fileOutputStream, Charsets.UTF_8);
            createJsonGenerator.serialize(this.credentials);
            createJsonGenerator.close();
        } finally {
            fileOutputStream.close();
        }
    }

    public void delete(String str, Credential credential) throws IOException {
        this.lock.lock();
        try {
            this.credentials.delete(str);
            save();
        } finally {
            this.lock.unlock();
        }
    }

    protected boolean isSymbolicLink(File file) throws IOException {
        if (!IS_WINDOWS) {
            if (file.getParent() != null) {
                file = new File(file.getParentFile().getCanonicalFile(), file.getName());
            }
            if (!file.getCanonicalFile().equals(file.getAbsoluteFile())) {
                return true;
            }
        }
        return false;
    }

    public boolean load(String str, Credential credential) {
        this.lock.lock();
        try {
            boolean load = this.credentials.load(str, credential);
            return load;
        } finally {
            this.lock.unlock();
        }
    }

    public void store(String str, Credential credential) throws IOException {
        this.lock.lock();
        try {
            this.credentials.store(str, credential);
            save();
        } finally {
            this.lock.unlock();
        }
    }
}
