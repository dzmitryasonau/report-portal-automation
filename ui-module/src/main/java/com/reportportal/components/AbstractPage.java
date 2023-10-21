package com.reportportal.components;

import org.springframework.beans.factory.annotation.Autowired;

import com.reportportal.annotations.Page;
import com.reportportal.browser.WebDriverHolder;
import com.reportportal.meta.BaseEntity;
import com.reportportal.reporting.ReportService;

@Page
public abstract class AbstractPage extends BaseEntity
{
    @Autowired
    protected ReportService reportService;

    public abstract String getHomeUrl();

    public void openUrl(String url)
    {
        reportService.info("User opens url %s...", url);
        WebDriverHolder.getInstance().getWebDriver().get(url);
    }

    public String getCurrentUrl()
    {
        return WebDriverHolder.getInstance().getWebDriver().getCurrentUrl();
    }
}
