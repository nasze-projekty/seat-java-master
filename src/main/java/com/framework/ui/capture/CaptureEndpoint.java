package com.framework.ui.capture;

import com.framework.common.properties.Property;
import com.framework.api.Endpoint;

/** The various Endpoints of Capture. */
enum CaptureEndpoint implements Endpoint {

    BASE_URI(Property.CAPTURE_URL.getValue()),
    EXECUTIONS(BASE_URI.url + "/executions"),
    SCREENSHOT(BASE_URI.url + "/screenshot");

    private String url;

    CaptureEndpoint(String url) {
        this.url = url;
    }

    @Override
    public String getUrl(Object... params) {
        return String.format(url, params);
    }

}
