package com.reportportal.browser.strategy;

import io.github.bonigarcia.wdm.config.DriverManagerType;
import com.reportportal.models.WebConfiguration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;

public class ChromeStrategy extends AbstractDriverStrategy {

    public ChromeStrategy(WebConfiguration webConfiguration) {
        super(webConfiguration);
    }

    @Override
    protected WebDriver getLocalDriverInstance() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--remote-allow-origins=*");
        return new ChromeDriver(options);
    }

    @Override
    protected AbstractDriverOptions<?> getSpecificRemoteDriverOptions() {
        return new ChromeOptions();
    }

    @Override
    protected DriverManagerType getDriverManagerType() {
        return DriverManagerType.CHROME;
    }
}
