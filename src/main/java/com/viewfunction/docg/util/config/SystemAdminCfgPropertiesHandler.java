package com.viewfunction.docg.util.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class SystemAdminCfgPropertiesHandler {

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
