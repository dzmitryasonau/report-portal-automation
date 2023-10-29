package com.reportportal.tests.test_ng.api;

import java.util.List;
import java.util.Map;

import com.reportportal.core.test_ng.AbstractTestNG;
import com.reportportal.models.User;
import com.reportportal.models.launch.Attribute;
import com.reportportal.models.launch.Launch;
import com.reportportal.service.TestNGDataProvider;
import com.reportportal.service.UserDataService;
import com.reportportal.steps.api.ApiSteps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LaunchInfoTests extends AbstractTestNG
{
    private static final String FILTER = "demo";
    private static final Integer LAST_LAUNCH_ID = 6262805;
    @Autowired
    private ApiSteps apiSteps;
    @Autowired
    private UserDataService userDataService;
    @Value("${rp.project}")
    private String projectName;
    private User user;

    @BeforeMethod
    private void setup()
    {
        user = userDataService.getUser();
    }

    @Test
    public void checkProjectLaunchesWithFilter()
    {
        List<String> launches = apiSteps.getLaunchesByProjectName(user, projectName, FILTER);
        verifyThat.actualIsEqualToExpected(launches, List.of("Demo Api Tests"), "Launches name are correct, ");
    }

    @Test
    public void checkLastLaunch()
    {
        Launch launch = apiSteps.getLastLaunchesByProjectName(user, projectName);
        verifyThat.actualIsEqualToExpected(launch.getContent().get(0).getId(), LAST_LAUNCH_ID,
                "Last launch id is correct, ");
    }

    @Test(dataProvider = "launches", dataProviderClass = TestNGDataProvider.class)
    public void checkLaunchStatus(Integer launchID, String expectedStatus)
    {
        Map<String, String> launch = apiSteps.getLaunchStatus(user, projectName, launchID);
        verifyThat.actualIsEqualToExpected(launch.get(launchID.toString()), expectedStatus,
                "Launch status is " + "correct, ");
    }

    @Test(dataProvider = "attributes", dataProviderClass = TestNGDataProvider.class)
    public void checkAttributeUpdates(Integer launchID, List<Attribute> attributes)
    {
        String response = apiSteps.updateLaunch(user, projectName, launchID, attributes);
        String expectedResponse = String.format("{\"message\":\"Launch with ID = '%s' successfully updated.\"}",
                launchID);
        verifyThat.actualIsEqualToExpected(response, expectedResponse, "Response is correct, ");
    }

    @AfterMethod
    private void tearDown()
    {
        userDataService.releaseUser(user);
    }

}
