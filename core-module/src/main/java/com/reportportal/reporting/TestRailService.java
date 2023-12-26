package com.reportportal.reporting;


import com.reportportal.support.CustomPropertySourcesPlaceholderConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestRailService {

    private final CustomPropertySourcesPlaceholderConfigurer placeholderConfigurer;

    public TestRailService(@Autowired CustomPropertySourcesPlaceholderConfigurer placeholderConfigurer) {
        this.placeholderConfigurer = placeholderConfigurer;
    }

    public String getUrl() {
        return placeholderConfigurer.get("tms.url");
    }

    public String getTicketPath() {
        return placeholderConfigurer.get("tms.ticket.path");
    }

    public String getUserName() {
        return placeholderConfigurer.get("tms.username");
    }

    public String getApiKey() {
        return placeholderConfigurer.get("tms.apiKey");
    }

    public int getProjectId() {
        return Integer.parseInt(placeholderConfigurer.get("tms.projectId"));
    }

    public boolean isCompleteRun() {
        return Boolean.parseBoolean(placeholderConfigurer.get("tms.completeRun"));
    }

    public boolean isEnabled() {
        return Boolean.parseBoolean(placeholderConfigurer.get("tms.enabled"));
    }

    public String getFullLinkToTheTestCaseId(int testCaseId) {
        return getUrl() + getTicketPath() + testCaseId;
    }

}
