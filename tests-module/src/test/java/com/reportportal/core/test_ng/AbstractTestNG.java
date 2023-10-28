package com.reportportal.core.test_ng;

import com.reportportal.asserts.VerifyThat;
import com.reportportal.config.SpringDomainConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = { SpringDomainConfig.class })
public abstract class AbstractTestNG extends AbstractTestNGSpringContextTests
{
    @Autowired
    protected VerifyThat verifyThat;

}
