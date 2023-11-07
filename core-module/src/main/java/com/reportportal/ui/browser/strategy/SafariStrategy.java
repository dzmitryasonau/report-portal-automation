package com.reportportal.ui.browser.strategy;

import com.reportportal.ui.WebConfiguration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import io.github.bonigarcia.wdm.config.DriverManagerType;

public class SafariStrategy extends AbstractDriverStrategy
{

    public SafariStrategy(WebConfiguration webConfiguration)
    {
        super(webConfiguration);
    }

    @Override
    protected WebDriver getLocalDriverInstance()
    {
        SafariDriver driver = new SafariDriver(new SafariOptions());
        driver.manage().window().maximize();
        return driver;
    }

    @Override
    protected DriverManagerType getDriverManagerType()
    {
        return DriverManagerType.FIREFOX;
    }
}
