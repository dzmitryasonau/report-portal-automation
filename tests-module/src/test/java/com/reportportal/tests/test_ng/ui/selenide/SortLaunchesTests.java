package com.reportportal.tests.test_ng.ui.selenide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import com.reportportal.core.test_ng.AbstractSelenideWebTestNG;
import com.reportportal.exceptions.AutomationException;
import com.reportportal.models.User;
import com.reportportal.service.UserDataService;
import com.reportportal.ui.pages.selenide.LaunchesPage;
import com.reportportal.ui.steps.selenide.LoginSteps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SortLaunchesTests extends AbstractSelenideWebTestNG
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

    @Test
    public void checkLaunchesDefaultSortByRecent()
    {
        loginSteps.login(user);
        List<String> startTimes = launchesPage.open(reportPortalProject).showFullLaunchStartTime().getStartTimes();
        verifyThat.isTrue(checkOrder(startTimes, false), "order is correct");
    }

    @Test
    public void checkLaunchesReSortByRecent()
    {
        loginSteps.login(user);
        List<String> startTimes = launchesPage.open(reportPortalProject).sortByStartTime().showFullLaunchStartTime()
                .getStartTimes();
        verifyThat.isTrue(checkOrder(startTimes, true), "order is correct");
    }

    public boolean checkOrder(List<String> dates, boolean ascendingOrder)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try
        {
            for (int i = 0; i < dates.size() - 1; i++)
            {
                int comparison = sdf.parse(dates.get(i)).compareTo(sdf.parse(dates.get(i + 1)));
                if (ascendingOrder && comparison > 0 || !ascendingOrder && comparison < 0)
                {
                    throw new AutomationException("Elements not sorted: ", dates);
                }
            }
            return true;
        }
        catch (ParseException ex)
        {
            throw new AutomationException("Unable to parse", ex);
        }
    }

    @AfterMethod
    private void tearDown()
    {
        userDataService.releaseUser(user);
    }

}
