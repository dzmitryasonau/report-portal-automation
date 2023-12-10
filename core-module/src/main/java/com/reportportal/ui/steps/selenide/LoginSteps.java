package com.reportportal.ui.steps.selenide;

import com.epam.reportportal.annotations.Step;
import com.reportportal.models.User;
import com.reportportal.service.AesCryptoService;
import com.reportportal.ui.pages.selenide.LoginPage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("selenideLoginSteps")
public class LoginSteps
{
    @Autowired
    @Qualifier("selenideLoginPage")
    private LoginPage loginPage;
    @Autowired
    private AesCryptoService aesCryptoService;

    @Step("User logins to the 'Report Portal'")
    public LoginSteps login(User user)
    {
        loginPage.open().fillLogin(user.getLogin()).fillPassword(aesCryptoService.decrypt(user.getPassword())).login();
        return this;
    }
}
