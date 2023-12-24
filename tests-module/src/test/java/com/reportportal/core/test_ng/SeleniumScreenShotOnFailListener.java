package com.reportportal.core.test_ng;

import com.reportportal.ui.browser.WebDriverHolder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.testng.ITestListener;
import org.testng.ITestResult;

@Component
public class SeleniumScreenShotOnFailListener implements ITestListener
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SeleniumScreenShotOnFailListener.class);

    @Override
    public void onTestFailure(ITestResult testResult)
    {
        LOGGER.info(testResult.getName() + " test is failed");
        LOGGER.info("Fail screenshot path: " + WebDriverHolder.getInstance().takeScreenshot(testResult));
    }
}
