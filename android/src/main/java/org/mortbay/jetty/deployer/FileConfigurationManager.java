package org.mortbay.jetty.deployer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.Properties;
import org.mortbay.resource.Resource;

public class FileConfigurationManager implements ConfigurationManager {
    private Resource _file;
    private Properties _properties = new Properties();

    private void loadProperties() throws FileNotFoundException, IOException {
        if (this._properties.isEmpty()) {
            this._properties.load(this._file.getInputStream());
        }
    }

    public Map getProperties() {
        try {
            loadProperties();
            return this._properties;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public void setFile(String str) throws MalformedURLException, IOException {
        this._file = Resource.newResource(str);
    }
}
