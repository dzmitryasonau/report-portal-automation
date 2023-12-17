package com.reportportal.tests.test_ng.api.different_clients;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.reportportal.api.HttpClient;
import com.reportportal.api.core.CustomResponse;
import com.reportportal.core.test_ng.AbstractTestNG;
import com.reportportal.models.User;
import com.reportportal.models.launch.api.StartLaunchRequest;
import com.reportportal.service.AesCryptoService;
import com.reportportal.service.UserDataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CreateNewLaunchTests extends AbstractTestNG
{
    private static final String LAUNCH_NAME = "Demo_Api_Tests";
    private static final Integer LAST_LAUNCH_ID = 6262805;
    @Autowired
    private UserDataService userDataService;
    @Autowired
    private AesCryptoService aesCryptoService;
    @Value("${rp.project}")
    private String projectName;
    @Autowired
    private HttpClient httpClient;
    private User user;
    private String token;

    @BeforeMethod
    private void setup()
    {
        user = userDataService.getUser();
        token = aesCryptoService.getDecryptedBearerApiKey(user);
    }

    @Test
    public void checkStartNewLaunch()
    {
        CustomResponse response = httpClient.postNewLaunch(token, projectName,
                new StartLaunchRequest(LAUNCH_NAME, ZonedDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneOffset.UTC))));

        verifyThat.actualIsEqualToExpected(response.getStatusCode(), 201, "Status code is correct, ");
    }

    @Test
    public void checkStartNewLaunchIncorrectLaunchName()
    {
        String launchName = "Incorrect launch name";
        CustomResponse response = httpClient.postNewLaunch(token, projectName,
                new StartLaunchRequest(launchName, ZonedDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneOffset.UTC))));

        verifyThat.actualIsEqualToExpected(response.getStatusCode(), 404, "Status code is correct, ");

        String expectedMessage = String.format("Launch '%s' not found. Did you use correct Launch ID?", launchName);
        verifyThat.actualIsContainExpected(expectedMessage, response.getBody(), "Message is correct, ");
    }

    @Test
    public void checkStartNewLaunchMissedLaunchTime()
    {
        CustomResponse response = httpClient.postNewLaunch(token, projectName,
                new StartLaunchRequest(LAUNCH_NAME, null));

        verifyThat.actualIsEqualToExpected(response.getStatusCode(), 400, "Status code is correct, ");

        String expectedMessage = "Incorrect Request. [Field 'startTime' should not be null.] ";
        verifyThat.actualIsContainExpected(expectedMessage, response.getBody(), "Message is correct, ");
    }

    @AfterMethod
    private void tearDown()
    {
        userDataService.releaseUser(user);
    }
}
