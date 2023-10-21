package com.reportportal.core.utils;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.testng.ITestResult;

@Service
public class SauceLabsTextNGCloudParameters {

    public Map<String, Object> getCloudParameters(ITestResult result) {
        String jobName = result.getMethod().getMethodName();
        boolean isPassed = result.isSuccess();
        return Map.of("name", jobName, "passed", isPassed);
    }
}
