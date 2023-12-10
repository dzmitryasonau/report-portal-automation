package com.reportportal.ui.pages.selenide;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.reportportal.ui.components.AbstractSelenidePage;
import com.reportportal.ui.core.SelenideActions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LaunchStatistics extends AbstractSelenidePage
{
    private static final String LAUNCH_ID_TEMPLATE = "//div[@data-id='%s']";
    private static final String LAUNCH_EXECUTION_TEMPLATE = "//span[text()='%s']/following-sibling::a";
    private static final String LAUNCH_BUGS_TEMPLATE =
            "//span[text()='%s']/..//div[contains(@class," + "'donutChart__total')]";
    private static final String LAUNCH_NAME_TEMPLATE =
            "//div[@data-id='%s']//td//div[contains(@class," + "'itemInfo__name')]";
    @Autowired
    private SelenideActions selenideActions;
    @Autowired
    private LaunchInfo launchInfo;

    public boolean isLaunchStatisticsBeVisible(Integer launchId)
    {
        selenideActions.scrollToElement(Selenide.$x(String.format(LAUNCH_ID_TEMPLATE, launchId)));
        return Selenide.$x(String.format(LAUNCH_ID_TEMPLATE, launchId)).isDisplayed();
    }

    public LaunchInfo openLaunchByName(Integer launchId)
    {
        Selenide.$x(String.format(LAUNCH_NAME_TEMPLATE, launchId)).click();
        return launchInfo;
    }

    public LaunchInfo openTotalPage(Integer launchId)
    {
        Selenide.$x(String.format(LAUNCH_ID_TEMPLATE, launchId) + String.format(LAUNCH_EXECUTION_TEMPLATE, "total"))
                .click();
        return launchInfo;
    }

    public Integer getTotal(Integer launchId)
    {
        return getTextFromElements(launchId, LAUNCH_EXECUTION_TEMPLATE, "total");
    }

    public LaunchInfo openPassedPage(Integer launchId)
    {
        Selenide.$x(String.format(LAUNCH_ID_TEMPLATE, launchId) + String.format(LAUNCH_EXECUTION_TEMPLATE, "passed"))
                .click();
        return launchInfo;
    }

    public Integer getPassed(Integer launchId)
    {
        return getTextFromElements(launchId, LAUNCH_EXECUTION_TEMPLATE, "passed");
    }

    public LaunchInfo openFailedPage(Integer launchId)
    {
        Selenide.$x(String.format(LAUNCH_ID_TEMPLATE, launchId) + String.format(LAUNCH_EXECUTION_TEMPLATE, "failed"))
                .click();
        return launchInfo;
    }

    public Integer getFailed(Integer launchId)
    {
        return getTextFromElements(launchId, LAUNCH_EXECUTION_TEMPLATE, "failed");
    }

    public LaunchInfo openSkippedPage(Integer launchId)
    {
        Selenide.$x(String.format(LAUNCH_ID_TEMPLATE, launchId) + String.format(LAUNCH_EXECUTION_TEMPLATE, "skipped"))
                .click();
        return launchInfo;
    }

    public Integer getSkipped(Integer launchId)
    {
        return getTextFromElements(launchId, LAUNCH_EXECUTION_TEMPLATE, "skipped");
    }

    public Integer getProjectBugs(Integer launchId)
    {
        return getTextFromElements(launchId, LAUNCH_BUGS_TEMPLATE, "pb");
    }

    public Integer getSystemIssues(Integer launchId)
    {
        return getTextFromElements(launchId, LAUNCH_BUGS_TEMPLATE, "si");
    }

    public Integer getAutomationBug(Integer launchId)
    {
        return getTextFromElements(launchId, LAUNCH_BUGS_TEMPLATE, "ab");
    }

    public LaunchInfo openToInvestigateBugs(Integer launchId)
    {
        Selenide.$x(String.format(LAUNCH_ID_TEMPLATE, launchId) + String.format(LAUNCH_BUGS_TEMPLATE, "ti")).click();
        return launchInfo;
    }

    public Integer getToInvestigateBugs(Integer launchId)
    {
        return getTextFromElements(launchId, LAUNCH_BUGS_TEMPLATE, "ti");
    }

    private Integer getTextFromElements(Integer launchId, String template, String predicate)
    {
        ElementsCollection elements = Selenide.$$x(
                String.format(LAUNCH_ID_TEMPLATE, launchId) + String.format(template, predicate));
        return elements.size() == 0 ? 0 : Integer.parseInt(elements.get(0).getText());
    }
}
