package com.reportportal.tests.test_ng.ui.selenide;

import com.reportportal.core.test_ng.AbstractSelenideWebTestNG;
import com.reportportal.models.User;
import com.reportportal.service.AesCryptoService;
import com.reportportal.service.UserDataService;
import com.reportportal.ui.pages.selenide.LaunchesPage;
import com.reportportal.ui.pages.selenide.LoginPage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginTests extends AbstractSelenideWebTestNG
{
    @Autowired
    private UserDataService userDataService;
    @Autowired
    private AesCryptoService aesCryptoService;
    @Autowired
    @Qualifier("selenideLaunchesPage")
    private LaunchesPage launchesPage;
    @Autowired
    @Qualifier("selenideLoginPage")
    private LoginPage loginPage;
    private User user;

    @BeforeMethod
    private void setup()
    {
        user = userDataService.getUser();
    }

    @Test
    public void loginToUI()
    {
        loginPage.open().fillLogin(user.getLogin()).fillPassword(aesCryptoService.decrypt(user.getPassword())).login();
        launchesPage.waitUntilSuccessfullyLoginBannerDisappear();
        verifyThat.actualIsEqualToExpected(launchesPage.openUserBlock().getUserName().toLowerCase(),
                user.getLogin().toLowerCase(), "User name is correct, ");
    }

    @Test
    public void loginToUIFailedCheck()
    {
        loginPage.open().fillLogin(user.getLogin()).fillPassword(aesCryptoService.decrypt(user.getPassword())).login();
        launchesPage.waitUntilSuccessfullyLoginBannerDisappear();
        verifyThat.actualIsEqualToExpected(launchesPage.openUserBlock().getUserName().toLowerCase(), "Incorrect name",
                "User name is correct, ");
    }

    @AfterMethod
    private void tearDown()
    {
        userDataService.releaseUser(user);
    }

}
