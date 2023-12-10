package com.reportportal.api;

import com.reportportal.api.core.ApacheHttpClient;
import com.reportportal.api.core.RestAssuredClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class HttpClientConfig
{
    @Profile("use_rest_assured")
    @Bean
    public HttpClient restAssuredClient()
    {
        return new RestAssuredClient();
    }

    @Profile("use_apache_http_client")
    @Bean
    public HttpClient apacheHttpClient()
    {
        return new ApacheHttpClient();
    }
}
