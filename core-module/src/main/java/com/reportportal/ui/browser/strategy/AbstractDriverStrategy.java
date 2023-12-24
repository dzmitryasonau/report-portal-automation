package com.reportportal.ui.browser.strategy;

import java.util.Objects;

import com.reportportal.ui.WebConfiguration;

import org.openqa.selenium.WebDriver;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.DriverManagerType;

public abstract class AbstractDriverStrategy
{
    private final WebConfiguration webConfiguration;

    protected AbstractDriverStrategy(WebConfiguration webConfiguration)
    {
        this.webConfiguration = webConfiguration;
    }

    protected abstract WebDriver getLocalDriverInstance();

    protected abstract DriverManagerType getDriverManagerType();

    public WebDriver getDriver()
    {
        WebDriverManager instance = WebDriverManager.getInstance(getDriverManagerType());
        String browserVersion = webConfiguration.getBrowserVersion();
        boolean isLatest = Objects.nonNull(browserVersion) && "latest".equalsIgnoreCase(browserVersion);
        if (!isLatest)
        {
            instance = instance.browserVersion(browserVersion);
        }
        instance.setup();
        return getLocalDriverInstance();
    }
}
