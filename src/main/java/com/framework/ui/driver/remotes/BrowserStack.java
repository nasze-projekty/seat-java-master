package com.framework.ui.driver.remotes;

import com.framework.common.properties.Property;

import java.net.MalformedURLException;
import java.net.URL;

public class BrowserStack {

    private BrowserStack() {
        // hide default constructor for this util class
    }

    public static URL getURL() throws MalformedURLException {
        return new URL(String.format("https://%s:%s@hub-cloud.browserstack.com/wd/hub",
                System.getenv("BROWSER_STACK_USERNAME"),
                System.getenv("BROWSER_STACK_ACCESS_KEY")));
    }

    public static boolean isDesired() {
        return Property.BROWSER_STACK.getBoolean();
    }
}
