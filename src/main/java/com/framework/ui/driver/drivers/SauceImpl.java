package com.framework.ui.driver.drivers;

import com.framework.common.properties.Property;
import com.framework.ui.driver.AbstractDriver;
import com.framework.ui.driver.Driver;
import com.framework.ui.driver.DriverSetup;
import com.framework.ui.driver.remotes.Sauce;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.net.URL;

public class SauceImpl extends AbstractDriver {

    private DriverSetup.Platform platform;
    private Capabilities capabilities;
    private URL remoteURL;

    public SauceImpl(DriverSetup.Platform platform, Capabilities capabilities) {
        this.platform = platform;
        this.capabilities = capabilities;
        this.remoteURL = Sauce.getURL();
    }

    @Override
    public Capabilities getCapabilities() {
        MutableCapabilities mutableCapabilities;
        if (Driver.isNative()) {
            mutableCapabilities = getAppiumCapabilities();
        } else {
            mutableCapabilities = getCapabilitiesBasedOnPlatform();
        }
        mutableCapabilities.setCapability("capture-html", true);
        mutableCapabilities.setCapability("sauce-advisor", false);
        mutableCapabilities.setCapability("build", Property.BUILD.getValue());
        return mutableCapabilities;
    }

    @Override
    public WebDriver getWebDriver(Capabilities capabilities) {
        return new RemoteWebDriver(remoteURL, capabilities);
    }

    private MutableCapabilities getCapabilitiesBasedOnPlatform() {
        switch (platform) {
            case WINDOWS:
                return getDesktopCapabilities("Windows");
            case OSX:
                return getDesktopCapabilities("OS X");
            case ANDROID:
                return getAndroidCapabilities();
            case IOS:
                return getIOSCapabilities();
            default:
                throw new IllegalStateException(
                        "Unrecognised platform: " + platform);
        }
    }

    private MutableCapabilities getDesktopCapabilities(String platformName) {
        if (!Property.PLATFORM_VERSION.isSpecified()) {
            throw new IllegalArgumentException(
                    "Platform version required for " + platformName + "  SauceLabs!");
        }
        MutableCapabilities caps = new MutableCapabilities(capabilities);
        caps.setCapability(
                "platform", platformName + " " + Property.PLATFORM_VERSION.getValue());
        if (Property.BROWSER_VERSION.isSpecified()) {
            caps.setCapability("version", Property.BROWSER_VERSION.getValue());
        }
        return caps;
    }

    private MutableCapabilities getAndroidCapabilities() {
        MutableCapabilities caps = new MutableCapabilities(capabilities);
        caps.merge(DesiredCapabilities.android());
        caps.setCapability("platform", "Linux");
        if (Property.PLATFORM_VERSION.isSpecified()) {
            caps.setCapability("version", Property.PLATFORM_VERSION.getValue());
        }
        caps.setCapability("deviceName", "Android Emulator");
        setPortraitOrientation(caps);
        return caps;
    }

    private MutableCapabilities getIOSCapabilities() {
        MutableCapabilities caps = new MutableCapabilities(capabilities);
        caps.merge(DesiredCapabilities.iphone());
        caps.setCapability("platform", "OS X 10.10");
        if (Property.PLATFORM_VERSION.isSpecified()) {
            caps.setCapability("version", Property.PLATFORM_VERSION.getValue());
        }
        if (Property.DEVICE.isSpecified()) {
            caps.setCapability("deviceName", Property.DEVICE.getValue() + " Simulator");
        }
        setPortraitOrientation(caps);
        return caps;
    }

    private MutableCapabilities getAppiumCapabilities() {
        MutableCapabilities caps = new MutableCapabilities(capabilities);
        caps.setCapability(
                "app", "sauce-storage:" + new File(Property.APP_PATH.getValue()).getName());
        caps.setCapability("appiumVersion", "1.4.10");
        setPortraitOrientation(caps);
        caps.setCapability("browserName", "");
        switch (platform) {
            case IOS:
                caps.merge(DesiredCapabilities.iphone());
                return getAppiumCapabilities(caps, "iOS", "Simulator");
            case ANDROID:
                caps.merge(DesiredCapabilities.android());
                return getAppiumCapabilities(caps, "Android", "Emulator");
            default:
                throw new IllegalStateException("Appium is only available on iOS/Android");
        }
    }

    private MutableCapabilities getAppiumCapabilities(
            Capabilities commonCaps, String platformName, String emulatorOrSimulator) {
        MutableCapabilities caps = new MutableCapabilities(commonCaps);
        if (Property.DEVICE.isSpecified()) {
            caps.setCapability(
                    "deviceName", Property.DEVICE.getValue() + " " + emulatorOrSimulator);
        }
        caps.setCapability("platformName", platformName);
        if (Property.PLATFORM_VERSION.isSpecified()) {
            caps.setCapability("platformVersion", Property.PLATFORM_VERSION.getValue());
        }
        return caps;
    }

    private void setPortraitOrientation(MutableCapabilities caps) {
        caps.setCapability("deviceOrientation", "portrait");
    }
}
