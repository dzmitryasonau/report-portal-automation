package com.reportportal.browser;

import com.reportportal.browser.strategy.AbstractDriverStrategy;
import com.reportportal.browser.strategy.ChromeStrategy;
import com.reportportal.browser.strategy.FirefoxStrategy;
import com.reportportal.browser.strategy.SafariStrategy;
import com.reportportal.exceptions.InitializationException;
import com.reportportal.models.WebConfiguration;
import io.vavr.control.Try;

import java.util.Map;

public class DriverMapping {
    private static final Map<String, Class<? extends AbstractDriverStrategy>> DRIVER_MAPPINGS = Map.of(
            "chrome", ChromeStrategy.class,
            "firefox", FirefoxStrategy.class,
            "safari", SafariStrategy.class
    );

    public static AbstractDriverStrategy getDriverStrategy(WebConfiguration webConfiguration) {
        String driverName = webConfiguration.getBrowserName();

        if (!DRIVER_MAPPINGS.containsKey(driverName)) {
            throw new InitializationException("There is no registered DriverStrategy for the driverType = %s. Available values are: %s", driverName, DRIVER_MAPPINGS.keySet());
        }

        return Try.of(() -> DRIVER_MAPPINGS.get(driverName).getConstructor(WebConfiguration.class).newInstance(webConfiguration)).get();
    }
}
