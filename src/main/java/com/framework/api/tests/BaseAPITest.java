package com.framework.api.tests;

import com.framework.common.listeners.MethodInterceptor;
import com.framework.common.listeners.ResultLoggerListener;
import com.framework.common.listeners.TestListener;
import com.framework.common.reporting.allure.AllureProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners({MethodInterceptor.class,
        TestListener.class,
        ResultLoggerListener.class})
@Test(groups = "base-api")
public abstract class BaseAPITest {

    protected final Logger logger = LogManager.getLogger(this);

    /**
     * Creates the allure properties for the report, after the test run.
     */
    @AfterSuite(alwaysRun = true)
    public static void createAllureProperties() {
        AllureProperties.createAPI();
    }

}
