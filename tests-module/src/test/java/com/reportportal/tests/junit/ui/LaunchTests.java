package com.reportportal.tests.junit.ui;

import java.util.List;
import java.util.stream.Stream;

import com.reportportal.core.junit.AbstractWebJUnit;
import com.reportportal.models.User;
import com.reportportal.service.SuitesDataReaderService;
import com.reportportal.service.UserDataService;
import com.reportportal.ui.pages.LaunchesPage;
import com.reportportal.ui.steps.LoginSteps;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class LaunchTests extends AbstractWebJUnit
{
    @Autowired
    private LoginSteps loginSteps;
    @Autowired
    private UserDataService userDataService;
    @Autowired
    private LaunchesPage launchesPage;
    @Value("${rp.project}")
    private String projectName;
    private User user;

    @BeforeEach
    public void setup()
    {
        user = userDataService.getUser();
    }

    @ParameterizedTest(name = "{index} => launchID={0}")
    @MethodSource("getSuites")
    public void checkLaunches(Integer launchID)
    {
        loginSteps.login(user);
        launchesPage.openCurrentPage(projectName);
        List<String> suites = launchesPage.openLaunch(launchID.toString()).getSuitesName();
        SuitesDataReaderService suitesReader = new SuitesDataReaderService();
        verifyThat.listOfPrimitivesAreEqual(suites, suitesReader.getSuitesName(launchID));
    }

    private static Stream<Arguments> getSuites()
    {
        return Stream.of(Arguments.of(6262801), Arguments.of(6262802), Arguments.of(6262805), Arguments.of(6262804),
                Arguments.of(6262805));
    }

    @AfterEach
    public void tearDown()
    {
        userDataService.releaseUser(user);
    }

}
