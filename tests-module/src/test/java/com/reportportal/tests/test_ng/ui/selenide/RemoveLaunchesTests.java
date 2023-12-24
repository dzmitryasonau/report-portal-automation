package com.reportportal.tests.test_ng.ui.selenide;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reportportal.api.HttpClient;
import com.reportportal.api.core.CustomResponse;
import com.reportportal.core.test_ng.AbstractSelenideWebTestNG;
import com.reportportal.models.User;
import com.reportportal.models.launch.Launch;
import com.reportportal.models.launch.api.FinishLaunchRequest;
import com.reportportal.models.launch.api.StartLaunchRequest;
import com.reportportal.service.AesCryptoService;
import com.reportportal.service.UserDataService;
import com.reportportal.ui.pages.selenide.LaunchesPage;
import com.reportportal.ui.steps.selenide.LoginSteps;
import com.reportportal.utils.CommonUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RemoveLaunchesTests extends AbstractSelenideWebTestNG
{
    private static final String LAUNCH_NAME = "Created_Api_Tests";
    @Autowired
    private UserDataService userDataService;
    @Autowired
    private AesCryptoService aesCryptoService;
    @Value("${rp.project}")
    private String reportPortalProject;
    @Autowired
    @Qualifier("selenideLaunchesPage")
    private LaunchesPage launchesPage;
    @Autowired
    @Qualifier("selenideLoginSteps")
    private LoginSteps loginSteps;
    @Autowired
    private HttpClient httpClient;
    private String token;
    private String launchName;
    private Launch launch;
    private User user;

    @BeforeMethod
    private void setup()
    {
        user = userDataService.getUser();
        token = aesCryptoService.getDecryptedBearerApiKey(user);
        launchName = LAUNCH_NAME + CommonUtils.getRandomInteger(200, 20);
        createLaunch();
        finishLaunch();
    }

    @Test
    public void removeLaunch()
    {
        loginSteps.login(user);
        launchesPage.open(reportPortalProject).openHamburgerManu(launchName).openDeleteWindow(launchName);

        verifyThat.isTrue(launchesPage.getDeleteWindowText().contains(launchName),
                "launch name in confirm delete launch window correct");

        List<String> currentLaunchesNames = launchesPage.deleteLaunch().getLaunchesNames();

        verifyThat.itemIsNotPresentInList(launchName, currentLaunchesNames,
                "launch name in confirm delete launch window correct");
    }

    private void createLaunch()
    {
        try
        {
            httpClient.postNewLaunch(token, reportPortalProject, new StartLaunchRequest(launchName, ZonedDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneOffset.UTC))));

            CustomResponse response = httpClient.getLaunchByName(token, reportPortalProject, launchName);
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
        httpClient.finishLaunch(token, reportPortalProject, launch.getContent().get(0).getUuid(),
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
