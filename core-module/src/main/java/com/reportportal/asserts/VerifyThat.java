package com.reportportal.asserts;

import java.util.List;
import java.util.stream.Collectors;

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

    public <T> VerifyThat itemIsPresentInList(T expected, List<T> actual, String message) {
        decorateLogForList(expected, actual);
        ThreadLocal<SoftAssertions> softAssertions = ThreadLocal.withInitial(SoftAssertions::new);
        softAssertions.get().assertThat(actual).as(message).contains(expected);
        softAssertions.get().assertAll();
        return this;
    }

    public <T> VerifyThat itemIsNotPresentInList(T expected, List<T> actual, String message) {
        decorateLogForList(expected, actual);
        ThreadLocal<SoftAssertions> softAssertions = ThreadLocal.withInitial(SoftAssertions::new);
        softAssertions.get().assertThat(actual).as(message).doesNotContain(expected);
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

    public <T> VerifyThat listOfPrimitivesAreEqual(List<T> expected, List<T> actual) {
        ThreadLocal<SoftAssertions> softAssertionsList = ThreadLocal.withInitial(SoftAssertions::new);
        softAssertionsList.get().assertThat(actual).as("List with actual data is empty!").isNotEmpty();
        softAssertionsList.get().assertThat(actual.size()).as(String.format("Lists sizes are equal, Actual   - %s, expected - %s", actual.size(), expected.size())).isEqualTo(expected.size());
        softAssertionsList.get().assertThat(actual).isEqualTo(expected);
        softAssertionsList.get().assertAll();
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

    private <T> void decorateLogForList(T expected, List<T> actual) {
        reportService.info("Verify that 'EXPECTED' object is present in list: %s", expected.toString());
        reportService.info("Actual data in the list: %s", actual.stream().map(Object::toString).collect(Collectors.joining(", ")));
    }
}
