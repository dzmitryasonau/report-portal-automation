package com.reportportal.service;

import java.util.List;

import com.epam.reportportal.annotations.Step;
import com.reportportal.api_clients.ApiClient;
import com.reportportal.core.FeignBaseClient;

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
}
