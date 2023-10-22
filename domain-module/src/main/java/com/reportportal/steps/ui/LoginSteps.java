package com.reportportal.steps.ui;

import com.epam.reportportal.annotations.Step;
import com.reportportal.models.User;
import com.reportportal.pages.LoginPage;
import com.reportportal.service.AesCryptoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoginSteps
{
    @Autowired
    private LoginPage loginPage;
    @Autowired
    private AesCryptoService aesCryptoService;

    @Step("User logins to the 'Report Portal'")
    public LoginSteps login(User user)
    {
        loginPage.openCurrentPage();
        loginPage.fillLogin(user.getLogin()).fillPassword(aesCryptoService.decrypt(user.getPassword())).login();
        return this;
    }
}
