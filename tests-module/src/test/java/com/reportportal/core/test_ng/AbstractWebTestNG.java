package com.reportportal.core.test_ng;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.reportportal.ui.browser.WebDriverHolder;
import com.reportportal.ui.WebConfiguration;

public abstract class AbstractWebTestNG extends AbstractTestNG
{

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
