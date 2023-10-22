package com.reportportal.tests.api;

import java.util.List;

import com.reportportal.annotations.TmsId;
import com.reportportal.core.AbstractWebTest;
import com.reportportal.models.User;
import com.reportportal.services.UserDataService;
import com.reportportal.steps.api.ApiSteps;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class GetLaunchNamesWithFilterTests extends AbstractWebTest
{
    private static final String PROJECT_NAME = "default_personal";
    private static final String FILTER = "demo";
    @Autowired
    private ApiSteps apiSteps;
    @Autowired
    private UserDataService userDataService;
    private User user;

    @BeforeMethod
    private void setup()
    {
        user = null;
    }

    @Test
    @TmsId(20550)
    public void getDefaultPersonalLaunchNamesWithDemoFilter()
    {
        user = userDataService.getUser();
        List<String> launches = apiSteps.getLaunchesByProjectName(user, PROJECT_NAME, FILTER);
        verifyThat.actualIsEqualToExpected(launches, List.of("Demo Api Tests"), "Launches name are correct, ");
    }

    @AfterMethod
    private void tearDown()
    {
        userDataService.releaseUser(user);
    }

}
