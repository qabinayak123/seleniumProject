package com.qualitrix.common.utils;

import org.apache.commons.configuration.ConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropUtils {

    private Map<String, Object> globalVariable = new HashMap<String, Object>();

    /**
     * Returns null if something went wrong
     */
    public Properties getProperties(String filePath) {
        return this.getProperties(new File(filePath));
    }

    public Properties getProperties(File file) {
        try (InputStream inputStream = new FileInputStream(file)) {
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addProperties(Properties properties,
                              String filePath) throws ConfigurationException {
        this.addProperties(properties, new File(filePath));
    }

    public void addProperties(Properties properties1, File file) throws ConfigurationException {
        Properties oldProp = this.getProperties(file);
        oldProp.putAll(properties1);
        try (OutputStream outputStream = new FileOutputStream(file)) {
            oldProp.store(outputStream, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setGlobalVariable(String key, Object value) {
        this.globalVariable.put(key, value);
    }

    public Object getGlobalVariable(String key) {
        return this.globalVariable.get(key);
    }

    public String getFileExtension(String fileName) {
        String extension = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1);
        }
        return extension;
    }
}
