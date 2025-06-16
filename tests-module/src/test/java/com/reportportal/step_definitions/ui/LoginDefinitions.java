package com.reportportal.step_definitions.ui;

import com.reportportal.asserts.VerifyThat;
import com.reportportal.models.User;
import com.reportportal.service.AesCryptoService;
import com.reportportal.service.UserDataService;
import com.reportportal.ui.components.GlobalVariablesService;
import com.reportportal.ui.pages.LaunchesPage;
import com.reportportal.ui.pages.LoginPage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

@SpringBootTest
public class LoginDefinitions
{
    @Autowired
    private AesCryptoService aesCryptoService;
    @Autowired
    private UserDataService userDataService;
    @Autowired
    private LaunchesPage launchesPage;
    @Autowired
    protected VerifyThat verifyThat;
    @Autowired
    private LoginPage loginPage;
    private final GlobalVariablesService globalVariablesService = GlobalVariablesService.getInstance();

    @Given("I am logged in as {string}")
    public void loginToAppByUserName(String userName)
    {
        User user = userDataService.getUserByLogin(userName);
        loginPage.openCurrentPage();
        loginPage.fillLogin(userName).fillPassword(aesCryptoService.decrypt(user.getPassword())).login();
    }

    @When("I open login page")
    public void openLoginPage()
    {
        loginPage.openCurrentPage();
    }

    @When("I fill login {string}")
    public void fillUserName(String userName)
    {
        loginPage.fillLogin(userName);
    }

    @When("I fill password {string}")
    public void fillUserPassword(String password)
    {
        loginPage.fillPassword(password);
    }

    @When("I click on login button")
    public void clickOnLoginButton()
    {
        loginPage.login();
    }

    @When("I login in app")
    public void loginToApp()
    {
        User user = (User) globalVariablesService.getVariable("user");
        loginPage.openCurrentPage();
        loginPage.fillLogin(user.getLogin()).fillPassword(aesCryptoService.decrypt(user.getPassword())).login();
    }

    @When("I open block with userinfo")
    public void checkUserNameOfCurrentUser()
    {
        launchesPage.openUserBlock();
    }

    @Then("Name of current user is {string}")
    public void checkUserNameOfCurrentUser(String userName)
    {
        verifyThat.actualIsEqualToExpected(launchesPage.getUserName().toLowerCase(), userName.toLowerCase(),
                "User name is correct, ");
    }

    @Then("User should see an error message")
    public void checkPresenceLoginErrorMessage()
    {
        verifyThat.isTrue(loginPage.isErrorMessagePresent(), "Error notification is present");
    }
}
