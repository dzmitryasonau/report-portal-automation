package com.reportportal.core.junit;

import com.reportportal.asserts.VerifyThat;
import com.reportportal.config.SpringDomainConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@SpringJUnitConfig(classes = { SpringDomainConfig.class })
public abstract class AbstractJUnit
{

    @Autowired
    protected VerifyThat verifyThat;

}
