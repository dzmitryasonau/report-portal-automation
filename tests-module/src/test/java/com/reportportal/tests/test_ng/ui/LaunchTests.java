package com.reportportal.tests.test_ng.ui;

import java.util.List;

import com.reportportal.core.test_ng.AbstractWebTestNG;
import com.reportportal.models.User;
import com.reportportal.pages.LaunchesPage;
import com.reportportal.service.SuitesDataReader;
import com.reportportal.service.UserDataService;
import com.reportportal.steps.ui.LoginSteps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
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

    @Test(dataProvider = "suites")
    public void checkLaunches(Integer launchID)
    {
        loginSteps.login(user);
        launchesPage.openCurrentPage(projectName);
        List<String> suites = launchesPage.openLaunch(launchID.toString()).getSuitesName();
        SuitesDataReader suitesReader = new SuitesDataReader();
        verifyThat.listOfPrimitivesAreEqual(suites, suitesReader.getSuitesName(launchID));
    }

    @DataProvider(name = "suites")
    public Object[][] getSuites()
    {
        return new Object[][] { { 6262801 }, { 6262802 }, { 6262803 }, { 6262804 }, { 6262805 } };
    }

    @AfterMethod
    private void tearDown()
    {
        userDataService.releaseUser(user);
    }

}
