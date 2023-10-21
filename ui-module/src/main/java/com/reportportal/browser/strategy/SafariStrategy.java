package com.reportportal.browser.strategy;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;

import io.github.bonigarcia.wdm.config.DriverManagerType;
import com.reportportal.models.WebConfiguration;

public class SafariStrategy extends AbstractDriverStrategy {

    public SafariStrategy(WebConfiguration webConfiguration) {
        super(webConfiguration);
    }

    @Override
    protected WebDriver getLocalDriverInstance() {
        SafariDriver driver = new SafariDriver(new SafariOptions());
        driver.manage().window().maximize();
        return driver;
    }

    @Override
    protected AbstractDriverOptions<?> getSpecificRemoteDriverOptions() {
        return new SafariOptions();
    }

    @Override
    protected DriverManagerType getDriverManagerType() {
        return DriverManagerType.FIREFOX;
    }
}
