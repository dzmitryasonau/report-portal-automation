package com.reportportal.services;

import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.SauceREST;
import com.reportportal.support.properties.CustomPropertySourcesPlaceholderConfigurer;
import io.vavr.control.Try;
import org.openqa.selenium.remote.SessionId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SauceLabsService {

    private final CustomPropertySourcesPlaceholderConfigurer placeholderConfigurer;

    public SauceLabsService(@Autowired CustomPropertySourcesPlaceholderConfigurer placeholderConfigurer) {
        this.placeholderConfigurer = placeholderConfigurer;
    }

    @Bean
    public SauceREST getSauceRest() {
        return new SauceREST(getUsername(), getAccessKey(), DataCenter.fromString(getDataCenter()));
    }

    public String getUsername() {
        return placeholderConfigurer.get("browser.username");
    }

    public String getAccessKey() {
        return placeholderConfigurer.get("browser.accessKey");
    }

    public String getDataCenter() {
        return placeholderConfigurer.get("browser.remote.data.center");
    }

    public String getDataCenterShort() {
        return placeholderConfigurer.get("browser.remote.data.center.short");
    }

    public Try<String> getPublicJobLink(SessionId sessionId) {
        String sessionIdString = sessionId.toString();
        return Try.of(() -> getSauceRest().getPublicJobLink(sessionIdString));
    }

    public Try<Void> updateJobInfo(SessionId sessionId, Map<String, Object> jobInfo) {
        String sessionIdString = sessionId.toString();
        return Try.runRunnable(() -> getSauceRest().updateJobInfo(sessionIdString, jobInfo));
    }
}
