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
import com.reportportal.models.launch.api.FinishLaunchRequest;
import com.reportportal.models.launch.api.StartLaunchRequest;
import com.reportportal.service.AesCryptoService;
import com.reportportal.service.UserDataService;
import com.reportportal.utils.CommonUtils;
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

public class DeleteLaunchTests extends AbstractTestNG
{
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
    private String launchName;

    @BeforeMethod
    private void setup()
    {
        user = userDataService.getUser();
        token = aesCryptoService.getDecryptedBearerApiKey(user);
        launchName = LAUNCH_NAME + CommonUtils.getRandomInteger(200, 20);
        createLaunch();
    }

    @Test
    @TmsId(20611)
    public void checkLaunchDeletion()
    {
        finishLaunch();
        String launchId = launch.getContent().get(0).getId().toString();
        CustomResponse response = httpClient.deleteLaunch(token, projectName, launchId);

        String expectedMessage = String.format("Launch with ID = '%s' successfully deleted.", launchId);
        Assertions.assertThat(response.getBody())
                .contains(expectedMessage)
                .as("Deletion message is correct");
    }

    @Test
    @TmsId(20612)
    public void checkRunningLaunchDeletion()
    {
        String launchId = launch.getContent().get(0).getId().toString();
        CustomResponse response = httpClient.deleteLaunch(token, projectName, launchId);

        verifyThat.actualIsEqualToExpected(response.getStatusCode(), 406, "Status code is correct, ");
        String expectedMessage = String.format(
                "Unable to perform operation for non-finished launch. Unable to delete launch '%s' in progress state",
                launchId);
        Assertions.assertThat(response.getBody())
                .contains(expectedMessage)
                .as("Deletion message is correct");

        launchApiSteps.deleteLaunchWithFinish(token, projectName, launch);
    }

    private void createLaunch()
    {
        try
        {
            httpClient.postNewLaunch(token, projectName, new StartLaunchRequest(launchName, ZonedDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneOffset.UTC))));

            CustomResponse response = httpClient.getLaunchByName(token, projectName, launchName);
            launch = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .readValue(response.getBody(), Launch.class);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Unable to parse launch creation", e);
        }

    }

    private void finishLaunch()
    {
        httpClient.finishLaunch(token, projectName, launch.getContent().get(0).getUuid(),
                new FinishLaunchRequest(ZonedDateTime.now().plusDays(2)
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneOffset.UTC)),
                        "PASSED"));
    }

    @AfterMethod
    private void tearDown()
    {
        userDataService.releaseUser(user);
    }
}
