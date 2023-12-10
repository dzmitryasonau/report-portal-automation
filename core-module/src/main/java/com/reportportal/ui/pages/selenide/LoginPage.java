package com.reportportal.ui.pages.selenide;

import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.reportportal.ui.components.AbstractSelenidePage;

import org.springframework.stereotype.Component;

@Component("selenideLoginPage")
public class LoginPage extends AbstractSelenidePage
{
    private final SelenideElement login = Selenide.$(Selectors.byName("login"));
    private final SelenideElement password = Selenide.$(Selectors.byName("password"));
    private final SelenideElement loginButton = Selenide.$(Selectors.byText("Login"));
    private final SelenideElement successfulLoginMessage = Selenide.$(Selectors.byText("Signed in successfully"));

    public LoginPage fillLogin(String userLogin)
    {
        selenideActions.setValue(login, userLogin);
        return this;
    }

    public LoginPage fillPassword(String userPassword)
    {
        selenideActions.setValue(password, userPassword, true);
        return this;
    }

    public void login()
    {
        selenideActions.click(loginButton);
        selenideActions.waitUntilVisible(successfulLoginMessage);
    }

    public LoginPage open()
    {
        Selenide.open("ui/#login");
        return this;
    }
}
