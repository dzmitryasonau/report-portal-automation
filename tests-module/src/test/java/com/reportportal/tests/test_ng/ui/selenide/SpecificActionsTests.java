package com.reportportal.tests.test_ng.ui.selenide;

import java.awt.*;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.reportportal.core.test_ng.AbstractSelenideWebTestNG;
import com.reportportal.exceptions.AutomationException;
import com.reportportal.models.User;
import com.reportportal.service.UserDataService;
import com.reportportal.ui.core.SelenideActions;
import com.reportportal.ui.pages.selenide.DashboardPage;
import com.reportportal.ui.steps.selenide.LoginSteps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SpecificActionsTests extends AbstractSelenideWebTestNG
{
    private static final String DRAGGABLE_ELEMENT_TEMPLATE =
            "//div[text()='%s']/ancestor::div[contains(@class," + "'draggable-field')]";
    private static final String GRID_ITEM_TEMPLATE = "/ancestor::div[contains(@class,'react-grid-item')]";
    private static final String RESIZE_TEMPLATE = "//span[contains(@class, 'react-resizable-handle')]";
    private static final String LAUNCH_AREA = "LAUNCH STATISTICS AREA";
    private static final String LAUNCH_BAR = "LAUNCH STATISTICS BAR";
    private static final String FLAKY_TEST_CASES = "FLAKY TEST CASES";
    private static final String DASHBOARD_NAME = "DEMO DASHBOARD";
    @Autowired
    private UserDataService userDataService;
    @Autowired
    private SelenideActions selenideActions;
    @Value("${rp.project}")
    private String reportPortalProject;
    @Autowired
    private DashboardPage dashboardPage;
    @Autowired
    @Qualifier("selenideLoginPage")
    private LoginSteps loginSteps;
    private User user;

    @BeforeMethod
    private void setup()
    {
        user = userDataService.getUser();
    }

    @Test
    public void dragAndDropActionTest()
    {
        loginSteps.login(user);
        dashboardPage.open(reportPortalProject).openDashboardByName(DASHBOARD_NAME);

        Point areaCoordinatesBefore = getElementStyleCoordinates(
                Selenide.$x(String.format(DRAGGABLE_ELEMENT_TEMPLATE, LAUNCH_AREA) + GRID_ITEM_TEMPLATE));
        Point barCoordinatesBefore = getElementStyleCoordinates(
                Selenide.$x(String.format(DRAGGABLE_ELEMENT_TEMPLATE, LAUNCH_BAR) + GRID_ITEM_TEMPLATE));

        SelenideElement statisticsArea = Selenide.$x(String.format(DRAGGABLE_ELEMENT_TEMPLATE, LAUNCH_AREA));
        SelenideElement statisticsBar = Selenide.$x(String.format(DRAGGABLE_ELEMENT_TEMPLATE, LAUNCH_BAR));

        selenideActions.dragAndDrop(statisticsArea, statisticsBar);

        Point areaCoordinatesAfter = getElementStyleCoordinates(
                Selenide.$x(String.format(DRAGGABLE_ELEMENT_TEMPLATE, LAUNCH_AREA) + GRID_ITEM_TEMPLATE));
        Point barCoordinatesAfter = getElementStyleCoordinates(
                Selenide.$x(String.format(DRAGGABLE_ELEMENT_TEMPLATE, LAUNCH_BAR) + GRID_ITEM_TEMPLATE));

        verifyThat.actualIsEqualToExpected(areaCoordinatesBefore, barCoordinatesAfter,
                "Area coordinates change is correct: ");
        verifyThat.actualIsEqualToExpected(areaCoordinatesAfter, barCoordinatesBefore,
                "Area coordinates change is correct: ");
    }

    @Test
    public void resizeActionTest()
    {
        loginSteps.login(user);
        dashboardPage.open(reportPortalProject).openDashboardByName(DASHBOARD_NAME);

        Dimension sizeBeforeResize = getSize(
                Selenide.$x(String.format(DRAGGABLE_ELEMENT_TEMPLATE, LAUNCH_AREA) + GRID_ITEM_TEMPLATE));

        SelenideElement statisticsArea = Selenide.$x(
                String.format(DRAGGABLE_ELEMENT_TEMPLATE, LAUNCH_AREA) + GRID_ITEM_TEMPLATE + RESIZE_TEMPLATE);

        int additionalWidth = 100;
        int additionalHeight = 50;
        selenideActions.resizeElement(statisticsArea, additionalWidth, additionalHeight);
        Dimension sizeAfterResize = getSize(
                Selenide.$x(String.format(DRAGGABLE_ELEMENT_TEMPLATE, LAUNCH_AREA) + GRID_ITEM_TEMPLATE));

        verifyThat.isTrue(sizeAfterResize.getWidth() - sizeBeforeResize.getWidth() > 0, "width is increased");
        verifyThat.isTrue(sizeAfterResize.getHeight() - sizeBeforeResize.getHeight() > 0, "height is increased");
    }

    @Test
    public void scrollToElementTest()
    {
        loginSteps.login(user);
        dashboardPage.open(reportPortalProject).openDashboardByName(DASHBOARD_NAME);
        SelenideElement flakyTestCases = Selenide.$x(String.format(DRAGGABLE_ELEMENT_TEMPLATE, FLAKY_TEST_CASES));
        selenideActions.scrollToElement(flakyTestCases);
        verifyThat.isTrue(selenideActions.isElementInViewport(flakyTestCases), "element in focus");
    }

    //Works only with chromium browsers
    private Point getElementStyleCoordinates(SelenideElement element)
    {
        Integer[] coordinates = getStyleIntegers(element, "translate\\((\\d+)px, (\\d+)px\\)");
        return new Point(coordinates[0], coordinates[1]);
    }

    //Works only with chromium browsers
    private Dimension getSize(SelenideElement element)
    {
        Integer[] dimensions = getStyleIntegers(element, "width: (\\d+)px; height: (\\d+)px;");
        return new Dimension(dimensions[0], dimensions[1]);
    }

    private Integer[] getStyleIntegers(SelenideElement element, String regex)
    {
        String styleDescription = Objects.requireNonNull(element.getAttribute("style"));
        Matcher matcher = Pattern.compile(regex).matcher(styleDescription);

        if (matcher.find())
        {
            Integer[] parsedIntegers = new Integer[matcher.groupCount()];
            for (int i = 1; i <= matcher.groupCount(); i++)
            {
                parsedIntegers[i - 1] = Integer.parseInt(matcher.group(i));
            }
            return parsedIntegers;
        }
        else
        {
            throw new AutomationException("Unable to find matching integers in style for element: " + element);
        }
    }

    @AfterMethod
    private void tearDown()
    {
        userDataService.releaseUser(user);
    }

}
