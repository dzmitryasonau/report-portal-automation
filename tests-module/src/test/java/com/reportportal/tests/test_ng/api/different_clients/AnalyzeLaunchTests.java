package com.reportportal.tests.test_ng.api.different_clients;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reportportal.api.HttpClient;
import com.reportportal.api.core.CustomResponse;
import com.reportportal.core.test_ng.AbstractTestNG;
import com.reportportal.models.User;
import com.reportportal.models.launch.Launch;
import com.reportportal.models.launch.api.AnalysisRequest;
import com.reportportal.models.launch.api.StartLaunchRequest;
import com.reportportal.service.AesCryptoService;
import com.reportportal.service.UserDataService;
import com.reportportal.utils.CommonUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AnalyzeLaunchTests extends AbstractTestNG
{
    private static final String LAUNCH_NAME = "Demo_Api_Tests";
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
    public void checkAnalyzeLaunch()
    {
        String analyzerTypeName = "autoAnalyzer";
        Integer launchID = launch.getContent().get(0).getId();
        AnalysisRequest analysisRequest = new AnalysisRequest(List.of("TO_INVESTIGATE"), "ALL", analyzerTypeName,
                launchID);
        CustomResponse response = httpClient.analyzeLaunch(token, projectName, analysisRequest);

        verifyThat.actualIsEqualToExpected(response.getStatusCode(), 200, "Status code is correct, ");

        String expectedMessage = String.format("%s analysis for launch with ID='%d' started.", analyzerTypeName,
                launchID);
        verifyThat.actualIsContainExpected(expectedMessage, response.getBody(), "Message is correct, ");
    }

    @Test
    public void compareLaunches()
    {
        CustomResponse response = httpClient.compareLaunch(token, projectName, List.of(6262801, 6262802));

        verifyThat.actualIsEqualToExpected(response.getStatusCode(), 200, "Status code is correct, ");
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

    @AfterMethod
    private void tearDown()
    {
        userDataService.releaseUser(user);
    }
}
