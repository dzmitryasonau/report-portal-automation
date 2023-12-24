package com.reportportal.ui.pages.selenide;

import java.util.List;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.reportportal.ui.components.AbstractSelenidePage;
import com.reportportal.ui.core.SelenideActions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("selenideLaunchesPage")
public class LaunchesPage extends AbstractSelenidePage
{
    private static final String HAMBURGER_MENU_TEMPLATE = "//td//div[contains(@class,'itemInfo__name')]/span[text()"
            + "='%s']/ancestor::div[contains(@class,'gridRow')]//td[contains(@class,'hamburger')]/div";
    private static final String OPEN_DELETE_LAUNCH_TEMPLATE = "/div//div[text()='Delete']";
    private final SelenideElement username = Selenide.$x("//div[contains(@class,'userBlock__username')]");
    private final SelenideElement openUserBlock = Selenide.$x("//div[contains(@class,'userBlock__user-block')]/div");
    private final SelenideElement successfullyLoginBanner = Selenide.$x(
            "//div[contains(@class,'notificationItem__success')]");
    private final SelenideElement sortByStartTime = Selenide.$(Selectors.byText("start time"));
    private final SelenideElement actionMenu = Selenide.$x("//i[contains(@class,'ghostMenuButton')]/..");
    private final SelenideElement compareLaunches = Selenide.$(Selectors.byText("Compare")).parent();
    private final SelenideElement compareLaunchesWindowHeader = Selenide.$(Selectors.byText("Compare launches"));
    private final SelenideElement deleteWindowText = Selenide.$x("//p[contains(@class,'deleteItemsModal')" + "]");
    private final SelenideElement deleteLaunch = Selenide.$x("//button[text()='Delete']");
    private final ElementsCollection startTimes = Selenide.$$x("//span[contains(@class,'absRelTime__absolute-time')]");
    private final ElementsCollection showFullLaunchStartTime = Selenide.$$x("//span[contains(@class,'absRelTime')]");
    private final ElementsCollection launchesCheckboxes = Selenide.$$x("//td//div[contains(@class,'checkIcon')]");
    private final ElementsCollection launchesCompareBar = Selenide.$$x(
            "//div[contains(@class,'c3')" + "]/*/*[@transform]/*[@clip-path]/*[@class='c3-chart-bars"
                    + "']/*/*/*[@class]");
    private final ElementsCollection launchesNames = Selenide.$$x("//td//div[contains(@class,'itemInfo__name')]/span");
    @Autowired
    private SelenideActions selenideActions;
    @Autowired
    private LaunchStatistics launchStatistics;

    public LaunchStatistics getLaunchStatistics()
    {
        return launchStatistics;
    }

    public LaunchesPage openHamburgerManu(String launchName)
    {
        selenideActions.click(
                selenideActions.refreshUntilVisible(Selenide.$x(String.format(HAMBURGER_MENU_TEMPLATE, launchName))));
        return this;
    }

    public LaunchesPage openDeleteWindow(String launchName)
    {
        selenideActions.click(
                Selenide.$x(String.format(HAMBURGER_MENU_TEMPLATE, launchName) + OPEN_DELETE_LAUNCH_TEMPLATE));
        return this;
    }

    public LaunchesPage deleteLaunch()
    {
        selenideActions.click(deleteLaunch);
        return this;
    }

    public String getDeleteWindowText()
    {
        return selenideActions.getText(deleteWindowText);
    }

    public void waitUntilSuccessfullyLoginBannerDisappear()
    {
        successfullyLoginBanner.should(Condition.disappear);
    }

    public String getUserName()
    {
        return username.shouldBe(Condition.visible).text();
    }

    public ElementsCollection getLaunchesCheckboxes()
    {
        ElementsCollection elements = launchesCheckboxes;
        elements.shouldHave(CollectionCondition.sizeGreaterThan(0));
        return elements;
    }

    public Integer getNumberOfLaunchesCompareBar()
    {
        launchesCompareBar.shouldHave(CollectionCondition.sizeGreaterThan(0));
        return launchesCompareBar.size();
    }

    public LaunchesPage showFullLaunchStartTime()
    {
        selenideActions.jsClick(showFullLaunchStartTime.get(0));
        return this;
    }

    public LaunchesPage sortByStartTime()
    {
        selenideActions.click(sortByStartTime);
        return this;
    }

    public List<String> getStartTimes()
    {
        return selenideActions.getTexts(startTimes);
    }

    public List<String> getLaunchesNames()
    {
        return selenideActions.getTexts(launchesNames);
    }

    public LaunchesPage openUserBlock()
    {
        selenideActions.jsClick(openUserBlock);
        return this;
    }

    public LaunchesPage openActionMenu()
    {
        selenideActions.click(actionMenu);
        return this;
    }

    public LaunchesPage openCompareLaunchWindow()
    {
        selenideActions.click(compareLaunches);
        return this;
    }

    public boolean isCompareLaunchesWindowHeader()
    {
        return compareLaunchesWindowHeader.isDisplayed();
    }

    public LaunchesPage open(String projectName)
    {
        Selenide.open("/ui/#" + projectName + "/launches/all");
        return this;
    }
}
