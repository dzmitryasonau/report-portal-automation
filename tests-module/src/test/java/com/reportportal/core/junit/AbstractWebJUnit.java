package com.reportportal.core.junit;

import com.reportportal.browser.WebDriverHolder;
import com.reportportal.models.WebConfiguration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractWebJUnit extends AbstractJUnit
{

    @Autowired
    private WebConfiguration webConfiguration;

    @BeforeEach
    public void beforeEachMethodSetupWeb()
    {
        WebDriverHolder.getInstance().setDriver(webConfiguration);
    }

    @AfterEach
    public void afterEachMethodTearDownWeb()
    {
        WebDriverHolder.getInstance().tearDown();
    }
}
