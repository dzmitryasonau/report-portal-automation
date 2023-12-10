package com.reportportal.tests.test_ng.ui.selenide;

import java.util.Objects;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.reportportal.core.test_ng.AbstractSelenideWebTestNG;
import com.reportportal.models.User;
import com.reportportal.service.UserDataService;
import com.reportportal.ui.core.SelenideActions;
import com.reportportal.ui.pages.selenide.LaunchesPage;
import com.reportportal.ui.steps.selenide.LoginSteps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CompareLaunchesTests extends AbstractSelenideWebTestNG
{
    private static final Integer NUMBER_OF_LAUNCHES_TYPE = 8;
    @Autowired
    private UserDataService userDataService;
    @Autowired
    private SelenideActions selenideActions;
    @Value("${rp.project}")
    private String reportPortalProject;
    @Autowired
    @Qualifier("selenideLaunchesPage")
    private LaunchesPage launchesPage;
    @Autowired
    @Qualifier("selenideLoginSteps")
    private LoginSteps loginSteps;
    private User user;

    @BeforeMethod
    private void setup()
    {
        user = userDataService.getUser();
    }

    @Test(dataProvider = "comparedLaunchesNumber")
    public void compareLaunch(Integer comparedLaunchesNumber)
    {
        loginSteps.login(user);
        launchesPage.open(reportPortalProject);

        launchesPage.getLaunchesCheckboxes();
        ElementsCollection launchesCheckboxes = launchesPage.getLaunchesCheckboxes();
        selectLaunches(launchesCheckboxes, comparedLaunchesNumber);
        LaunchesPage openedCompareWindow = launchesPage.openActionMenu().openCompareLaunchWindow();

        verifyThat.isTrue(openedCompareWindow.isCompareLaunchesWindowHeader(), "compare launches window is presents");
        verifyThat.isTrue(
                openedCompareWindow.getNumberOfLaunchesCompareBar() / NUMBER_OF_LAUNCHES_TYPE == comparedLaunchesNumber,
                "number of compared launches is correct");
    }

    @DataProvider(name = "comparedLaunchesNumber")
    private static Object[][] getComparedLaunchesNumber()
    {
        return new Object[][] { { 2 }, { 3 } };
    }

    private void selectLaunches(ElementsCollection launchesCheckboxes, Integer numberOfSelectedElements)
    {
        for (SelenideElement launchCheckBox : launchesCheckboxes)
        {
            if (!Objects.requireNonNull(launchCheckBox.getAttribute("className")).contains("checked"))
            {
                selenideActions.click(launchCheckBox);
                numberOfSelectedElements--;
                if (numberOfSelectedElements == 0)
                {
                    break;
                }
            }
        }
    }

    @AfterMethod
    private void tearDown()
    {
        userDataService.releaseUser(user);
    }

}
