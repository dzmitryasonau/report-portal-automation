package com.reportportal.tests.test_ng.api.different_clients;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reportportal.annotations.TmsId;
import com.reportportal.api.HttpClient;
import com.reportportal.api.core.CustomResponse;
import com.reportportal.api.steps.LaunchApiSteps;
import com.reportportal.core.test_ng.AbstractTestNG;
import com.reportportal.models.User;
import com.reportportal.models.launch.Launch;
import com.reportportal.models.launch.api.StartLaunchRequest;
import com.reportportal.service.AesCryptoService;
import com.reportportal.service.UserDataService;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class CreateNewLaunchTests extends AbstractTestNG {
    private static final String LAUNCH_NAME = "Created_Api_Tests";
    @Autowired
    private UserDataService userDataService;
    @Autowired
    private AesCryptoService aesCryptoService;
    @Autowired
    private LaunchApiSteps launchApiSteps;
    @Value("${rp.project}")
    private String projectName;
    @Autowired
    private HttpClient httpClient;
    private User user;
    private String token;
    private Launch launch;

    @BeforeMethod
    private void setup() {
        user = userDataService.getUser();
        token = aesCryptoService.getDecryptedBearerApiKey(user);
    }

    @Test
    @TmsId(20608)
    public void checkStartNewLaunch() {
        CustomResponse response = httpClient.postNewLaunch(token, projectName,
                new StartLaunchRequest(LAUNCH_NAME, ZonedDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneOffset.UTC))));
        Assertions.assertThat(response.getStatusCode())
                .isEqualTo(201)
                .as("Status code is correct");
        getLaunchInfo();
    }

    @Test
    @TmsId(20609)
    public void checkStartNewLaunchIncorrectProjectName() {
        String launchName = "Incorrect launch name";
        String projectName = "Incorrect_project_name";
        CustomResponse response = httpClient.postNewLaunch(token, projectName,
                new StartLaunchRequest(launchName, ZonedDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneOffset.UTC))));

        Assertions.assertThat(response.getStatusCode())
                .isEqualTo(403)
                .as("Status code is correct");
        Assertions.assertThat(response.getBody())
                .contains("You do not have enough permissions")
                .as("Message is correct");
    }

    @Test
    @TmsId(20610)
    public void checkStartNewLaunchMissedLaunchTime() {
        CustomResponse response = httpClient.postNewLaunch(token, projectName,
                new StartLaunchRequest(LAUNCH_NAME, null));

        verifyThat.actualIsEqualToExpected(response.getStatusCode(), 400, "Status code is correct, ");

        String expectedMessage = "Incorrect Request. [Field 'startTime' should not be null.] ";

        Assertions.assertThat(response.getBody())
                .contains(expectedMessage)
                .as("Message is correct");
    }

    private void getLaunchInfo() {
        try {
            CustomResponse launchResponse = httpClient.getLaunchByName(token, projectName, LAUNCH_NAME);
            launch = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .readValue(launchResponse.getBody(), Launch.class);
        } catch (IOException e) {
            throw new RuntimeException("Unable to parse launch creation", e);
        }
    }

    @AfterMethod
    private void tearDown() {
        userDataService.releaseUser(user);
        if (token != null && projectName != null && launch != null) {
            launchApiSteps.deleteLaunchWithFinish(token, projectName, launch);
        }
    }
}
