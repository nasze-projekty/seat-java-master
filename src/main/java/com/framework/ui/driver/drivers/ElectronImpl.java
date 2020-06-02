package com.framework.ui.driver.drivers;

import com.framework.common.properties.Property;
import com.framework.ui.driver.AbstractDriver;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class ElectronImpl extends AbstractDriver {

    private static URL remoteURL;

    static {
        try {
            remoteURL = new URL("http://localhost:9515");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ChromeOptions getCapabilities() {
        if (!Property.APP_PATH.isSpecified()) {
            throw new IllegalStateException(
                    "App path must be specified when using Electron!");
        }

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setBinary(Property.APP_PATH.getValue());
        return chromeOptions;
    }

    @Override
    public WebDriver getWebDriver(Capabilities capabilities) {
        return new RemoteWebDriver(remoteURL, capabilities);
    }
}
