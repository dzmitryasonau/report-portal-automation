package com.reportportal.tests.test_ng.api.different_clients;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reportportal.annotations.TmsId;
import com.reportportal.api.HttpClient;
import com.reportportal.api.core.CustomResponse;
import com.reportportal.api.steps.LaunchApiSteps;
import com.reportportal.core.test_ng.AbstractTestNG;
import com.reportportal.models.User;
import com.reportportal.models.launch.Content;
import com.reportportal.models.launch.Launch;
import com.reportportal.models.launch.api.FinishLaunchRequest;
import com.reportportal.models.launch.api.LaunchMergeRequest;
import com.reportportal.models.launch.api.StartLaunchRequest;
import com.reportportal.service.AesCryptoService;
import com.reportportal.service.UserDataService;
import com.reportportal.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MergeLaunchTests extends AbstractTestNG {
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
    private Launch firstLaunch;
    private Launch secondLaunch;
    private final Launch mergeLaunch = new Launch();

    @BeforeMethod
    private void setup() {
        user = userDataService.getUser();
        token = aesCryptoService.getDecryptedBearerApiKey(user);
        firstLaunch = createLaunch(LAUNCH_NAME + CommonUtils.getRandomInteger(200, 20));
        finishLaunch(firstLaunch.getContent().get(0).getUuid());
        secondLaunch = createLaunch(LAUNCH_NAME + CommonUtils.getRandomInteger(200, 20));
        finishLaunch(secondLaunch.getContent().get(0).getUuid());
    }

    @Test
    @TmsId(20619)
    public void checkMergeLaunch() {
        LaunchMergeRequest launchMergeRequest = new LaunchMergeRequest(
                List.of(firstLaunch.getContent().get(0).getId(), secondLaunch.getContent().get(0).getId()), true,
                "Merged launches " + CommonUtils.getRandomInteger(200, 20));
        CustomResponse response = httpClient.mergeLaunches(token, projectName, launchMergeRequest);

        verifyThat.actualIsEqualToExpected(response.getStatusCode(), 200, "Status code is correct, ");
        updateMergeLaunchContent(response);
    }

    private void updateMergeLaunchContent(CustomResponse response) {
        try {
            Content content = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .readValue(response.getBody(), Content.class);
            mergeLaunch.setContent(List.of(content));
        } catch (IOException e) {
            throw new RuntimeException("Unable to parse launch creation", e);
        }

    }

    private Launch createLaunch(String launchName) {
        try {
            httpClient.postNewLaunch(token, projectName, new StartLaunchRequest(launchName, ZonedDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneOffset.UTC))));

            CustomResponse response = httpClient.getLaunchByName(token, projectName, launchName);
            return new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .readValue(response.getBody(), Launch.class);
        } catch (IOException e) {
            throw new RuntimeException("Unable to parse launch creation", e);
        }

    }

    private void finishLaunch(String uuid) {
        httpClient.finishLaunch(token, projectName, uuid, new FinishLaunchRequest(ZonedDateTime.now().plusDays(2)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneOffset.UTC)),
                "PASSED"));
    }

    @AfterMethod
    private void tearDown() {
        userDataService.releaseUser(user);
        if (token != null && projectName != null) {
            launchApiSteps.deleteLaunch(token, projectName, mergeLaunch);
        }
    }
}
