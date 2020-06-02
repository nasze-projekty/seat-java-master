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

    public static String getAuthTokenUsername() {
        return properties.getProperty("authtoken.username");
    }

    public static String getAuthTokenPassword() {
        return properties.getProperty("authtoken.password");
    }

    public static String getAuthorisedEmail() {
        return properties.getProperty("authorise.email");
    }

    public static String getAuthorisedUser() {
        return properties.getProperty("authorise.user");
    }

    public static String getAuthorisedPassword() {
        return properties.getProperty("authorise.password");
    }

    public static String getApiToken() {
        return properties.getProperty("api.token");
    }

    public static String getChatBotClintId() {
        return properties.getProperty("core.chatbot.clientId");
    }

    public static String getFeClintId() {
        return properties.getProperty("core.fe.clientId");
    }

    public static String getHiveClintId() {
        return properties.getProperty("core.hive.clientId");
    }

    public static String getSxsClintId() {
        return properties.getProperty("core.sxs.clientId");
    }

    public static String getCoreApiHeaderKey() {
        return properties.getProperty("header.key");
    }

    public static String getCoreApiHeaderValue() {
        return properties.getProperty("header.value");
    }


}
