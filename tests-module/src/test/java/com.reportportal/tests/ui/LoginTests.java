package com.reportportal.tests.ui;

import com.epam.reportportal.annotations.attribute.AttributeValue;
import com.epam.reportportal.annotations.attribute.Attributes;
import com.reportportal.annotations.TmsId;
import com.reportportal.core.AbstractWebTest;
import com.reportportal.models.User;
import com.reportportal.pages.LaunchesPage;
import com.reportportal.services.UserDataService;
import com.reportportal.steps.ui.LoginSteps;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Attributes(attributeValues = @AttributeValue("test"))
public class LoginTests extends AbstractWebTest
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
    public void loginToUI()
    {
        user = userDataService.getUser();
        loginSteps.login(user);
        verifyThat.actualIsEqualToExpected(launchesPage.getUserName().toLowerCase(), user.getLogin(), "User name is correct, ");
    }

    @AfterMethod
    private void tearDown()
    {
        userDataService.releaseUser(user);
    }

}
