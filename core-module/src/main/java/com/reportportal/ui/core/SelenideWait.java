package com.reportportal.ui.core;

import java.time.Duration;
import java.util.function.Function;

import com.codeborne.selenide.WebDriverRunner;
import com.reportportal.reporting.ReportService;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SelenideWait
{
    @Autowired
    private ReportService reportService;
    @Value("${ui.wait.timeout.seconds}")
    private Long defaultTimeout;
    @Value("${ui.polling.timeout.milliseconds}")
    private Long pollingTimeOutMilliSeconds;

    public <T> T until(Function<WebDriver, T> func, String message, Object... args)
    {
        reportService.debug(message, args);
        return getWait().until(driver -> func.apply(WebDriverRunner.getWebDriver()));
    }

    private FluentWait<WebDriver> getWait()
    {
        return new FluentWait<>(WebDriverRunner.getWebDriver()).withTimeout(Duration.ofSeconds(defaultTimeout))
                .pollingEvery(Duration.ofMillis(pollingTimeOutMilliSeconds)).ignoring(Exception.class);
    }
}
