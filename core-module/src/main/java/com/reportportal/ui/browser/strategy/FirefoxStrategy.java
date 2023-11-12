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
    protected WebDriver getLocalDriverInstance()
    {
        FirefoxDriver driver = new FirefoxDriver(new FirefoxOptions());
        driver.manage().window().maximize();
        return driver;
    }

    @Override
    protected DriverManagerType getDriverManagerType()
    {
        return DriverManagerType.FIREFOX;
    }
}
