package com.reportportal.core.test_ng;

import com.codeborne.selenide.Selenide;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.testng.ITestListener;
import org.testng.ITestResult;

@Component
public class SelenideScreenShotOnFailListener implements ITestListener
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SelenideScreenShotOnFailListener.class);

    @Override
    public void onTestFailure(ITestResult testResult)
    {
        LOGGER.info(testResult.getName() + " test is failed");
        LOGGER.info("Fail screenshot path: " + takeScreenShot(testResult));
    }

    private String takeScreenShot(ITestResult result)
    {
        String testName = result.id() + "/" + result.getMethod().getMethodName();
        return Selenide.screenshot(testName);
    }
}
