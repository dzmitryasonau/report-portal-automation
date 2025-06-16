package com.reportportal.step_definitions.ui;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.reportportal.asserts.VerifyThat;
import com.reportportal.ui.components.GlobalVariablesService;
import com.reportportal.ui.pages.LaunchPage;
import com.reportportal.ui.pages.LaunchesPage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

@SpringBootTest
public class LaunchDefinitions
{
    @Autowired
    private LaunchesPage launchesPage;
    @Autowired
    private LaunchPage launchPage;
    @Autowired
    protected VerifyThat verifyThat;
    private final GlobalVariablesService globalVariablesService = GlobalVariablesService.getInstance();

    @When("I open launches page")
    public void openLaunchesPage()
    {
        launchesPage.openCurrentPage(globalVariablesService.getVariable("projectName"));
    }

    @When("I open launch by it's id {int}")
    public void checkUserNameOfCurrentUser(Integer id)
    {
        launchesPage.openLaunch(String.valueOf(id));
    }

    @Then("the following suites exists:")
    public void theFollowingSuitesExists(DataTable suitesNames)
    {
        List<String> actualSuites = launchPage.getSuitesName();
        List<String> expectedSuites = suitesNames.cells().stream().flatMap(Collection::stream).toList();
        verifyThat.listOfPrimitivesAreEqual(actualSuites, expectedSuites);
    }

    @Then("the following suites absent:")
    public void theFollowingSuitesAbsent(DataTable suitesNames)
    {
        List<String> actualSuites = launchPage.getSuitesName();
        List<String> expectedNotIncludedSuites = suitesNames.cells().stream().flatMap(Collection::stream).toList();
        verifyThat.isTrue(Collections.disjoint(actualSuites, expectedNotIncludedSuites),
                "No unexpected suites presents");
    }
}
