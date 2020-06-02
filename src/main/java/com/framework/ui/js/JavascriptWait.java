package com.framework.ui.js;

import com.framework.ui.ExtraExpectedConditions;
import com.paulhammant.ngwebdriver.NgWebDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;

/**
 * Frameworkium implementation of waiting for JS events on page-load.
 */
public class JavascriptWait {

    private final Wait<WebDriver> wait;
    private final JavascriptExecutor javascriptExecutor;


    public JavascriptWait(
            JavascriptExecutor javascriptExecutor, Wait<WebDriver> wait) {
        this.wait = wait;
        this.javascriptExecutor = javascriptExecutor;
    }

    /**
     * Default entry to {@link JavascriptWait}.
     * The following actions are waited for:
     * <ol>
     * <li>Document state to be ready</li>
     * <li>If page is using Angular, it will detect and wait</li>
     * </ol>
     */
    public void waitForJavascriptEventsOnLoad() {
        waitForDocumentReady();
        waitForAngular();
    }

    /**
     * If a page is using a supported JS framework, it will wait until it's ready.
     */
    public void waitForJavascriptFramework() {
        waitForAngular();
    }

    private void waitForDocumentReady() {
        wait.until(ExtraExpectedConditions.documentBodyReady());
    }

    private void waitForAngular() {
        new NgWebDriver(javascriptExecutor).waitForAngularRequestsToFinish();
    }

    public void waitForAngularLoad() {
        String angularReadyScript = "return angular.element(document).injector().get('$http').pendingRequests.length === 0";
        angularLoads(angularReadyScript);
    }

    private void angularLoads(String angularReadyScript) {
        try {
            ExpectedCondition<Boolean> angularLoad = driver -> Boolean.valueOf(((JavascriptExecutor) driver)
                    .executeScript(angularReadyScript).toString());

            boolean angularReady = Boolean.valueOf(javascriptExecutor.executeScript(angularReadyScript).toString());

            if (!angularReady) {
                wait.until(angularLoad);
            }
        } catch (WebDriverException ignored) {
        }
    }

}
