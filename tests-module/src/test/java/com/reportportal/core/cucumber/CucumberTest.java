package com.reportportal.core.cucumber;

import com.reportportal.config.SpringDomainConfig;
import com.reportportal.models.User;
import com.reportportal.reporting.ReportService;
import com.reportportal.service.UserDataService;
import com.reportportal.ui.WebConfiguration;
import com.reportportal.ui.browser.WebDriverHolder;
import com.reportportal.ui.components.GlobalVariablesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.DataProvider;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.spring.CucumberContextConfiguration;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = { SpringDomainConfig.class })
@CucumberContextConfiguration
@CucumberOptions(features = "src/test/resources/features",
        glue = "com.reportportal",
        plugin = { "pretty", "com.epam.reportportal.cucumber.ScenarioReporter" },
        tags = "@ui and @cucumber_smoke")
public class CucumberTest extends AbstractTestNGCucumberTests
{
    @Autowired
    private WebConfiguration webConfiguration;
    @Autowired
    private UserDataService userDataService;
    @Autowired
    private ReportService reportService;
    private final GlobalVariablesService globalVariablesService = GlobalVariablesService.getInstance();

    @Before("@api")
    public void beforeEachAPIScenario(Scenario scenario)
    {
        reportService.info("API Scenario with name: %s, was started", scenario.getName());
    }

    @Before("@ui")
    public void beforeEachUIScenario(Scenario scenario)
    {
        reportService.info("UI Scenario with name: %s, was started", scenario.getName());
        WebDriverHolder.getInstance().setDriver(webConfiguration);
    }

    @Before("@used_API_connection")
    public void beforeAPIConnection()
    {
        reportService.info("Connection via API will be used");
    }

    @Before("@used_UI_login")
    public void beforeUILogin()
    {
        reportService.info("Login via UI will be used");
    }

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios()
    {
        return super.scenarios();
    }

    @After("@ui")
    public void afterEachMethodTearDown()
    {
        WebDriverHolder.getInstance().tearDown();
    }

    @After
    public void releaseUsedUsers(Scenario scenario)
    {
        if (globalVariablesService.getVariable("user") != null)
        {
            userDataService.releaseUser((User) globalVariablesService.getVariable("user"));
        }
        reportService.info("Execution of scenario with name: %s, ended with status: %s", scenario.getName(),
                scenario.getStatus());
    }
}
