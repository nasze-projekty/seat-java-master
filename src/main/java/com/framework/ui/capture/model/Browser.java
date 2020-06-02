package com.framework.ui.capture.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.framework.common.properties.Property;
import com.framework.ui.UITestLifecycle;
import com.framework.ui.driver.DriverSetup;
import net.sf.uadetector.ReadableUserAgent;
import net.sf.uadetector.UserAgentStringParser;
import net.sf.uadetector.service.UADetectorServiceFactory;

import java.util.Optional;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Browser {

    public String name;
    public String version;
    public String device;
    public String platform;
    public String platformVersion;

    /**
     * Create browser object.
     */
    public Browser() {

        Optional<String> userAgent = UITestLifecycle.get().getUserAgent();
        if (userAgent.isPresent() && !userAgent.get().isEmpty()) {
            UserAgentStringParser uaParser = UADetectorServiceFactory.getResourceModuleParser();
            ReadableUserAgent agent = uaParser.parse(userAgent.get());

            this.name = agent.getName();
            this.version = agent.getVersionNumber().toVersionString();
            this.device = agent.getDeviceCategory().getName();
            this.platform = agent.getOperatingSystem().getName();
            this.platformVersion = agent.getOperatingSystem().getVersionNumber().toVersionString();

        } else {
            // Fall-back to the Property class
            if (Property.BROWSER.isSpecified()) {
                this.name = Property.BROWSER.getValue().toLowerCase();
            } else {
                this.name = DriverSetup.DEFAULT_BROWSER.toString();
            }
            if (Property.BROWSER_VERSION.isSpecified()) {
                this.version = Property.BROWSER_VERSION.getValue();
            }
            if (Property.DEVICE.isSpecified()) {
                this.device = Property.DEVICE.getValue();
            }
            if (Property.PLATFORM.isSpecified()) {
                this.platform = Property.PLATFORM.getValue();
            }
            if (Property.PLATFORM_VERSION.isSpecified()) {
                this.platformVersion = Property.PLATFORM_VERSION.getValue();
            }
        }
    }

}
