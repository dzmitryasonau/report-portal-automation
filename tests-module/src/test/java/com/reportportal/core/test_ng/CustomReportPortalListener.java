package com.reportportal.core.test_ng;

import com.epam.reportportal.testng.BaseTestNGListener;
import com.epam.reportportal.testng.ITestNGService;
import com.epam.reportportal.utils.MemoizingSupplier;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.ITestResult;

import java.util.function.Supplier;

public class CustomReportPortalListener extends BaseTestNGListener {

    public static final Supplier<ITestNGService> SERVICE = new MemoizingSupplier<>(CustomTestNGService::new);

    public CustomReportPortalListener() {
        super(SERVICE.get());
    }

    @Override
    public void onExecutionStart() {
        super.onExecutionStart();
    }

    @Override
    public void onExecutionFinish() {
        super.onExecutionFinish();
    }

    @Override
    public void onStart(ISuite suite) {
        super.onStart(suite);
    }

    @Override
    public void onFinish(ISuite suite) {
        super.onFinish(suite);
    }

    @Override
    public void onStart(ITestContext testContext) {
        //do nothing
    }

    @Override
    public void onFinish(ITestContext testContext) {
        super.onFinish(testContext);
    }

    @Override
    public void onTestStart(ITestResult testResult) {
        super.onTestStart(testResult);
    }

    @Override
    public void onTestSuccess(ITestResult testResult) {
        super.onTestSuccess(testResult);
    }

    @Override
    public void onTestFailure(ITestResult testResult) {
        super.onTestFailure(testResult);
    }

    @Override
    public void onTestSkipped(ITestResult testResult) {
        super.onTestSkipped(testResult);
    }

    @Override
    public void beforeConfiguration(ITestResult testResult) {
        //don't post any before/after TestNG methods
    }

    @Override
    public void onConfigurationFailure(ITestResult testResult) {
        SERVICE.get().startConfiguration(testResult);
        super.onConfigurationFailure(testResult);
    }

    @Override
    public void onConfigurationSuccess(ITestResult testResult) {
        //don't post any before/after TestNG methods
    }

    @Override
    public void onConfigurationSkip(ITestResult testResult) {
        super.onConfigurationSkip(testResult);
    }

}

