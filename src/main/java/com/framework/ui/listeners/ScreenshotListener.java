package com.framework.ui.listeners;

import com.framework.common.properties.Property;
import com.framework.ui.UITestLifecycle;
import com.framework.ui.driver.DriverSetup;
import com.framework.ui.tests.BaseUITest;
import com.framework.ui.capture.ScreenshotCapture;
import io.qameta.allure.Attachment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.*;

public class ScreenshotListener extends TestListenerAdapter {

    private static final Logger logger = LogManager.getLogger();
    private final boolean captureEnabled = ScreenshotCapture.isRequired();

    @Override
    public void onTestFailure(ITestResult failingTest) {
        if (!captureEnabled && isScreenshotSupported(failingTest)) {
            takeScreenshotAndSaveLocally(failingTest.getName());
        }
    }

    @Override
    public void onTestSkipped(ITestResult skippedTest) {
        if (!captureEnabled && isScreenshotSupported(skippedTest)) {
            takeScreenshotAndSaveLocally(skippedTest.getName());
        }
    }

    private void takeScreenshotAndSaveLocally(String testName) {
        takeScreenshotAndSaveLocally(
                testName, (TakesScreenshot) UITestLifecycle.get().getWebDriver());
    }

    private void takeScreenshotAndSaveLocally(String testName, TakesScreenshot driver) {
        String screenshotDirectory = System.getProperty("screenshotDirectory");
        if (screenshotDirectory == null) {
            screenshotDirectory = "screenshots";
        }
        String fileName = String.format(
                "%s_%s.png",
                System.currentTimeMillis(),
                testName);
        Path screenshotPath = Paths.get(screenshotDirectory);
        Path absolutePath = screenshotPath.resolve(fileName);
        if (createScreenshotDirectory(screenshotPath)) {
            writeScreenshotToFile(driver, absolutePath);
            logger.info("Written screenshot to " + absolutePath);
        } else {
            logger.error("Unable to create " + screenshotPath);
        }
    }

    private boolean createScreenshotDirectory(Path screenshotDirectory) {
        try {
            Files.createDirectories(screenshotDirectory);
        } catch (IOException e) {
            logger.error("Error creating screenshot directory", e);
        }
        return Files.isDirectory(screenshotDirectory);
    }

    @Attachment(value = "Screenshot on failure", type = "image/png")
    private byte[] writeScreenshotToFile(TakesScreenshot driver, Path screenshot) {
        try (OutputStream screenshotStream = Files.newOutputStream(screenshot)) {
            byte[] bytes = driver.getScreenshotAs(OutputType.BYTES);
            screenshotStream.write(bytes);
            screenshotStream.close();
            return bytes;
        } catch (IOException e) {
            logger.error("Unable to write " + screenshot, e);
        } catch (WebDriverException e) {
            logger.error("Unable to take screenshot.", e);
        }
        return null;
    }

    private boolean isScreenshotSupported(ITestResult testResult) {
        boolean isElectron = Property.BROWSER.isSpecified()
                && DriverSetup.Browser.ELECTRON.equals(DriverSetup.Browser.valueOf(Property.BROWSER.getValue().toUpperCase()));
        boolean isUITest = testResult.getInstance() instanceof BaseUITest;
        return isUITest && !isElectron;
    }
}
