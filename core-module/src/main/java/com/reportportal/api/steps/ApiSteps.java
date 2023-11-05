package com.reportportal.api.steps;

import java.util.List;
import java.util.Map;

import com.epam.reportportal.annotations.Step;
import com.reportportal.models.User;
import com.reportportal.models.launch.Attribute;
import com.reportportal.models.launch.Launch;
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

    @Step("User gets project launches")
    public List<String> getLaunchesByProjectName(User user, String name, String filter)
    {
        return apiService.getLaunchesByProjectName(getBearerApiKey(user), name, filter);
    }

    @Step("User gets last launch")
    public Launch getLastLaunchesByProjectName(User user, String name)
    {
        return apiService.getLastLaunchByProjectName(getBearerApiKey(user), name);
    }

    @Step("User gets launch status")
    public Map<String, String> getLaunchStatus(User user, String name, Integer... id)
    {
        return apiService.getLaunchStatus(getBearerApiKey(user), name, id);
    }

    @Step("User gets launch status")
    public String updateLaunch(User user, String name, Integer id, List<Attribute> attributes)
    {
        return apiService.updateLaunch(getBearerApiKey(user), name, id, attributes);
    }

    private String getBearerApiKey(User user)
    {
        return "Bearer " + aesCryptoService.decrypt(user.getApiKey());
    }
}
