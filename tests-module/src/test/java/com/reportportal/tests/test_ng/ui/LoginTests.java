package com.reportportal.tests.test_ng.ui;

import com.reportportal.core.test_ng.AbstractWebTestNG;
import com.reportportal.models.User;
import com.reportportal.service.UserDataService;
import com.reportportal.ui.pages.LaunchesPage;
import com.reportportal.ui.steps.LoginSteps;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginTests extends AbstractWebTestNG
{
    @Autowired
    private LoginSteps loginSteps;
    @Autowired
    private UserDataService userDataService;
    @Autowired
    private LaunchesPage launchesPage;
    private User user;

    @BeforeMethod
    private void setup()
    {
        user = null;
    }

    @Test
    public void loginToUI()
    {
        user = userDataService.getUser();
        loginSteps.login(user);
        verifyThat.actualIsEqualToExpected(launchesPage.getUserName().toLowerCase(), user.getLogin().toLowerCase(),
                "User name is correct, ");
    }

    @AfterMethod
    private void tearDown()
    {
        userDataService.releaseUser(user);
    }

}
