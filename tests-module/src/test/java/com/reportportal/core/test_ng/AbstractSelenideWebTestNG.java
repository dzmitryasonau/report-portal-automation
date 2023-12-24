package com.reportportal.core.test_ng;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.epam.reportportal.selenide.ReportPortalSelenideEventListener;

import org.springframework.beans.factory.annotation.Value;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class AbstractSelenideWebTestNG extends AbstractTestNG
{
    @Value("${browser.name}")
    private String browserName;
    @Value("${app.base.url}")
    private String url;

    @BeforeMethod(alwaysRun = true)
    public void beforeEachMethodSetupWeb()
    {
        Configuration.baseUrl = url;
        Configuration.browser = browserName;
        Selenide.open("/");
        SelenideLogger.addListener("Report Portal logger", new ReportPortalSelenideEventListener());
        WebDriverRunner.getWebDriver().manage().window().maximize();
    }

    @AfterMethod(alwaysRun = true)
    public void afterEachMethodTearDownWeb()
    {
        Selenide.closeWebDriver();
    }
}
