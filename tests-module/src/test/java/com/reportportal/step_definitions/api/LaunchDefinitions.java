package com.reportportal.step_definitions.api;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.reportportal.api.steps.ApiSteps;
import com.reportportal.asserts.VerifyThat;
import com.reportportal.models.User;
import com.reportportal.models.launch.api.Attribute;
import com.reportportal.ui.components.GlobalVariablesService;
import com.reportportal.utils.CommonUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

@SpringBootTest
public class LaunchDefinitions
{

    @Autowired
    private ApiSteps apiSteps;
    @Autowired
    protected VerifyThat verifyThat;
    private final GlobalVariablesService globalVariablesService = GlobalVariablesService.getInstance();
    private String responseMessage;
    private final Map<String, String> launchWithStatus = new HashMap<>();
    private static final String ATTRIBUTE_PREFIX = "Atr: ";
    private static final String ATTRIBUTE_VALUE = "value";

    @When("I get status of launches:")
    public void getStatusesOfLaunches(DataTable launches)
    {
        List<String> launchesID = launches.cells().stream().flatMap(Collection::stream).toList();
        User user = (User) globalVariablesService.getVariable("user");
        String projectName = globalVariablesService.getVariable("projectName").toString();

        for (String launchID : launchesID)
        {
            Map<String, String> launchStatus = apiSteps.getLaunchStatus(user, projectName, Integer.valueOf(launchID));
            launchWithStatus.putAll(launchStatus);
        }
    }

    @When("I send request to update attribute with launch id {int}")
    public void openLaunchesPage(Integer launchId)
    {
        responseMessage = apiSteps.updateLaunch((User) globalVariablesService.getVariable("user"),
                globalVariablesService.getVariable("projectName").toString(), launchId,
                List.of(new Attribute(ATTRIBUTE_PREFIX + CommonUtils.getRandomInteger(launchId), ATTRIBUTE_VALUE)));
    }

    @Then("response message about updating attribute of launch with id {int} is correct")
    public void checkUpdateAttributeResponseMessage(Integer launchId)
    {
        String expectedResponse = String.format("{\"message\":\"Launch with ID = '%s' successfully updated.\"}",
                launchId);
        verifyThat.actualIsEqualToExpected(responseMessage, expectedResponse, "Response is correct, ");
    }

    @Then("launch statuses correspond to the table:")
    public void checkLaunchStatuses(DataTable launches)
    {
        Map<String, String> expectedLaunchesMap = new HashMap<>();
        List<Map<String, String>> data = launches.asMaps(String.class, String.class);
        for (Map<String, String> datum : data)
        {
            expectedLaunchesMap.put(datum.get("launchId"), datum.get("launchStatus"));
        }
        verifyThat.isTrue(launchWithStatus.equals(expectedLaunchesMap), "Expected launches status correct");

    }

}
