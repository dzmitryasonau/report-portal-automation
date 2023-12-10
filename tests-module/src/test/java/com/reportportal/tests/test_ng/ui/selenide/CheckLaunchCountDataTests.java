package com.reportportal.tests.test_ng.ui.selenide;

import com.reportportal.core.test_ng.AbstractSelenideWebTestNG;
import com.reportportal.models.User;
import com.reportportal.models.launch.LaunchStatisticsCard;
import com.reportportal.service.UserDataService;
import com.reportportal.ui.pages.selenide.LaunchStatistics;
import com.reportportal.ui.pages.selenide.LaunchesPage;
import com.reportportal.ui.steps.selenide.LoginSteps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CheckLaunchCountDataTests extends AbstractSelenideWebTestNG
{
    @Autowired
    private UserDataService userDataService;
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

    @Test(dataProvider = "launchesData")
    public void checkLaunchCountData(LaunchStatisticsCard launchStatisticsCard)
    {
        loginSteps.login(user);
        launchesPage.open(reportPortalProject);
        LaunchStatistics launchStatistics = launchesPage.getLaunchStatistics();
        Integer launchId = launchStatisticsCard.getIndex();
        verifyThat.isTrue(launchStatistics.isLaunchStatisticsBeVisible(launchId), "launch statistics is visible");
        verifyThat.actualIsEqualToExpected(launchStatistics.getTotal(launchId), launchStatisticsCard.getTotal(),
                "number of total tests is correct");
        verifyThat.actualIsEqualToExpected(launchStatistics.getPassed(launchId), launchStatisticsCard.getPassed(),
                "number of passed tests is correct");
        verifyThat.actualIsEqualToExpected(launchStatistics.getFailed(launchId), launchStatisticsCard.getFailed(),
                "number of failed tests is correct");
        verifyThat.actualIsEqualToExpected(launchStatistics.getSkipped(launchId), launchStatisticsCard.getSkipped(),
                "number of skipped tests is correct");
        verifyThat.actualIsEqualToExpected(launchStatistics.getProjectBugs(launchId),
                launchStatisticsCard.getProductBug(), "number of product bugs is correct");
        verifyThat.actualIsEqualToExpected(launchStatistics.getAutomationBug(launchId),
                launchStatisticsCard.getAutoBug(), "number of auto bugs is correct");
        verifyThat.actualIsEqualToExpected(launchStatistics.getSystemIssues(launchId),
                launchStatisticsCard.getSystemIssue(), "number of system issues is correct");
        verifyThat.actualIsEqualToExpected(launchStatistics.getToInvestigateBugs(launchId),
                launchStatisticsCard.getToInvestigate(), "number of investigate bugs is correct");
    }

    @DataProvider(name = "launchesData")
    private static Object[][] getLaunchesData()
    {
        return new Object[][] { { new LaunchStatisticsCard(6262801, 10, 1, 9, 0, 0, 1, 8, 5) },
                { new LaunchStatisticsCard(6262802, 15, 5, 9, 1, 1, 5, 4, 8) } };
    }

    @AfterMethod
    private void tearDown()
    {
        userDataService.releaseUser(user);
    }

}
