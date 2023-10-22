package com.reportportal.steps.api;

import java.util.List;

import com.epam.reportportal.annotations.Step;
import com.reportportal.models.User;
import com.reportportal.service.AesCryptoService;
import com.reportportal.service.ApiService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApiSteps
{
    @Autowired
    private ApiService apiService;
    @Autowired
    private AesCryptoService aesCryptoService;

    @Step("Client creates single task project via API")
    public List<String> getLaunchesByProjectName(User user, String name, String filter)
    {
        String bearerApiKey = "Bearer " + aesCryptoService.decrypt(user.getApiKey());
        return apiService.getLaunchesByProjectName(bearerApiKey, name, filter);
    }
}
