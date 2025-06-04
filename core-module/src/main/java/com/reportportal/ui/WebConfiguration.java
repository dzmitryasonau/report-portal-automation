package com.reportportal.ui;

import com.reportportal.meta.BaseEntity;
import com.reportportal.meta.RunType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component
@Getter
@Setter
@NoArgsConstructor
public class WebConfiguration extends BaseEntity
{
    @Value("${browser.name}")
    private String browserName;
    @Value("${browser.headless}")
    private String headless;
    @Value("${browser.run.type}")
    private RunType runType;
    @Value("${browser.version}")
    private String browserVersion;
    @Value("${browser.username}")
    private String username;
    @Value("${browser.accessKey}")
    private String accessKey;
    @Value("${ui.wait.timeout.seconds}")
    private Long timeOutSeconds;
    @Value("${ui.polling.timeout.milliseconds}")
    private Long pollingTimeOutMilliSeconds;
    private Map<String, String> remoteCapabilities;

    @PostConstruct
    private void init()
    {
        remoteCapabilities = Map.of("username", username, "accessKey", accessKey, "extendedDebugging", "true",
                "screenResolution", "1920x1080");
    }
}
