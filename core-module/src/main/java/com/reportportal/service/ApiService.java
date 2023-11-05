package com.reportportal.service;

import java.util.List;
import java.util.Map;

import com.epam.reportportal.annotations.Step;
import com.reportportal.api.ApiClient;
import com.reportportal.api.core.FeignBaseClient;
import com.reportportal.models.launch.Attribute;
import com.reportportal.models.launch.Launch;
import com.reportportal.models.launch.UpdateLaunchRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
public class ApiService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiService.class);
    @Value("${app.base.url}")
    private String url;
    @Autowired
    private FeignBaseClient feignBaseClient;

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ApiClient apiClient()
    {
        return feignBaseClient.getFeignBuilder().requestInterceptor(
                rt -> rt.header("Origin", StringUtils.removeEnd(url, "/"))
                        .header("Referer", StringUtils.removeEnd(url, "/"))).target(ApiClient.class, url);
    }

    @Step("User gets project launches by name={name} and filter={filter}")
    public List<String> getLaunchesByProjectName(String authToken, String name, String filter)
    {
        return apiClient().getLaunchesByProjectName(authToken, name, filter).getBody();
    }

    @Step("User gets last launch")
    public Launch getLastLaunchByProjectName(String authToken, String name)
    {
        return apiClient().getLatestLaunchByProjectName(authToken, name).getBody();
    }

    @Step("User gets launch status")
    public Map<String, String> getLaunchStatus(String authToken, String name, Integer... id)
    {
        return apiClient().getLaunchStatus(authToken, name, id).getBody();
    }

    @Step("User update launch attributes")
    public String updateLaunch(String authToken, String name, Integer launchId, List<Attribute> attributes)
    {
        return apiClient().updateLaunch(authToken, name, launchId, new UpdateLaunchRequest(attributes)).getBody();
    }
}
