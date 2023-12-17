package com.reportportal.ui.browser.strategy;

import com.reportportal.ui.WebConfiguration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import io.github.bonigarcia.wdm.config.DriverManagerType;

public class FirefoxStrategy extends AbstractDriverStrategy
{

    public FirefoxStrategy(WebConfiguration webConfiguration)
    {
        super(webConfiguration);
    }

    @Override
    protected WebDriver getLocalDriverInstance(Boolean isHeadless)
    {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("-headless");
        if (isHeadless) {
            options.addArguments("-headless");
        }
        FirefoxDriver driver = new FirefoxDriver(options);
        driver.manage().window().maximize();
        return driver;
    }

    @Override
    protected DriverManagerType getDriverManagerType()
    {
        return DriverManagerType.FIREFOX;
    }
}
