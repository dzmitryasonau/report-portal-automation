package com.reportportal.tests.junit.api;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.reportportal.core.junit.AbstractJUnit;
import com.reportportal.models.User;
import com.reportportal.models.launch.Launch;
import com.reportportal.service.UserDataService;
import com.reportportal.steps.api.ApiSteps;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class LaunchInfoTests extends AbstractJUnit
{
    private static final String FILTER = "demo";
    @Autowired
    private ApiSteps apiSteps;
    @Autowired
    private UserDataService userDataService;
    @Value("${rp.project}")
    private String projectName;
    private User user;

    @BeforeEach
    public void setup()
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
        verifyThat.actualIsEqualToExpected(launch.getContent().get(0).getId(), 6262805, "Last launch id is correct, ");
    }

    @ParameterizedTest(name = "{index} => launchID={0}, expectedStatus={1}")
    @MethodSource("getLaunches")
    public void checkLaunchStatus(Integer launchID, String expectedStatus)
    {
        Map<String, String> launch = apiSteps.getLaunchStatus(user, projectName, launchID);
        verifyThat.actualIsEqualToExpected(launch.get(launchID.toString()), expectedStatus,
                "Launch status is correct, ");
    }

    private static Stream<Arguments> getLaunches()
    {
        return Stream.of(Arguments.of(6262801, "FAILED"), Arguments.of(6262802, "FAILED"),
                Arguments.of(6262803, "FAILED"), Arguments.of(6262804, "FAILED"), Arguments.of(6262805, "PASSED"));
    }

    @AfterEach
    public void tearDown()
    {
        userDataService.releaseUser(user);
    }
}
