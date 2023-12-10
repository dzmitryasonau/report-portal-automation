package com.reportportal.tests.test_ng.ui.selenium;

import java.util.List;

import com.reportportal.core.test_ng.AbstractWebTestNG;
import com.reportportal.models.User;
import com.reportportal.service.SuitesDataReaderService;
import com.reportportal.service.TestNGDataProvider;
import com.reportportal.service.UserDataService;
import com.reportportal.ui.pages.LaunchesPage;
import com.reportportal.ui.steps.LoginSteps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LaunchTests extends AbstractWebTestNG
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

    @BeforeMethod
    private void setup()
    {
        user = userDataService.getUser();
    }

    @Test(dataProvider = "suites", dataProviderClass = TestNGDataProvider.class)
    public void checkLaunches(Integer launchID)
    {
        loginSteps.login(user);
        launchesPage.openCurrentPage(projectName);
        List<String> suites = launchesPage.openLaunch(launchID.toString()).getSuitesName();
        SuitesDataReaderService suitesReader = new SuitesDataReaderService();
        verifyThat.listOfPrimitivesAreEqual(suites, suitesReader.getSuitesName(launchID));
    }

    @AfterMethod
    private void tearDown()
    {
        userDataService.releaseUser(user);
    }

}
