package com.reportportal.step_defenitions.ui;

import com.reportportal.models.User;
import com.reportportal.service.UserDataService;
import com.reportportal.ui.components.GlobalVariablesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.cucumber.java.en.Given;

@SpringBootTest
public class BackgroundDefinitions
{
    @Autowired
    private UserDataService userDataService;

    private final GlobalVariablesService globalVariablesService = GlobalVariablesService.getInstance();

    @Given("a user with user name {string}")
    public void recordUserByName(String userName)
    {
        User user = userDataService.getUserByLogin(userName);
        globalVariablesService.addVariable("user", user);
    }

    @Given("a random user")
    public void recordRandomUser()
    {
        globalVariablesService.addVariable("user", userDataService.getUser());
    }

    @Given("a project with name {string}")
    public void recordProjectName(String projectName)
    {
        globalVariablesService.addVariable("projectName", projectName);
    }
}
