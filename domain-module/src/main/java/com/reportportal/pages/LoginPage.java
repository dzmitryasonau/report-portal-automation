package com.reportportal.pages;

import com.reportportal.annotations.Loggable;
import com.reportportal.annotations.Page;
import com.reportportal.annotations.Path;
import com.reportportal.components.AbstractPage;

import org.openqa.selenium.By;

@Page
@Path("/ui/#login")
public class LoginPage extends AbstractPage
{
    private final By login = By.xpath("//input[@name='login']");
    private final By password = By.xpath("//input[@name='password']");
    private final By loginButton = By.xpath("//button[@type='submit' and text()='Login']");

    @Loggable("User fills login")
    public LoginPage fillLogin(String userLogin)
    {
        browserActions.inputText(login, userLogin);
        return this;
    }

    @Loggable("User fills password")
    public LoginPage fillPassword(String userPassword)
    {
        browserActions.inputText(password, userPassword, true);
        return this;
    }

    @Loggable("User clicks on login button")
    public void login()
    {
        browserActions.click(loginButton);
    }
}
