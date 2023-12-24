package com.reportportal.tests.test_ng.ui.selenium;

import com.reportportal.annotations.TmsId;
import com.reportportal.core.test_ng.AbstractWebTestNG;
import com.reportportal.models.User;
import com.reportportal.service.UserDataService;
import com.reportportal.ui.pages.LaunchesPage;
import com.reportportal.ui.steps.LoginSteps;
import org.assertj.core.api.Assertions;
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
    @TmsId(20549)
    public void loginToUI() {
        user = userDataService.getUser();
        loginSteps.login(user);
        Assertions.assertThat(launchesPage.openUserBlock().getUserName().toLowerCase())
                .isEqualTo(user.getLogin().toLowerCase())
                .as("User name is correct");
    }

    @Test
    @TmsId(20605)
    public void failedLoginToUI() {
        user = userDataService.getUser();
        loginSteps.login(user);
        Assertions.assertThat(launchesPage.openUserBlock().getUserName().toLowerCase()).isEqualTo("Incorrect name")
                .as("User name is incorrect");
    }

    @AfterMethod
    private void tearDown()
    {
        userDataService.releaseUser(user);
    }

}
