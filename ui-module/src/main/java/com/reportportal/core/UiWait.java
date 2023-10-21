package com.reportportal.core;

import com.reportportal.browser.WebDriverHolder;
import com.reportportal.reporting.ReportService;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class UiWait {
    @Autowired
    private ReportService reportService;

    public <T> T until(Function<WebDriver, T> func, String message, Object... args) {
        reportService.debug(message, args);
        return getWait().until(func);
    }

    public FluentWait<WebDriver> getWait() {
        return WebDriverHolder.getInstance().getWebDriverWait();
    }
}
