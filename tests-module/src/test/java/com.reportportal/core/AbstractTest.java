package com.reportportal.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Listeners;

import com.reportportal.asserts.VerifyThat;
import com.reportportal.config.SpringDomainConfig;
import core.test_ng.CustomExecutionListener;
import core.test_ng.CustomReportPortalListener;
import core.utils.NotificationService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = {SpringDomainConfig.class})
@Listeners({CustomExecutionListener.class, CustomReportPortalListener.class})
public abstract class AbstractTest extends AbstractTestNGSpringContextTests {
    @Autowired
    protected VerifyThat verifyThat;

    @AfterSuite(alwaysRun = true)
    public void teardownAfter(ITestContext context) {
        new NotificationService().onFinish(context);
    }

}
