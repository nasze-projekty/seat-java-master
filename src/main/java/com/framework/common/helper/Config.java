package com.framework.common.helper;

import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final Properties properties = new Properties();
    private static final String environmentPrefix;

    static {
        initPropertiesFromFile();
        environmentPrefix = System.getProperty("environmentKey", "dev") + ".";
    }

    private static void initPropertiesFromFile() {
        try (InputStream stream =
                     Config.class.getResourceAsStream("/properties")) {
            properties.load(stream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String getProperty(String s) {
        return properties.getProperty(environmentPrefix + s);
    }

    /* getters for environment properties */
    public static String getBaseURL() {
        return getProperty("baseUrl");
    }


}
