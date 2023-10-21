package com.reportportal.asserts;

import com.reportportal.reporting.ReportService;

import org.assertj.core.api.SoftAssertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VerifyThat
{

    @Autowired
    private ReportService reportService;

    public <T> VerifyThat actualIsEqualToExpected(T expected, T actual, String startMessage)
    {
        ThreadLocal<SoftAssertions> softAssertions = ThreadLocal.withInitial(SoftAssertions::new);
        String message = startMessage + String.format(" expected: '%s' found: '%s'", expected, actual);
        reportService.info(message);
        softAssertions.get().assertThat(actual).as(message).isEqualTo(expected);
        softAssertions.get().assertAll();
        return this;
    }

    public <T> VerifyThat actualIsContainExpected(T expected, T actual, String startMessage)
    {
        ThreadLocal<SoftAssertions> softAssertions = ThreadLocal.withInitial(SoftAssertions::new);
        String message = startMessage + String.format(" expected: '%s' found: '%s'", expected, actual);
        reportService.info(message);
        softAssertions.get().assertThat(actual).asString().as(message).contains(expected.toString());
        softAssertions.get().assertAll();
        return this;
    }

    public VerifyThat isTrue(Boolean expected, String expectedResult)
    {
        String message = String.format("Verify that condition is true:%nExpected %s", expectedResult);
        reportService.info(message);
        ThreadLocal<SoftAssertions> softAssertions = ThreadLocal.withInitial(SoftAssertions::new);
        softAssertions.get().assertThat(expected).as(message).isTrue();
        return this;
    }
}
