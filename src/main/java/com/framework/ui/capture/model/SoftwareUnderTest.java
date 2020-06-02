package com.framework.ui.capture.model;

import com.framework.common.properties.Property;

public class SoftwareUnderTest {

    public String name;
    public String version;

    /**
     * Software under test object.
     */
    public SoftwareUnderTest() {
        if (Property.SUT_NAME.isSpecified()) {
            this.name = Property.SUT_NAME.getValue();
        }
        if (Property.SUT_VERSION.isSpecified()) {
            this.version = Property.SUT_VERSION.getValue();
        }
    }
}
