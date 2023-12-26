package com.reportportal.api.steps;

import com.epam.reportportal.annotations.Step;
import com.reportportal.api.HttpClient;
import com.reportportal.models.launch.Launch;
import com.reportportal.models.launch.api.FinishLaunchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class LaunchApiSteps {
    @Autowired
    private HttpClient httpClient;

    @Step("User finish launch")
    public void deleteLaunchWithFinish(String authToken, String projectName, Launch launch) {
        httpClient.finishLaunch(authToken, projectName, launch.getContent().get(0).getUuid(),
                new FinishLaunchRequest(ZonedDateTime.now().plusDays(2)
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneOffset.UTC)),
                        "PASSED"));
        deleteLaunch(authToken, projectName, launch);
    }

    @Step("User delete launch")
    public void deleteLaunch(String authToken, String projectName, Launch launch) {
        String launchId = launch.getContent().get(0).getId().toString();
        httpClient.deleteLaunch(authToken, projectName, launchId);
    }
}
