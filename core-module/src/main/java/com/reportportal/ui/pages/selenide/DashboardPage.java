package com.reportportal.ui.pages.selenide;

import com.codeborne.selenide.Selenide;
import com.reportportal.annotations.Page;
import com.reportportal.ui.components.AbstractSelenidePage;

@Page
public class DashboardPage extends AbstractSelenidePage
{
    private final static String DASHBOARD_ROW_TEMPLATE = "//div[contains(@class,'gridRow')]//a[text()='%s']";

    public void openDashboardByName(String dashboardName)
    {
        selenideActions.click(Selenide.$x(String.format(DASHBOARD_ROW_TEMPLATE, dashboardName)));
    }

    public DashboardPage open(String reportPortalProject)
    {
        Selenide.open("ui/#" + reportPortalProject + "/dashboard");
        return this;
    }
}
