package com.reportportal.core.test_ng;

import com.reportportal.asserts.VerifyThat;
import com.reportportal.config.SpringDomainConfig;
import com.reportportal.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Listeners;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = { SpringDomainConfig.class })
@Listeners({CustomReportPortalListener.class, CustomExecutionListener.class})
public abstract class AbstractTestNG extends AbstractTestNGSpringContextTests
{
    @Autowired
    protected VerifyThat verifyThat;

    @AfterSuite
    public void teardownAfter(ITestContext context) {
        new NotificationService().onFinish(context);
    }

}
