package com.reportportal.tests.test_ng.api;

import java.util.List;
import java.util.Map;

import com.reportportal.core.test_ng.AbstractTestNG;
import com.reportportal.models.User;
import com.reportportal.models.launch.Attribute;
import com.reportportal.models.launch.Launch;
import com.reportportal.service.UserDataService;
import com.reportportal.steps.api.ApiSteps;
import com.reportportal.utils.CommonUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LaunchInfoTests extends AbstractTestNG
{
    private static final String FILTER = "demo";
    private static final Integer LAST_LAUNCH_ID = 6262805;
    private static final String ATTRIBUTE_PREFIX = "Atr: ";
    private static final String ATTRIBUTE_VALUE = "value";
    private static final Integer MAX_INDEX = 10000;
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

    @Test(dataProvider = "launches")
    public void checkLaunchStatus(Integer launchID, String expectedStatus)
    {
        Map<String, String> launch = apiSteps.getLaunchStatus(user, projectName, launchID);
        verifyThat.actualIsEqualToExpected(launch.get(launchID.toString()), expectedStatus,
                "Launch status is " + "correct, ");
    }

    @Test(dataProvider = "attributes")
    public void checkAttributeUpdates(Integer launchID, List<Attribute> attributes)
    {
        String response = apiSteps.updateLaunch(user, projectName, launchID, attributes);
        String expectedResponse = String.format("{\"message\":\"Launch with ID = '%s' successfully updated.\"}",
                launchID);
        verifyThat.actualIsEqualToExpected(response, expectedResponse, "Response is correct, ");
    }

    @DataProvider(name = "attributes")
    public Object[][] getAttributes()
    {
        return new Object[][] { { 6262801,
                List.of(new Attribute(ATTRIBUTE_PREFIX + CommonUtils.getRandomInteger(MAX_INDEX), ATTRIBUTE_VALUE)) },
                { 6262802, List.of(new Attribute(ATTRIBUTE_PREFIX + CommonUtils.getRandomInteger(MAX_INDEX),
                                ATTRIBUTE_VALUE),
                        new Attribute(ATTRIBUTE_PREFIX + CommonUtils.getRandomInteger(MAX_INDEX), ATTRIBUTE_VALUE)) },
                { 6262803, List.of(new Attribute(ATTRIBUTE_PREFIX + CommonUtils.getRandomInteger(MAX_INDEX),
                        ATTRIBUTE_VALUE)) }, { 6262804,
                List.of(new Attribute(ATTRIBUTE_PREFIX + CommonUtils.getRandomInteger(MAX_INDEX), ATTRIBUTE_VALUE)) },
                { 6262805, List.of(new Attribute(ATTRIBUTE_PREFIX + CommonUtils.getRandomInteger(MAX_INDEX),
                        ATTRIBUTE_VALUE)) } };
    }

    @DataProvider(name = "launches", parallel = true)
    public Object[][] getLaunches()
    {
        return new Object[][] { { 6262801, "FAILED" }, { 6262802, "FAILED" }, { 6262803, "FAILED" },
                { 6262804, "FAILED" }, { 6262805, "PASSED" } };
    }

    @AfterMethod
    private void tearDown()
    {
        userDataService.releaseUser(user);
    }

}
