package com.reportportal.tests.junit.api;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.reportportal.api.steps.ApiSteps;
import com.reportportal.core.junit.AbstractJUnit;
import com.reportportal.models.User;
import com.reportportal.models.launch.api.Attribute;
import com.reportportal.models.launch.Launch;
import com.reportportal.service.UserDataService;
import com.reportportal.utils.CommonUtils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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
        verifyThat.actualIsEqualToExpected(launch.getContent().get(0).getId(), 8931847, "Last launch id is correct, ");
    }

    @ParameterizedTest(name = "{index} => launchID={0}, expectedStatus={1}")
    @MethodSource("getLaunches")
    public void checkLaunchStatus(Integer launchID, String expectedStatus)
    {
        Map<String, String> launch = apiSteps.getLaunchStatus(user, projectName, launchID);

        Assertions.assertEquals(launch.get(launchID.toString()), expectedStatus, "Launch status is correct, ");
    }

    @ParameterizedTest
    @MethodSource("attributes")
    void checkAttributeUpdates(Integer launchID, List<Attribute> attributes)
    {
        String response = apiSteps.updateLaunch(user, projectName, launchID, attributes);
        String expectedResponse = String.format("{\"message\":\"Launch with ID = '%s' successfully updated.\"}",
                launchID);

        Assertions.assertEquals(expectedResponse, response, "Response is not correct");
    }

    private static Stream<Arguments> getLaunches()
    {
        return Stream.of(Arguments.of(8931843, "FAILED"), Arguments.of(8931844, "FAILED"),
                Arguments.of(8931845, "FAILED"), Arguments.of(8931846, "FAILED"), Arguments.of(8931847, "PASSED"));
    }

    private static Stream<Arguments> attributes()
    {
        return Stream.of(Arguments.of(8931843,
                        List.of(new Attribute(ATTRIBUTE_PREFIX + CommonUtils.getRandomInteger(MAX_INDEX),
                                ATTRIBUTE_VALUE))),
                Arguments.of(8931844, List.of(new Attribute(ATTRIBUTE_PREFIX + CommonUtils.getRandomInteger(MAX_INDEX),
                                ATTRIBUTE_VALUE),
                        new Attribute(ATTRIBUTE_PREFIX + CommonUtils.getRandomInteger(MAX_INDEX), ATTRIBUTE_VALUE))),
                Arguments.of(8931845, List.of(new Attribute(ATTRIBUTE_PREFIX + CommonUtils.getRandomInteger(MAX_INDEX),
                        ATTRIBUTE_VALUE))), Arguments.of(8931846,
                        List.of(new Attribute(ATTRIBUTE_PREFIX + CommonUtils.getRandomInteger(MAX_INDEX),
                                ATTRIBUTE_VALUE))), Arguments.of(8931847,
                        List.of(new Attribute(ATTRIBUTE_PREFIX + CommonUtils.getRandomInteger(MAX_INDEX),
                                ATTRIBUTE_VALUE))));
    }

    @AfterEach
    public void tearDown()
    {
        userDataService.releaseUser(user);
    }
}
