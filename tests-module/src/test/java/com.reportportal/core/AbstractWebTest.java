package com.reportportal.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.reportportal.browser.WebDriverHolder;
import com.reportportal.models.WebConfiguration;

public abstract class AbstractWebTest extends AbstractTest {

    @Autowired
    private WebConfiguration webConfiguration;

    @BeforeMethod(alwaysRun = true)
    public void beforeEachMethodSetupWeb() {
        WebDriverHolder.getInstance().setDriver(webConfiguration);
    }

    @AfterMethod(alwaysRun = true)
    public void afterEachMethodTearDownWeb() {
        WebDriverHolder.getInstance().tearDown();
    }
}
