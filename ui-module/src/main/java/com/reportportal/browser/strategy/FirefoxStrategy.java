package com.reportportal.browser.strategy;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;

import io.github.bonigarcia.wdm.config.DriverManagerType;
import com.reportportal.models.WebConfiguration;

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
    protected AbstractDriverOptions<?> getSpecificRemoteDriverOptions()
    {
        return new FirefoxOptions();
    }

    @Override
    protected DriverManagerType getDriverManagerType()
    {
        return DriverManagerType.FIREFOX;
    }
}
