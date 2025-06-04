package com.reportportal.tests.test_ng.ui.selenide;

import com.codeborne.selenide.Selenide;
import com.reportportal.core.test_ng.AbstractSelenideWebTestNG;
import com.reportportal.models.User;
import com.reportportal.models.launch.LaunchStatisticsCard;
import com.reportportal.service.UserDataService;
import com.reportportal.ui.pages.selenide.LaunchInfo;
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

public class MoveToLaunchViewTests extends AbstractSelenideWebTestNG
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
        loginSteps.login(user);
        launchesPage.open(reportPortalProject);
    }

    @Test(dataProvider = "launchesData")
    public void checkSuitePresent(LaunchStatisticsCard launchStatisticsCard)
    {
        Integer launchId = launchStatisticsCard.getIndex();
        LaunchStatistics launchStatistics = launchesPage.getLaunchStatistics();

        verifyThat.isTrue(launchStatistics.openLaunchByName(launchId).isSuiteNamePresent(),
                "user open launch on Suite level");
    }

    @Test(dataProvider = "launchesData")
    public void checkTotalTestsNumber(LaunchStatisticsCard launchStatisticsCard)
    {
        Integer launchId = launchStatisticsCard.getIndex();
        LaunchStatistics launchStatistics = launchesPage.getLaunchStatistics();
        verifyThat.actualIsEqualToExpected(launchStatistics.openTotalPage(launchId).getTestRows().size(),
                launchStatisticsCard.getTotal(), "number of total tests is correct in the info page");
        Selenide.back();
    }

    @Test(dataProvider = "launchesData")
    public void checkPassedTestsCountStatus(LaunchStatisticsCard launchStatisticsCard)
    {
        Integer launchId = launchStatisticsCard.getIndex();
        LaunchInfo infoPage = launchesPage.getLaunchStatistics().openPassedPage(launchId);
        verifyThat.actualIsEqualToExpected(infoPage.getTestRows().size(), launchStatisticsCard.getPassed(),
                "number of Passed tests is correct in the info page");
        verifyThat.isTrue(infoPage.getTestStatuses().stream().allMatch(s -> s.equals("Passed")),
                "all tests has Passed status");
        Selenide.back();
    }

    @Test(dataProvider = "launchesData")
    public void checkFailedTestsCountStatus(LaunchStatisticsCard launchStatisticsCard)
    {
        Integer launchId = launchStatisticsCard.getIndex();
        LaunchInfo infoPage = launchesPage.getLaunchStatistics().openFailedPage(launchId);
        verifyThat.actualIsEqualToExpected(infoPage.getTestRows().size(), launchStatisticsCard.getFailed(),
                "number of Failed tests is correct in the info page");
        verifyThat.isTrue(infoPage.getTestStatuses().stream().allMatch(s -> s.equals("Failed")),
                "all tests has Failed status");
        Selenide.back();
    }

    @Test(dataProvider = "launchesData")
    public void checkSkippedTestsCountStatus(LaunchStatisticsCard launchStatisticsCard)
    {
        Integer launchId = launchStatisticsCard.getIndex();
        LaunchInfo infoPage = launchesPage.getLaunchStatistics().openSkippedPage(launchId);
        verifyThat.actualIsEqualToExpected(infoPage.getTestRows().size(), launchStatisticsCard.getSkipped(),
                "number of Skipped tests is correct in the info page");
        verifyThat.isTrue(infoPage.getTestStatuses().stream().allMatch(s -> s.equals("Skipped")),
                "all tests has Skipped status");
        Selenide.back();
    }

    @Test(dataProvider = "launchesData")
    public void checkToInvestigateTestsCountDefectStatus(LaunchStatisticsCard launchStatisticsCard)
    {
        Integer launchId = launchStatisticsCard.getIndex();
        LaunchInfo infoPage = launchesPage.getLaunchStatistics().openToInvestigateBugs(launchId);
        verifyThat.actualIsEqualToExpected(infoPage.getTestRows().size(), launchStatisticsCard.getToInvestigate(),
                "number of To Investigate tests is correct in the info page");
        verifyThat.isTrue(infoPage.getDefectTypes().stream().allMatch(s -> s.equals("To Investigate")),
                "all defect types has To Investigate status");
    }

    @DataProvider(name = "launchesData")
    private static Object[][] getLaunchesData()
    {
        return new Object[][] { { new LaunchStatisticsCard(8931845, 20, 10, 8, 2, 4, 4, 0, 10) },
                { new LaunchStatisticsCard(8931844, 15, 5, 9, 1, 1, 5, 4, 8) } };
    }

    @AfterMethod
    private void tearDown()
    {
        userDataService.releaseUser(user);
    }

}
