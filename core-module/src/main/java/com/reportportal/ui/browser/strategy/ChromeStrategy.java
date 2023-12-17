package com.reportportal.ui.browser.strategy;

import com.reportportal.ui.WebConfiguration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.config.DriverManagerType;

public class ChromeStrategy extends AbstractDriverStrategy {

    public ChromeStrategy(WebConfiguration webConfiguration) {
        super(webConfiguration);
    }

    @Override
    protected WebDriver getLocalDriverInstance(Boolean isHeadless) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--remote-allow-origins=*");
        if (isHeadless) {
            options.addArguments("--headless");
        }
        return new ChromeDriver(options);
    }

    @Override
    protected DriverManagerType getDriverManagerType() {
        return DriverManagerType.CHROME;
    }
}
