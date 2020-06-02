package com.framework.ui.driver.drivers;

import com.framework.common.properties.Property;
import com.framework.ui.driver.AbstractDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class GridImpl extends AbstractDriver {

    private URL remoteURL;
    private Capabilities capabilities;

    /**
     * Implementation of driver for the Selenium Grid .
     */
    public GridImpl(Capabilities capabilities) {
        this.capabilities = capabilities;
        try {
            this.remoteURL = new URL(Property.GRID_URL.getValue());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Capabilities getCapabilities() {
        MutableCapabilities mutableCapabilities = new MutableCapabilities(capabilities);
        if (Property.BROWSER_VERSION.isSpecified()) {
            mutableCapabilities.setCapability("version", Property.BROWSER_VERSION.getValue());
        }
        if (Property.PLATFORM.isSpecified()) {
            mutableCapabilities.setCapability("platform", Property.PLATFORM_VERSION.getValue());
        }
        if (Property.APPLICATION_NAME.isSpecified()) {
            mutableCapabilities.setCapability("applicationName", Property.APPLICATION_NAME.getValue());
        }
        return mutableCapabilities;
    }

    @Override
    public WebDriver getWebDriver(Capabilities capabilities) {
        return new RemoteWebDriver(remoteURL, capabilities);
    }
}
