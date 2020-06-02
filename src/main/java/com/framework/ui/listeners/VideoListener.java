package com.framework.ui.listeners;

import com.framework.ui.UITestLifecycle;
import com.framework.ui.video.VideoCapture;
import org.testng.*;

public class VideoListener extends TestListenerAdapter {

    @Override
    public void onTestStart(ITestResult iTestResult) {
        if (VideoCapture.isRequired()) {
            VideoCapture.saveTestSessionID(
                    iTestResult.getName(),
                    UITestLifecycle.get().getRemoteSessionId());
        }
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        if (VideoCapture.isRequired()) {
            VideoCapture videoCapture = new VideoCapture();
            iTestContext
                    .getFailedTests()
                    .getAllResults()
                    .stream()
                    .map(ITestResult::getName)
                    .forEach(videoCapture::fetchAndSaveVideo);
        }
    }

}
