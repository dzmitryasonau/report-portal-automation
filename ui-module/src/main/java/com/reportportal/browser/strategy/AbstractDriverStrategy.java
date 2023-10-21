package com.reportportal.browser.strategy;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.DriverManagerType;
import com.reportportal.exceptions.InitializationException;
import com.reportportal.meta.RunType;
import com.reportportal.models.WebConfiguration;
import io.vavr.control.Try;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.Objects;

public abstract class AbstractDriverStrategy {
    private final WebConfiguration webConfiguration;

    protected AbstractDriverStrategy(WebConfiguration webConfiguration) {
        this.webConfiguration = webConfiguration;
    }

    protected abstract WebDriver getLocalDriverInstance();

    protected abstract AbstractDriverOptions<?> getSpecificRemoteDriverOptions();

    protected abstract DriverManagerType getDriverManagerType();

    public WebDriver getDriver() {
        return webConfiguration.getRunType() == RunType.LOCAL ?
                getLocalDriver() :
                getRemoteDriver();
    }

    private WebDriver getLocalDriver() {
        WebDriverManager instance = WebDriverManager.getInstance(getDriverManagerType());
        String browserVersion = webConfiguration.getBrowserVersion();
        boolean isLatest = Objects.nonNull(browserVersion) && "latest".equalsIgnoreCase(browserVersion);
        if (!isLatest) {
            instance = instance.browserVersion(browserVersion);
        }
        instance.setup();
        return getLocalDriverInstance();
    }

    private WebDriver getRemoteDriver() {
        String stringUrl = webConfiguration.getRemoteUrl();
        URL url = Try.of(() -> new URL(stringUrl))
                .getOrElseThrow(() -> new InitializationException("Unable to "));
        RemoteWebDriver driver = new RemoteWebDriver(url, getRemoteOptions());
        driver.setFileDetector(new LocalFileDetector());
        return driver;
    }

    private AbstractDriverOptions<?> getRemoteOptions() {
        AbstractDriverOptions<?> driverOptions = getSpecificRemoteDriverOptions();

        var browserVersion = webConfiguration.getBrowserVersion();
        var capabilities = webConfiguration.getRemoteCapabilities();

        driverOptions.setBrowserVersion(browserVersion);

        driverOptions.setCapability("sauce:options", capabilities);

        return driverOptions;
    }
}
