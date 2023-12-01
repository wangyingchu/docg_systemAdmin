package com.viewfunction.docg.util.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class SystemAdminCfgPropertiesHandler {

    public static String TEMP_FILES_STORAGE_LOCATION = "TEMP_FILES_STORAGE_LOCATION";
    public static String MAX_SIZE_OF_FILE_IN_MB_FOR_UPLOAD = "MAX_SIZE_OF_FILE_IN_MB_FOR_UPLOAD";
    public static String SESSION_MAX_INACTIVE_INTERVAL_IN_SECOND = "SESSION_MAX_INACTIVE_INTERVAL_IN_SECOND";
    public static String ENABLE_USER_LOCK_APPLICATION = "ENABLE_USER_LOCK_APPLICATION";
    public static String USER_NAME = "USER_NAME";
    public static String USER_PWD = "USER_PWD";
    private static Properties _properties;

    public static String getPropertyValue(String propertyName) {
        if(_properties == null){
            String configPath = "SystemAdminCfg.properties";
            _properties = new Properties();
            try {
                _properties.load(new FileInputStream(configPath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return _properties.getProperty(propertyName);
    }
}
